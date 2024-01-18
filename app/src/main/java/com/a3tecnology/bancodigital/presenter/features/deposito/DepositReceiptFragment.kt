package com.a3tecnology.bancodigital.presenter.features.deposito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.Deposit
import com.a3tecnology.bancodigital.databinding.FragmentDepositReceiptBinding
import com.a3tecnology.bancodigital.util.GetMask
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {

    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: DepositReceiptFragmentArgs by navArgs()

    private val depositReceiptViewModel: DepositReceiptViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initToolbar(binding.toolbar, args.homeAsUpEnabled)
       getDeposit()
       initListeners()
    }

    private fun initListeners() {
        binding.btnDepositReceipt.setOnClickListener {
            if (args.homeAsUpEnabled) {
                 findNavController().popBackStack()
            } else {
                val navOptions: NavOptions =
                    NavOptions.Builder().setPopUpTo(R.id.depositFormFragment,true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun getDeposit() {

        depositReceiptViewModel.getDeposit(args.idDeposit).observe(viewLifecycleOwner) {stateView ->

            when(stateView) {
                is StateView.Loading -> {}
                is StateView.Success -> {
                   stateView.data?.let {
                       configData(it)
                   }
                }
                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun configData(deposit: Deposit) {
        binding.txtDepositCode.text = deposit.id
        binding.txtDepositDate.text = GetMask.getFormatedDate(deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.txtDepositAmount.text = getString(R.string.txt_formated_value,
            GetMask.getFormatedValue(deposit.amount)) // formate money + date firebase
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}