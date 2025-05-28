package br.com.aurora.lojavirtual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.aurora.lojavirtual.model.Produto
import androidx.compose.foundation.layout.Column

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(pedidoConfirmado: List<Produto>?,  idUsuario: String) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pedidos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9932CC),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

        }
    }
}
