package com.example.petfinderremake.common.domain.model.animal.media

data class Photo(
    val small: String,
    val medium: String,
    val full: String
) {

    companion object {
        const val NO_SIZE_AVAILABLE = ""
    }

    fun getSmallestAvailablePhoto(): String {
        return when {
            isValidPhoto(small) -> small
            isValidPhoto(medium) -> medium
            isValidPhoto(full) -> full
            else -> NO_SIZE_AVAILABLE
        }
    }

    fun getLargestAvailablePhoto(): String {
        return when {
            isValidPhoto(full) -> full
            isValidPhoto(medium) -> medium
            isValidPhoto(small) -> small
            else -> NO_SIZE_AVAILABLE
        }
    }

    private fun isValidPhoto(photo: String): Boolean {
        return photo.isNotEmpty()
    }
}