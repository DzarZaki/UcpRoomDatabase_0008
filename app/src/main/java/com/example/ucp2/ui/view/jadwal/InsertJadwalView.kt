package com.example.ucp2.ui.view.jadwal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.ui.costumwidget.TopAppBar
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import com.example.ucp2.ui.viewmodel.jadwal.FormErrorState
import com.example.ucp2.ui.viewmodel.jadwal.JadwalEvent
import com.example.ucp2.ui.viewmodel.jadwal.JadwalUIState
import com.example.ucp2.ui.viewmodel.jadwal.JadwalViewModel
import kotlinx.coroutines.launch

@Composable
fun InsertJadwalView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState // Ambil UI state dari ViewModel
    val snackBarHostState = remember { SnackbarHostState() } // Snackbar state
    val coroutineScope = rememberCoroutineScope()

    // Observasi perubahan snackbarMessage
    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message) // Tampilkan Snackbar
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TopAppBar(
                onBack = onBack,
                showBackButton = true, // Menampilkan tombol kembali
                judul = "Tambah Jadwal"
            )
            // Isi body
            InsertBodyJadwal(
                onValueChange = { updateEvent ->
                    viewModel.updateState(updateEvent) // Update state di ViewModel
                },
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveData() // Simpan Data
                    }
                    onNavigate() // Navigasi ke halaman lain setelah data disimpan
                },
                uiState = uiState
            )
        }
    }
}

@Composable
fun InsertBodyJadwal(
    modifier: Modifier = Modifier,
    onValueChange: (JadwalEvent) -> Unit,
    uiState: JadwalUIState, // State UI yang diambil dari ViewModel
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Form input untuk data Jadwal
        FormJadwal(
            jadwalEvent = uiState.jadwalEvent, // Data jadwal yang sedang diedit
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@Composable
fun FormJadwal(
    jadwalEvent: JadwalEvent = JadwalEvent(),
    onValueChange: (JadwalEvent) -> Unit, // Callback untuk perubahan pada input
    errorState: FormErrorState = FormErrorState(), // State untuk validasi input
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Input untuk Nama Dokter
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.namaDokter,
            onValueChange = { onValueChange(jadwalEvent.copy(namaDokter = it)) },
            label = { Text("Nama Dokter") },
            isError = errorState.namaDokter != null,
            placeholder = { Text("Masukkan Nama Dokter") }
        )
        Text(text = errorState.namaDokter ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Nama Pasien
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.namaPasien,
            onValueChange = { onValueChange(jadwalEvent.copy(namaPasien = it)) },
            label = { Text("Nama Pasien") },
            isError = errorState.namaPasien != null,
            placeholder = { Text("Masukkan Nama Pasien") }
        )
        Text(text = errorState.namaPasien ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk No HP
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.noHp,
            onValueChange = { onValueChange(jadwalEvent.copy(noHp = it)) },
            label = { Text("No HP") },
            isError = errorState.noHp != null,
            placeholder = { Text("Masukkan No HP") }
        )
        Text(text = errorState.noHp ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Tanggal Konsultasi
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.tanggalKonsultasi,
            onValueChange = { onValueChange(jadwalEvent.copy(tanggalKonsultasi = it)) },
            label = { Text("Tanggal Konsultasi") },
            isError = errorState.tanggalKonsultasi != null,
            placeholder = { Text("Masukkan Tanggal Konsultasi") }
        )
        Text(text = errorState.tanggalKonsultasi ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Status
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.status,
            onValueChange = { onValueChange(jadwalEvent.copy(status = it)) },
            label = { Text("Status") },
            isError = errorState.status != null,
            placeholder = { Text("Masukkan Status") }
        )
        Text(text = errorState.status ?: "", color = Color.Red)
    }
}