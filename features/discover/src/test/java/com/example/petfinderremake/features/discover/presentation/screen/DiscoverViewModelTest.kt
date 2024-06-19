package com.example.petfinderremake.features.discover.presentation.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.error.Error
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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class DiscoverViewModelTest : AnimalRepositoryTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var requestDiscoverPageUseCase: RequestDiscoverPageUseCase
    private lateinit var getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase
    private lateinit var requestAnimalTypesUseCase: RequestAnimalTypesUseCase
    private lateinit var getAnimalTypesUseCase: GetAnimalTypesUseCase
    private val typeName = ""

    @MockK(relaxed = true)
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


    @Test
    fun `when view model is initialize, then getDiscoverPaginatedAnimalsUseCase is execute`() {
        //when
        discoverViewModel

        //then
        verify {
            getDiscoverPaginatedAnimalsUseCase()
        }

    }

    @Test
    fun `when view model is initialize, then getAnimalTypesUseCase is execute`() {

        //when
        discoverViewModel

        //then
        verify {
            getAnimalTypesUseCase()
        }

    }

    @Test
    fun `when view model requestData method is execute, then requestDiscoverPageUseCase is executed`() {

        //when
        discoverViewModel.requestData()

        //then
        verify {
            requestDiscoverPageUseCase(any())
        }

    }

    @Test
    fun `when view model requestData method is execute, then requestAnimalTypesUseCase is executed`() {
        //when
        discoverViewModel.requestData()

        //then
        verify {
            requestAnimalTypesUseCase(any())
        }

    }

    @Test
    fun `given paginatedAnimals, when and navigateToGallery with existing media, then gallerySenderEvent has SenderEvent NavigateToGallery media`() {
        //given
        discoverViewModel.requestData()

        //when
        val testObserver = TestObserver<GallerySender.SenderEvent>()
        val result = discoverViewModel.getGalleryEvent()
        result.subscribe(testObserver)

        discoverViewModel.navigateToGallery(GallerySender.GalleryArg.GalleryId(animalWithMedia.id))

        //then
        testObserver.assertValue(GallerySender.SenderEvent.NavigateToGallery(animalWithMedia.media))
    }

    @Test
    fun `given paginatedAnimals, when navigateToGallery with no existing media, then gallerySenderEvent has SenderEvent DisplayNoInfo`() {
        //given
        discoverViewModel.requestData()

        //when
        val testObserver = TestObserver<GallerySender.SenderEvent>()
        val result = discoverViewModel.getGalleryEvent()
        result.subscribe(testObserver)

        discoverViewModel.navigateToGallery(GallerySender.GalleryArg.GalleryId(animalWithNoMedia.id))

        //then
        testObserver.assertValue(GallerySender.SenderEvent.DisplayNoInfo)
    }

    @Test
    fun `given paginatedAnimals, when navigateToGallery with no existing animal, then Exception is thrown`() {

        var exception: Exception? = null

        //given
        discoverViewModel.requestData()

        try {
            //when
            discoverViewModel.navigateToGallery(
                GallerySender.GalleryArg.GalleryId(
                    AnimalWithDetails.noAnimalWithDetails.id
                )
            )
        } catch (e: Exception) {
            exception = e
        }

        //then
        assertThat(exception).isNotNull()

    }

    @Test
    fun `when navigateToAnimalDetails, then animalDetailsSenderEvent has SenderEvent NavigateToAnimalDetails`() {
        //when
        val testObserver = TestObserver<AnimalDetailsSender.SenderEvent>()
        val result = discoverViewModel.animalDetailsSenderSubject
        result.subscribe(testObserver)

        discoverViewModel.navigateToAnimalDetails(animalWithNoMedia.id)

        //then
        testObserver.assertValue(
            AnimalDetailsSender.SenderEvent.NavigateToAnimalDetails(
                animalWithNoMedia.id
            )
        )
    }

    @Test
    fun `when navigateToSearch with type, then discoverEventChannel has SenderEvent DiscoverEvent NavigateToSearch type`() {
        //when
        val testObserver = TestObserver<DiscoverViewModel.DiscoverEvent>()
        val result = discoverViewModel.discoverEvent
        result.subscribe(testObserver)

        discoverViewModel.navigateToSearch(typeName)

        //then
        testObserver.assertValue(
            DiscoverViewModel.DiscoverEvent.NavigateToSearch(typeName)
        )
    }

    @Test
    fun `when navigateToSearch empty type, then discoverEventChannel has SenderEvent DiscoverEvent NavigateToSearch empty type`() {
        //when
        val testObserver = TestObserver<DiscoverViewModel.DiscoverEvent>()
        val result = discoverViewModel.discoverEvent
        result.subscribe(testObserver)

        discoverViewModel.navigateToSearch()

        //then
        testObserver.assertValue(
            DiscoverViewModel.DiscoverEvent.NavigateToSearch("")
        )

    }

    @Test
    fun `when view model requestData method is execute, then discoverUiState is not noDiscoverUiState`() {
        //when
        discoverViewModel.requestData()

        //then
        val testObserver = TestObserver<DiscoverUiState>()
        val result = discoverViewModel.discoverUiState
        result.subscribe(testObserver)

        val sut = testObserver.values().first()
        assertThat(sut).isNotEqualTo(DiscoverUiState.noDiscoverUiState)
    }

    @Test
    fun `when view model requestData and repository requestDiscoverPage method throws network exception ACCESS_DENIED_INVALID_CREDENTIALS, then networkErrorChannel has ACCESS_DENIED_INVALID_CREDENTIALS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestDiscoverPage() }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Error>()
        val result = mockDiscoverViewModel.networkError
        result.subscribe(testObserver)

        mockDiscoverViewModel.requestData()

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS)
    }

    @Test
    fun `when view model requestData and repository requestDiscoverPage method throws storage exception NO_DATA_TO_STORE, then storageErrorChannel has NO_DATA_TO_STORE`() {
        //when
        every { mockAnimalRepository.requestDiscoverPage() }.returns(
            Observable.just(
                PaginatedAnimals.initPaginatedAnimals
            )
        )

        val testObserver = TestObserver<Error>()
        val result = mockDiscoverViewModel.storageError
        result.subscribe(testObserver)

        mockDiscoverViewModel.requestData()

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(StorageError.NO_DATA_TO_STORE)
    }

    @Test
    fun `when view model requestData and repository requestAnimalTypes method throws network exception ACCESS_DENIED_INVALID_CREDENTIALS, then networkErrorChannel has ACCESS_DENIED_INVALID_CREDENTIALS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalTypes() }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Error>()
        val result = mockDiscoverViewModel.networkError
        result.subscribe(testObserver)

        mockDiscoverViewModel.requestData()

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS)

    }

    @Test
    fun `when view model requestData and repository requestAnimalTypes method throws storage exception NO_DATA_TO_STORE, then storageErrorChannel has NO_DATA_TO_STORE`() {
        //when
        every { mockAnimalRepository.requestAnimalTypes() }.returns(Observable.just(emptyList()))

        val testObserver = TestObserver<Error>()
        val result = mockDiscoverViewModel.storageError
        result.subscribe(testObserver)


        mockDiscoverViewModel.requestData()

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(StorageError.NO_DATA_TO_STORE)
    }
}