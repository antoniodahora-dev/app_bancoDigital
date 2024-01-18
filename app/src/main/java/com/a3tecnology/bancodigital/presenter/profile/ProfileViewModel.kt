package com.a3tecnology.bancodigital.presenter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.domain.profile.GetProfileUseCase
import com.a3tecnology.bancodigital.domain.profile.SaveImageProfileUseCase
import com.a3tecnology.bancodigital.domain.profile.SaveProfileUseCase
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveProfileImageUseCase: SaveImageProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    fun saveProfile(user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar


            saveProfileUseCase.invoke(user)
            emit(StateView.Success(null)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun saveImageProfile(imageProfile: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar


           val urlImage = saveProfileImageUseCase.invoke(imageProfile)
            emit(StateView.Success(urlImage)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val user = getProfileUseCase.invoke(FirebaseHelp.getUserId())
            emit(StateView.Success(user)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}