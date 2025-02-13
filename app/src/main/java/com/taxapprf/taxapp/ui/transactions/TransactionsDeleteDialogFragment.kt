package com.taxapprf.taxapp.ui.transactions

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.taxapprf.taxapp.R

class TransactionsDeleteDialogFragment : DialogFragment() {
    private val prevStackSavedState by lazy {
        findNavController().previousBackStackEntry!!.savedStateHandle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.transactions_delete_title)
            .setMessage(R.string.transactions_delete_message)
            .setPositiveButton(R.string.transactions_delete_ok) { _, _ ->
                popBackStackWithBundle(true)
            }
            .setNegativeButton(R.string.transactions_delete_cancel) { _, _ ->
                popBackStackWithBundle(false)
            }
            .create()

    private fun popBackStackWithBundle(state: Boolean) {
        prevStackSavedState[TransactionsFragment.TRANSACTIONS_DELETE_DIALOG_RESULT] = state
        findNavController().popBackStack()
    }
}