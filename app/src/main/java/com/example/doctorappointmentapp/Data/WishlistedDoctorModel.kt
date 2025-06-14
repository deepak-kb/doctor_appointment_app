package com.example.doctorappointmentapp.Data

import android.os.Parcel
import android.os.Parcelable

data class WishlistedDoctorModel(
    val address: String = "",
    val biography: String = "",
    val id: Int = 0,
    val name: String = "",
    val picture: String = "",
    val special: String = "",
    val experiense: Int = 0,
    val location: String = "",
    val mobile: String = "",
    val patiens: String = "",
    val rating: Double = 0.0,
    val site: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(biography)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(picture)
        parcel.writeString(special)
        parcel.writeInt(experiense)
        parcel.writeString(location)
        parcel.writeString(mobile)
        parcel.writeString(patiens)
        parcel.writeDouble(rating)
        parcel.writeString(site)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WishlistedDoctorModel> {
        override fun createFromParcel(parcel: Parcel): WishlistedDoctorModel = WishlistedDoctorModel(parcel)
        override fun newArray(size: Int): Array<WishlistedDoctorModel?> = arrayOfNulls(size)
    }
}

fun WishlistedDoctorModel.toDoctorsModel(): DoctorsModel {
    return DoctorsModel(
        Address = address,
        Biography = biography,
        Id = id,
        Name = name,
        Picture = picture,
        Special = special,
        Expriense = experiense,
        Location = location,
        Mobile = mobile,
        Patiens = patiens,
        Rating = rating,
        Site = site
    )
}