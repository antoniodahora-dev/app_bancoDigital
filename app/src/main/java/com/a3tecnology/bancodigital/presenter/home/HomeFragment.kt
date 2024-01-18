package com.a3tecnology.bancodigital.presenter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.MainGraphDirections
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.enum.TransactionOperation
import com.a3tecnology.bancodigital.data.enum.TransactionType
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.databinding.FragmentHomeBinding
import com.a3tecnology.bancodigital.presenter.features.deposito.DepositFormFragmentDirections
import com.a3tecnology.bancodigital.presenter.features.recharge.RechargeFormFragmentDirections
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.GetMask
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var adapterTransaction: TransactionsAdapter

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecycleView()
        getTransactions()
        initListeners()
        getProfile()
    }

    private fun initListeners() {
        //logout app
        binding.imgHomeLogout.setOnClickListener{
            FirebaseHelp.getAuth().signOut()

            val navOptions: NavOptions =
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
            findNavController().navigate(R.id.action_global_authentication, null, navOptions)

        }

        binding.cardVHomeDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }

        binding.cardVHomePerfil.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.txtShowAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.cardVHomeExtract.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.cardVHomeRecharge.setOnClickListener {
            findNavController().navigate(R.id.rechargeFormFragment)
        }

        binding.cardVHomeTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferUserListFragment)
        }
    }

    private fun configRecycleView() {

        adapterTransaction = TransactionsAdapter(requireContext()) { transaction ->
            when(transaction.operation) {
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

    private fun getProfile() {
        homeViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBarRv.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBarRv.isVisible = false

                    stateView.data?.let {
                        configData(it)
                    }

                }

                is StateView.Error -> {
                    binding.progressBarRv.isVisible = false
                    showBottomSheet(
                        message = getString(
                            FirebaseHelp.validatorError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun configData(user: User) {

        if (user.image == "") {
            binding.progressBarImg.isVisible = false
            binding.imgHomeUser.isVisible = true
            binding.imgHomeUser.setImageResource(R.drawable.ic_user)
        } else {

            Picasso.get()
                .load(user.image)
                .tag(tagPicasso)
                .fit()
                .centerCrop()
                .rotate(90f) // orientation photo
                .into(binding.imgHomeUser, object : Callback {
                    override fun onSuccess() {
                        binding.imgHomeUser.isVisible = true
                        binding.progressBarImg.isVisible = false
                    }
                    override fun onError(e: Exception?) {
                    }
                })
        }


    }

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBarRv.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBarRv.isVisible = false

                    adapterTransaction.submitList(stateView.data?.reversed()?.take(12)) //take diz a quantidade de item a serem exibidos
                    showBalance(stateView.data ?: emptyList())

                    binding.txtMessageEmpty.isVisible = stateView.data?.isEmpty() == true
                }
                is StateView.Error -> {
                    binding.progressBarRv.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun showBalance(transactions: List<Transaction>) {

        var cashIn = 0f
        var cashOut = 0f

        transactions.forEach { transaction ->
            if (transaction.type == TransactionType.CASH_IN) {
                cashIn += transaction.amount
            } else {
                cashOut += transaction.amount
            }
        }
        binding.txtHomeBalance.text =
            getString(R.string.txt_formated_value, GetMask.getFormatedValue(cashIn - cashOut))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }

}