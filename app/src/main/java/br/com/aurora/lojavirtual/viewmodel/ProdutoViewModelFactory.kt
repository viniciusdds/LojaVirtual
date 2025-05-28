import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.aurora.lojavirtual.repository.ProdutoRepository
import br.com.aurora.lojavirtual.viewmodel.ProdutoViewModel

class ProdutoViewModelFactory(
    private val repository: ProdutoRepository // Supondo que você tenha um repositório
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdutoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProdutoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}