package com.taxapprf.domain.transaction

import com.taxapprf.domain.FirebasePathModel
import com.taxapprf.domain.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun execute(firebasePathModel: FirebasePathModel) =
        repository.deleteTransaction(firebasePathModel)
}