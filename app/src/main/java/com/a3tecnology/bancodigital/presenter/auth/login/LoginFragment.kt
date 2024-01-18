package com.a3tecnology.bancodigital.presenter.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.databinding.FragmentLoginBinding
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.txtRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }

        binding.btnLogin.setOnClickListener {
            validatorData()
        }
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email , password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                    findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
//                    Toast.makeText(requireContext(), "Login com Sucesso.", Toast.LENGTH_LONG).show()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    Log.i("teste", "Login ${stateView.message}")
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
        val email = binding.edtLoginEmail.text.toString().trim()
        val password = binding.edtLoginPassword.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {

                binding.progressBar.isVisible = true

                hideKeyboard()
                loginUser(email, password)

            } else {
                showBottomSheet(message = getString(R.string.txt_password_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.txt_email_empty))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}