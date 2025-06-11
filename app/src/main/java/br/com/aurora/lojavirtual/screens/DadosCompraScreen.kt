package br.com.aurora.lojavirtual.screens

import DadosRepository
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.EnderecoAPI
import br.com.aurora.lojavirtual.network.RetrofitInstance
import br.com.aurora.lojavirtual.utils.Validator
import br.com.aurora.lojavirtual.viewmodel.EnderecoViewModel
import br.com.aurora.lojavirtual.viewmodel.EnderecoViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DadosCompraScreen(
    pedidosIds: String,
    navController: NavController,
    viewModel: EnderecoViewModel = viewModel(factory = EnderecoViewModelFactory (DadosRepository(RetrofitInstance.api)))
) {
    val cep by viewModel.cep.collectAsState()
    val numero by viewModel.numero.collectAsState()
    val endereco by viewModel.endereco.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val erro by viewModel.erro.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    val dadosMessage by viewModel.dadosMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var formaPagamento by remember { mutableStateOf("PIX") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dados da Compra") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF9932CC)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(containerColor = Color(0xFF9932CC)) {
                Button(
                    onClick = {
                        if (!Validator.isEmailValid(email.trim())) {
                            errorMessage = "E-mail inválido"
                            viewModel.limparMensagem()
                            return@Button
                        }

                        if (!Validator.isNotEmpty(numero.trim())) {
                            errorMessage = "Informe o número"
                            viewModel.limparMensagem()
                            return@Button
                        }

                        if (!Validator.isCepValid(cep.trim())) {
                            errorMessage = "Informe o CEP"
                            viewModel.limparMensagem()
                            return@Button
                        }

                        viewModel.enviarDadosCompra(
                            pedidosIds = pedidosIds,
                            email = email,
                            formaPagamento = formaPagamento,
                            endereco = endereco?.let { viaCepResponse ->
                                EnderecoAPI(
                                    logradouro = viaCepResponse.logradouro ?: "",
                                    bairro = viaCepResponse.bairro ?: "",
                                    cidade = viaCepResponse.localidade ?: "",
                                    estado = viaCepResponse.uf ?: "",
                                    cep = viaCepResponse.cep ?: ""
                                )
                            },
                            numero = numero,
                            onSuccess = {
                                navController.navigate("pagamento/$formaPagamento/$pedidosIds")
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )

                        // Aqui você pode validar e enviar os dados
                        //navController.navigate("confirmacao/$pedidosIds") // Ex: próxima tela
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                ) {
                    Text("Pagamento", color = Color.White)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Campo de CEP
            OutlinedTextField(
                value = cep,
                onValueChange = {  viewModel.onCepChanged(it) },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            Button(
                onClick = { viewModel.buscarEndereco() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar CEP")
            }

            OutlinedTextField(
                value = numero,
                onValueChange = {  viewModel.onNumChanged(it) },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)

            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
            }

            erro?.let {
                Text("Erro: $it", color = MaterialTheme.colorScheme.error)
            }

            // Endereço retornado
            endereco?.let { endereco ->
                Spacer(modifier = Modifier.height(16.dp))
                Text("Logradouro: ${endereco.logradouro ?: "-"}", color = Color.Black)
                Text("Bairro: ${endereco.bairro ?: "-"}", color = Color.Black)
                Text("Cidade: ${endereco.localidade ?: "-"}", color = Color.Black)
                Text("Estado: ${endereco.uf ?: "-"}", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )

            if(errorMessage.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Forma de Pagamento", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            // Cartão PIX
            Card(
                onClick = { formaPagamento = "PIX" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                border = BorderStroke(
                    2.dp,
                    if (formaPagamento == "PIX") Color(0xFF00BFFF) else Color.LightGray
                ),
                colors = CardDefaults.cardColors(Color(0xFF9932CC))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = formaPagamento == "PIX",
                        onClick = { formaPagamento = "PIX" }
                    )
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Pix",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF32BCAD) // Cor verde do Pix
                    )
                    Text("PIX", modifier = Modifier.padding(start = 8.dp))
                }
            }

            // Cartão Boleto
            Card(
                onClick = { formaPagamento = "BOLETO" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                border = BorderStroke(
                    2.dp,
                    if (formaPagamento == "BOLETO") Color(0xFF00BFFF) else Color.LightGray
                ),
                colors = CardDefaults.cardColors(Color(0xFF9932CC))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = formaPagamento == "BOLETO",
                        onClick = { formaPagamento = "BOLETO" }
                    )
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "Boleto",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF32BCAD) // Cor roxa
                    )
                    Text("Boleto", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

