package app.xl.androidapp.presentation.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentRepositoriesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoriesListViewModel by viewModels()

    private lateinit var repoAdapter: RepoAdapter

    private val divider by lazy {
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                setDrawable(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupRecyclerView()
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToViewModel() {
        bindRepositoriesListState()
        bindActions()
    }

    private fun setupNavigationBar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    viewModel.onLogoutButtonPressed()
                    true
                } else -> false
            }
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        repoAdapter = RepoAdapter()
        adapter = repoAdapter
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(divider)
        setHasFixedSize(true)
    }

    private fun bindRepositoriesListState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                RepositoriesListViewModel.State.Empty -> {
                    // TODO: - show empty placeholder
                    binding.recyclerView.isVisible = false
                }

                RepositoriesListViewModel.State.Loading -> {
                    // TODO: - show loader
                    binding.recyclerView.isVisible = false
                }

                is RepositoriesListViewModel.State.Loaded -> {
                    repoAdapter.submitList(state.repos)
                    binding.recyclerView.isVisible = true
                }

                is RepositoriesListViewModel.State.Error -> {
                    // TODO: - show error placeholder
                    binding.recyclerView.isVisible = false
                }
            }
        }
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        RepositoriesListViewModel.Action.RouteToDetail -> {
                            // TODO: - navigate to detail
                        }

                        RepositoriesListViewModel.Action.Logout -> {
                            navigateToAuth()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToAuth() {
        findNavController().navigate(R.id.action_global_authFragment)
    }
}
