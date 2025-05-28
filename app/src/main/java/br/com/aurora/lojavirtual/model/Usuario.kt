package br.com.aurora.lojavirtual.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val nome: String,
    val cpf: String,
    val email: String,
    val senha: String = "",
    val telefone: String,
    val id_usuario: String = "", // <- CPF em MD5
) : Parcelable
