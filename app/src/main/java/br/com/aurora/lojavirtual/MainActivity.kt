package br.com.aurora.lojavirtual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import br.com.aurora.lojavirtual.screens.CadastroScreen
import br.com.aurora.lojavirtual.screens.LoginScreen
import br.com.aurora.lojavirtual.ui.theme.LojaVirtualTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.aurora.lojavirtual.network.RetrofitInstance
import br.com.aurora.lojavirtual.repository.UsuarioRepository
import br.com.aurora.lojavirtual.screens.CategoriasScreen
import br.com.aurora.lojavirtual.screens.HomeScreen
import br.com.aurora.lojavirtual.screens.PedidosScreen
import br.com.aurora.lojavirtual.screens.ProdutosScreen
import br.com.aurora.lojavirtual.screens.RedefinirSenhaScreen
import br.com.aurora.lojavirtual.viewmodel.CadastroViewModel
import br.com.aurora.lojavirtual.viewmodel.CadastroViewModelFactory
import br.com.aurora.lojavirtual.viewmodel.LoginViewModel
import br.com.aurora.lojavirtual.viewmodel.LoginViewModelFactory
import br.com.aurora.lojavirtual.viewmodel.RedefinirSenhaViewModel
import br.com.aurora.lojavirtual.viewmodel.RedefinirSenhaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LojaVirtualTheme {
                val navController = rememberNavController()
                val apiService = RetrofitInstance.api // sua instância do Retrofit
                val repository = UsuarioRepository(apiService)
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(repository)
                )


                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "produtos",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                loginViewModel = loginViewModel,
                                onLoginSuccess = {
                                    // ação depois do login
                                    navController.navigate("home"){
                                        popUpTo("login"){ inclusive = true }
                                    }
                                },
                                onCadastroClick = {
                                    navController.navigate("cadastro")
                                },
                                onRedefinirSenhaClick = {
                                    navController.navigate("redefinir")
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable("cadastro") {
                            val repository = UsuarioRepository(RetrofitInstance.api)
                            val factory = CadastroViewModelFactory(repository)
                            val viewModel: CadastroViewModel = viewModel(factory = factory)

                            CadastroScreen(
                                navController = navController,
                                viewModel = viewModel,
                                onCadastroConcluido = {
                                    navController.popBackStack("login", false)
                                }
                            )
                        }
                        composable("redefinir") {
                           val repository = UsuarioRepository(RetrofitInstance.api)
                           val factory = RedefinirSenhaViewModelFactory(repository)
                           val viewModel: RedefinirSenhaViewModel = viewModel(factory = factory)

                            RedefinirSenhaScreen(
                                onVoltarLogin = {
                                    navController.popBackStack()
                                },
                                viewModel = viewModel,
                            )
                        }
                        @OptIn(ExperimentalMaterial3Api::class)
                        composable("home") {
                            HomeScreen(
                                navController = navController,
                                onLogoutClick = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true } // Remove a Home da pilha
                                    }
                                }
                            )
                        }
                        composable("produtos") {
                            ProdutosScreen(
                                navController = navController,
                                onLogoutClick = {
                                    navController.navigate("login"){
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("categorias") {
                            CategoriasScreen()
                        }
                        composable("pedidos") {
                            PedidosScreen()
                        }
                    }
                }
            }
        }

    }
}

