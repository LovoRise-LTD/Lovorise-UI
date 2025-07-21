package com.lovorise.app.lovorise_hearts.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.lovorise_hearts.domain.PurchaseRepo
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.lovorise_hearts.presentation.states.TransactionScreenState
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PurchaseScreenModel(private val purchaseRepo: PurchaseRepo) : ScreenModel {

    private val _transactionState = MutableStateFlow(TransactionScreenState())
    val transactionState = _transactionState.asStateFlow()

    private var prefs: SharedPrefs? = null



    fun loadAllTransactions(context: PlatformContext,months:List<String>){
        val token = getToken(context) ?: return
        screenModelScope.launch {
            purchaseRepo.getTransactions(token).collectLatest { res->
                when(res){
                    is Resource.Success -> {
                        _transactionState.update{
                            it.copy(
                                transactions = res.data ?: emptyList()
                            )
                        }
                        categorizeTransactions(months)
                    }
                    is Resource.Loading -> {
                        _transactionState.update{
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }
                    is Resource.Error -> {}
                }
            }

        }
    }

    fun updateTransactionFilterSheetState(value:Boolean){
        _transactionState.update {
            it.copy(
                showTransactionFilterSheet = value
            )
        }
    }

    private fun categorizeTransactions(months:List<String>){

        val transactions = transactionState.value.transactions.sortedByDescending { it.timestamp }
        if (transactions.isEmpty()) return

        val transactionsWithCategory = transactionState.value.transactionsWithCategory.toMutableList()

        transactions.forEach { transactionData ->
            val formattedDate = formatDate(transactionData.timestamp,months)
            val category = formatDateMonthYear(transactionData.timestamp,months)
            val index = transactionsWithCategory.indexOfFirst { it.category == category }
            if (index != -1){
                val transactionWithCategory = transactionsWithCategory[index]
                transactionsWithCategory.apply {
                    add(index,removeAt(index).copy(transactions = transactionWithCategory.transactions.toMutableList().apply { add(transactionData.copy(formattedDate = formattedDate))}))
                }
            }else{
                transactionsWithCategory.add(TransactionScreenState.TransactionsWithCategory(category, listOf(transactionData.copy(formattedDate = formattedDate))))
            }
        }


        val incomingTransactionsWithCategory = transactionsWithCategory.mapNotNull { category ->
            val filteredTransactions = category.transactions.filter { it.type == TransactionData.Type.INCOMING }
            if (filteredTransactions.isNotEmpty()) TransactionScreenState.TransactionsWithCategory(
                category.category,
                filteredTransactions
            ) else null
        }

        val outgoingTransactionsWithCategory = transactionsWithCategory.mapNotNull { category ->
            val filteredTransactions = category.transactions.filter { it.type == TransactionData.Type.OUTGOING }
            if (filteredTransactions.isNotEmpty()) TransactionScreenState.TransactionsWithCategory(
                category.category,
                filteredTransactions
            ) else null
        }


        _transactionState.update{
            it.copy(
                transactionsWithCategory = transactionsWithCategory,
                incomingTransactionsWithCategory = incomingTransactionsWithCategory,
                outgoingTransactionsWithCategory = outgoingTransactionsWithCategory
            )
        }
    }

    private fun getToken(ctx: PlatformContext):String?{
        if (prefs == null){
            prefs = SharedPrefsImpl(ctx)
        }
        return prefs?.getString(PreferencesKeys.AUTH_TOKEN)
    }

    private fun formatDateMonthYear(timestamp:String,monthNames:List<String>): String {
        return try {
            // Create LocalDate from user input
            val localDate =
                Instant.parse(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date

            // Map month number to month name
            val monthName = monthNames[localDate.monthNumber - 1]

            // Format the date
            "$monthName ${localDate.year}"
        } catch (e: Exception) {
            // Handle invalid dates
            "Invalid Date"
        }
    }

    private fun formatDate(timestamp:String,monthNames:List<String>): String {
        return try {
            // Create LocalDate from user input
            val localDate =
                Instant.parse(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date


            // Map month number to month name
            val monthName = monthNames[localDate.monthNumber - 1]

            // Format the date
            "$monthName ${localDate.dayOfMonth}, ${localDate.year}"
        } catch (e: Exception) {
            // Handle invalid dates
            "Invalid Date"
        }
    }

}