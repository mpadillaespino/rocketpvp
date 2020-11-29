package com.mfpe.rocketpvp.models

data class League(
    val name: String,
    val maxCp: Int
) {
    override fun toString(): String {
        return name
    }
}