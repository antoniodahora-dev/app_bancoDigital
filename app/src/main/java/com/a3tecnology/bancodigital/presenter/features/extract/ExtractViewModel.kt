package com.a3tecnology.bancodigital.presenter.features.extract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.transaction.GetTransactionsUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ExtractViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
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

}