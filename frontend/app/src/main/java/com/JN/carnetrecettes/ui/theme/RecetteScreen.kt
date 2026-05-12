package com.JN.carnetrecettes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.JN.carnetrecettes.model.Recette
import com.JN.carnetrecettes.viewmodel.RecetteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetteScreen(
    modifier: Modifier = Modifier,
    vm: RecetteViewModel = viewModel(),
    onRecetteClick: (Recette) -> Unit
) {
    val recettes by vm.recettes.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val errorMessage by vm.errorMessage.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var recetteToEdit by remember { mutableStateOf<Recette?>(null) }
    var recetteToDelete by remember { mutableStateOf<Recette?>(null) }

    // Dialog Ajouter
    if (showAddDialog) {
        RecetteFormDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                vm.addRecette(it)
                showAddDialog = false
            }
        )
    }

    // Dialog Modifier
    if (recetteToEdit != null) {
        RecetteFormDialog(
            recette = recetteToEdit,
            onDismiss = { recetteToEdit = null },
            onConfirm = {
                vm.updateRecette(it.id, it)
                recetteToEdit = null
            }
        )
    }

    // Dialog Confirmation Suppression
    if (recetteToDelete != null) {
        AlertDialog(
            onDismissRequest = { recetteToDelete = null },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer \"${recetteToDelete!!.nom}\" ?") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.deleteRecette(recetteToDelete!!.id)
                        recetteToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { recetteToDelete = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carnet de Recettes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Ajouter")
            }
        }
    ) { innerPadding ->

        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {

            // Loading spinner
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Écran vide
            else if (recettes.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Aucune recette pour le moment",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Appuyez sur + pour ajouter une recette",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Liste des recettes
            else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(recettes) { recette ->
                        RecetteItem(
                            recette = recette,
                            onClick = { onRecetteClick(recette) },
                            onEdit = { recetteToEdit = recette },
                            onDelete = { recetteToDelete = recette }
                        )
                    }
                }
            }

            // Message d'erreur
            errorMessage?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { vm.clearError() }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun RecetteItem(
    recette: Recette,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    recette.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${recette.temps_prep_min} min · ${recette.portions} parts",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row {
                TextButton(onClick = onEdit) {
                    Text("Éditer", color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Supprimer", tint = Color.Red)
                }
            }
        }
    }
}