package com.a3tecnology.bancodigital.domain.auth

import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: String) : User {
        return authFirebaseDataSourceImpl.register(name, email, password, phone)
    }
}