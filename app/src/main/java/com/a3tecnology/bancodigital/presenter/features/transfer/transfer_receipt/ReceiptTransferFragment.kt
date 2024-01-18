package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.Transfer
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.databinding.FragmentTransferReceiptBinding
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.GetMask
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptTransferFragment : Fragment() {

    private var _binding: FragmentTransferReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: ReceiptTransferFragmentArgs by navArgs()
    private val receiptTransferViewModel: ReceiptTransferViewModel by viewModels()

    private val tagPicasso = "tagPicasso"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initToolbar(binding.toolbar, args.homeAsUpEnable)
       getTransfer()
       initListeners()
    }

    private fun initListeners() {
        binding.btnTransferReceipt.setOnClickListener {
            if (args.homeAsUpEnable) {
                findNavController().popBackStack()
            } else {
                val navOptions: NavOptions =
                    NavOptions.Builder().setPopUpTo(R.id.transferUserListFragment,true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun getTransfer() {
        receiptTransferViewModel.getTransfer(args.idTransfer)
            .observe(viewLifecycleOwner) { stateView ->

            when (stateView) {
                is StateView.Loading -> { }

                is StateView.Success -> {
                    stateView.data?.let { transfer ->
                        val userId =
                            if (transfer.idUserSent == FirebaseHelp.getUserId()) {
                                transfer.idUserReceived
                            } else {
                                transfer.idUserSent
                            }

                        getProfile(userId)
                        configTransfer(transfer)
                    }
                }

                is StateView.Error -> {
                    showBottomSheet(message = stateView.message)
                }
            }

        }
    }

    private fun getProfile(id: String) {
        receiptTransferViewModel.getProfile(id).observe(viewLifecycleOwner) { stateView ->

            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let {
                        configProfile(it)
                    }
                }

                is StateView.Error -> {
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun configTransfer(transfer: Transfer) {

        binding.txtSendReceivedTransaction.text =
         if (transfer.idUserSent == FirebaseHelp.getUserId()) {
             getString(R.string.txt_code_transfer_send_transaction_user)
        } else {
             getString(R.string.txt_code_transfer_receipt_transaction_user)
        }

        binding.txtTransferCode.text = transfer.id
        binding.txtTransferDate.text = GetMask.getFormatedDate(transfer.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)

        binding.txtTransferAmount.text = getString(R.string.txt_formated_value,
            GetMask.getFormatedValue(transfer.amount)) // formate money + date firebase
    }

    private fun configProfile(user: User) {

        if (user.image == "") {
            binding.progressBarImg.isVisible = false
            binding.imgHomeUser.isVisible = true
            binding.imgHomeUser.setImageResource(R.drawable.ic_user_place_holder)
        } else {

            Picasso.get()
                .load(user.image)
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
        binding.txtTransferUser.text = user.name
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }
}