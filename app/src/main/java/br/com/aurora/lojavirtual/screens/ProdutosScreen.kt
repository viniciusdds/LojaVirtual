package br.com.aurora.lojavirtual.screens

import PedidoRepository
import ProdutoViewModelFactory
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.aurora.lojavirtual.model.ItemCarrinho
import br.com.aurora.lojavirtual.network.RetrofitInstance
import br.com.aurora.lojavirtual.repository.ProdutoRepository
import br.com.aurora.lojavirtual.viewmodel.PedidoViewModel
import br.com.aurora.lojavirtual.viewmodel.ProdutoViewModel
import com.seuapp.viewmodel.PedidoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(
    categoriaId: Int,
    idUsuario: String,
    navController: NavController
) {
    val repository = ProdutoRepository(RetrofitInstance.api) // ou da forma que usa
    val viewModelFactory = ProdutoViewModelFactory(repository)

    val produtoViewModel: ProdutoViewModel = viewModel(factory = viewModelFactory)
    val produtos = produtoViewModel.produtos
    val totalItens = produtoViewModel.quantidadeTotal()




    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    val leftDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var rightDrawerOpened by remember { mutableStateOf(false) }

    val pedidoViewModel: PedidoViewModel = viewModel(
        factory = PedidoViewModelFactory(PedidoRepository(RetrofitInstance.api))
    )

    Log.d("ProdutoScreen", "Usuário recebido: ${idUsuario ?: "null"}")

    // Carregar produtos ao abrir ou quando categoria mudar
    LaunchedEffect(categoriaId) {
        produtoViewModel.carregarProdutos(categoriaId)
    }

    Box {
    // Drawer da esquerda (Menu)
    ModalNavigationDrawer(
        drawerState = leftDrawerState,
        drawerContent = {
            Surface(
                color = Color(0xFF9932CC)
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = {
                        navController.navigate("home")
                        scope.launch { leftDrawerState.close() }
                    }
                    ) {
                        Text("Inicial")
                    }

                    TextButton(onClick = {
                        navController.navigate("categorias/${idUsuario}")
                        scope.launch { leftDrawerState.close() }
                    }) {
                        Text("Categorias")
                    }

                    TextButton(onClick = {
                        navController.navigate("pedidos/${idUsuario}")
                        scope.launch { leftDrawerState.close() }
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
                        IconButton(onClick = { scope.launch { leftDrawerState.open() } }){
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Carrinho com badge
                        BadgedBox(
                            badge = {
                                if(produtoViewModel.quantidadeTotal() > 0){
                                    Badge { Text(produtoViewModel.quantidadeTotal().toString()) }
                                }
                            },
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable{
                                    rightDrawerOpened = true
                                }
                        ) {
                            Icon(Icons.Default.AddShoppingCart, contentDescription = "Carrinho")
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
        ){ innerPadding  ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .background(Color.White)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(produtos){ produto  ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                                produtoSelecionado = produto
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF9932CC) // Cor do TopBar
                        ),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.White)
                                .clickable {
                                    produtoSelecionado = produto
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = produto.imagemResId),
                                contentDescription = produto.nome,
                                modifier = Modifier
                                    .height(100.dp)
                                    .background(Color.White)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                            Text("R$ ${produto.preco}", style =  MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                       produtoViewModel.removerQuantidade(produto)
                                    },
                                    enabled = produto.quantidade > 0
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Remover")
                                  }

                                Text(produto.quantidade.toString())

                                IconButton(
                                    onClick = {
                                       produtoViewModel.adicionarQuantidade(produto)
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
            produtoSelecionado?.let { produto ->
                AnimatedVisibility(
                    visible = produtoSelecionado != null,
                    enter = scaleIn(
                         initialScale = 0.5f,
                         animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(animationSpec = tween(400)),
                    exit = scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(durationMillis = 250)
                    ) + fadeOut(animationSpec = tween(250))
                ) {
                    Dialog(onDismissRequest = { produtoSelecionado = null }) {
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
                                        onClick = { produtoSelecionado = null },
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
                                painter = painterResource(id = produto.imagemResId),
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

        // Drawer da direita com fundo clicável para fechar ao tocar fora
        AnimatedVisibility(
            visible = rightDrawerOpened,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)) // Fundo escurecido
                    .clickable { rightDrawerOpened = false } // Fecha ao clicar fora
            ) {
                Surface(
                    modifier = Modifier
                        .width(250.dp)
                        .wrapContentHeight()
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable(enabled = false) {}, // Impede clique de fechar o drawer ao clicar dentro
                    color = Color(0xFF9932CC),
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Carrinho",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Button(
                            onClick = {
                                // Ação de confirmar pedido
                                val itensCarrinho = produtoViewModel.confirmarPedido()
                                pedidoViewModel.confirmarPedido(idUsuario = idUsuario, itens = itensCarrinho)

                                rightDrawerOpened = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Confirmar Pedido", color = Color(0xFF9932CC))
                        }
                    }
                }
            }
        }

    }
}
