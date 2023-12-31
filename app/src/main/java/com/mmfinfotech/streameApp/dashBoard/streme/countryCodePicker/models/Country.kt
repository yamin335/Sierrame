package com.mmfinfotech.streameApp.dashBoard.streme.countryCodePicker.models

import android.os.Parcel
import android.os.Parcelable

data class Country(
    val currencytSymbol: String?,
    val isoCode: String?,
    val currencyCode: String?,
    val countryCode: String?,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currencytSymbol)
        parcel.writeString(isoCode)
        parcel.writeString(currencyCode)
        parcel.writeString(countryCode)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }

}