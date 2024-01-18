package com.a3tecnology.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    var name: String = "",
    val email: String = "",
    @get:Exclude // não irá salvar o campo senha no Realtime Database
    val password: String = "",
    var phone: String = "",
    var image: String = ""
): Parcelable
