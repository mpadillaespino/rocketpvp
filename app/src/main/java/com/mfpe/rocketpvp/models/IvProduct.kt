package com.mfpe.rocketpvp.models

import android.os.Parcel
import android.os.Parcelable

data class IvProduct(
    val statProduct: Double,
    val level: Int,
    val atk: Int,
    val def: Int,
    val hp: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(statProduct)
        parcel.writeInt(level)
        parcel.writeInt(atk)
        parcel.writeInt(def)
        parcel.writeInt(hp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IvProduct> {
        override fun createFromParcel(parcel: Parcel): IvProduct {
            return IvProduct(parcel)
        }

        override fun newArray(size: Int): Array<IvProduct?> {
            return arrayOfNulls(size)
        }
    }
}