package com.a3tecnology.bancodigital.di

import com.a3tecnology.bancodigital.data.repository.auth.AuthFirebaseDataSource
import com.a3tecnology.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.a3tecnology.bancodigital.data.repository.deposit.DepositDataSource
import com.a3tecnology.bancodigital.data.repository.deposit.DepositDataSourceImpl
import com.a3tecnology.bancodigital.data.repository.recharge.RechargeDataSource
import com.a3tecnology.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import com.a3tecnology.bancodigital.data.repository.transaction.TransactionDataSource
import com.a3tecnology.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import com.a3tecnology.bancodigital.data.repository.transfer.TransferDataSource
import com.a3tecnology.bancodigital.data.repository.transfer.TransferDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsAuthDataSource(
        authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
    ): AuthFirebaseDataSource

    @Binds
    abstract fun bindsTransactionDataSource(
        transactionDataSourceImpl: TransactionDataSourceImpl
    ): TransactionDataSource

    @Binds
    abstract fun bindsDepositDataSource(
       depositDataSourceImp: DepositDataSourceImpl
    ): DepositDataSource

    @Binds
    abstract fun bindsRechargeDataSource(
        rechargeDataSourceImp: RechargeDataSourceImpl
    ): RechargeDataSource

    @Binds
    abstract fun bindsTransferDataSource(
        transferDataSourceImp: TransferDataSourceImpl
    ): TransferDataSource


}