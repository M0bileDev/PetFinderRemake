package com.example.petfinderremake.common.ext

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.petfinderremake.common.domain.result.error.Error
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.presentation.utils.commonString
import com.example.petfinderremake.common.presentation.utils.networkErrorStringResource
import com.example.petfinderremake.common.presentation.utils.showErrorDialog
import com.example.petfinderremake.common.presentation.utils.showNotYetImplementedSnackBar
import com.example.petfinderremake.common.presentation.utils.storageErrorStringResource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

fun LifecycleOwner.observeError(errorFlow: Flow<Error>, view: View) = withLifecycleOwner {
    errorFlow.distinctUntilChanged().collectLatest { error ->
        val (errorMessage, titleMessage) = when (error) {
            is NetworkError -> networkErrorStringResource(error) to commonString.network_error
            is StorageError -> storageErrorStringResource(error) to commonString.storage_error
            else -> throw IllegalStateException("Unsupported type")
        }

        showErrorDialog(
            view.context,
            titleMessage,
            errorMessage,
            onPositiveButton = {
                showNotYetImplementedSnackBar(view)
            })
    }
}

fun LifecycleOwner.withLifecycleOwner(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit,
): Job {
    val job: Job
    with(this) {
        job = lifecycleScope.launch {
            repeatOnLifecycle(lifecycleState) {
                block()
            }
        }
    }
    return job
}