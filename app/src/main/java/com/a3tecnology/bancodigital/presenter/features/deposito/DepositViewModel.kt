package com.a3tecnology.bancodigital.presenter.features.deposito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.data.model.Deposit
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.domain.deposit.SaveDepositUseCase
import com.a3tecnology.bancodigital.domain.transaction.SaveTransactionUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val saveDepositUseCase: SaveDepositUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    fun saveDeposit(deposit: Deposit) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val depositResult = saveDepositUseCase.invoke(deposit)

            emit(StateView.Success(depositResult)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun saveTransaction(transaction: Transaction) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar


          saveTransactionUseCase.invoke(transaction)

            emit(StateView.Success(Unit)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}