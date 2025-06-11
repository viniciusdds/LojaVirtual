import br.com.aurora.lojavirtual.model.ApiResponse
import br.com.aurora.lojavirtual.model.DadosCompra
import br.com.aurora.lojavirtual.network.ApiService
import android.util.Log

class DadosRepository(private val api: ApiService){

    suspend fun enviarDadosCompra(dadosCompra: DadosCompra) : Result<ApiResponse> {
        return try{
            Log.d("Repository", "Enviando dados: ${dadosCompra}")

            val response = api.enviarDadosCompra(dadosCompra)

            if(response.isSuccessful){
                response.body()?.let {
                    Log.d("Repository", "Resposta bem-sucedida: $it")
                    Result.success(it)
                } ?: Result.failure(Exception("Resposta vazia da API"))
            }else{
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                Result.failure(Exception("Erro na API: $errorBody"))
            }
        }catch (e: Exception){
            Log.e("Repository", "Exceção na chamada à API: ${e.message}", e)
            Result.failure(e)
        }
    }

}

