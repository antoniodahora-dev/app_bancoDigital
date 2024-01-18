package com.a3tecnology.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.recharge.GetRechargeUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeReceiptViewModel @Inject constructor(
    private val getRechargeUseCase: GetRechargeUseCase,
) : ViewModel() {

    fun getRecharge(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val getRecharge = getRechargeUseCase.invoke(id)

            emit(StateView.Success(getRecharge)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}