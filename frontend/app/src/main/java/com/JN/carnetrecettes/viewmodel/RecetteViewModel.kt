package com.JN.carnetrecettes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.JN.carnetrecettes.api.RetrofitInstance
import com.JN.carnetrecettes.model.Recette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecetteViewModel : ViewModel() {

    private val _recettes = MutableStateFlow<List<Recette>>(emptyList())
    val recettes: StateFlow<List<Recette>> = _recettes

    init {
        loadRecettes()
    }

    fun loadRecettes() {
        viewModelScope.launch {
            try {
                _recettes.value = RetrofitInstance.api.getRecettes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addRecette(recette: Recette) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.createRecette(recette)
                loadRecettes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateRecette(id: Int, recette: Recette) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.updateRecette(id, recette)
                loadRecettes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteRecette(id: Int) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteRecette(id)
                loadRecettes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}