package com.example.petfinderremake.filter

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.testing.EmptyFragmentActivity
import com.example.petfinderremake.features.filter.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(
            intent.getIntExtra(
                EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY,
                androidx.fragment.testing.manifest.R.style.FragmentScenarioEmptyFragmentActivityTheme
            )
        )
    }
}