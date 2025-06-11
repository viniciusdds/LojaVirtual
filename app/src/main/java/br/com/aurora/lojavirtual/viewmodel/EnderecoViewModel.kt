package br.com.aurora.lojavirtual.viewmodel

import DadosRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.DadosCompra
import br.com.aurora.lojavirtual.model.EnderecoAPI
import br.com.aurora.lojavirtual.model.EnderecoCompleto
import br.com.aurora.lojavirtual.model.ViaCepResponse
import br.com.aurora.lojavirtual.network.ViaCepService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnderecoViewModel(private val repository: DadosRepository) : ViewModel() {

    private val _dadosMessage = MutableStateFlow("")
    val dadosMessage: StateFlow<String> = _dadosMessage

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/ws/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ViaCepService::class.java)

    private val _cep = MutableStateFlow("")
    val cep = _cep.asStateFlow()

    private val _numero = MutableStateFlow("")
    val numero = _numero.asStateFlow()

    private val _endereco = MutableStateFlow<ViaCepResponse?>(null)
    val endereco = _endereco.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro = _erro.asStateFlow()

    fun onCepChanged(novoCep: String) {
        _cep.value = novoCep
    }

    fun onNumChanged(novoNumero: String) {
        _numero.value = novoNumero
    }

    fun buscarEndereco() {
        viewModelScope.launch {
            _isLoading.value = true
            _erro.value = null
            try {
                val response = service.buscarCep(_cep.value)
                if (response.isSuccessful) {
                    _endereco.value = response.body()
                } else {
                    _erro.value = "Erro ao buscar CEP"
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão $e"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun enviarDadosCompra(
        pedidosIds: String,
        email: String,
        formaPagamento: String,
        endereco: EnderecoAPI?,
        numero: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            try{
                // Validar dados
                if(endereco == null){
                    onError("Endereço inválido")
                    return@launch
                }

                val dadosCompra = DadosCompra(
                    pedidosIds = pedidosIds.split(","),
                    email = email,
                    formaPagamento = formaPagamento,
                    endereco = EnderecoCompleto(
                        logradouro = endereco.logradouro,
                        numero = numero,
                        bairro = endereco.bairro,
                        cidade = endereco.cidade,
                        estado = endereco.estado,
                        cep = endereco.cep
                    )
                )
                println("teste123")
                val response =  repository.enviarDadosCompra(dadosCompra)

                if (response.isSuccess) {
                    Log.d("DadosViewModel", "ID do usuário: $response")
                    onSuccess()
                } else {
                    onError("Erro ao enviar: ${response.isFailure}")
                }

            }catch (e: Exception){
                Log.d("DadosViewModel", "ID do usuário: $e")
                onError("Erro: ${e.message}")
            }
        }
    }

    fun limparMensagem() {
        _dadosMessage.value = ""
    }
}
