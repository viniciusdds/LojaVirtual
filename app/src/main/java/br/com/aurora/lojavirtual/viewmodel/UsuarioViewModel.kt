package br.com.aurora.lojavirtual.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import br.com.aurora.lojavirtual.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel : ViewModel() {
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    fun setUsuario(usuario: Usuario) {
        _usuario.value = usuario
    }

    fun limparUsuario() {
        _usuario.value = null
    }
}