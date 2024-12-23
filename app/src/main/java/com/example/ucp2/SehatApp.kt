package com.example.ucp2

import android.app.Application
import com.example.ucp2.dependenciesinjection.ContainerApp

class SehatApp : Application() {
    lateinit var containerApp: ContainerApp // Fungsinya untuk menyimpan instance
    override fun onCreate() {
        super.onCreate()
        containerApp = ContainerApp(this) // Membuat instance ContainerApp
    }
}