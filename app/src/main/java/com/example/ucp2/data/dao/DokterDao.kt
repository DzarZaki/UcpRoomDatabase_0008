package com.example.ucp2.data.dao

import androidx.room.Insert
import com.example.ucp2.data.entity.Dokter

interface DokterDao {

    @Insert
    suspend fun insertDokter(dokter: Dokter)

}