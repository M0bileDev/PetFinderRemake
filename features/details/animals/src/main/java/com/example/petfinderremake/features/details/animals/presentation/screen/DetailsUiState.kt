package com.example.petfinderremake.features.details.animals.presentation.screen

import androidx.annotation.StringRes
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.animal.details.toPresentationData
import com.example.petfinderremake.common.domain.model.animal.details.toStringRes
import com.example.petfinderremake.common.ext.toPresentationData
import com.example.petfinderremake.common.presentation.utils.DateFormatUtils
import com.example.petfinderremake.common.presentation.utils.PresentationData
import com.example.petfinderremake.features.details.animals.R

typealias SectionDataList = Pair<DetailsSections, List<Pair<Int, PresentationData>>>
typealias SectionData = Pair<DetailsSections, PresentationData>

data class DetailsUiState(
    private val animalWithDetails: AnimalWithDetails = AnimalWithDetails.noAnimalWithDetails
) {

    val detailsHeadline: DetailsHeadline = animalWithDetails.fromAnimalWithDetailsToHeadline()
    val descriptionSection: Pair<DetailsSections, PresentationData> =
        DetailsSections.DESCRIPTION to PresentationData.StringValue(animalWithDetails.details.description)
    val detailsSection =
        DetailsSections.DETAILS to animalWithDetails.fromAnimalWithDetailsToDetails()
    val healthDetailsSection =
        DetailsSections.HEALTH_DETAILS to animalWithDetails.fromAnimalDetailsToHealthDetails()
    val habitatAdaptationSection =
        DetailsSections.HABITAT_ADAPTATION to animalWithDetails.fromAnimalDetailsToHabitatAdaptation()
    val media = animalWithDetails.media
    val url = animalWithDetails.url
//    val organizationSection = DetailsSections.ORGANIZATION to animalWithDetails.details.organization

    private fun AnimalWithDetails.fromAnimalWithDetailsToDetails() =
        listOf<Pair<@StringRes Int, PresentationData>>(
            R.string.age to PresentationData.StringResource(details.age.toStringRes()),
            R.string.species to PresentationData.StringValue(details.species),
            R.string.details_breed to details.breed.toPresentationData(),
            R.string.color to details.colors.toPresentationData(),
            R.string.gender to PresentationData.StringResource(details.gender.toStringRes()),
            R.string.size to PresentationData.StringResource(details.size.toStringRes()),
            R.string.coat to PresentationData.StringResource(details.coat.toStringRes())
        )

    private fun AnimalWithDetails.fromAnimalDetailsToHealthDetails() =
        listOf<Pair<@StringRes Int, PresentationData>>(
            R.string.spayed_or_neutered to details.healthDetails.isSpayedOrNeutered.toPresentationData(),
            R.string.declawed to details.healthDetails.isDeclawed.toPresentationData(),
            R.string.special_needs to details.healthDetails.hasSpecialNeeds.toPresentationData(),
            R.string.shots_are_current to details.healthDetails.shotsAreCurrent.toPresentationData(),
        )

    private fun AnimalWithDetails.fromAnimalDetailsToHabitatAdaptation() =
        listOf<Pair<@StringRes Int, PresentationData>>(
            R.string.good_with_children to details.habitatAdaptation.goodWithChildren.toPresentationData(),
            R.string.good_with_dogs to details.habitatAdaptation.goodWithDogs.toPresentationData(),
            R.string.good_with_cats to details.habitatAdaptation.goodWithCats.toPresentationData(),
        )

//    private fun AnimalWithDetails.fromAnimalDetailsToOrganization() =
//        listOf<Pair<@StringRes Int, PresentationData>>(
//            R.string.good_with_children to details.habitatAdaptation.goodWithChildren.toPresentationData(),
//            R.string.good_with_dogs to details.habitatAdaptation.goodWithDogs.toPresentationData(),
//            R.string.good_with_cats to details.habitatAdaptation.goodWithCats.toPresentationData(),
//        )


    private fun AnimalWithDetails.fromAnimalWithDetailsToHeadline() =
        DetailsHeadline(
            media.getFirstLargestAvailablePhoto(),
            name,
            age,
            DateFormatUtils.format(publishedAt)
        )


    companion object {
        val noDetailsUiState = DetailsUiState()
    }

    data class DetailsHeadline(
        val image: String = "",
        val name: String = "",
        val age: String = "",
        val publishedAt: String = ""
    ) {
        companion object {
            val noDetailsHeadline = DetailsHeadline()


        }
    }
}

enum class DetailsSections(@StringRes val stringId: Int) {
    DESCRIPTION(R.string.description),
    DETAILS(R.string.details),
    HEALTH_DETAILS(R.string.health_details),
    HABITAT_ADAPTATION(R.string.habitat_adaptation),
    ORGANIZATION(R.string.organization)
}