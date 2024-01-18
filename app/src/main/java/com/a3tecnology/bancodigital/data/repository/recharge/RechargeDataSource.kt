package com.a3tecnology.bancodigital.data.repository.recharge

import com.a3tecnology.bancodigital.data.model.Recharge

interface RechargeDataSource {

    suspend fun saveRecharge(recharge: Recharge): Recharge
    suspend fun getRecharge(id: String): Recharge
}