package com.a3tecnology.bancodigital.domain.deposit

import com.a3tecnology.bancodigital.data.model.Deposit
import com.a3tecnology.bancodigital.data.repository.deposit.DepositDataSourceImpl
import javax.inject.Inject

class SaveDepositUseCase @Inject constructor(
    private val depositDataSourceImpl: DepositDataSourceImpl
) {
    suspend operator fun invoke(deposit: Deposit) : Deposit {
        return depositDataSourceImpl.saveDeposit(deposit)
    }
}
