package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import br.com.aurora.lojavirtual.repository.UsuarioRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RedefinirSenhaViewModel(private val repository: UsuarioRepository): ViewModel(){
    private val _mensagem = MutableStateFlow("")
    val mensagem: StateFlow<String> = _mensagem

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando

    fun enviarLinkRedefinicao(email: String){
        viewModelScope.launch {
            _carregando.value = true

            try {
                val response = repository.enviarLinkRedefinicao(email)
                _mensagem.value = response.mensagem
            }catch (e: Exception){
                _mensagem.value = "Erro: ${e.message}"
            }finally {
                _carregando.value = false
            }
        }
    }

    fun limparMensagem() {
        _mensagem.value = ""
    }
}