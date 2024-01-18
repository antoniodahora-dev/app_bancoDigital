package com.a3tecnology.bancodigital.presenter.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.auth.RecoverUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor(
    private val recoverUseCase: RecoverUseCase
) : ViewModel() {
    fun recover(email: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar


            recoverUseCase.invoke(email)
            emit(StateView.Success(null)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}