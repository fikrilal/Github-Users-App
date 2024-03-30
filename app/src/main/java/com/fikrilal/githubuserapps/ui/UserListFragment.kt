package com.fikrilal.githubuserapps.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.adapter.FollowersAdapter
import com.fikrilal.githubuserapps.databinding.FragmentUserListBinding
import com.fikrilal.githubuserapps.ui.decoration.SpacesItemDecoration
import com.fikrilal.githubuserapps.viewModel.UserListViewModel
import com.google.android.material.snackbar.Snackbar

class UserListFragment : Fragment() {
    companion object {
        fun newInstance() = UserListFragment()
        private const val TAG = "UserListFragment"
    }

    private val viewModel: UserListViewModel by viewModels()
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView called")
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        setupRecyclerView()
        observeViewModel()

        processArguments()
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView")
        with(binding.idListCardFragment) {
            layoutManager = LinearLayoutManager(context)
            adapter = FollowersAdapter(emptyList()) { username ->
                Log.d(TAG, "Item clicked: $username")
                navigateToUserDetails(username)
            }
            val spaceInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                resources.displayMetrics
            ).toInt()
            addItemDecoration(SpacesItemDecoration(spaceInPixels))
        }
    }


    private fun processArguments() {
        arguments?.let { bundle ->
            val userTypeIndex = bundle.getInt("section_number")
            val username = bundle.getString("username") ?: ""
            Log.d(TAG, "Fetching user data for username: $username, typeIndex: $userTypeIndex")

            val userType = if (userTypeIndex == 0) UserListViewModel.UserType.FOLLOWERS
            else UserListViewModel.UserType.FOLLOWING
            viewModel.fetchUserData(username, userType)
        }
    }

    private fun observeViewModel() {
        Log.d(TAG, "Observing ViewModel")
        viewModel.apply {
            loadingStatusLiveData.observe(viewLifecycleOwner) { isLoading ->
                Log.d(TAG, "Loading status changed: $isLoading")
                showLoading(isLoading)
            }

            snackbarMessageLiveData.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { snackBarMessage ->
                    Log.d(TAG, "Showing Snackbar: $snackBarMessage")
                    Snackbar.make(requireView().rootView, snackBarMessage, Snackbar.LENGTH_SHORT).show()
                }
            }

            followerLiveData.observe(viewLifecycleOwner) { users ->
                Log.d(TAG, "Observing followers: ${users.size} users found")
                (binding.idListCardFragment.adapter as FollowersAdapter).updateFollowers(users)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        Log.d(TAG, "Show loading: $isLoading")
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToUserDetails(username: String) {
        Log.d(TAG, "Navigating to UserDetails with username: $username")
        val detailFragment = UserDetailsFragment().apply {
            arguments = Bundle().apply { putString("username", username) }
        }
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.id_container, detailFragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called - clearing binding")
        _binding = null
    }
}