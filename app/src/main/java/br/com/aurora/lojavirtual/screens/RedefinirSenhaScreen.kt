package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.aurora.lojavirtual.utils.Validator
import br.com.aurora.lojavirtual.viewmodel.RedefinirSenhaViewModel

@Composable
fun RedefinirSenhaScreen(
    onVoltarLogin: () -> Unit,
    viewModel: RedefinirSenhaViewModel
) {
    var email by remember { mutableStateOf("") }
    val mensagem by viewModel.mensagem.collectAsState()
    val carregando by viewModel.carregando.collectAsState()
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Redefinir Senha", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!Validator.isEmailValid(email)) {
                    // Chamar API para enviar e-mail

                    errorMessage = "E-mail inválido"
                    viewModel.limparMensagem()
                    return@Button
                }

                viewModel.enviarLinkRedefinicao(email)
            },
            enabled = !carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if(carregando) "Enviando..." else "Enviar solicitação")
        }

        if (mensagem.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(mensagem, color = Color.Blue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onVoltarLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao login")
        }
    }
}
