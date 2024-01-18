package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.data.model.Transfer
import com.a3tecnology.bancodigital.domain.transaction.GetBalanceUseCase
import com.a3tecnology.bancodigital.domain.transfer.SaveTransferTransactionUseCase
import com.a3tecnology.bancodigital.domain.transfer.SaveTransferUseCase
import com.a3tecnology.bancodigital.domain.transfer.UpdateTransferTransactionUseCase
import com.a3tecnology.bancodigital.domain.transfer.UpdateTransferUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel  @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val updateTransferUseCase: UpdateTransferUseCase,
    private val saveTransferTransactionUseCase: SaveTransferTransactionUseCase,
    private val updateTransferTransactionUseCase: UpdateTransferTransactionUseCase
) : ViewModel() {

    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val balance = getBalanceUseCase.invoke()
            emit(StateView.Success(balance)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            saveTransferUseCase.invoke(transfer)
            emit(StateView.Success(Unit))

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun updateTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            updateTransferUseCase.invoke(transfer)
            emit(StateView.Success(Unit))

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun saveTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            saveTransferTransactionUseCase.invoke(transfer)

            emit(StateView.Success(Unit)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun updateTransferTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            updateTransferTransactionUseCase.invoke(transfer)
            emit(StateView.Success(Unit))

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}