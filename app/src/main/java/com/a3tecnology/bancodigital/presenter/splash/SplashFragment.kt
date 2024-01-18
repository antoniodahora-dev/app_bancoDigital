package com.a3tecnology.bancodigital.presenter.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.databinding.FragmentSplashBinding
import com.a3tecnology.bancodigital.util.FirebaseHelp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // count 3s open view HomeFragment
        Handler(Looper.getMainLooper()).postDelayed(this::verifyAuht, 3000)
    }

    //verify authentication
    private fun verifyAuht() {

        // if auth != null -> go to home
        val action =
            if (FirebaseHelp.isAuthentication()) {
                R.id.action_global_homeFragment
            } else {  // if auth == null  go to login
                R.id.action_global_authentication
            }

        val navOptions: NavOptions =
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        findNavController().navigate(action, null, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}