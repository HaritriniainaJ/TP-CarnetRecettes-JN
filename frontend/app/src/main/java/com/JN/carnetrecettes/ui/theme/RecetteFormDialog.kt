package com.JN.carnetrecettes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.JN.carnetrecettes.model.Recette

@Composable
fun RecetteFormDialog(
    recette: Recette? = null,
    onDismiss: () -> Unit,
    onConfirm: (Recette) -> Unit
) {
    var nom by remember { mutableStateOf(recette?.nom ?: "") }
    var ingredients by remember { mutableStateOf(recette?.ingredients ?: "") }
    var instructions by remember { mutableStateOf(recette?.instructions ?: "") }
    var tempsPrepMin by remember { mutableStateOf(recette?.temps_prep_min?.toString() ?: "") }
    var portions by remember { mutableStateOf(recette?.portions?.toString() ?: "") }

    // Erreurs de validation
    var nomError by remember { mutableStateOf(false) }
    var ingredientsError by remember { mutableStateOf(false) }
    var instructionsError by remember { mutableStateOf(false) }
    var tempsError by remember { mutableStateOf(false) }
    var portionsError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (recette == null) "Nouvelle Recette" else "Modifier la Recette")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it; nomError = false },
                    label = { Text("Nom de la recette *") },
                    isError = nomError,
                    supportingText = { if (nomError) Text("Champ obligatoire") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it; ingredientsError = false },
                    label = { Text("Ingrédients *") },
                    isError = ingredientsError,
                    supportingText = { if (ingredientsError) Text("Champ obligatoire") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it; instructionsError = false },
                    label = { Text("Instructions *") },
                    isError = instructionsError,
                    supportingText = { if (instructionsError) Text("Champ obligatoire") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tempsPrepMin,
                        onValueChange = { tempsPrepMin = it; tempsError = false },
                        label = { Text("Temps (min) *") },
                        isError = tempsError,
                        supportingText = { if (tempsError) Text("Invalide") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = portions,
                        onValueChange = { portions = it; portionsError = false },
                        label = { Text("Portions *") },
                        isError = portionsError,
                        supportingText = { if (portionsError) Text("Invalide") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // Validation
                nomError = nom.isBlank()
                ingredientsError = ingredients.isBlank()
                instructionsError = instructions.isBlank()
                tempsError = tempsPrepMin.toIntOrNull() == null || tempsPrepMin.toInt() < 1
                portionsError = portions.toIntOrNull() == null || portions.toInt() < 1

                if (!nomError && !ingredientsError && !instructionsError
                    && !tempsError && !portionsError
                ) {
                    onConfirm(
                        Recette(
                            id = recette?.id ?: 0,
                            nom = nom,
                            ingredients = ingredients,
                            instructions = instructions,
                            temps_prep_min = tempsPrepMin.toInt(),
                            portions = portions.toInt()
                        )
                    )
                }
            }) {
                Text(if (recette == null) "Ajouter" else "Modifier")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}