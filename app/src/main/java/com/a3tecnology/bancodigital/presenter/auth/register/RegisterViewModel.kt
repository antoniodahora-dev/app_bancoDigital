package com.a3tecnology.bancodigital.presenter.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.domain.auth.RegisterUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    fun register(
        name: String,
        email: String,
        password: String,
        phone: String ) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val user =
            registerUseCase.invoke(name, email, password, phone)
            emit(StateView.Success(user)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}