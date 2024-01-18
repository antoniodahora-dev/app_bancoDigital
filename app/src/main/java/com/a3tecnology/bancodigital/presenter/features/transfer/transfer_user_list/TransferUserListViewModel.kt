package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.a3tecnology.bancodigital.domain.profile.GetProfileListUseCase
import com.a3tecnology.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserListViewModel  @Inject constructor(
    private val getProfileListUseCase: GetProfileListUseCase
) : ViewModel() {

    fun getProfileList() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading()) // irá exibir progressBar

            val userList = getProfileListUseCase.invoke()
            emit(StateView.Success(userList)) // ira retornar em caso de Success

        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }
}