package com.fikrilal.githubuserapps.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.adapter.TabsAdapter
import com.fikrilal.githubuserapps.data.response.UserDetailsResponse
import com.fikrilal.githubuserapps.databinding.FragmentUserDetailsBinding
import com.fikrilal.githubuserapps.viewModel.UserDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailsFragment : Fragment() {
    companion object {
        private const val TAG = "UserDetailsFragment"
        fun newInstance() = UserDetailsFragment()
    }

    private val viewModel: UserDetailsViewModel by viewModels()
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding cannot be accessed before onCreateView or after onDestroyView.")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView called")
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        val username = arguments?.getString("username") ?: throw IllegalArgumentException("Username is required.")
        startShimmer()
        viewModel.fetchUserDetails(username)
        observeViewModel()
        setupTabBar(username)
    }

    private fun observeViewModel() {
        Log.d(TAG, "Setting up ViewModel observers")
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { snackBarMessage ->
                Snackbar.make(requireView(), snackBarMessage, Snackbar.LENGTH_SHORT).show()
                Log.d(TAG, "Snackbar displayed: $snackBarMessage")
            }
        }

        viewModel.userDetail.observe(viewLifecycleOwner, Observer { userDetails ->
            userDetails?.let {
                updateUI(it)
                Log.d(TAG, "User details updated")
            }
        })
    }

    private fun startShimmer() {
        Log.d(TAG, "Starting shimmer effect")
        binding?.apply {
            idShimmerFrameTop.startShimmer()
            idShimmerFrameFollower.startShimmer()
            idShimmerFrameFollowing.startShimmer()
        }
    }

    private fun stopShimmer() {
        Log.d(TAG, "Stopping shimmer effect")
        binding?.apply {
            idShimmerFrameTop.hideShimmer()
            idShimmerFrameFollower.hideShimmer()
            idShimmerFrameFollowing.hideShimmer()
        }
    }

    private fun setupTabBar(username: String) {
        Log.d(TAG, "Setting up TabBar for $username")
        binding?.apply {
            viewPager.adapter = TabsAdapter(this@UserDetailsFragment, username)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = if (position == 0) "Followers" else "Following"
            }.attach()
        }
    }

    private fun updateUI(user: UserDetailsResponse) {
        stopShimmer()

        val shimmer = Shimmer.AlphaHighlightBuilder().setDuration(1800).setBaseAlpha(0.7f)
            .setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.RIGHT_TO_LEFT).setAutoStart(true).build()
        val shimmerDrawable = ShimmerDrawable().apply { setShimmer(shimmer) }

        binding?.apply {
            Glide.with(requireActivity()).load(user.avatarUrl).placeholder(shimmerDrawable).into(imageView)
            idTvName.text = user.name
            idTvUsername.text = user.login
            idTvFollowing.text = user.following.toString()
            idTvFollower.text = user.followers.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called - clearing binding")
        _binding = null
    }
}