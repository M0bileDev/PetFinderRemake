/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.petfinderremake.common.data.network.interceptor

import com.example.petfinderremake.common.data.network.api.ApiConstants
import com.example.petfinderremake.common.data.network.api.ApiConstants.AUTH_ENDPOINT
import com.example.petfinderremake.common.data.network.api.ApiConstants.BASE_ENDPOINT
import com.example.petfinderremake.common.data.network.api.ApiParameters.AUTH_HEADER
import com.example.petfinderremake.common.data.network.api.ApiParameters.CLIENT_ID
import com.example.petfinderremake.common.data.network.api.ApiParameters.CLIENT_SECRET
import com.example.petfinderremake.common.data.network.api.ApiParameters.GRANT_TYPE_KEY
import com.example.petfinderremake.common.data.network.api.ApiParameters.GRANT_TYPE_VALUE
import com.example.petfinderremake.common.data.network.api.ApiParameters.TOKEN_TYPE
import com.example.petfinderremake.common.data.network.api.model.ApiToken
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.preferences.delete.DeleteTokenInfoUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.get.GetTokenExpirationTimeUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.get.GetTokenUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.put.PutTokenExpirationTimeUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.put.PutTokenTypeUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.put.PutTokenUseCase
import com.example.petfinderremake.logging.Logger
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.time.Instant
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val deleteTokenInfoUseCase: DeleteTokenInfoUseCase,
    private val getTokenExpirationTimeUseCase: GetTokenExpirationTimeUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val putTokenExpirationTimeUseCase: PutTokenExpirationTimeUseCase,
    private val putTokenTypeUseCase: PutTokenTypeUseCase,
    private val putTokenUseCase: PutTokenUseCase,
    private val moshi: Moshi
) : Interceptor {

    companion object {
        const val UNAUTHORIZED = 401
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var token = ""
        var tokenExpirationTime: Instant = Instant.now()
        runBlocking {
            launch { getTokenUseCase().first().onSuccess { token = it.success } }
            launch {
                getTokenExpirationTimeUseCase().first()
                    .onSuccess { tokenExpirationTime = Instant.ofEpochSecond(it.success) }
            }
        }

        val request = chain.request()

        // For requests that don't need authentication
        // if (chain.request().headers[NO_AUTH_HEADER] != null) return chain.proceed(request)
        val interceptedRequest: Request

        if (tokenExpirationTime.isAfter(Instant.now())) {
            // token is still valid, so we can proceed with the request
            interceptedRequest = chain.createAuthenticatedRequest(token)
        } else {

            // Token expired. Gotta refresh it before proceeding with the actual request
            val tokenRefreshResponse = chain.refreshToken()

            interceptedRequest = if (tokenRefreshResponse.isSuccessful) {
                val newToken = mapToken(tokenRefreshResponse)

                if (newToken.isValid()) {
                    storeNewToken(newToken)
                    chain.createAuthenticatedRequest(newToken.accessToken!!)
                } else {
                    request
                }
            } else {
                request
            }
        }

        return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader(AUTH_HEADER, TOKEN_TYPE + token)
            .build()
    }

    private fun Interceptor.Chain.refreshToken(): Response {
        val url = BASE_ENDPOINT + AUTH_ENDPOINT

        val body = FormBody.Builder()
            .add(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
            .add(CLIENT_ID, ApiConstants.KEY)
            .add(CLIENT_SECRET, ApiConstants.SECRET)
            .build()

        val tokenRefresh = request()
            .newBuilder()
            .post(body)
            .url(url)
            .build()

        return proceedDeletingTokenIfUnauthorized(tokenRefresh)
    }

    private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request): Response {

        val response = proceed(request)

        if (response.code == UNAUTHORIZED) {
            runBlocking { deleteTokenInfoUseCase() }
        }

        return response
    }

    private fun mapToken(tokenRefreshResponse: Response): ApiToken {
        val tokenAdapter = moshi.adapter(ApiToken::class.java)

        return try {
            val responseBody = tokenRefreshResponse.body!!
            tokenAdapter.fromJson(responseBody.string())!!
        } catch (ioe: IOException) {
            Logger.e(ioe.cause, ioe.message.orEmpty())
            ApiToken.INVALID
        }
    }

    private fun storeNewToken(apiToken: ApiToken) {
        runBlocking {
            // TODO: Add logic to handle null type, and results
            launch { putTokenTypeUseCase(apiToken.tokenType!!) }
            launch { putTokenExpirationTimeUseCase(apiToken.expiresAt) }
            launch { putTokenUseCase(apiToken.accessToken!!) }
        }

    }


}
