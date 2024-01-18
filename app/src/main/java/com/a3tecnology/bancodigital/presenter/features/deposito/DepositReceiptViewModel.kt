package com.a3tecnology.bancodigital.presenter.features.deposito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.deposit.GetDepositUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositReceiptViewModel @Inject constructor(
    private val getDepositUseCase: GetDepositUseCase
) : ViewModel() {

    fun getDeposit(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val getDeposit = getDepositUseCase.invoke(id)

            emit(StateView.Success(getDeposit)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}