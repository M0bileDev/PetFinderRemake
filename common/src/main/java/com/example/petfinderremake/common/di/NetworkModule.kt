package com.example.petfinderremake.common.di

import com.example.petfinderremake.common.data.network.api.ApiConstants
import com.example.petfinderremake.common.data.network.api.PetFinderApi
import com.example.petfinderremake.common.data.network.interceptor.AuthenticationInterceptor
import com.example.petfinderremake.common.data.network.interceptor.LoggingInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object NetworkModule {

    @[Provides Singleton]
    fun providePetFinderApi(
        retrofitBuilder: Retrofit.Builder
    ): PetFinderApi = retrofitBuilder.build().create(PetFinderApi::class.java)

    @[Provides Singleton]
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))

    @[Provides Singleton]
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticationInterceptor: AuthenticationInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(authenticationInterceptor)
        .build()

    @[Provides Singleton]
    fun provideHttpLoggingInterceptor(
        loggingInterceptor: LoggingInterceptor
    ) = HttpLoggingInterceptor(loggingInterceptor).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @[Provides Singleton]
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}