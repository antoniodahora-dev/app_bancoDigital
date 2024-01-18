package com.a3tecnology.bancodigital.domain.deposit

import com.a3tecnology.bancodigital.data.model.Deposit
import com.a3tecnology.bancodigital.data.repository.deposit.DepositDataSourceImpl
import java.net.IDN
import javax.inject.Inject

class GetDepositUseCase @Inject constructor(
    private val depositDataSourceImpl: DepositDataSourceImpl
) {
    suspend operator fun invoke(id: String) : Deposit {
        return depositDataSourceImpl.getDeposit(id)
    }
}
