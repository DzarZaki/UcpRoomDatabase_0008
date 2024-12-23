package com.example.ucp2.ui.viewmodel.dokter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.repository.RepositoryDokter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeDokterViewModel(
    private val repositoryDokter: RepositoryDokter // Repositori untuk mengakses data Dokter
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = repositoryDokter.getAllDokter() // Mengambil data Dokter dari repository
        .filterNotNull()
        .map {
            HomeUiState(
                listDokter = it.toList(),
                isLoading = false
            )
        }
        .onStart {
            emit(HomeUiState(isLoading = true))
            delay(900)
        }
        .catch {
            // Menangani error yang terjadi selama pengambilan data
            emit(
                HomeUiState(
                    isLoading = false,
                    isError = true, // Menandai adanya error
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
}

// Data class untuk merepresentasikan state halaman Home
data class HomeUiState(
    val listDokter: List<Dokter> = listOf(), // Daftar Dokter untuk ditampilkan di UI
    val isLoading: Boolean = false, // Menandai apakah proses pemuatan sedang berlangsung
    val isError: Boolean = false, // Menandai apakah ada error
    val errorMessage: String = "" // Menyimpan pesan error jika ada
)