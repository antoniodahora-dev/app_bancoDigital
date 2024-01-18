package com.a3tecnology.bancodigital.data.repository.deposit

import com.a3tecnology.bancodigital.data.model.Deposit

interface DepositDataSource {

    suspend fun saveDeposit(deposit: Deposit): Deposit
    suspend fun getDeposit(id: String): Deposit
}