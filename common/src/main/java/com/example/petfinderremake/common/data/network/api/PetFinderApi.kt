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

package com.example.petfinderremake.common.data.network.api

import com.example.petfinderremake.common.data.network.api.ApiConstants.ID_PATH
import com.example.petfinderremake.common.data.network.api.ApiConstants.TYPE_PATH
import com.example.petfinderremake.common.data.network.api.model.ApiAnimal
import com.example.petfinderremake.common.data.network.api.model.ApiAnimalBreeds
import com.example.petfinderremake.common.data.network.api.model.ApiAnimalTypes
import com.example.petfinderremake.common.data.network.api.model.ApiPaginatedAnimals
import com.example.petfinderremake.common.data.network.api.model.ApiType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PetFinderApi {

    @GET(ApiConstants.ANIMALS_ENDPOINT)
    suspend fun getAnimalsPage(
        @Query(ApiParameters.TYPE) type: String? = null,
        @Query(ApiParameters.BREED) breed: List<String>? = null,
        @Query(ApiParameters.SIZE) size: List<String>? = null,
        @Query(ApiParameters.GENDER) gender: List<String>? = null,
        @Query(ApiParameters.AGE) age: List<String>? = null,
        @Query(ApiParameters.COLOR) color: List<String>? = null,
        @Query(ApiParameters.COAT) coat: List<String>? = null,
        @Query(ApiParameters.STATUS) status: List<String>? = listOf("adoptable"),
        @Query(ApiParameters.NAME) name: String? = null,
        @Query(ApiParameters.ORGANIZATION) organization: String? = null,
        @Query(ApiParameters.GOOD_WITH_CHILDREN) goodWithChildren: Boolean? = null,
        @Query(ApiParameters.GOOD_WITH_DOGS) goodWithDogs: Boolean? = null,
        @Query(ApiParameters.GOOD_WITH_CATS) goodWithCats: Boolean? = null,
        @Query(ApiParameters.HOUSE_TRAINED) houseTrained: Boolean? = null,
        @Query(ApiParameters.DECLAWED) declawed: Boolean? = null,
        @Query(ApiParameters.SPECIAL_NEEDS) specialNeeds: Boolean? = null,
        @Query(ApiParameters.LOCATION) location: String? = null,
        @Query(ApiParameters.DISTANCE) distance: Int? = null,
        @Query(ApiParameters.BEFORE) before: String? = null,
        @Query(ApiParameters.AFTER) after: String? = null,
        @Query(ApiParameters.SORT) sort: String? = null,
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.LIMIT) pageSize: Int
    ): ApiPaginatedAnimals

    @GET(ApiConstants.ANIMAL_ENDPOINT)
    suspend fun getAnimal(@Path(ID_PATH) id: Long): ApiAnimal

    @GET(ApiConstants.TYPES_ENDPOINT)
    suspend fun getAnimalTypes(): ApiAnimalTypes

    @GET(ApiConstants.TYPE_ENDPOINT)
    suspend fun getAnimalType(@Path(TYPE_PATH) type: String): ApiType

    @GET(ApiConstants.BREEDS_ENDPOINT)
    suspend fun getAnimalBreeds(@Path(TYPE_PATH) type: String): ApiAnimalBreeds
}
