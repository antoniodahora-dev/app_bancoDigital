package com.a3tecnology.bancodigital.util

import com.a3tecnology.bancodigital.R
import com.google.firebase.auth.FirebaseAuth

class FirebaseHelp {

    //function verify authentication
    companion object {
        fun getAuth() = FirebaseAuth.getInstance()

        fun isAuthentication() = FirebaseAuth.getInstance().currentUser != null

        // return id user
        fun getUserId() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // message of error Firebase
        fun validatorError(error: String) : Int {
            return when {
                error.contains("There is no user record corresponding to this identifier") ->
                {  R.string.txt_email_send_recover_error }

                error.contains(
                    "The supplied auth credential is incorrect, malformed or has expired.") -> {
                    R.string.account_not_registered_register_fragment
                }
                error.contains("The email address is badly formatted.") -> {
                    R.string.invalid_email_register_fragment
                }

                error.contains("The email address is already in use by another account.") -> {
                    R.string.email_in_use_register_fragment
                }

                error.contains("The given password is invalid. [ Password should be at least 6 characters ]")
                -> { R.string.strong_password_register_fragment}

                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.email_password_login_error
                }

                else -> {
                    R.string.error_generic
                }
            }
        }


    }

}