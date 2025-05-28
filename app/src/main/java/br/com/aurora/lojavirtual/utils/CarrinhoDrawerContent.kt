package br.com.aurora.lojavirtual.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import br.com.aurora.lojavirtual.model.Produto

@Composable
fun CarrinhoDrawerContent(
    carrinho: List<Produto>,
    onFechar: () -> Unit,
    onConfirmar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Seu Carrinho", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onFechar) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        carrinho.filter { it.quantidade > 0 }.forEach { produto ->
            Text("${produto.nome} x${produto.quantidade} - R$ ${"%.2f".format(produto.preco * produto.quantidade)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val total = carrinho.sumOf { it.preco * it.quantidade }
        Text("Total: R$ ${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onConfirmar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9932CC))
        ) {
            Text("Confirmar Pedido")
        }
    }
}
