package com.mfpe.rocketpvp.models

data class IvRange(
    val value: Int,
    val description: String
) {
    override fun toString(): String {
        return description
    }
}