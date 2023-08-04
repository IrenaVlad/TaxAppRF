package com.taxapprf.taxapp.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.taxapprf.domain.taxes.DeleteTaxModel
import com.taxapprf.domain.taxes.DeleteTaxUseCase
import com.taxapprf.domain.transaction.GetTransactionsUseCase
import com.taxapprf.domain.transaction.TransactionModel
import com.taxapprf.domain.transaction.TransactionsModel
import com.taxapprf.taxapp.ui.BaseState
import com.taxapprf.taxapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTaxUseCase: DeleteTaxUseCase,
) : BaseViewModel() {
    val year = savedStateHandle.get<String>(YEAR)!!

    private val _transactions = MutableLiveData<TransactionsModel>()
    val transactions: LiveData<TransactionsModel> = _transactions
    fun loadTransactions(account: String) = viewModelScope.launch(Dispatchers.IO) {
        // TODO нет обработки пустого результа, показ какого то сообщения
        getTransactionsUseCase.execute(account, year)
            .onStart { loading() }
            .catch { error(it) }
            .onEach { success() }
            .collectLatest {
                success()
                _transactions.postValue(it)
            }
    }

    /*        firebaseTransactions = FirebaseTransactions(UserLivaData().getFirebaseUser(), account)
            firebaseTransactions.readTransactions(year, object : DataStatus() {
                fun DataIsLoaded(transactionsDB: List<Transaction>) {
                    transactions.setValue(transactionsDB)
                    mtransactions = transactionsDB
                }

                fun DataIsInserted() {}
                fun DataIsUpdated() {}
                fun DataIsDeleted() {}
            })
            firebaseYearSum = FirebaseYearSum(UserLivaData().getFirebaseUser(), account)
            firebaseYearSum.readYearSum(year, object : DataStatus() {
                fun DataIsLoaded(sumTaxesDB: Double) {
                    sumTaxes.setValue(sumTaxesDB)
                    msumTaxes = sumTaxesDB
                }
            })*/


    fun deleteTax(account: String) = viewModelScope.launch(Dispatchers.IO) {
        val deleteTaxModel = DeleteTaxModel(account, year)
        deleteTaxUseCase.execute(deleteTaxModel)
            .onStart { loading() }
            .catch { error(it) }
            .collectLatest {
                success(BaseState.SuccessDelete)
            }
    }

    fun downloadStatement() {}
    /*        year = settings.getString(Settings.YEAR.name, "")
            return try {
                val excelStatement =
                    CreateExcelInDownload(year, msumTaxes, mtransactions)
                excelStatement.create()
            } catch (e: IOException) {
                //e.printStackTrace(); //...
                null
            }*/


    fun createLocalStatement() {
        /*        return try {
                    val excelStatement =
                        CreateExcelInLocal(getApplication(), year, msumTaxes, mtransactions)
                    var file: File? = null
                    file = excelStatement.create()
                    file
                } catch (e: IOException) {
                    //e.printStackTrace();
                    null
                }*/
    }

    companion object {
        const val YEAR = "year"
    }
}