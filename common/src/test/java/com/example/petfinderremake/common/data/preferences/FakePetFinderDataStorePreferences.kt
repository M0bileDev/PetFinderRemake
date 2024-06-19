package com.example.petfinderremake.common.data.preferences

import com.example.petfinderremake.common.domain.preferences.Preferences
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

class FakePetFinderDataStorePreferences : Preferences {

    private var fakeToken = ""
    private var fakeTokenExpirationTime = -1L
    private var fakeTokenType = ""
    private var fakeNotificationsPermanentlyDenied: Boolean = false

    override fun putToken(token: String): Completable {
        fakeToken = token
        return Completable.complete()
    }

    override fun getToken(): Flowable<String> {
        return Observable.just(fakeToken).toFlowable(BackpressureStrategy.LATEST)
    }

    override fun putTokenExpirationTime(time: Long): Completable {
        fakeTokenExpirationTime = time
        return Completable.complete()

    }

    override fun getTokenExpirationTime(): Flowable<Long> {
        return Observable.just(fakeTokenExpirationTime).toFlowable(BackpressureStrategy.LATEST)
    }

    override fun putTokenType(tokenType: String): Completable {
        fakeTokenType = tokenType
        return Completable.complete()

    }

    override fun getTokenType(): Flowable<String> {
        return Observable.just(fakeTokenType).toFlowable(BackpressureStrategy.LATEST)
    }

    override fun deleteTokenInfo(): Completable {
        fakeToken = ""
        fakeTokenType = ""
        fakeTokenExpirationTime = -1L
        return Completable.complete()

    }

    override fun putNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean): Completable {
        fakeNotificationsPermanentlyDenied = isPermanentlyDenied
        return Completable.complete()
    }

    override fun getNotificationPermanentlyDenied(): Flowable<Boolean> {
        return Observable.just(fakeNotificationsPermanentlyDenied)
            .toFlowable(BackpressureStrategy.LATEST)
    }

}