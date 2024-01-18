package com.a3tecnology.bancodigital.data.repository.auth

import com.a3tecnology.bancodigital.data.model.User

interface AuthFirebaseDataSource {

    suspend fun login(
        email: String,
        password: String
    )

    suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String
    ) : User

    suspend fun recover(
        email: String
    )
}