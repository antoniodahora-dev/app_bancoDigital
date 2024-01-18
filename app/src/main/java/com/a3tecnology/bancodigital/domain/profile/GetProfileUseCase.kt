package com.a3tecnology.bancodigital.domain.profile

import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke(id: String): User {
        return profileDataSourceImpl.getProfile(id)
    }
}