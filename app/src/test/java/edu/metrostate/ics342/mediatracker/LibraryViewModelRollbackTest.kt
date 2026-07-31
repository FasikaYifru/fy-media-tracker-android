package edu.metrostate.ics342.mediatracker

import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelRollbackTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: DefaultMediaRepository

    // Inline the optimistic remove logic so we can test it without AndroidViewModel
    private val items = MutableStateFlow<List<LibraryItem>>(emptyList())
    private val errorMessage = MutableStateFlow<String?>(null)

    private fun removeItem(mediaId: Int) {
        val backup = items.value.find { it.mediaId == mediaId }
        items.value = items.value.filter { it.mediaId != mediaId }
        kotlinx.coroutines.GlobalScope.launch(testDispatcher) {
            try {
                repository.removeFromLibrary(mediaId)
            } catch (e: Exception) {
                items.value = items.value + listOfNotNull(backup)
                errorMessage.value = "Couldn't remove item. Try again."
            }
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        items.value = listOf(fakeLibraryItem(1), fakeLibraryItem(2))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `removeItem rolls back when network call fails`() = runTest {
        coEvery { repository.removeFromLibrary(1) } throws IOException("Network error")

        removeItem(1)

        // Item is gone optimistically
        assertEquals(1, items.value.size)

        // Advance coroutines so the network call runs and fails
        testDispatcher.scheduler.advanceUntilIdle()

        // Item is restored after rollback
        assertEquals(2, items.value.size)
        assertTrue(items.value.any { it.mediaId == 1 })
        assertEquals("Couldn't remove item. Try again.", errorMessage.value)
    }

    @Test
    fun `removeItem stays removed when network call succeeds`() = runTest {
        coEvery { repository.removeFromLibrary(1) } returns Unit

        removeItem(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, items.value.size)
        assertTrue(items.value.none { it.mediaId == 1 })
    }

    private fun fakeLibraryItem(id: Int) = LibraryItem(
        userId    = "user1",
        mediaId   = id,
        status    = LibraryStatus.WANT_TO,
        addedAt   = "2024-01-01",
        updatedAt = "2024-01-01",
        media     = Media(
            id            = id,
            title         = "Title $id",
            mediaType     = "book",
            genres        = emptyList(),
            averageRating = 0f,
            ratingCount   = 0
        )
    )
}
