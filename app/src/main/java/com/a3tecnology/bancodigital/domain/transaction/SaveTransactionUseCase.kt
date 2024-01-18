package com.a3tecnology.bancodigital.domain.transaction

import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val transactionDataSourceImpl: TransactionDataSourceImpl
){
    suspend operator fun invoke(transaction: Transaction) {
        transactionDataSourceImpl.saveTransaction(transaction)
    }
}