package com.example.ucp2.ui.viewmodel.jadwal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import com.example.ucp2.ui.navigation.DestinasiDetailJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailJadwalViewModel(
    savedStateHandle: SavedStateHandle, // Menyimpan state yang terkait dengan navigasi
    private val repositoryJadwal: RepositoryJadwal // Repositori untuk mengakses data Jadwal
) : ViewModel() {
    private val _id: String = checkNotNull(savedStateHandle[DestinasiDetailJadwal.ID])

    val detailUiState: StateFlow<DetailUiState> = repositoryJadwal.getJadwal(_id) // Mendapatkan data Jadwal berdasarkan ID
        .filterNotNull()
        .map {
            DetailUiState(
                detailUiEvent = it.toDetailUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailUiState(isLoading = true),
        )

    fun deleteJadwal() {
        val currentEvent = detailUiState.value.detailUiEvent // Mendapatkan event Jadwal saat ini
        if (currentEvent != JadwalEvent()) { // Validasi agar tidak kosong
            viewModelScope.launch {
                repositoryJadwal.deleteJadwal(currentEvent.toJadwalEntity())
            }
        }
    }
}

// Data class untuk merepresentasikan UI State dari halaman Detail
data class DetailUiState(
    val detailUiEvent: JadwalEvent = JadwalEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == JadwalEvent()
    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != JadwalEvent()
}

// Fungsi untuk mengonversi data Jadwal entity menjadi JadwalEvent untuk kebutuhan UI
fun Jadwal.toDetailUiEvent(): JadwalEvent {
    return JadwalEvent(
        id = id,
        namaDokter = namadokter,
        namaPasien = namapasien,
        noHp = nohp,
        tanggalKonsultasi = tanggalkonsultasi,
        status = status,
    )
}
