package com.a3tecnology.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.data.model.Recharge
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.domain.recharge.SaveRechargeUseCase
import com.a3tecnology.bancodigital.domain.transaction.SaveTransactionUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val saveRechargeUseCase: SaveRechargeUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    fun saveRecharge(recharge: Recharge) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val rechargeResult = saveRechargeUseCase.invoke(recharge)

            emit(StateView.Success(rechargeResult)) // ira retornar em caso de Success

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