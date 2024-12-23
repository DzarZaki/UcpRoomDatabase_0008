package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.SehatApp
import com.example.ucp2.ui.viewmodel.dokter.DokterViewModel
import com.example.ucp2.ui.viewmodel.dokter.HomeDokterViewModel
import com.example.ucp2.ui.viewmodel.jadwal.DetailJadwalViewModel
import com.example.ucp2.ui.viewmodel.jadwal.HomeJadwalViewModel
import com.example.ucp2.ui.viewmodel.jadwal.JadwalViewModel
import com.example.ucp2.ui.viewmodel.jadwal.UpdateJadwalViewModel

// Objek yang menyediakan ViewModel untuk berbagai layar
object PenyediaViewModel {
    // Factory untuk membuat instance ViewModel
    val Factory = viewModelFactory {
        // Inisialisasi ViewModel HomeDokterViewModel
        initializer {
            HomeDokterViewModel(
                sehatApp().containerApp.repositoryDokter // Mengambil repositori Dokter dari aplikasi
            )
        }
        // Inisialisasi ViewModel HomeJadwalViewModel
        initializer {
            HomeJadwalViewModel(
                sehatApp().containerApp.repositoryJadwal // Mengambil repositori Jadwal
            )
        }
        // Inisialisasi ViewModel DetailJadwalViewModel
        initializer {
            DetailJadwalViewModel(
                createSavedStateHandle(), // Membuat SavedStateHandle untuk mempertahankan state
                sehatApp().containerApp.repositoryJadwal
            )
        }
        // Inisialisasi ViewModel UpdateJadwalViewModel
        initializer {
            UpdateJadwalViewModel(
                createSavedStateHandle(),
                sehatApp().containerApp.repositoryJadwal
            )
        }
        // Inisialisasi ViewModel JadwalViewModel
        initializer {
            JadwalViewModel(
                sehatApp().containerApp.repositoryJadwal // Mengambil repositori Jadwal
            )
        }
        // Inisialisasi ViewModel DokterViewModel
        initializer {
            DokterViewModel(
                sehatApp().containerApp.repositoryDokter // Repositori Dokter
            )
        }
    }
}

// Fungsi ekstensi untuk mengambil instance SehatApp dari CreationExtras
fun CreationExtras.sehatApp(): SehatApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SehatApp)
