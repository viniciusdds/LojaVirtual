package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.aurora.lojavirtual.R
import kotlinx.coroutines.launch
import androidx.navigation.NavController

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavController,
    onLogoutClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                color = Color(0xFF9932CC)
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = {
                        navController.navigate("produtos")
                        scope.launch { drawerState.close() }
                    }
                    ) {
                        Text("Produtos")
                    }

                    TextButton(onClick = {
                        navController.navigate("categorias")
                        scope.launch { drawerState.close() }
                    }) {
                        Text("Categorias")
                    }

                    TextButton(onClick = {
                        navController.navigate("pedidos")
                        scope.launch { drawerState.close() }
                    }) {
                        Text("Meus Pedidos")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Minha Loja") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onLogoutClick) {
                            Icon(Icons.Default.Logout, contentDescription = "Sair")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF9932CC),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xFF9932CC), // Cor de fundo azul escuro
                    modifier = Modifier.height(50.dp)
                ) {

                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.principal),
                    contentDescription = "Imagem da Loja",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
