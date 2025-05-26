package br.com.aurora.lojavirtual.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.R
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(
    navController: NavController,
    onLogoutClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var imagemSelecionada by remember { mutableStateOf<Int?>(null) }

    //Lista de produtos simulada
    val produtos = remember {
        mutableStateListOf(
            Produto(1, "Camisa Polo", R.drawable.produto1, 59.90),
            Produto(2, "Tênis Esportivo", R.drawable.produto2, 189.90),
            Produto(3, "Calça Jeans", R.drawable.produto3, 129.90),
            Produto(4, "Boné Estiloso", R.drawable.produto4, 49.90)
        )
    }

    val totalCarrinho by derivedStateOf {
       produtos.sumOf{ it.quantidade }
    }

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
                    title = { Text("Lista de Produtos") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }){
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Carrinho com badge
                        BadgedBox(
                            badge = {
                                if(totalCarrinho > 0){
                                    Badge { Text(totalCarrinho.toString()) }
                                }
                            }
                        ) {
                           IconButton(onClick = {
                                // ação futura para abrir carrinho
                           }) {
                               Icon(
                                   imageVector = Icons.Default.ShoppingCart,
                                   contentDescription = "Carrinho",
                                   tint = Color.White
                               )
                           }
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
                ){}
            },
            containerColor = Color(0xFFFFFFFF)
        ){ padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp)
                    .background(Color.White)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(produtos.size){ index ->
                    val produto = produtos[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF9932CC) // Cor do TopBar
                        ),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = produto.imagemUrl),
                                contentDescription = produto.nome,
                                modifier = Modifier
                                    .height(100.dp)
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .clickable {
                                        imagemSelecionada = produto.imagemUrl
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                            Text("R$ %.2f".format(produto.preco), style =  MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        if(produto.quantidade > 0){
                                            produtos[index] = produto.copy(quantidade = produto.quantidade - 1)
                                        }
                                    },
                                    enabled = produto.quantidade > 0
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Remover")
                                  }

                                Text(produto.quantidade.toString())

                                IconButton(
                                    onClick = {
                                        produtos[index] = produto.copy(quantidade = produto.quantidade + 1)
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                                }
                            }
                        }
                    }
                }
            }

            // Modal com imagem ampliada
            if(imagemSelecionada != null){
                AnimatedVisibility(visible = imagemSelecionada != null) {
                    Dialog(onDismissRequest = { imagemSelecionada = null }) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 8.dp,
                                    color = Color(0xFF9932CC),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                        ){
                            Column {
                                Box(modifier = Modifier.fillMaxWidth()){
                                    IconButton(
                                        onClick = { imagemSelecionada = null },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                            .background(
                                                color = Color(0xFF9932CC),
                                                shape = RoundedCornerShape(50)
                                            )
                                            .shadow(4.dp, shape = RoundedCornerShape(50))
                                            .size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Fechar",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Image(
                                painter = painterResource(id = imagemSelecionada!!),
                                contentDescription = "Imagem Ampliada",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //.aspectRatio(1f)
                                    .height(500.dp)
                                    .padding(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }
}
