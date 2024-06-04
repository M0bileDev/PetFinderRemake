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

object ApiConstants {

    const val BASE_ENDPOINT = "https://api.petfinder.com/v2/"
    const val AUTH_ENDPOINT = "oauth2/token/"

    const val ID_PATH = "id"
    private const val BREEDS_PATH = "breeds"
    const val TYPE_PATH = "type"

    const val ENDPOINT_ID_PATH = "{$ID_PATH}/"
    const val ENDPOINT_TYPE_PATH = "{$TYPE_PATH}/"

    const val ANIMALS_ENDPOINT = "animals/"
    const val TYPES_ENDPOINT = "types/"
    const val ANIMAL_ENDPOINT = "$ANIMALS_ENDPOINT$ENDPOINT_ID_PATH"
    const val TYPE_ENDPOINT = "$TYPES_ENDPOINT$ENDPOINT_TYPE_PATH"
    const val BREEDS_ENDPOINT = "$TYPE_ENDPOINT$BREEDS_PATH"

    const val KEY = "Create your own key at https://www.petfinder.com/developers/v2/docs/"
    const val SECRET = "Create your own secret at https://www.petfinder.com/developers/v2/docs/"
}

object ApiParameters {
    const val TOKEN_TYPE = "Bearer "
    const val AUTH_HEADER = "Authorization"
    const val GRANT_TYPE_KEY = "grant_type"
    const val GRANT_TYPE_VALUE = "client_credentials"
    const val CLIENT_ID = "client_id"
    const val CLIENT_SECRET = "client_secret"

    const val TYPE = "type"
    const val BREED = "breed"
    const val SIZE = "size"
    const val GENDER = "gender"
    const val AGE = "age"
    const val COLOR = "color"
    const val COAT = "coat"
    const val STATUS = "status"
    const val NAME = "name"
    const val ORGANIZATION = "organization"
    const val GOOD_WITH_CHILDREN = "good_with_children"
    const val GOOD_WITH_DOGS = "good_with_dogs"
    const val GOOD_WITH_CATS = "good_with_cats"
    const val HOUSE_TRAINED = "house_trained"
    const val DECLAWED = "declawed"
    const val SPECIAL_NEEDS = "special_needs"
    const val LOCATION = "location"
    const val DISTANCE = "distance"
    const val BEFORE = "before"
    const val AFTER = "after"
    const val SORT = "sort"
    const val PAGE = "page"
    const val LIMIT = "limit"
}
