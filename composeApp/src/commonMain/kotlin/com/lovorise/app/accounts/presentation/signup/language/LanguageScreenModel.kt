package com.lovorise.app.accounts.presentation.signup.language

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageScreenModel : ScreenModel {

    private var _state = MutableStateFlow(LanguageScreenState())
    val state = _state.asStateFlow()


    fun loadLanguages(languages:List<String>){
        _state.update {
            it.copy(
                allLanguages = languages
            )
        }
    }

    fun search(query:String){
        screenModelScope.launch {
            val filteredItems = state.value.allLanguages.filter { it.lowercase().contains(query) }
            _state.update { currentState ->
                currentState.copy(
                    searchQuery = query,
                    searchResults = filteredItems
                )
            }
        }
    }

    fun reloadItems(selectedItems:List<String>){
        val items = state.value.selectedLanguages.toMutableList().apply {
            addAll(selectedItems)
        }.toSet().toList()

        _state.update { currentState ->
            currentState.copy(
                selectedLanguages = items,
                allLanguages = state.value.allLanguages
            )
        }
    }

    fun isItemSelected(language:String):Boolean{
        return state.value.selectedLanguages.contains(language)
    }

    fun onItemClicked(language:String) {
        val updatedList = state.value.selectedLanguages.toMutableList()
        if (updatedList.contains(language)){
            updatedList.remove(language)
        }else{
            if (updatedList.size == 5) return
            updatedList.add(language)
        }
        _state.update {
            it.copy(
                selectedLanguages = updatedList
            )
        }
    }
}