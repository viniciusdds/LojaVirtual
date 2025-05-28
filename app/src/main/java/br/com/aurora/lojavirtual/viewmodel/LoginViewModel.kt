package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginMessage = MutableStateFlow("")
    val loginMessage: StateFlow<String> = _loginMessage

    private val _usuarioLogado = MutableStateFlow<Usuario?>(null)
    val usuarioLogado: StateFlow<Usuario?> = _usuarioLogado

    fun login(email: String, senha: String, onSuccess: (Usuario) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.loginUsuario(email, senha)
                if (response.sucesso) {
                    _loginMessage.value = "Login realizado com sucesso!"
                    _usuarioLogado.value = response.usuario // <-- Aqui você salva o usuário logado
                    println(response.usuario?.nome+" teste")
                    onSuccess(response.usuario!!)
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

    fun navegarParaProdutos(navController: NavController, categoriaId: Int, idUsuario: String){
        navController.navigate("produtos/$categoriaId/$idUsuario")
    }
}
