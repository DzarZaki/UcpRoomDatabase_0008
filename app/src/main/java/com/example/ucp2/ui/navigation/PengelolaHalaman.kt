package com.example.ucp2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ucp2.ui.view.dokter.HomeDokterView
import com.example.ucp2.ui.view.dokter.InsertDokterView
import com.example.ucp2.ui.view.jadwal.DetailJadwalView
import com.example.ucp2.ui.view.jadwal.HomeJadwalView
import com.example.ucp2.ui.view.jadwal.InsertJadwalView
import com.example.ucp2.ui.view.jadwal.UpdateJadwalView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = DestinasiHomeDokter.route) {
        // Navigasi ke Home Dokter
        composable(
            route = DestinasiHomeDokter.route
        ) {
            HomeDokterView(
                onAddDokter = {
                    navController.navigate(DestinasiInsertDokter.route)
                },
                onViewJadwal = {
                    navController.navigate(DestinasiHomeJadwal.route)
                },
                modifier = modifier
            )
        }

        // Navigasi ke Insert Dokter
        composable(
            route = DestinasiInsertDokter.route
        ) {
            InsertDokterView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        // Navigasi ke Home Jadwal
        composable(
            route = DestinasiHomeJadwal.route
        ) {
            HomeJadwalView(
                onAddJadwal = {
                    navController.navigate(DestinasiInsertJadwal.route)
                },
                onBack = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        // Navigasi ke Insert Jadwal
        composable(
            route = DestinasiInsertJadwal.route
        ) {
            InsertJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        // Navigasi ke Detail Jadwal
        composable(
            DestinasiDetailJadwal.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiDetailJadwal.ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(DestinasiDetailJadwal.ID)
            id?.let { jadwalId ->
                DetailJadwalView(
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${DestinasiUpdateJadwal.route}/$jadwalId")
                    },
                    onDeleteClick = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }

        // Navigasi ke Update Jadwal
        composable(
            DestinasiUpdateJadwal.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdateJadwal.ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            UpdateJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
    }
}

