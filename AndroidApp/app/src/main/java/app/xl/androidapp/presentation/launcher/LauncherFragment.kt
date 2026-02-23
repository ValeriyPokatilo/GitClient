package app.xl.androidapp.presentation.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentLauncherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LauncherFragment : Fragment() {
    private var _binding: FragmentLauncherBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.destination
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { destination ->
                when (destination) {
                    LauncherViewModel.Destination.Auth -> navigateToAuth()
                    LauncherViewModel.Destination.Repositories -> navigateToRepositories()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            R.id.action_launcherFragment_to_authFragment
        )
    }

    private fun navigateToRepositories() {
        findNavController().navigate(
            R.id.action_launcherFragment_to_repositoriesListFragment
        )
    }
}