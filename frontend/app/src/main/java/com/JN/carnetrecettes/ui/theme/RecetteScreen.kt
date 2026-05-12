package com.JN.carnetrecettes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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

@Composable
fun RecetteScreen(modifier: Modifier = Modifier, vm: RecetteViewModel = viewModel()) {
    val recettes by vm.recettes.collectAsState()

    var nom by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var tempsPrepMin by remember { mutableStateOf("") }
    var portions by remember { mutableStateOf("") }
    var editId by remember { mutableStateOf<Int?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Text(
            "Carnet de Recettes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom de la recette") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingrédients") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instructions") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = tempsPrepMin,
                onValueChange = { tempsPrepMin = it },
                label = { Text("Temps (min)") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = portions,
                onValueChange = { portions = it },
                label = { Text("Portions") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val recette = Recette(
                    id = editId ?: 0,
                    nom = nom,
                    ingredients = ingredients,
                    instructions = instructions,
                    temps_prep_min = tempsPrepMin.toIntOrNull() ?: 0,
                    portions = portions.toIntOrNull() ?: 0
                )
                if (editId != null) {
                    vm.updateRecette(editId!!, recette)
                    editId = null
                } else {
                    vm.addRecette(recette)
                }
                nom = ""; ingredients = ""; instructions = ""
                tempsPrepMin = ""; portions = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editId != null) "Modifier" else "Ajouter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(recettes) { recette ->
                RecetteItem(
                    recette = recette,
                    onDelete = { vm.deleteRecette(recette.id) },
                    onEdit = {
                        editId = recette.id
                        nom = recette.nom
                        ingredients = recette.ingredients
                        instructions = recette.instructions
                        tempsPrepMin = recette.temps_prep_min.toString()
                        portions = recette.portions.toString()
                    }
                )
            }
        }
    }
}

@Composable
fun RecetteItem(recette: Recette, onDelete: () -> Unit, onEdit: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    recette.nom,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    Text(
                        "${recette.temps_prep_min} min · ${recette.portions} parts",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    TextButton(onClick = onEdit) {
                        Text("Éditer", color = Color.Blue)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Supprimer", tint = Color.Red)
                    }
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ingrédients :", fontWeight = FontWeight.Bold)
                Text(recette.ingredients, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Instructions :", fontWeight = FontWeight.Bold)
                Text(recette.instructions, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}