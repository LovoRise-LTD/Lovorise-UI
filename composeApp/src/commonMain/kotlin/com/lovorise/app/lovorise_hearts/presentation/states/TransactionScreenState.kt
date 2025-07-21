package com.lovorise.app.lovorise_hearts.presentation.states

import com.lovorise.app.lovorise_hearts.domain.model.TransactionData

data class TransactionScreenState(
    val transactions:List<TransactionData> = emptyList(),
    val transactionsWithCategory:List<TransactionsWithCategory> = emptyList(),
    val incomingTransactionsWithCategory:List<TransactionsWithCategory> = emptyList(),
    val outgoingTransactionsWithCategory:List<TransactionsWithCategory> = emptyList(),
    val isLoading:Boolean = false,
    val showTransactionFilterSheet:Boolean = false,
){
    data class TransactionsWithCategory(
        val category:String,
        val transactions:List<TransactionData>
    )
}
