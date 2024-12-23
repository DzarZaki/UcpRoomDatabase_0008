package com.example.ucp2.ui.viewmodel.jadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeJadwalViewModel(
    private val repositoryJadwal: RepositoryJadwal // Repositori untuk mengakses data Jadwal
) : ViewModel() {

    val homeUiState: StateFlow<HomeJadwalUiState> = repositoryJadwal.getAllJadwal() // Mengambil data Jadwal dari repository
        .filterNotNull()
        .map {
            HomeJadwalUiState(
                listJadwal = it.toList(),
                isLoading = false
            )
        }
        .onStart {
            emit(HomeJadwalUiState(isLoading = true))
            delay(900)
        }
        .catch {
            // Menangani error yang terjadi selama pengambilan data
            emit(
                HomeJadwalUiState(
                    isLoading = false,
                    isError = true, // Menandai adanya error
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeJadwalUiState(isLoading = true)
        )
}

// Data class untuk merepresentasikan state halaman Home Jadwal
data class HomeJadwalUiState(
    val listJadwal: List<Jadwal> = listOf(), // Daftar Jadwal untuk ditampilkan di UI
    val isLoading: Boolean = false, // Menandai apakah proses pemuatan sedang berlangsung
    val isError: Boolean = false, // Menandai apakah ada error
    val errorMessage: String = "" // Menyimpan pesan error jika ada
)