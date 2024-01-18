package com.a3tecnology.bancodigital.data.repository.profile

import com.a3tecnology.bancodigital.data.model.User

interface ProfileDataSource {

    suspend fun saveProfile(user: User)
    suspend fun getProfile(id: String): User
    suspend fun getProfileList(): List<User>
    suspend fun saveImage(imageProfile: String): String
}