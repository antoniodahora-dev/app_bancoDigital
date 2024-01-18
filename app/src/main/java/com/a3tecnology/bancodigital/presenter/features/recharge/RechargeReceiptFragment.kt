package com.a3tecnology.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.Recharge
import com.a3tecnology.bancodigital.databinding.FragmentRechargeReceiptBinding
import com.a3tecnology.bancodigital.util.Constants
import com.a3tecnology.bancodigital.util.GetMask
import com.a3tecnology.bancodigital.util.Mask
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {

    private var _binding: FragmentRechargeReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: RechargeReceiptFragmentArgs by navArgs()
    private val rechargeReceiptViewModel: RechargeReceiptViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      _binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, homeAsUpEnabled = args.homeAsUpEnabled)
        getRecharge()
        initListeners()
    }

    private fun initListeners() {
        binding.btnRechargeReceipt.setOnClickListener {
            if (args.homeAsUpEnabled) {
                findNavController().popBackStack()
            } else {
                val navOptions: NavOptions =
                    NavOptions.Builder().setPopUpTo(R.id.rechargeFormFragment,true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun getRecharge() {

        rechargeReceiptViewModel.getRecharge(args.idRecharge).observe(viewLifecycleOwner) {stateView ->

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

    private fun configData(recharge: Recharge) {
        binding.txtRechargeCode.text = recharge.id

        binding.txtRechargeDate.text =
            GetMask.getFormatedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)

        binding.txtRechargeAmount.text = getString(R.string.txt_formated_value,
            GetMask.getFormatedValue(recharge.amount)) // formate money + date firebase

        binding.txtRechargePhone.text = Mask.mask(Constants.Mask.MASK_PHONE , recharge.phone) // mask textView
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}