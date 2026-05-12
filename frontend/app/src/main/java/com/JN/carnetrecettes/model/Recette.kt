package com.JN.carnetrecettes.model

data class Recette(
    val id: Int = 0,
    val nom: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val temps_prep_min: Int = 0,
    val portions: Int = 0
)