package com.a3tecnology.bancodigital.data.repository.transaction

import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionDataSourceImpl @Inject constructor(
    database: FirebaseDatabase
): TransactionDataSource {

    private val transactionReference = database.reference
        .child("transaction")
        .child(FirebaseHelp.getUserId())

    override suspend fun saveTransaction(transaction: Transaction) {
       return suspendCoroutine { continuation ->
           transactionReference
               .child(transaction.id)
               .setValue(transaction)
               .addOnCompleteListener { task ->
                   if (task.isSuccessful) {

                       val dateReference = transactionReference
                           .child(transaction.id)
                           .child("date")
                       dateReference.setValue(ServerValue.TIMESTAMP)

                       continuation.resumeWith(Result.success(Unit))
                   } else {
                       task.exception?.let {
                           continuation.resumeWith(Result.failure(it))
                       }
                   }
               }
       }
    }

    override suspend fun getTransactions(): List<Transaction> {
        return suspendCoroutine { continuation ->
            transactionReference
                // atualiza as informações da tela pushando para baixo
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transactions = mutableListOf<Transaction>()

                        for (ds in snapshot.children) {
                           val transaction = ds.getValue(Transaction::class.java)
                            transaction?.let { transactions.add(it) }
                        }
                        continuation.resumeWith(Result.success(transactions))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException().let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                })
        }
    }
}