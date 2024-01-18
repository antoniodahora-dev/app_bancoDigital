package com.a3tecnology.bancodigital.presenter.auth.recover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.databinding.FragmentRecoverBinding
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverFragment : BaseFragment() {

    private var _binding: FragmentRecoverBinding? = null

    private val binding get() = _binding!!

    private val recoverViewModel: RecoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener() {
        binding.btnRecover.setOnClickListener {
            validatorData()
        }
    }

    private fun recoverAccount(email: String) {
        recoverViewModel.recover(email).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    showBottomSheet(message = getString(R.string.txt_email_send_recover_success))
                    Log.i("email" , "Recover ${stateView.message}")

                }

                is StateView.Error -> {
                    Log.i("email" , "Error ${stateView.message}")
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

    private fun validatorData() {
        val email = binding.editRecoverEmail.text.toString().trim()

        if (email.isNotEmpty()) {

            binding.progressBar.isVisible = true
            hideKeyboard()
            recoverAccount(email)

        } else {
            showBottomSheet(message = getString(R.string.txt_email_empty))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}