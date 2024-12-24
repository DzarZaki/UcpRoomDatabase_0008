package com.example.ucp2.ui.view.dokter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.R
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import com.example.ucp2.ui.viewmodel.dokter.HomeDokterViewModel
import com.example.ucp2.ui.costumwidget.TopAppBar
import com.example.ucp2.ui.theme.SpesialisWarna

@Composable
fun HomeDokterView(
    viewModel: HomeDokterViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddDokter: () -> Unit = {},
    onViewJadwal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onBack = {},
                showBackButton = false,
                judul = "AYO SEHAT"
            )
        }
    ) { innerPadding ->
        val homeUiState by viewModel.homeUiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF6FF))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sehat), // Gambar icon kesehatan
                        contentDescription = "Health Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Chat Dokter di Ayo Sehat",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Layanan telemedisin yang siap siaga untuk bantu kamu hidup lebih sehat.",
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Search and Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onAddDokter, modifier = Modifier.weight(1f)) {
                    Text(text = "Tambah Dokter")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onViewJadwal, modifier = Modifier.weight(1f)) {
                    Text(text = "Lihat Jadwal")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Body: List Dokter
            when {
                homeUiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                homeUiState.isError -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Terjadi kesalahan: ${homeUiState.errorMessage}")
                    }
                }

                homeUiState.listDokter.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada data dokter.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(homeUiState.listDokter) { dokter ->
                            CardDokter(dokter = dokter)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardDokter(dokter: Dokter, modifier: Modifier = Modifier) {
    // Ambil warna berdasarkan spesialis, default ke warna abu-abu jika spesialis tidak ditemukan
    val warnaSpesialis = SpesialisWarna[dokter.spesialis] ?: Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = warnaSpesialis)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.size(48.dp),
                tint = Color.White // Icon berwarna putih agar kontras dengan latar belakang
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "drg. ${dokter.nama}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White // Teks utama berwarna putih
                )
                Text(
                    text = dokter.spesialis,
                    fontSize = 14.sp,
                    color = Color.White // Teks spesialis berwarna putih
                )
                Text(
                    text = dokter.klinik,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = dokter.jamkerja,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
