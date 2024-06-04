package com.example.petfinderremake.common.ext

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.petfinderremake.common.R
import com.example.petfinderremake.common.presentation.utils.commonDrawable

@SuppressLint("CheckResult")
fun ImageView.setImage(url: String, scaleType: ScaleType = ScaleType.NONE) {
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .error(R.drawable.ic_no_image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply {
            when (scaleType) {
                ScaleType.NONE -> return
                ScaleType.FIT_CENTER -> fitCenter()
                ScaleType.CENTER_CROP -> centerCrop()
            }
        }
        .into(this)
}

enum class ScaleType {
    NONE,
    FIT_CENTER,
    CENTER_CROP
}