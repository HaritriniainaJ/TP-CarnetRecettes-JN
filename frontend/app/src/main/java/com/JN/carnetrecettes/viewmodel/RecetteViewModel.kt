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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadRecettes()
    }

    fun loadRecettes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _recettes.value = RetrofitInstance.api.getRecettes()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur réseau : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecette(recette: Recette) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.api.createRecette(recette)
                loadRecettes()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de l'ajout : ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun updateRecette(id: Int, recette: Recette) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.api.updateRecette(id, recette)
                loadRecettes()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la modification : ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun deleteRecette(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.api.deleteRecette(id)
                loadRecettes()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la suppression : ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}