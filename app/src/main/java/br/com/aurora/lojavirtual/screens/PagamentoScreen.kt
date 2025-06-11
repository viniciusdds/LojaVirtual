package br.com.aurora.lojavirtual.screens

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.network.RetrofitInstance
import br.com.aurora.lojavirtual.repository.PagamentoRepository
import br.com.aurora.lojavirtual.viewmodel.PagamentoViewModel
import br.com.aurora.lojavirtual.viewmodel.PagamentoViewModelFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.UUID
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentoScreen(
    formaPagamento: String,
    idsPedidos: String,
    navController: NavController,
    viewModel: PagamentoViewModel = viewModel(factory = PagamentoViewModelFactory(PagamentoRepository(RetrofitInstance.api)))
) {
    // Gera dados fictícios para demonstração
    val codigoPix = remember { UUID.randomUUID().toString() }
    val codigoBoleto = remember { generateRandomBarcode() }
    val clipboardManager = LocalClipboardManager.current
    val qrBitmap = remember { generateQrCodeBitmap(codigoPix) }
    val barcodeBitmap = remember { generateBarcodeBitmap(codigoBoleto) }

    val pagamentoEfetuado by viewModel.pagamentoEfetuado.collectAsState()

    // Efeito para voltar automaticamente após 5 segundos
    LaunchedEffect(pagamentoEfetuado) {
        if (pagamentoEfetuado) {
            delay(5000) // 5 segundos
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Pagamento") },
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
            if(!pagamentoEfetuado){
                BottomAppBar(containerColor = Color(0xFF9932CC)) {
                    Button(
                        onClick = {
                            // Lógica para confirmar pagamento
                            viewModel.efetuarPagamento(idsPedidos)

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                    ) {
                        Text("Confirmar Pagamento", color = Color.White)
                    }
                }
            }
        }
    ) { padding ->

        if(pagamentoEfetuado){
            AnimatedVisibility(
                visible = pagamentoEfetuado,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                // Tela de confirmação
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sucesso",
                        tint = Color.Green,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Pagamento Efetuado com Sucesso!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Seus pedidos ${idsPedidos.split("-").joinToString(", ")} foram confirmados.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Text("Voltar ao Início")
                    }
                }
            }
        }else{
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (formaPagamento == "PIX") "Pagamento via PIX" else "Pagamento via Boleto",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (formaPagamento == "PIX") {
                    // Tela de PIX
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // QRCode (usando uma biblioteca externa)
                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .background(Color.White)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.size(250.dp)
                            )

                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Chave PIX:",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Text(
                            codigoPix,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )

                        Button(
                            onClick = {
                                // Copia para área de transferência

                                clipboardManager.setText(AnnotatedString(codigoPix))
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copiar Chave")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "1. Abra seu app de banco\n2. Escolha pagar via PIX\n3. Cole a chave ou escaneie o QR Code",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                } else {
                    // Tela de Boleto
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Código de barras (simulação)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = barcodeBitmap.asImageBitmap(),
                                contentDescription = "Código de Barras",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                // Copia código para área de transferência
                                clipboardManager.setText(AnnotatedString(codigoBoleto))
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copiar Código")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "1. Abra seu app de banco\n2. Escolha pagar via Boleto\n3. Digite o código ou pague em lotéricas",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Informações adicionais
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Instruções importantes:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• O pagamento pode levar até 30 minutos para ser confirmado")
                        Text("• Em caso de dúvidas, entre em contato com nosso suporte")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

// Função auxiliar para gerar código de barras fictício
fun generateRandomBarcode(): String {
    val chars = ('0'..'9').toList()
    return (1..44).map { chars.random() }.joinToString("")
}

// Extensão para desenhar QRCode (simplificado)
fun DrawScope.drawQrCode(
    data: String,
    density: Density,
    padding: Float = 0.1f
) {
    // Implementação simplificada - na prática use uma biblioteca
    val size = size.minDimension
    val cellSize = size * (1 - 2 * padding) / 21f // QRCode versão 1 tem 21x21 módulos

    // Desenha fundo branco
    drawRect(Color.White, size = Size(size, size))

    // Desenha padrão simplificado (na prática use uma lib)
    drawRect(
        color = Color.Black,
        topLeft = Offset(size * padding, size * padding),
        size = Size(cellSize * 7, cellSize * 7)
    )

}

fun generateQrCodeBitmap(content: String, size: Int = 512): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    for(x in 0 until size){
        for (y in 0 until size){
            bitmap.setPixel(x, y, if(bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE )
        }
    }
    return bitmap
}

fun generateBarcodeBitmap(content: String, width: Int = 600, height: Int = 200): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        content,
        BarcodeFormat.CODE_128,
        width,
        height
    )
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}
