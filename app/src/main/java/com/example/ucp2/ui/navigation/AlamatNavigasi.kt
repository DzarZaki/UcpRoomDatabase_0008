package com.example.ucp2.ui.navigation

interface AlamatNavigasi {
    val route: String
}

// Destinasi untuk HomeDokter
object DestinasiHomeDokter : AlamatNavigasi {
    override val route = "home_dokter"
}
object DestinasiInsertDokter : AlamatNavigasi {
    override val route = "insert_dokter"
}

// Destinasi untuk DetailJadwal

object DestinasiDetailJadwal : AlamatNavigasi {
    override val route = "detail_jadwal"
    const val ID = "id"
    val routesWithArg = "$route/{$ID}"
}

// Destinasi untuk UpdateJadwal
object DestinasiUpdateJadwal : AlamatNavigasi {
    override val route = "update_jadwal"
    const val ID = "id"
    val routesWithArg = "$route/{$ID}"
}

// Destinasi untuk HomeJadwal
object DestinasiHomeJadwal : AlamatNavigasi {
    override val route = "home_jadwal"
}
object DestinasiInsertJadwal : AlamatNavigasi {
    override val route = "insert_jadwal"
}
