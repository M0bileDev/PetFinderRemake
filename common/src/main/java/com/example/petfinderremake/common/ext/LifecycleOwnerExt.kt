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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun LifecycleOwner.subscribeError(
    view: View,
    errorObservable: Observable<Error>,
    jobBlock: (Job) -> Unit,
    onDispose: (Disposable) -> Unit
) = withLifecycleOwner(
    disposableBlock = {
        errorObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { error ->
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
    },
    jobBlock = jobBlock,
    onDispose = onDispose
)


fun LifecycleOwner.withLifecycleOwner(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    jobBlock: (Job) -> Unit,
    disposableBlock: () -> Disposable,
    onDispose: (Disposable) -> Unit = {}
) {
    with(this) {
        lifecycleScope.launch {
            repeatOnLifecycle(lifecycleState) {
                val dispose = disposableBlock()
                onDispose(dispose)
            }
        }.run {
            jobBlock(this)
        }
    }
}