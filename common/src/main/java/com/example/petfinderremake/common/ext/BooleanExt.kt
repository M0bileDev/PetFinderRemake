package com.example.petfinderremake.common.ext

import com.example.petfinderremake.common.presentation.utils.PresentationData
import com.example.petfinderremake.common.presentation.utils.commonString

fun Boolean?.orFalse() = this ?: false

fun Boolean.toPresentationData() =
    when (this) {
        true -> PresentationData.StringResource(commonString.yes)
        false -> PresentationData.StringResource(commonString.no)
    }