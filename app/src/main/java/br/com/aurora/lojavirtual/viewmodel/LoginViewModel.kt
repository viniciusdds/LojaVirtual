package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginMessage = MutableStateFlow("")
    val loginMessage: StateFlow<String> = _loginMessage

    fun login(email: String, senha: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.loginUsuario(email, senha)
                if (response.sucesso) {
                    _loginMessage.value = "Login realizado com sucesso!"
                    onSuccess()
                } else {
                    _loginMessage.value = response.mensagem
                }
            } catch (e: Exception) {
                _loginMessage.value = "Erro ao fazer login: ${e.message}"
            }
        }
    }

    fun limparMensagem() {
        _loginMessage.value = ""
    }
}
