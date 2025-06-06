package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.VisualTransformation
import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.utils.Validator
import br.com.aurora.lojavirtual.viewmodel.LoginViewModel

@Composable
fun LoginScreen( loginViewModel: LoginViewModel, onLoginSuccess: (Usuario) -> Unit, onCadastroClick: () -> Unit,  onRedefinirSenhaClick: () -> Unit, modifier: Modifier = Modifier){
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val loginMessage by loginViewModel.loginMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if(senhaVisivel == true)  VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val imagem = if(senhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility
                val descricao = if(senhaVisivel) "Ocultar" else "Mostrar"

                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(imageVector = imagem, contentDescription = descricao)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(errorMessage.isNotEmpty()){
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red)
        }

        Button(
            onClick = {
                if (!Validator.isEmailValid(email.trim())) {
                    errorMessage = "E-mail inválido"
                    loginViewModel.limparMensagem()
                    return@Button
                }

                if (!Validator.isPasswordStrong(senha.trim())) {
                    errorMessage = "Senha fraca: mínimo 6 caracteres, letra e número"
                    loginViewModel.limparMensagem()
                    return@Button
                }

                loginViewModel.login(email.trim(), senha.trim(), { usuario ->
                    if(usuario != null){
                        onLoginSuccess(usuario)
                    }else{
                        errorMessage = "Erro: usuário inválido"
                    }
                })
                errorMessage = ""

//                if(email == "admin@teste.com" && senha == "123456"){
//                    errorMessage = ""
//                    onLoginSuccess()
//                }else{
//                    errorMessage = "E-mail ou senha incorretos."
//                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        if (loginMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginMessage,
                color = if (loginMessage.contains("sucesso", true)) Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onCadastroClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastre-se")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onRedefinirSenhaClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Esqueceu a senha?", color = Color.Blue)
        }

    }
}