package com.a3tecnology.bancodigital.presenter.features.extract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.MainGraphDirections
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.enum.TransactionOperation
import com.a3tecnology.bancodigital.databinding.FragmentExtractBinding
import com.a3tecnology.bancodigital.presenter.home.HomeFragmentDirections
import com.a3tecnology.bancodigital.presenter.home.TransactionsAdapter
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val binding get() = _binding!!

    private val extractViewModel: ExtractViewModel by viewModels()
    private lateinit var adapterTransaction: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        configRecycleView()
        getTransactions()
    }

    private fun configRecycleView() {

        adapterTransaction = TransactionsAdapter(requireContext()) { transaction ->
            when (transaction.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action = MainGraphDirections
                        .actionGlobalDepositReceiptFragment(transaction.id, true)

                    findNavController().navigate(action)
                }

                TransactionOperation.RECHARGE -> {
                    val action = MainGraphDirections
                        .actionGlobalRechargeReceiptFragment(transaction.id, true)

                    findNavController().navigate(action)

                }

                TransactionOperation.TRANSFER -> {
                    val action = MainGraphDirections
                        .actionGlobalReceiptTransferFragment(transaction.id, true)

                    findNavController().navigate(action)

                }

                else -> {

                }
            }
        }
        with(binding.rvHomeTransactions) {
            setHasFixedSize(true)
            adapter = adapterTransaction
        }
    }

    private fun getTransactions() {
        extractViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBarRv.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBarRv.isVisible = false
                    adapterTransaction.submitList(stateView.data?.reversed()) //?.take(6) diz a quantidade de item a serem exibidos
                    binding.txtMessageEmpty.isVisible = stateView.data?.isEmpty() == true
                }

                is StateView.Error -> {
                    binding.progressBarRv.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}