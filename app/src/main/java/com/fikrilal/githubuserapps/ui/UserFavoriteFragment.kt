package com.fikrilal.githubuserapps.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.adapter.UsersFavAdapter
import com.fikrilal.githubuserapps.data.database.UsersFavDb
import com.fikrilal.githubuserapps.data.repository.UsersFavRepository
import com.fikrilal.githubuserapps.databinding.FragmentUserFavoriteBinding
import com.fikrilal.githubuserapps.viewModel.UserFavViewModel
import com.fikrilal.githubuserapps.viewModel.UserFavViewModelFac
import kotlinx.coroutines.launch

class UserFavoriteFragment : Fragment() {
    private var _binding: FragmentUserFavoriteBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding cannot be accessed before onCreateView or after onDestroyView.")

    private val viewModel: UserFavViewModel by viewModels {
        UserFavViewModelFac(UsersFavRepository(UsersFavDb.getDatabase(requireContext()).usersDatabase()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("UserFavoriteFragment", "onCreateView: Inflating the fragment's view")
        _binding = FragmentUserFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("UserFavoriteFragment", "onViewCreated: Setting up the fragment")
        setupListAdapter()
        setupBackButton()
    }

    private fun setupListAdapter() {
        Log.d("UserFavoriteFragment", "Setting up list adapter for user favorites")
        val favUserAdapter = UsersFavAdapter(listOf()) { username ->
            navigateToUserDetails(username)
        }
        binding.idRecycleUserfav.adapter = favUserAdapter

        lifecycleScope.launch {
            viewModel.userFavLiveData.collect { users ->
                Log.d("UserFavoriteFragment", "Updating list adapter with new data")
                favUserAdapter.updateUsers(users)
            }
        }
    }

    private fun setupBackButton() {
        Log.d("UserFavoriteFragment", "Setting up back button listener")
        binding.idButtonBack.setOnClickListener {
            Log.d("UserFavoriteFragment", "Back button clicked, popping back stack")
            parentFragmentManager.popBackStack()
        }
    }

    private fun navigateToUserDetails(username: String) {
        Log.d("UserFavoriteFragment", "Navigating to UserDetailsFragment with username: $username")
        val userDetailFragment = UserDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.id_container, userDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("UserFavoriteFragment", "onDestroyView: Clearing the binding reference")
    }

    companion object {
        fun newInstance() = UserFavoriteFragment()
    }
}
