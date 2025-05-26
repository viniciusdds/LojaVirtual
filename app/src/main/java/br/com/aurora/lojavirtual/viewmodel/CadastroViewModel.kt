package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.repository.UsuarioRepository
import kotlinx.coroutines.launch

class CadastroViewModel(private val repository: UsuarioRepository) : ViewModel() {

    fun cadastrarUsuario(
        nome: String,
        cpf: String,
        email: String,
        senha: String,
        telefone: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usuario = Usuario(nome, cpf, email, senha, telefone)
                val response = repository.cadastrar(usuario)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Erro ao cadastrar usuário.")
                }
            } catch (e: Exception) {
                onError("Falha: ${e.localizedMessage}")
            }
        }
    }
}
