package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.Categoria
import br.com.aurora.lojavirtual.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(navController: NavController, idUsuario: String, onLogoutClick: () -> Unit){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categorias = listOf(
        Categoria(1, "Eletrônicos", Icons.Default.PhoneAndroid),
        Categoria(2, "Roupas", Icons.Default.Checkroom),
        Categoria(3, "Livros", Icons.Default.MenuBook),
        Categoria(4, "Alimentos", Icons.Default.Fastfood)
    )

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
                        navController.navigate("home")
                        scope.launch { drawerState.close() }
                    }
                    ) {
                        Text("Inicial")
                    }

                    TextButton(onClick = {
                        val categoriaId = 0 // por exemplo, ou pegue de uma lista
                        navController.navigate("produtos/${categoriaId}/${idUsuario}")
                        scope.launch { drawerState.close() }
                    }) {
                        Text("Produtos")
                    }

                    TextButton(onClick = {
                        navController.navigate("pedidos/${idUsuario}")
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
                    title = { Text("Categorias") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() }}) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF9932CC),
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xFF9932CC),
                    modifier = Modifier.height(50.dp)
                ){

                }
            },
            containerColor = Color(0xFFFFFFFF)
        ){ padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .background(Color.White)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(categorias.size) { index ->
                    val categoria = categorias[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clickable {
                                // Navegar para a tela de produtos da categoria
                                navController.navigate("produtos/${categoria.id}/${idUsuario}")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF9932CC)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = categoria.icone,
                                contentDescription = categoria.nome,
                                tint = Color.White ,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = categoria.nome,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }


            }
        }
    }
}