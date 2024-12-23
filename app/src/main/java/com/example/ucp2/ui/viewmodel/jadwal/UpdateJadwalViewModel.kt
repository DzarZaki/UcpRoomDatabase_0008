package com.example.ucp2.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import com.example.ucp2.ui.navigation.DestinasiUpdateJadwal
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateJadwalViewModel(
    savedStateHandle: SavedStateHandle, // Untuk menyimpan state terkait ViewModel
    private val repositoryJadwal: RepositoryJadwal // Repositori untuk mengelola data Jadwal
) : ViewModel() {

    // State untuk UI terkait pembaruan data Jadwal
    var updateUIState by mutableStateOf(JadwalUIState())
        private set

    // Mendapatkan nilai ID dari savedStateHandle
    private val _id: String = checkNotNull(savedStateHandle[DestinasiUpdateJadwal.ID])

    init {
        // Inisialisasi data Jadwal berdasarkan ID
        viewModelScope.launch {
            updateUIState = repositoryJadwal.getJadwal(_id)
                .filterNotNull() // Memfilter data yang tidak null
                .first() // Mengambil item pertama dari Flow
                .toUIStateJadwal() // Mengubah entity Jadwal ke UI state
        }
    }

    // Fungsi untuk memperbarui state ketika ada perubahan input dari pengguna
    fun updateState(jadwalEvent: JadwalEvent) {
        updateUIState = updateUIState.copy(
            jadwalEvent = jadwalEvent, // Menyalin event Jadwal yang baru
        )
    }

    // Validasi input untuk memastikan semua field terisi
    fun validateFields(): Boolean {
        val event = updateUIState.jadwalEvent
        val errorState = FormErrorState(
            id = if (event.id.isNotEmpty()) null else "ID tidak boleh kosong",
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "No HP tidak boleh kosong",
            tanggalKonsultasi = if (event.tanggalKonsultasi.isNotEmpty()) null else "Tanggal Konsultasi tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong"
        )
        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid() // Mengembalikan status validasi
    }

    // Fungsi untuk memperbarui data Jadwal ke database
    fun updateData() {
        val currentEvent = updateUIState.jadwalEvent

        if (validateFields()) { // Jika validasi berhasil
            viewModelScope.launch {
                try {
                    // Memperbarui data Jadwal di repositori
                    repositoryJadwal.updateJadwal(currentEvent.toJadwalEntity())
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data berhasil diupdate", // Menampilkan pesan sukses
                        jadwalEvent = JadwalEvent(), // Mereset event Jadwal
                        isEntryValid = FormErrorState() // Mereset state validasi
                    )
                } catch (e: Exception) {
                    // Menangani error jika update gagal
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data gagal diupdate"
                    )
                }
            }
        } else {
            // Menampilkan pesan error jika validasi gagal
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data gagal diupdate"
            )
        }
    }

    // Fungsi untuk mereset pesan Snackbar
    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }
}

// Fungsi ekstensi untuk mengonversi entity Jadwal ke UI state
fun Jadwal.toUIStateJadwal(): JadwalUIState = JadwalUIState(
    jadwalEvent = this.toDetailUiEvent()
)