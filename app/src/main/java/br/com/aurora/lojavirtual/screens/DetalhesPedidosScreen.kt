package br.com.aurora.lojavirtual.screens

import PedidoRepository
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.aurora.lojavirtual.viewmodel.PedidoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.network.RetrofitInstance
import com.seuapp.viewmodel.PedidoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesPedidosScreen(
    pedidosIds: String,
    navController: NavController,
    viewModel: PedidoViewModel = viewModel(factory = PedidoViewModelFactory(PedidoRepository(RetrofitInstance.api)))
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Logs para depuração
    LaunchedEffect(pedidosIds) {
        Log.d("DetalhesScreen", "IDs recebidos: $pedidosIds")
        Log.d("DetalhesScreen", "Total de pedidos carregados: ${pedidos.size}")
    }


    val pedidosDetalhados = pedidosIds.split(",").mapNotNull { entrada ->
        val partes = entrada.split("-")
        if (partes.size == 3) {
            Triple(
                partes[0], // Código do pedido
                partes[1].toDoubleOrNull() ?: 0.0, // Valor total
                partes[2].split(";") // Lista de produtos
            )
        } else null
    }

    val totalGeral = pedidosDetalhados.sumOf { it.second }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos Selecionados") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF9932CC)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF9932CC),
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Total à pagar: R$ ${"%.2f".format(totalGeral)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp)) // espaço entre texto e botão
                    Button(
                        onClick = { navController.navigate("dados_compra/$pedidosIds")  },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                    ) {
                        Text("Finalizar Compra", color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(count = pedidosDetalhados.size) { index ->
                    val item = pedidosDetalhados[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.LightGray
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Pedido: ${item.first}", fontWeight = FontWeight.Bold, color = Color.Black)

                            // Lista de produtos
                            Text(
                                "Produtos:",
                                modifier = Modifier.padding(top = 8.dp),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                item.third.forEach { produto ->
                                    Text("- $produto", color = Color.Black)
                                }
                            }

                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Valor do pedido: ", color = Color.Black, fontWeight = FontWeight.Medium)
                                Text("R$ ${"%.2f".format(item.second)}", color = Color.Black)
                            }
                        }

                    }
                }
            }
    }
}