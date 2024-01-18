package com.a3tecnology.bancodigital.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.profile.GetProfileUseCase
import com.a3tecnology.bancodigital.domain.transaction.GetTransactionsUseCase
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    fun getTransactions() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar


           val transactions = getTransactionsUseCase.invoke()
            emit(StateView.Success(transactions)) // ira retornar em caso de Success

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