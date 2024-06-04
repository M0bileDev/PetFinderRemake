package com.example.petfinderremake.features.discover.presentation.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.usecase.animal.get.GetAnimalTypesUseCase
import com.example.petfinderremake.common.domain.usecase.animal.request.RequestAnimalTypesUseCase
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsSender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.features.discover.AnimalRepositoryTest
import com.example.petfinderremake.features.discover.TestUtils.animalWithMedia
import com.example.petfinderremake.features.discover.TestUtils.animalWithNoMedia
import com.example.petfinderremake.features.discover.domain.usecase.get.GetDiscoverPaginatedAnimalsUseCase
import com.example.petfinderremake.features.discover.domain.usecase.request.RequestDiscoverPageUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest : AnimalRepositoryTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var requestDiscoverPageUseCase: RequestDiscoverPageUseCase
    private lateinit var getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase
    private lateinit var requestAnimalTypesUseCase: RequestAnimalTypesUseCase
    private lateinit var getAnimalTypesUseCase: GetAnimalTypesUseCase
    private val typeName = ""

    private lateinit var mockDiscoverViewModel: DiscoverViewModel

    @MockK(relaxed = true)
    lateinit var mockAnimalRepository: AnimalRepository

    @MockK
    lateinit var httpException: HttpException

    private lateinit var mockRequestDiscoverPageUseCase: RequestDiscoverPageUseCase

    private lateinit var mockGetDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase

    private lateinit var mockRequestAnimalTypesUseCase: RequestAnimalTypesUseCase

    private lateinit var mockGetAnimalTypesUseCase: GetAnimalTypesUseCase

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)

        requestDiscoverPageUseCase = spyk(RequestDiscoverPageUseCase(animalRepository))
        getDiscoverPaginatedAnimalsUseCase =
            spyk(GetDiscoverPaginatedAnimalsUseCase(animalRepository))
        requestAnimalTypesUseCase = spyk(RequestAnimalTypesUseCase(animalRepository))
        getAnimalTypesUseCase = spyk(GetAnimalTypesUseCase(animalRepository))

        discoverViewModel = spyk(
            DiscoverViewModel(
                requestDiscoverPageUseCase,
                getDiscoverPaginatedAnimalsUseCase,
                requestAnimalTypesUseCase,
                getAnimalTypesUseCase
            )
        )

        mockRequestDiscoverPageUseCase = RequestDiscoverPageUseCase(mockAnimalRepository)
        mockGetDiscoverPaginatedAnimalsUseCase =
            GetDiscoverPaginatedAnimalsUseCase(mockAnimalRepository)
        mockRequestAnimalTypesUseCase = RequestAnimalTypesUseCase(mockAnimalRepository)
        mockGetAnimalTypesUseCase = GetAnimalTypesUseCase(mockAnimalRepository)

        mockDiscoverViewModel =
            DiscoverViewModel(
                mockRequestDiscoverPageUseCase,
                mockGetDiscoverPaginatedAnimalsUseCase,
                mockRequestAnimalTypesUseCase,
                mockGetAnimalTypesUseCase
            )

    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `when view model is initialize, then getDiscoverPaginatedAnimalsUseCase is execute`() =
        runTest {

            //when
            discoverViewModel

            //then
            verify {
                getDiscoverPaginatedAnimalsUseCase()
            }

        }

    @Test
    fun `when view model is initialize, then getAnimalTypesUseCase is execute`() =
        runTest {

            //when
            discoverViewModel

            //then
            verify {
                getAnimalTypesUseCase()
            }

        }

    @Test
    fun `when view model requestData method is execute, then requestDiscoverPageUseCase is executed`() =
        runTest {

            //when
            discoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            coVerify {
                requestDiscoverPageUseCase(any())
            }

        }

    @Test
    fun `when view model requestData method is execute, then requestAnimalTypesUseCase is executed`() =
        runTest {

            //when
            discoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            coVerify {
                requestAnimalTypesUseCase(any())
            }

        }

    @Test
    fun `given paginatedAnimals, when and navigateToGallery with existing media, then gallerySenderEvent has SenderEvent NavigateToGallery media`() =
        runTest {

            //given
            discoverViewModel.requestData()
            testDispatcher.scheduler.advanceUntilIdle()

            //when
            discoverViewModel.navigateToGallery(GallerySender.GalleryArg.GalleryId(animalWithMedia.id))
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.gallerySenderEvent.receiveAsFlow().first()
            assertThat(result).isEqualTo(GallerySender.SenderEvent.NavigateToGallery(animalWithMedia.media))

        }

    @Test
    fun `given paginatedAnimals, when navigateToGallery with no existing media, then gallerySenderEvent has SenderEvent DisplayNoInfo`() =
        runTest {

            //given
            discoverViewModel.requestData()
            testDispatcher.scheduler.advanceUntilIdle()

            //when
            discoverViewModel.navigateToGallery(GallerySender.GalleryArg.GalleryId(animalWithNoMedia.id))
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.gallerySenderEvent.receiveAsFlow().first()
            assertThat(result).isEqualTo(GallerySender.SenderEvent.DisplayNoInfo)

        }

    @Test
    fun `given paginatedAnimals, when navigateToGallery with no existing animal, then Exception is thrown`() =
        runTest {

            var exception: Exception? = null

            try {
                //given
                discoverViewModel.requestData()
                testDispatcher.scheduler.advanceUntilIdle()

                //when
                discoverViewModel.navigateToGallery(
                    GallerySender.GalleryArg.GalleryId(
                        AnimalWithDetails.noAnimalWithDetails.id
                    )
                )
                testDispatcher.scheduler.advanceUntilIdle()
            } catch (e: Exception) {
                exception = e
            }


            //then
            assertThat(exception).isNotNull()

        }

    @Test
    fun `when navigateToAnimalDetails, then animalDetailsSenderEvent has SenderEvent NavigateToAnimalDetails`() =
        runTest {

            //when
            discoverViewModel.navigateToAnimalDetails(animalWithNoMedia.id)
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.animalDetailsSenderEvent.receiveAsFlow().first()
            assertThat(result).isEqualTo(
                AnimalDetailsSender.SenderEvent.NavigateToAnimalDetails(
                    animalWithNoMedia.id
                )
            )

        }

    @Test
    fun `when navigateToSearch with type, then discoverEventChannel has SenderEvent DiscoverEvent NavigateToSearch type`() =
        runTest {

            //when
            discoverViewModel.navigateToSearch(typeName)
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.discoverEvent.first()
            assertThat(result).isEqualTo(
                DiscoverViewModel.DiscoverEvent.NavigateToSearch(typeName)
            )

        }

    @Test
    fun `when navigateToSearch empty type, then discoverEventChannel has SenderEvent DiscoverEvent NavigateToSearch empty type`() =
        runTest {

            //when
            discoverViewModel.navigateToSearch()
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.discoverEvent.first()
            assertThat(result).isEqualTo(
                DiscoverViewModel.DiscoverEvent.NavigateToSearch("")
            )

        }

    @Test
    fun `when view model requestData method is execute, then discoverUiState is not noDiscoverUiState`() =
        runTest {

            //when
            discoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = discoverViewModel.discoverUiState.first()
            assertThat(result).isNotEqualTo(DiscoverUiState.noDiscoverUiState)

        }

    @Test
    fun `when view model requestData and repository requestDiscoverPage method throws network exception ACCESS_DENIED_INVALID_CREDENTIALS, then networkErrorChannel has ACCESS_DENIED_INVALID_CREDENTIALS`() =
        runTest {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            mockDiscoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = mockDiscoverViewModel.networkError.first()
            assertThat(result).isEqualTo(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS)

        }

    @Test
    fun `when view model requestData and repository requestDiscoverPage method throws storage exception NO_DATA_TO_STORE, then storageErrorChannel has NO_DATA_TO_STORE`() =
        runTest {

            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(PaginatedAnimals.initPaginatedAnimals)
            mockDiscoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = mockDiscoverViewModel.storageError.first()
            assertThat(result).isEqualTo(StorageError.NO_DATA_TO_STORE)

        }

    @Test
    fun `when view model requestData and repository requestAnimalTypes method throws network exception ACCESS_DENIED_INVALID_CREDENTIALS, then networkErrorChannel has ACCESS_DENIED_INVALID_CREDENTIALS`() =
        runTest {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimalTypes() }.throws(httpException)
            mockDiscoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = mockDiscoverViewModel.networkError.first()
            assertThat(result).isEqualTo(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS)

        }

    @Test
    fun `when view model requestData and repository requestAnimalTypes method throws storage exception NO_DATA_TO_STORE, then storageErrorChannel has NO_DATA_TO_STORE`() =
        runTest {

            //when
            coEvery { mockAnimalRepository.requestAnimalTypes() }.returns(emptyList())
            mockDiscoverViewModel.requestData()

            // Run the pending tasks in the dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            //then
            val result = mockDiscoverViewModel.storageError.first()
            assertThat(result).isEqualTo(StorageError.NO_DATA_TO_STORE)

        }
}