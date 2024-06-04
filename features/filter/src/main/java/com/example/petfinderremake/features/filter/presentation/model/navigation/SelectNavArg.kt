package com.example.petfinderremake.features.filter.presentation.model.navigation

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.io.IOException

@JsonClass(generateAdapter = true)
data class SelectNavArg(
    val names: List<String>,
    val filter: AnimalParameters,
    val selectType: SelectType
) {

    class SelectNavArgConvertError :
        IllegalStateException("An exception occurs during conversion SelectNavType")
}

fun String.toSelectNavArg(moshi: Moshi): SelectNavArg {
    return try {
        val adapter = SelectNavArgJsonAdapter(moshi)
        adapter.fromJson(this)!!
    } catch (ioe: IOException) {
        throw SelectNavArg.SelectNavArgConvertError()
    }
}

fun SelectNavArg.toString(moshi: Moshi): String {
    val adapter = SelectNavArgJsonAdapter(moshi)
    return adapter.toJson(this)
        ?: throw SelectNavArg.SelectNavArgConvertError()
}

