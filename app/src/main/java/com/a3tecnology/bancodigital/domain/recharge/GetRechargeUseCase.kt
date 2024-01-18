package com.a3tecnology.bancodigital.domain.recharge

import com.a3tecnology.bancodigital.data.model.Recharge
import com.a3tecnology.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class GetRechargeUseCase @Inject constructor(
    private val rechargeDataSourceImpl: RechargeDataSourceImpl
) {
    suspend operator fun invoke(id: String) : Recharge {
        return rechargeDataSourceImpl.getRecharge(id)
    }
}
