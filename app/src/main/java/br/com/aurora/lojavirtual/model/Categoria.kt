package br.com.aurora.lojavirtual.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Categoria(
    val id: Int,
    val nome: String,
    val icone: ImageVector
)