package app.xl.androidapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentDetailInfoBinding
import app.xl.androidapp.presentation.utils.MarkwonFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private var _binding: FragmentDetailInfoBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoryInfoViewModel by viewModels()

    private val args: DetailInfoFragmentArgs by navArgs()
    private val repositoryName: String
        get() = args.repositoryName

    private val markwon by lazy {
        MarkwonFactory.createMarkwon(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToViewModel() {
        bindActions()
        bindState()
        bindReadmeState()
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        RepositoryInfoViewModel.Action.Logout -> {
                            navigateToAuth()
                        }
                        RepositoryInfoViewModel.Action.RouteBack -> {
                            navigateToList()
                        }
                    }
                }
            }
        }
    }

    private fun bindState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                RepositoryInfoViewModel.State.Loading -> {
                    binding.progressIndicator.show()
                }
                is RepositoryInfoViewModel.State.Loaded -> {
                    binding.progressIndicator.hide()
                }
                is RepositoryInfoViewModel.State.Error -> {
                    binding.progressIndicator.hide()
                }
            }
        }
    }

    private fun bindReadmeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state is RepositoryInfoViewModel.State.Loaded) {
                val readmeTextView = binding.readmeTextView
                when (val readmeState = state.readmeState) {
                    is RepositoryInfoViewModel.ReadmeState.Loaded -> {
                        readmeState.markdown?.let {
                            markwon.setMarkdown(readmeTextView, readmeState.markdown)
                        }
                    }

                    RepositoryInfoViewModel.ReadmeState.Empty -> {
                        readmeTextView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white_70)
                        )
                        readmeTextView.setText(R.string.no_readme_md)
                    }

                    is RepositoryInfoViewModel.ReadmeState.Error -> {
                        // TODO: - set error
                    }

                    RepositoryInfoViewModel.ReadmeState.Loading -> {
                        // TODO: - show loader
                    }
                }
            }
        }
    }

    private fun setupNavigationBar() {
        binding.toolbar.title = repositoryName

        binding.toolbar.setNavigationIcon(
            R.drawable.ic_back
        )

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    viewModel.onLogoutPressed()
                    true
                } else -> false
            }
        }
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            R.id.action_global_authFragment
        )
    }

    private fun navigateToList() {
        findNavController().popBackStack()
    }
}