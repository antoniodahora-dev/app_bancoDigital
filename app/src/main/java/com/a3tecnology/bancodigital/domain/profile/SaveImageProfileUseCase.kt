package com.a3tecnology.bancodigital.domain.profile

import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class SaveImageProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke(imageProfile: String): String {
        return profileDataSourceImpl.saveImage(imageProfile)
    }
}