package com.a3tecnology.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
    var id: String = "",
    var idUserReceived: String = "",
    var idUserSent: String = "",
    var date: Long = 0,
    var amount: Float = 0f
): Parcelable {
    init {
        // identificador unico do deposito
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}
