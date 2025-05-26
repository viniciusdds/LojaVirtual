package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.utils.PhoneVisualTransformation
import br.com.aurora.lojavirtual.utils.Validator
import androidx.compose.material3.TextField
import br.com.aurora.lojavirtual.viewmodel.CadastroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    navController: NavController,
    viewModel: CadastroViewModel,
    onCadastroConcluido: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = telefone,
                onValueChange = { telefone = it.filter { it.isDigit() } },
                label = { Text("Telefone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                visualTransformation = PhoneVisualTransformation()
            )

            mensagemErro?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                if(!Validator.isNotEmpty(nome)){
                    mensagemErro = "Informe o seu nome"
                    return@Button
                }

                if(!Validator.isCPFValid(cpf)){
                    mensagemErro = "CPF inválido"
                    return@Button
                }

                if(!Validator.isEmailValid(email)){
                    mensagemErro = "E-mail inválido"
                    return@Button
                }

                if (!Validator.isPasswordStrong(senha)) {
                    mensagemErro = "Senha fraca: mínimo 6 caracteres, letra e número"
                    return@Button
                }

                viewModel.cadastrarUsuario(
                    nome, cpf, email, senha, telefone,
                    onSuccess = { onCadastroConcluido() },
                    onError = { mensagemErro = it }
                )
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar")
            }
        }
    }
}
