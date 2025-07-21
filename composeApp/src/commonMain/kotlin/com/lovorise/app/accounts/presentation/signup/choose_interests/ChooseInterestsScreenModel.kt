package com.lovorise.app.accounts.presentation.signup.choose_interests

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChooseInterestsScreenModel : ScreenModel {


    private var _state = MutableStateFlow(ChooseInterestsScreenState())
    val state = _state.asStateFlow()


    // Method to filter allItems based on searchQuery
    private fun filterItems(query: String): List<ChooseInterestsScreenState.InterestCategoryWithItems> {
        if (query.isBlank()) return emptyList()
        val lowerCaseQuery = query.lowercase()
        return state.value.allItems.mapNotNull { category ->
            // Filter items in the category based on the query
            val filteredItems = category.items.filter { item ->
                item.name.lowercase().contains(lowerCaseQuery)
            }

            // Include the category if it or any of its items match the query
            if (category.category.lowercase().contains(lowerCaseQuery) || filteredItems.isNotEmpty()) {
                category.copy(items = filteredItems)
            } else {
                null
            }
        }
    }

    fun reloadItems(selectedItems:List<String>){
        val items = state.value.selectedItems.toMutableList().apply {
            addAll(selectedItems)
        }.toSet().toList()

        _state.update { currentState ->
            currentState.copy(
                selectedItems = items,
                allItems = state.value.allItems
            )
        }
    }

    fun search(query:String){
        screenModelScope.launch {
            val filteredItems = filterItems(query)
            _state.update { currentState ->
                currentState.copy(
                    searchQuery = query,
                    searchResults = filteredItems
                )
            }
        }
    }

    fun isItemSelected(name:String):Boolean{
        return state.value.selectedItems.contains(name)
    }

    fun onShowMore(itemId:String){
        _state.update {
            it.copy(
                showMoreIds = state.value.showMoreIds.toMutableList().apply { add(itemId) }
            )
        }
    }

    fun onShowLess(itemId:String){
        _state.update {
            it.copy(
                showMoreIds = state.value.showMoreIds.toMutableList().apply { remove(itemId) }
            )
        }
    }

    fun getInterestsData(givenItems:List<String> = emptyList()):Map<String,List<String>>{
        val allItems = state.value.allItems
        val items = givenItems.ifEmpty { state.value.selectedItems }

        val result = mutableMapOf<String,List<String>>()

        items.forEach { item ->
            val itemWithCategory = allItems.firstOrNull { it.items.contains(ChooseInterestsScreenState.InterestCategoryWithItems.Item(item)) }
            if (itemWithCategory != null) {
                val key = itemWithCategory.id
                val currentValue = result[key]
                result[key] = currentValue?.toMutableList()?.apply { add(item) } ?: listOf(item)
            }
        }
        return result
    }

    fun onChipClicked(name:String) {
        val updatedList = state.value.selectedItems.toMutableList()
        val allItems = state.value.allItems
        if (updatedList.contains(name)){
            updatedList.remove(name)
        }else{
            if (updatedList.size == 10) return
            updatedList.add(name)
        }
        _state.update {
            it.copy(
                selectedItems = updatedList
            )
        }
        println("the updatedList is $updatedList")
    }



}