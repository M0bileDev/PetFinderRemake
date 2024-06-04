package com.example.petfinderremake.common.domain.model

import com.example.petfinderremake.logging.Logger
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.io.IOException

@JsonClass(generateAdapter = true)
data class AnimalParameters(
    val type: String? = null,
    val breed: List<String>? = null,
    val size: List<String>? = null,
    val gender: List<String>? = null,
    val age: List<String>? = null,
    val color: List<String>? = null,
    val coat: List<String>? = null,
    val status: List<String>? = null,
    val name: String? = null,
    val organization: String? = null,
    val goodWithChildren: Boolean? = null,
    val goodWithDogs: Boolean? = null,
    val goodWithCats: Boolean? = null,
    val houseTrained: Boolean? = null,
    val declawed: Boolean? = null,
    val specialNeeds: Boolean? = null,
    val location: String? = null,
    val distance: Int? = null,
    val before: String? = null,
    val after: String? = null,
    val sort: String? = null,
) {
    companion object {
        val noAnimalParameters = AnimalParameters()
    }

    class AnimalParametersConvertError :
        IllegalStateException("An exception occurs during conversion ResultNavType")
}

fun AnimalParameters.isEmpty() = this == AnimalParameters.noAnimalParameters
fun AnimalParameters.isNotEmpty() = this != AnimalParameters.noAnimalParameters

//fun AnimalParameters.mapToRequestParameters(): Map<String, String> {
//
//    if (this.isEmpty()) return emptyMap()
//
//    val queryMap = mutableMapOf<String, String>()
//
//    if (!type.isNullOrEmpty()) queryMap[ApiParameters.TYPE] = type
//    if (!breed.isNullOrEmpty()) queryMap[ApiParameters.BREED] = breed.toString()
//    if (!size.isNullOrEmpty()) queryMap[ApiParameters.SIZE] = size.toString()
//    if (!gender.isNullOrEmpty()) queryMap[ApiParameters.GENDER] = gender.toString()
//    if (!age.isNullOrEmpty()) queryMap[ApiParameters.AGE] = age.toString()
//    if (!color.isNullOrEmpty()) queryMap[ApiParameters.COLOR] = color.toString()
//    if (!coat.isNullOrEmpty()) queryMap[ApiParameters.COAT] = coat.toString()
//    if (!status.isNullOrEmpty()) queryMap[ApiParameters.STATUS] = status.toString()
//    if (!name.isNullOrEmpty()) queryMap[ApiParameters.NAME] = name
//    if (!organization.isNullOrEmpty()) queryMap[ApiParameters.ORGANIZATION] = organization
//    if (goodWithChildren.isNotNull()) queryMap[ApiParameters.GOOD_WITH_CHILDREN] =
//        goodWithChildren.toString()
//    if (goodWithDogs.isNotNull()) queryMap[ApiParameters.GOOD_WITH_DOGS] =
//        goodWithDogs.toString()
//    if (goodWithCats.isNotNull()) queryMap[ApiParameters.GOOD_WITH_CATS] =
//        goodWithCats.toString()
//    if (houseTrained.isNotNull()) queryMap[ApiParameters.HOUSE_TRAINED] =
//        houseTrained.toString()
//    if (declawed.isNotNull()) queryMap[ApiParameters.DECLAWED] = declawed.toString()
//    if (specialNeeds.isNotNull()) queryMap[ApiParameters.SPECIAL_NEEDS] =
//        specialNeeds.toString()
//    if (!location.isNullOrEmpty()) queryMap[ApiParameters.LOCATION] = location
//    if (maxDistance.isNotNull()) queryMap[ApiParameters.DISTANCE] = maxDistance.toString()
//    if (!before.isNullOrEmpty()) queryMap[ApiParameters.BEFORE] = before
//    if (!after.isNullOrEmpty()) queryMap[ApiParameters.AFTER] = after
//    if (!sort.isNullOrEmpty()) queryMap[ApiParameters.SORT] = sort
//
//    return queryMap
//}

fun String.toAnimalParameters(moshi: Moshi): AnimalParameters {
    return try {
        val animalParametersAdapter = AnimalParametersJsonAdapter(moshi)
        animalParametersAdapter.fromJson(this)!!
    } catch (ioe: IOException) {
        Logger.e(ioe, ioe.message.orEmpty())
        AnimalParameters.noAnimalParameters
    }
}

fun AnimalParameters.toString(moshi: Moshi): String {
    val adapter = AnimalParametersJsonAdapter(moshi)
    return adapter.toJson(this) ?: throw AnimalParameters.AnimalParametersConvertError()
}

