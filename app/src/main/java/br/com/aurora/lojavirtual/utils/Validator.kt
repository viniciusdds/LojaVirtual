package br.com.aurora.lojavirtual.utils

import android.util.Patterns

object Validator {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}