package com.a3tecnology.bancodigital.data.enum

enum class TransactionOperation {
    DEPOSIT,
    RECHARGE,
    TRANSFER;

    companion object {
        fun getOperation(operation: TransactionOperation) : String {
            return when (operation) {
                DEPOSIT -> {
                    "Depósito"
                }
                RECHARGE -> {
                    "Recarga de Telefone"
                }
                TRANSFER -> {
                    "Transferência"
                }
            }
        }
    }
}