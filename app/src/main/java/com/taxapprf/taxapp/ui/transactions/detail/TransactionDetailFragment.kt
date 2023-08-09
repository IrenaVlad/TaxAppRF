package com.taxapprf.taxapp.ui.transactions.detail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.taxapprf.taxapp.R
import com.taxapprf.taxapp.databinding.FragmentNewTransactionBinding
import com.taxapprf.taxapp.ui.BaseState
import com.taxapprf.taxapp.ui.BottomSheetBaseFragment
import com.taxapprf.taxapp.ui.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class TransactionDetailFragment : BottomSheetBaseFragment(R.layout.fragment_new_transaction) {
    private val binding by viewBinding(FragmentNewTransactionBinding::bind)
    private val viewModel by viewModels<TransactionDetailViewModel>()

    private lateinit var currenciesAdapter: ArrayAdapter<String>
    private lateinit var typeTransactionAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.account.observe(viewLifecycleOwner) { account ->
            viewModel.saveTransaction.accountKey = account.name
            activityViewModel.report?.let {
                viewModel.saveTransaction.from(it)
                activityViewModel.report = null
            }
            activityViewModel.transaction?.let {
                viewModel.saveTransaction.from(it)
                activityViewModel.transaction = null
            }
            updateUI()
        }

        prepCurrencies()
        prepTypeTransaction()
        prepListeners()

        viewModel.attachToBaseFragment()
        viewModel.state.observe(viewLifecycleOwner) { it.observeState() }
    }

    private fun prepCurrencies() {
        val currencies = resources.getStringArray(R.array.transaction_currencies)
        currenciesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        currenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerNewTransCurrencies.adapter = currenciesAdapter
        binding.spinnerNewTransCurrencies.setSelection(
            currencies.indexOf(resources.getString(R.string.transaction_currency_usd))
        )
    }

    private fun prepTypeTransaction() {
        val typeTransactions = resources.getStringArray(R.array.transaction_types)
        typeTransactionAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, typeTransactions)
        typeTransactionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerNewTransType.adapter = typeTransactionAdapter
        binding.spinnerNewTransType.setSelection(
            typeTransactions.indexOf(resources.getString(R.string.transaction_type_trade))
        )
    }

    private fun prepListeners() {
        binding.imageNewTransDate.setOnClickListener {
            showDatePicker {
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.saveTransaction.updateDate(year, month, dayOfMonth)
                    binding.editNewTransDate.setText(viewModel.saveTransaction.date)
                }
            }
        }

        binding.buttonNewTransAdd.setOnClickListener {
            viewModel.saveTransaction()
        }

        binding.spinnerNewTransType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                typeTransactionAdapter.getItem(position)?.let {
                    viewModel.saveTransaction.type = it
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.spinnerNewTransCurrencies.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currenciesAdapter.getItem(position)?.let {
                    viewModel.saveTransaction.currency = it
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.editNewTransSum.doOnTextChanged { text, _, _, _ ->
            val sum = text?.toString()?.toDouble() ?: run { 0.0 }
            viewModel.saveTransaction.sum = sum
        }

        binding.editNewTransId.doOnTextChanged { text, _, _, _ ->
            viewModel.saveTransaction.name = text.toString()
        }

        binding.editNewTransDate.doOnTextChanged { text, _, _, _ ->
            println(text)
            viewModel.saveTransaction.date = text.toString()
        }
    }

    private fun BaseState.observeState() = when (this) {
        is BaseState.SuccessDelete -> {
            binding.root.showSnackBar(R.string.transaction_detail_delete_success)
            popBackStack()
        }

        else -> {}
    }

    private fun updateUI() {
        with(viewModel.saveTransaction) {
            binding.editNewTransId.setText(name)
            binding.editNewTransDate.setText(date)
            if (sum > 0) binding.editNewTransSum.setText(sum.toString())
            updateCurrencies(currency)
            updateTypeTransaction(type)
        }
    }

    private fun updateCurrencies(currency: String) {
        binding.spinnerNewTransCurrencies.setSelection(currenciesAdapter.getPosition(currency))
    }

    private fun updateTypeTransaction(type: String) {
        binding.spinnerNewTransType.setSelection(typeTransactionAdapter.getPosition(type))
    }

    private fun showDatePicker(listener: () -> DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(), listener.invoke(),
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}