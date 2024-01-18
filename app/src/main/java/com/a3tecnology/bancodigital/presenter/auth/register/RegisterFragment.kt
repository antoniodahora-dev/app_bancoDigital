package com.a3tecnology.bancodigital.presenter.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.databinding.FragmentRegisterBinding
import com.a3tecnology.bancodigital.presenter.profile.ProfileViewModel
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
        hideKeyboard()
    }

    // action btn onclick
    private fun initListener() {
        binding.btnRegister.setOnClickListener {
            validatorData()
        }
    }

    // verify the editText
    private fun validatorData() {
        val name = binding.editRegisterNome.text.toString().trim()
        val email = binding.editRegisterEmail.text.toString().trim()
        val phone = binding.editRegisterPhone.unMaskedText // using mask phone
        val password = binding.editRegisterPassword.text.toString().trim()
        val confirmPassword = binding.editRegisterConfirmPassword.text.toString().trim()


        if (name.isNotEmpty()) {
            if (email.isNotEmpty()) {
                if (phone?.isNotEmpty() == true) { // verifica a condição do campo da mascara

                    if (phone.length == 11) {
                        if (password.isNotEmpty()) {
                            if (confirmPassword.isNotEmpty()) {
                                if (password == confirmPassword) {
                                    binding.progressBar.isVisible = true

                                    hideKeyboard()
                                    registerUser(name, email, password, phone)
                                } else {
                                    showBottomSheet(message = getString(R.string.txt_confirm_password_empty))
                                }

                            } else {
                                showBottomSheet(message = getString(R.string.txt_confirm_empty))
                            }
                        } else {
                            showBottomSheet(message = getString(R.string.txt_password_empty))
                        }

                    } else {
                        showBottomSheet(message = getString(R.string.msg_phone_register_invalid))
                    }
                } else {
                    showBottomSheet(message = getString(R.string.txt_phone_empty))
                }
            } else {
                showBottomSheet(message = getString(R.string.txt_email_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.txt_name_empty))
        }
    }

    // save user no Firebase Authentication
    private fun registerUser(name: String, email: String, password: String, phone: String) {
        registerViewModel.register(name, email, password, phone).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                   stateView.data?.let { saveProfile(it) }
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

    // save user no Firebase Realtime Database
    private fun saveProfile(user: User) {
        profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                    findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)

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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}