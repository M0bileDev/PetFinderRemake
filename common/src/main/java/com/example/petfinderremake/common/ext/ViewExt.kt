package com.example.petfinderremake.common.ext

import android.view.View

fun View.onClickAnimateWithAction(changeVisibilityAction: () -> Unit) {
    setOnClickListener {
        animate().apply {
            val rotation = rotation
            rotation(if (rotation == 0f) 180f else 0f)
            duration = 300
        }.start()
        changeVisibilityAction()
    }
}