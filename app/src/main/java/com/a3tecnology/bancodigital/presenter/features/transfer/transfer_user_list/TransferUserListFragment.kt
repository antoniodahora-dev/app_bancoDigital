package com.a3tecnology.bancodigital.presenter.features.transfer.transfer_user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.databinding.FragmentTransferUserListBinding
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferUserListFragment : Fragment() {

    private var _binding: FragmentTransferUserListBinding? = null
    private val binding get() = _binding!!

    private val transferUserListViewModel: TransferUserListViewModel by viewModels()
    private lateinit var transferUserAdapter: TransferUserAdapter

    private var profilesList: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTransferUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initRecyclerView()
        getProfileList()
        configSearchView()
    }

    private fun configSearchView() {
        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.isNotEmpty()) {
                    val newList = profilesList.filter { it.name.contains(newText, true) }

                    emptyUserList(newList)

                    transferUserAdapter.submitList(newList)

                    true
                } else {
                    emptyUserList(profilesList)

                    transferUserAdapter.submitList(profilesList)
                    false
                }
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }

        })

        binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                emptyUserList(profilesList)
                transferUserAdapter.submitList(profilesList)
            }

            override fun onSearchViewShownAnimation() {

            }

            override fun onSearchViewClosedAnimation() {

            }
        })
    }

    private fun initRecyclerView() {
        transferUserAdapter =  TransferUserAdapter { userSelect ->
            val action = TransferUserListFragmentDirections.actionTransferUserListFragmentToTransferFormFragment(userSelect)

            findNavController().navigate(action)
        }

        with(binding.rvTransferUser) {
            setHasFixedSize(true)
            adapter = transferUserAdapter
        }
    }

    private fun emptyUserList(list: List<User>) {
        binding.txtMessageEmpty.isVisible = list.isEmpty()
    }

    private fun getProfileList() {
        transferUserListViewModel.getProfileList().observe(viewLifecycleOwner) {stateView ->
            when(stateView) {
                is StateView.Loading -> { }

                is StateView.Success -> {
                    profilesList = stateView.data ?: emptyList()
                    transferUserAdapter.submitList(profilesList)
                    binding.progressBar.isVisible = false
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}