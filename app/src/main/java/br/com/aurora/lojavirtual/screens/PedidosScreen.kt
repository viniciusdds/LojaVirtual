package br.com.aurora.lojavirtual.screens

import PedidoRepository
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.PedidoCompleto
import br.com.aurora.lojavirtual.network.RetrofitInstance
import br.com.aurora.lojavirtual.viewmodel.PedidoViewModel
import com.seuapp.viewmodel.PedidoViewModelFactory
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    idUsuario: String,
    navController: NavController,
    viewModel: PedidoViewModel = viewModel(factory = PedidoViewModelFactory(PedidoRepository(RetrofitInstance.api)))
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarPedidos(idUsuario)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pedidos") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF9932CC)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(containerColor = Color(0xFF9932CC)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { /* ação de finalizar compra */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                    ) {
                        Text("Finalizar a Compra", color = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Erro: $errorMessage", color = Color.Red)
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(pedidos) { pedido: PedidoCompleto ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Pedido: ${pedido.codigo}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text("Data: ${pedido.dataPedido}", color = Color.Black)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    pedido.itens.forEach { item ->
                                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                            Text("Produto: ${item.produto}", color = Color.DarkGray)
                                            Text("Quantidade: ${item.quantidade}", color = Color.DarkGray)
                                            Text("Preço: R$ ${"%.2f".format(item.preco)}", color = Color.DarkGray)
                                        }
                                    }

                                    Text("Valor total: ${pedido.total}", color = Color.Black, fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Button(
                                        onClick = { /* cancelar pedido */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Text("Cancelar Pedido", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
