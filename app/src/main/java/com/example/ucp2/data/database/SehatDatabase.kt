package com.example.ucp2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2.data.dao.DokterDao
import com.example.ucp2.data.dao.JadwalDao
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.entity.Jadwal

@Database(entities = [Dokter::class, Jadwal::class], version = 1, exportSchema = false)
abstract class SehatDatabase : RoomDatabase() {

    // Mengidentifikasi fungsi untuk mengakses DAO Dokter dan Jadwal
    abstract fun dokterDao(): DokterDao
    abstract fun jadwalDao(): JadwalDao

    companion object {
        @Volatile
        private var Instance: SehatDatabase? = null

        fun getDatabase(context: Context): SehatDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SehatDatabase::class.java, // Class database
                    "SehatDatabase" // Nama database
                )
                    .fallbackToDestructiveMigration() // Menangani perubahan skema secara destruktif
                    .build().also { Instance = it }
            }
        }
    }
}