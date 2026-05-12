package com.JN.carnetrecettes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.JN.carnetrecettes.ui.RecetteDetailScreen
import com.JN.carnetrecettes.ui.RecetteScreen
import com.JN.carnetrecettes.ui.theme.CarnetRecettesTheme
import com.JN.carnetrecettes.viewmodel.RecetteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarnetRecettesTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val vm: RecetteViewModel = viewModel()
    val recettes by vm.recettes.collectAsState()

    NavHost(navController = navController, startDestination = "recettes") {

        // Écran liste
        composable("recettes") {
            RecetteScreen(
                vm = vm,
                onRecetteClick = { recette ->
                    val index = recettes.indexOf(recette)
                    navController.navigate("detail/$index")
                }
            )
        }

        // Écran détail
        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            val recette = recettes.getOrNull(index)
            recette?.let {
                RecetteDetailScreen(
                    recette = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}