package com.example.ucp2.ui.view.dokter

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
import com.example.ucp2.ui.viewmodel.dokter.DokterEvent
import com.example.ucp2.ui.viewmodel.dokter.DokterUIState
import com.example.ucp2.ui.viewmodel.dokter.DokterViewModel
import com.example.ucp2.ui.viewmodel.dokter.FormErrorState
import kotlinx.coroutines.launch

@Composable
fun InsertDokterView(
    onBack: () -> Unit, // Callback untuk navigasi kembali
    onNavigate: () -> Unit, // Callback untuk navigasi ke halaman lain setelah data disimpan
    modifier: Modifier = Modifier,
    viewModel: DokterViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
                judul = "Tambah Dokter"
            )
            // Isi body
            InsertBodyDokter(
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
fun InsertBodyDokter(
    modifier: Modifier = Modifier,
    onValueChange: (DokterEvent) -> Unit,
    uiState: DokterUIState, // State UI yang diambil dari ViewModel
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Form input untuk data Dokter
        FormDokter(
            dokterEvent = uiState.dokterEvent, // Data dokter yang sedang diedit
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
fun FormDokter(
    dokterEvent: DokterEvent = DokterEvent(),
    onValueChange: (DokterEvent) -> Unit, // Callback untuk perubahan pada input
    errorState: FormErrorState = FormErrorState(), // State untuk validasi input
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Input untuk Nama Dokter
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.nama,
            onValueChange = { onValueChange(dokterEvent.copy(nama = it)) },
            label = { Text("Nama Dokter") },
            isError = errorState.nama != null, // Validasi error untuk nama
            placeholder = { Text("Masukkan Nama Dokter") }
        )
        Text(text = errorState.nama ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Spesialis
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.spesialis,
            onValueChange = { onValueChange(dokterEvent.copy(spesialis = it)) },
            label = { Text("Spesialis") },
            isError = errorState.spesialis != null, // Validasi error untuk spesialis
            placeholder = { Text("Masukkan Spesialis") }
        )
        Text(text = errorState.spesialis ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Klinik
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.klinik,
            onValueChange = { onValueChange(dokterEvent.copy(klinik = it)) },
            label = { Text("Klinik") },
            isError = errorState.klinik != null, // Validasi error untuk klinik
            placeholder = { Text("Masukkan Klinik") }
        )
        Text(text = errorState.klinik ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk No HP
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.noHp,
            onValueChange = { onValueChange(dokterEvent.copy(noHp = it)) },
            label = { Text("No HP") },
            isError = errorState.noHp != null, // Validasi error untuk No HP
            placeholder = { Text("Masukkan No HP") }
        )
        Text(text = errorState.noHp ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Jam Kerja
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.jamKerja,
            onValueChange = { onValueChange(dokterEvent.copy(jamKerja = it)) },
            label = { Text("Jam Kerja") },
            isError = errorState.jamKerja != null, // Validasi error untuk Jam Kerja
            placeholder = { Text("Masukkan Jam Kerja") }
        )
        Text(text = errorState.jamKerja ?: "", color = Color.Red)
    }
}