package com.mfpe.rocketpvp.models

import android.os.Parcel
import android.os.Parcelable

data class Pokemon(
    val id: Int,
    val name: String,
    val atk: Int,
    val def: Int,
    val hp: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(atk)
        parcel.writeInt(def)
        parcel.writeInt(hp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }
}