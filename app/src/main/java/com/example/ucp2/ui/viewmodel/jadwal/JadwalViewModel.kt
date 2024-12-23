package com.example.ucp2.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.launch

class JadwalViewModel(private val repositoryJadwal: RepositoryJadwal) : ViewModel() {
    var uiState by mutableStateOf(JadwalUIState())

    fun updateState(jadwalEvent: JadwalEvent) {
        uiState = uiState.copy(
            jadwalEvent = jadwalEvent
        )
    }

    private fun validateFields(): Boolean {
        val event = uiState.jadwalEvent
        val errorState = FormErrorState(
            id = if (event.id.isNotEmpty()) null else "ID tidak boleh kosong",
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "No HP tidak boleh kosong",
            tanggalKonsultasi = if (event.tanggalKonsultasi.isNotEmpty()) null else "Tanggal Konsultasi tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong"
        )

        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData() {
        val currentEvent = uiState.jadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.insertJadwal(currentEvent.toJadwalEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorState()
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid, Periksa kembali data Anda"
            )
        }
    }

    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }
}

// Data class untuk merepresentasikan state UI Jadwal
data class JadwalUIState(
    val jadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null
)

// Data class untuk validasi form Jadwal
data class FormErrorState(
    val id: String? = null,
    val namaDokter: String? = null,
    val namaPasien: String? = null,
    val noHp: String? = null,
    val tanggalKonsultasi: String? = null,
    val status: String? = null
) {
    fun isValid(): Boolean {
        return id == null && namaDokter == null && namaPasien == null && noHp == null && tanggalKonsultasi == null && status == null
    }
}

// Data class untuk event input form Jadwal
data class JadwalEvent(
    val id: String = "",
    val namaDokter: String = "",
    val namaPasien: String = "",
    val noHp: String = "",
    val tanggalKonsultasi: String = "",
    val status: String = ""
)

// Fungsi ekstensi untuk mengonversi JadwalEvent menjadi entity Jadwal
fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    id = id,
    namadokter = namaDokter,
    namapasien = namaPasien,
    nohp = noHp,
    tanggalkonsultasi = tanggalKonsultasi,
    status = status
)