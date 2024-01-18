package com.a3tecnology.bancodigital.presenter.features.deposito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.MainGraphDirections
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.enum.TransactionOperation
import com.a3tecnology.bancodigital.data.enum.TransactionType
import com.a3tecnology.bancodigital.data.model.Deposit
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.databinding.FragmentDepositFormBinding
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.MoneyTextWatcher
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFormFragment : BaseFragment() {

    private var _binding: FragmentDepositFormBinding? = null

    private val binding get() = _binding!!

    private val depositViewModel: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)

        initListeners()
    }

    private fun initListeners() {
        with(binding.edtDeposit) {
            addTextChangedListener(MoneyTextWatcher(this))

            // limit of value editText Deposit
            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 99999.99F) {
                    this.setText("R$ 0,00")
                }
            }

            doAfterTextChanged {
                text?.length?.let { this.setSelection(it) }
            }
        }

        binding.btnDeposit.setOnClickListener {
            validateDeposit()
        }
    }

    private fun validateDeposit() {
//        val amount = binding.edtDeposit.text.toString()
        val amount = MoneyTextWatcher.getValueUnMasked(binding.edtDeposit)

        if (amount > 0F) {
            val deposit = Deposit(amount = amount) // convert of String para Float
            hideKeyboard()
            saveDeposit(deposit)

          binding.btnDeposit.isEnabled = true
        } else {
            showBottomSheet(message = getString(R.string.message_deposit_amount))
        }
    }

    private fun saveDeposit(deposit: Deposit) {
        depositViewModel.saveDeposit(deposit).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { saveTransaction(it) }

                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }

        }
    }

    private fun saveTransaction(deposit: Deposit) {

        val transaction = Transaction(
            id = deposit.id,
            operation = TransactionOperation.DEPOSIT,
            date = deposit.date,
            amount = deposit.amount,
            type = TransactionType.CASH_IN
        )

        depositViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {

                is StateView.Loading -> {
                }

                is StateView.Success -> {

                    val action = MainGraphDirections
                        .actionGlobalDepositReceiptFragment(deposit.id, false)

                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}