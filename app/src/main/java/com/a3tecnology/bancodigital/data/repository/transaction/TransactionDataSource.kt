package com.a3tecnology.bancodigital.data.repository.transaction

import com.a3tecnology.bancodigital.data.model.Transaction

interface TransactionDataSource {

    // save transaction
    suspend fun saveTransaction(transaction: Transaction)

    // search transaction save in Firebase
    suspend fun getTransactions(): List<Transaction>
}