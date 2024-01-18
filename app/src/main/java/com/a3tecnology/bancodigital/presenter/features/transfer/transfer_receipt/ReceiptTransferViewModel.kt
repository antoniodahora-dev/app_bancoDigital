package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.profile.GetProfileUseCase
import com.a3tecnology.bancodigital.domain.transfer.GetTransferUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ReceiptTransferViewModel  @Inject constructor(
    private val getTransferUseCase: GetTransferUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    fun getTransfer(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val userTransfer = getTransferUseCase.invoke(id)
            emit(StateView.Success(userTransfer))

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

    fun getProfile(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val user = getProfileUseCase.invoke(id)
            emit(StateView.Success(user))

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

}