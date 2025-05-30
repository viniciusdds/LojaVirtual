import br.com.aurora.lojavirtual.model.ItemCarrinho
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PedidoRequest(
    @SerializedName("id_usuario") val id_usuario: String,
    @SerializedName("itens") val itens: List<ItemCarrinho>
)