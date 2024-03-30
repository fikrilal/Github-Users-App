package com.fikrilal.githubuserapps.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.adapter.UsersAdapter
import com.fikrilal.githubuserapps.data.response.ItemsItem
import com.fikrilal.githubuserapps.databinding.FragmentUserSearchBinding
import com.fikrilal.githubuserapps.ui.decoration.SpacesItemDecoration
import com.fikrilal.githubuserapps.viewModel.UserSearchViewModel
import com.google.android.material.snackbar.Snackbar

class UserSearchFragment : Fragment() {
    private val viewModel: UserSearchViewModel by viewModels()
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        Log.d("UserSearchFragment", "Initializing UI components")
        viewModel.userSearch("a") // Initial search with default parameter
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("UserSearchFragment", "Setting up RecyclerView")
        binding.idCardUserSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UsersAdapter(emptyList()) { username ->
                navigateToUserDetail(username)
            }
            val spaceInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                resources.displayMetrics
            ).toInt()
            addItemDecoration(SpacesItemDecoration(spaceInPixels))
        }
    }


    private fun navigateToUserDetail(username: String) {
        Log.d("UserSearchFragment", "Navigating to UserDetailsFragment with username: $username")
        val detailFragment = UserDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.id_container, detailFragment)
            addToBackStack(null) // Optional, for adding the transaction to the back stack
            commit()
        }
    }

    private fun setupSearchView() {
        Log.d("UserSearchFragment", "Setting up SearchView")
        with(binding.searchView) {
            setupWithSearchBar(binding.idSearchBar)
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().takeIf { it.isNotEmpty() }?.let { searchQuery ->
                        viewModel.userSearch(searchQuery)
                        binding.idSearchBar.setText(text)
                        hide()
                    }
                    true
                } else false
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        Log.d("UserSearchFragment", "Setting loading visibility to $isLoading")
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.loadingStatusLiveData.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })
        viewModel.snackbarMessageLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(requireView().rootView, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.userSearchResult.observe(viewLifecycleOwner, Observer { searchResponse ->
            searchResponse?.items?.let { updateUI(it as List<ItemsItem>) }
        })
    }

    private fun updateUI(users: List<ItemsItem>) {
        Log.d("UserSearchFragment", "Updating UI with new user data")
        (binding.idCardUserSearch.adapter as? UsersAdapter)?.updateUsers(users)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("UserSearchFragment", "onDestroyView: View binding cleared")
    }

    companion object {
        fun newInstance() = UserSearchFragment()
    }
}
