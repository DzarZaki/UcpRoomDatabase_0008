package com.example.ucp2.data.entity

import androidx.room.Entity

@Entity(tableName = "mahasiswa")

data class Jadwal (
    val id :String,
    val namadokter :String,
    val namapasien :String,
    val nohp  :String,
    val tanggalkonsultasi :String,
    val status:String,
)