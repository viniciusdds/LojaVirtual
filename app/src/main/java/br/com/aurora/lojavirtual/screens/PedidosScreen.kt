package br.com.aurora.lojavirtual.screens

import PedidoRepository
import android.opengl.Visibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.test.espresso.matcher.ViewMatchers
import br.com.aurora.lojavirtual.model.StatusPedido
import kotlinx.coroutines.launch


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
    var pedidoASerCancelado by remember { mutableStateOf<PedidoCompleto?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val selecionados by viewModel.pedidosSelecionados.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarPedidos(idUsuario)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)},
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
               if(selecionados.isNotEmpty()){
                   Column(
                       modifier = Modifier.fillMaxSize(),
                       verticalArrangement = Arrangement.Bottom,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Button(
                           onClick = {
                               /* ação de finalizar compra */
                               val dadosParaNavegar = viewModel.getIdsSelecionadosParaNavegacao(pedidos)
                               navController.navigate("detalhes_pedidos/$dadosParaNavegar")
                           },
                           colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                       ) {
                           Text("Finalizar a Compra", color = Color.White)
                       }
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

                    pedidoASerCancelado?.let { pedido ->
                        AlertDialog(
                            onDismissRequest = { pedidoASerCancelado = null },
                            title = { Text("Cancelar Pedido") },
                            text = { Text("Tem certeza que deseja cancelar o pedido ${pedido.codigo}?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.cancelarPedido(pedido.codigo, idUsuario,
                                        onResult = { sucesso ->
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = if(sucesso) "Pedido cancelado com sucesso!" else "Erro ao cancelar o pedido.",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                            if(sucesso) viewModel.carregarPedidos(idUsuario)
                                            pedidoASerCancelado = null
                                        }
                                    )

                                }) {
                                    Text("Sim", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { pedidoASerCancelado = null }) {
                                    Text("Não")
                                }
                            }
                        )
                    }


                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(pedidos) { pedido: PedidoCompleto ->
                            val estaSelecionado = pedido.codigo in selecionados
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        if(pedido.status == StatusPedido.PENDENTE){
                                            viewModel.toggleSelecaoPedido(pedido.codigo)
                                        }
                                    },
                                border = BorderStroke(
                                    width = if(estaSelecionado) 2.dp else 1.dp,
                                    color = if(estaSelecionado) Color.Blue else Color.LightGray
                                ),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){

                                    if(pedido.status == StatusPedido.PENDENTE){
                                        Checkbox(
                                            checked = estaSelecionado,
                                            onCheckedChange = null
                                        )
                                    }

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


                                        Text("Status: ${pedido.status}", color = when(pedido.status) {
                                            StatusPedido.PENDENTE -> Color(0xFFFF8C00)
                                            StatusPedido.PAGO -> Color(0xFF00BFFF)
                                            StatusPedido.ENVIADO ->  Color(0xFF8A2BE2)
                                            StatusPedido.ENTREGUE -> Color(0xFF32CD32)
                                            StatusPedido.CANCELADO -> Color.Red
                                        }, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text("Valor total: ${pedido.total}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = {
                                                /* cancelar pedido */
                                                if (pedido.status != StatusPedido.ENTREGUE) {
                                                    pedidoASerCancelado = pedido
                                                }else{
                                                    viewModel.finalizarPedido(pedido.codigo, idUsuario)
                                                }
                                            },
                                            colors = if(pedido.status != StatusPedido.ENTREGUE) ButtonDefaults.buttonColors(containerColor = Color.Red) else ButtonDefaults.buttonColors(containerColor = Color.Blue)
                                        ) {
                                            Text(if(pedido.status != StatusPedido.ENTREGUE) "Cancelar Pedido" else "Finalizar", color = Color.White)
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
}
