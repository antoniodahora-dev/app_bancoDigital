package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.a3tecnology.bancodigital.MainGraphDirections
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.Transfer
import com.a3tecnology.bancodigital.databinding.FragmentTransferConfirmBinding
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.GetMask
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {

    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmTransferFragmentArgs by navArgs()
    private val confirmTransferViewModel: ConfirmTransferViewModel by viewModels()

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initToolbar(binding.toolbar, true)
        configData()
       initListeners()
    }

    private fun initListeners() {
        binding.btnTransferConfirm.setOnClickListener {
            binding.btnTransferConfirm.isEnabled = false
            getBalance()
        }
    }

    private fun getBalance() {
        confirmTransferViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {

                    if ((stateView.data ?: 0f) >= args.amount) {
                             val transfer = Transfer(
                                idUserReceived  = args.user.id,
                                idUserSent = FirebaseHelp.getUserId(),
                                amount = args.amount
                            )

                         saveTransfer(transfer)
                        binding.btnTransferConfirm.isEnabled = true

                    } else {
                        binding.progressBar.isVisible = false


                          showBottomSheet(
                              message = getString(R.string.txt_amount_transfer_insufficient_transaction)
                            )
                        }
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    binding.btnTransferConfirm.isEnabled = true

                    showBottomSheet(
                        message = getString(
                            FirebaseHelp.validatorError(stateView.message ?: "")))
                }
            }
        }
    }

    private fun saveTransfer(transfer: Transfer) {
        confirmTransferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
//                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
//                    binding.progressBar.isVisible = false
                    updateTransfer(transfer)
//                    Toast.makeText(requireContext(),
//                        "Valor Transferido.", Toast.LENGTH_SHORT).show()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
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

    private fun updateTransfer(transfer: Transfer) {
        confirmTransferViewModel.updateTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    saveTransaction(transfer)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
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

    private fun saveTransaction(transfer: Transfer) {

        confirmTransferViewModel.saveTransaction(transfer).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {

                is StateView.Loading -> {
                }

                is StateView.Success -> {
                    updateTransferTransaction(transfer)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }

        }
    }

    private fun updateTransferTransaction(transfer: Transfer) {
        confirmTransferViewModel.updateTransferTransaction(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    val action = MainGraphDirections
                        .actionGlobalReceiptTransferFragment(transfer.id, false)

                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
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

    private fun configData() {

        if (args.user.image == "") {
            binding.progressBarImg.isVisible = false
            binding.imgHomeUser.isVisible = true
            binding.imgHomeUser.setImageResource(R.drawable.ic_user_place_holder)
        } else {

            Picasso.get()
                .load(args.user.image)
                .tag(tagPicasso)
                .fit().centerCrop()
                .into(binding.imgHomeUser, object : Callback {
                    override fun onSuccess() {
                        binding.imgHomeUser.isVisible = true
                        binding.progressBarImg.isVisible = false
                    }
                    override fun onError(e: Exception?) {
                    }
                })
        }

        binding.txtTransferUser.text = args.user.name
        binding.txtTransferAmount.text = getString(R.string.txt_formated_value,
            GetMask.getFormatedValue(args.amount))

    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }
}