package com.a3tecnology.bancodigital.domain.transfer

import com.a3tecnology.bancodigital.data.model.Transfer
import com.a3tecnology.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class SaveTransferUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {
    suspend operator fun invoke(transfer: Transfer) {
        return transferDataSourceImpl.saveTransfer(transfer)
    }
}