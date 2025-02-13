package com.taxapprf.domain.transaction

import com.taxapprf.domain.TransactionRepository
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun execute(transaction: SaveTransactionModel) =
        repository.saveTransactionModel(transaction)
}