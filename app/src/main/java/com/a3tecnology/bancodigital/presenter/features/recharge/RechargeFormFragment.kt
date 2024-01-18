package com.a3tecnology.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.a3tecnology.bancodigital.data.model.Recharge
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.databinding.FragmentRechargeFormBinding
import com.a3tecnology.bancodigital.presenter.features.deposito.DepositFormFragmentDirections
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.MoneyTextWatcher
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFormFragment : BaseFragment() {

    private var _binding:  FragmentRechargeFormBinding? = null
    private val binding get() = _binding!!

    private val rechargeViewModel: RechargeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initListeners()
    }

    private fun initListeners() {

        with(binding.edtRecharge) {
            addTextChangedListener(MoneyTextWatcher(this))

            // limit of value editText Recharge
            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 100F) {
                    this.setText("R$ 0,00")
                }
            }

            doAfterTextChanged {
                text?.length?.let { this.setSelection(it) }
            }
        }
        binding.btnRecharge.setOnClickListener {
            validatorData()
        }
    }

    private fun saveRecharge(recharge: Recharge) {
        rechargeViewModel.saveRecharge(recharge).observe(viewLifecycleOwner) { stateView ->
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

    private fun saveTransaction(recharge: Recharge) {

        val transaction = Transaction(
            id = recharge.id,
            operation = TransactionOperation.RECHARGE,
            date = recharge.date,
            amount = recharge.amount,
            type = TransactionType.CASH_OUT
        )

        rechargeViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {

                is StateView.Loading -> {
                }

                is StateView.Success -> {

//                    Toast.makeText(requireContext(), "Transição Ok", Toast.LENGTH_SHORT).show()
                    val action = MainGraphDirections
                        .actionGlobalRechargeReceiptFragment(recharge.id, false)

                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }

        }
    }

    private fun validatorData() {
        val amount = MoneyTextWatcher.getValueUnMasked(binding.edtRecharge)
        val phone = binding.editRechargePhone.unMaskedText // using mask phone

        if (amount >= 10F) {
            if (phone?.isNotEmpty() == true) { // verifica a condição do campo da mascara
                if (phone.length == 11) {

                    hideKeyboard()

                    val recharge = Recharge(
                        amount = amount,
                        phone = phone
                    )
                    saveRecharge(recharge)
                    Toast.makeText(requireContext(), "Recarga realizada para o número ${phone}", Toast.LENGTH_SHORT).show()
                } else {
                    showBottomSheet(message = getString(R.string.msg_phone_register_invalid))
                }
            } else {
                showBottomSheet(message = getString(R.string.txt_phone_recharge_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.txt_amount_empty))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}