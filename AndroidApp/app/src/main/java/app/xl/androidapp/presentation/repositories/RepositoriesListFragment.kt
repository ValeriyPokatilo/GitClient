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
import app.xl.androidapp.domain.entity.AppError
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
        repoAdapter = RepoAdapter({ repository ->
            viewModel.onRepositoryItemPressed(repository)
        })
        adapter = repoAdapter
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(divider)
        setHasFixedSize(true)
    }

    private fun bindRepositoriesListState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                RepositoriesListViewModel.State.Empty -> {
                    binding.recyclerView.isVisible = false
                    binding.progressIndicator.hide()
                    binding.placeholderView.show(
                        iconRes = R.drawable.ic_empty,
                        titleText = getString(R.string.repositories_empty_title),
                        titleColorRes = R.color.blue,
                        messageText = getString(R.string.repositories_empty_message)
                    )
                }

                RepositoriesListViewModel.State.Loading -> {
                    binding.recyclerView.isVisible = false
                    binding.progressIndicator.show()
                    binding.placeholderView.hide()
                }

                is RepositoriesListViewModel.State.Loaded -> {
                    repoAdapter.submitList(state.repositories)
                    binding.recyclerView.isVisible = true
                    binding.progressIndicator.hide()
                    binding.placeholderView.hide()
                }

                is RepositoriesListViewModel.State.Error -> {
                    binding.recyclerView.isVisible = false
                    binding.progressIndicator.hide()

                    when (state.error) {
                        is AppError.Http -> {
                            binding.placeholderView.show(
                                iconRes = R.drawable.ic_error,
                                titleText = state.error.code.toString(),
                                titleColorRes = R.color.error,
                                messageText = state.error.message.toString()
                            )
                        }

                        is AppError.Network -> {
                            binding.placeholderView.show(
                                iconRes = R.drawable.ic_not_connected,
                                titleText = getString(R.string.repositories_connection_error_title),
                                titleColorRes = R.color.error,
                                messageText = getString(R.string.repositories_connection_error_message)
                            )
                        }

                        else -> {
                            Unit
                        }
                    }
                }
            }
        }
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is RepositoriesListViewModel.Action.RouteToDetail -> {
                            navigateToDetails(
                                owner = action.owner,
                                repoName = action.repoName,
                                branch = action.branch
                            )
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

    private fun navigateToDetails(owner: String, repoName: String, branch: String) {
        val action = RepositoriesListFragmentDirections
            .actionRepositoriesListFragmentToDetailInfoFragment(
                owner = owner,
                repoName = repoName,
                branch = branch
            )

        findNavController().navigate(action)
    }
}
