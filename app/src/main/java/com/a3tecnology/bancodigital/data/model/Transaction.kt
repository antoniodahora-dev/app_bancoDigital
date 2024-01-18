package com.a3tecnology.bancodigital.data.model

import com.a3tecnology.bancodigital.data.enum.TransactionOperation
import com.a3tecnology.bancodigital.data.enum.TransactionType

data class Transaction(
    var id: String = "",
    val operation: TransactionOperation? = null,
    val date: Long = 0,
    val amount: Float = 0f,
    var type: TransactionType? = null
)
