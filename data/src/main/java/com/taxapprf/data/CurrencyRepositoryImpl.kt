package com.taxapprf.data

import com.taxapprf.data.remote.cbrapi.CBRAPI
import com.taxapprf.domain.CurrencyRepository
import com.taxapprf.domain.currency.CurrencyModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CurrencyRepositoryImpl @Inject constructor(
    private val cbrapi: CBRAPI,
) : CurrencyRepository {
    override fun getTodayRate(date: String) = flow<List<CurrencyModel>> {
        val currencies = cbrapi.getCurrency(date).execute().body()?.currencyList?.mapNotNull {
            CurrencyModel(
                name = it.name,
                code = it.charCode,
                rate = it.value
            )
        }
        currencies?.let {
            emit(currencies)
        }
    }
}