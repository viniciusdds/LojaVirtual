package br.com.aurora.lojavirtual.utils

import android.util.Patterns

object Validator {

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordStrong(password: String): Boolean {
        // Exemplo: mínimo 6 caracteres, pelo menos 1 letra e 1 número
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$")
        return regex.matches(password)
    }

    fun isCPFValid(cpf: String): Boolean {
        val cleanCpf = cpf.replace(Regex("[^\\d]"), "")

        if (cleanCpf.length != 11 || cleanCpf.all { it == cleanCpf[0] }) return false

        val dv1 = cleanCpf.substring(0, 9).mapIndexed { index, c ->
            c.digitToInt() * (10 - index)
        }.sum().let {
            val resto = it % 11
            if (resto < 2) 0 else 11 - resto
        }

        val dv2 = cleanCpf.substring(0, 10).mapIndexed { index, c ->
            c.digitToInt() * (11 - index)
        }.sum().let {
            val resto = it % 11
            if (resto < 2) 0 else 11 - resto
        }

        return cleanCpf[9].digitToInt() == dv1 && cleanCpf[10].digitToInt() == dv2
    }

    fun isPhoneValid(phone: String): Boolean {
        // Valida números com DDD e 9 dígitos: (xx) 9xxxx-xxxx ou xxxxxxxxxxx
        val regex = Regex("^\\(?\\d{2}\\)?\\s?9?\\d{4}\\-?\\d{4}\$")
        return regex.matches(phone)
    }

    fun isNotEmpty(text: String): Boolean {
        return text.trim().isNotEmpty()
    }


}