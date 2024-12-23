package com.example.ucp2.ui.view.jadwal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ucp2.ui.costumwidget.TopAppBar
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import com.example.ucp2.ui.viewmodel.jadwal.UpdateJadwalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun UpdateJadwalView(
    onBack: () -> Unit, // Callback untuk tombol "kembali"
    onNavigate: () -> Unit, // Callback untuk navigasi setelah update berhasil
    modifier: Modifier = Modifier,
    viewModel: UpdateJadwalViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.updateUIState // Ambil UI state dari ViewModel
    val snackbarHostState = remember { SnackbarHostState() } // Host untuk menampilkan Snackbar
    val coroutineScope = rememberCoroutineScope()

    // Observasi perubahan snackBarMessage
    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetSnackBarMessage() // Reset snackbar message
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // Komponen TopAppBar untuk header
            TopAppBar(
                onBack = onBack, // Fungsi untuk tombol kembali
                judul = "Edit Jadwal",
                showBackButton = true // Menampilkan tombol kembali
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Isi body form untuk update data jadwal
            InsertBodyJadwal(
                uiState = uiState, // Mengirim UI state ke komponen form
                onValueChange = { updateEvent ->
                    viewModel.updateState(updateEvent) // Memperbarui state berdasarkan input pengguna
                },
                onClick = {
                    coroutineScope.launch {
                        // Validasi field sebelum menyimpan data
                        if (viewModel.validateFields()) {
                            viewModel.updateData() // Mengupdate data jadwal
                            delay(600)
                            withContext(Dispatchers.Main) {
                                onNavigate() // Navigasi di main thread
                            }
                        } else {
                            // Tampilkan error jika validasi gagal
                            snackbarHostState.showSnackbar("Field tidak valid!")
                        }
                    }
                }
            )
        }
    }
}