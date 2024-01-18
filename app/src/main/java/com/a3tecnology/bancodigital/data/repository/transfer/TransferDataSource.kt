package com.a3tecnology.bancodigital.data.repository.transfer

import com.a3tecnology.bancodigital.data.model.Transfer

interface TransferDataSource {

    suspend fun getTransfer(id: String) : Transfer
    suspend fun saveTransfer(transfer: Transfer)
    suspend fun updateTransfer(transfer: Transfer)
    suspend fun saveTransferTransaction(transfer: Transfer)
    suspend fun updateTransferTransaction(transfer: Transfer)
}