package com.a3tecnology.bancodigital.data.repository.profile

import android.net.Uri
import android.os.Build
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileDataSourceImpl @Inject constructor(
  database: FirebaseDatabase,
  storage: FirebaseStorage
) : ProfileDataSource {

    private val profileReference = database.reference
        .child("profile")
//        .child(FirebaseHelp.getUserId())

    private val profileStorageReference = storage.reference
        .child("image")
        .child("profiles")
        .child("${FirebaseHelp.getUserId()}.jpeg")


    override suspend fun saveProfile(user: User) {
        return suspendCoroutine { continuation ->
            profileReference
                .child(FirebaseHelp.getUserId())
                .setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getProfile(id: String): User {
        return suspendCoroutine { continuation ->
            profileReference
                .child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        continuation.resumeWith(Result.success(it))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })

        }
    }

    // trás toda a listagem de usuários do app
    override suspend fun getProfileList(): List<User> {
        return suspendCoroutine { continuation ->
            profileReference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userList: MutableList<User> = mutableListOf()

                        // ira percorrer por todos os usuários cadastrados
                        for (ds in snapshot.children) {
                            val user = ds.getValue(User::class.java)

                            user?.let {
                                userList.add(it)
                            }
                        }
                        continuation.resumeWith(Result.success(
                            userList.apply {
                                removeAll { it.id == FirebaseHelp.getUserId() }
                            }
                        ))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }
                })

        }
    }

    // return image Firebase format of String
    override suspend fun saveImage(imageProfile: String): String {
        return suspendCoroutine { continuation ->

            val uploadTask = profileStorageReference.putFile(Uri.parse(imageProfile))
            uploadTask.addOnSuccessListener {

                profileStorageReference.downloadUrl.addOnCompleteListener { task ->
                    continuation.resumeWith(Result.success(task.result.toString()))
                }
            }.addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}