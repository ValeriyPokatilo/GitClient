package app.xl.androidapp.presentation.details

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentDetailInfoBinding
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.RepositoryDetails
import app.xl.androidapp.presentation.models.PlaceholderModel
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
                    binding.detailsProgressIndicator.show()
                    binding.scrollView.isVisible = false
                    binding.placeholderView.hide()
                }
                is RepositoryInfoViewModel.State.Loaded -> {
                    binding.detailsProgressIndicator.hide()
                    binding.scrollView.isVisible = true
                    binding.placeholderView.hide()
                    setupDetails(state.githubRepo)
                }
                is RepositoryInfoViewModel.State.Error -> {
                    binding.detailsProgressIndicator.hide()
                    binding.scrollView.isVisible = false
                    showError(state.error)
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
                        binding.readmeProgressIndicator.hide()
                        readmeState.markdown?.let {
                            markwon.setMarkdown(readmeTextView, readmeState.markdown)
                        }
                    }

                    RepositoryInfoViewModel.ReadmeState.Empty -> {
                        binding.readmeProgressIndicator.hide()
                        readmeTextView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white_70)
                        )
                        readmeTextView.setText(R.string.no_readme_md)
                    }

                    is RepositoryInfoViewModel.ReadmeState.Error -> {
                        binding.readmeProgressIndicator.hide()
                        showError(readmeState.error)
                    }

                    RepositoryInfoViewModel.ReadmeState.Loading -> {
                        binding.readmeProgressIndicator.show()
                    }
                }
            }
        }
    }

    private fun setupDetails(details: RepositoryDetails) {
        val displayUrl = details.url.removePrefix("https://").removePrefix("http://")
        binding.linkTextView.text = displayUrl
        binding.linkTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.linkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, details.url.toUri())
            it.context.startActivity(intent)
        }

        details.license?.let {
            binding.licenselink.text = details.license.name
            binding.licenselink.movementMethod = LinkMovementMethod.getInstance()
            details.license.url?.let {
                binding.licenselink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, details.license.url.toUri())
                    it.context.startActivity(intent)
                }
            }
        }

        binding.starsCounter.text = details.stargazersCount.toString()
        binding.forksCounter.text = details.forksCount.toString()
        binding.watchersCounter.text = details.subscribersCount.toString()
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

    private fun showError(error: AppError) {
        when (error) {
            is AppError.Http -> {
                binding.placeholderView.show(
                    model = PlaceholderModel(
                        iconRes = R.drawable.ic_error,
                        title = error.code.toString(),
                        titleColorRes = R.color.error,
                        message = error.message.toString(),
                        buttonTitle = getString(R.string.retry),
                        buttonAction = {
                            viewModel.onRetryButtonPressed()
                        }
                    )
                )
            }

            is AppError.Network -> {
                binding.placeholderView.show(
                    model = PlaceholderModel(
                        iconRes = R.drawable.ic_not_connected,
                        title = getString(R.string.repositories_connection_error_title),
                        titleColorRes = R.color.error,
                        message = getString(R.string.repositories_connection_error_message),
                        buttonTitle = getString(R.string.retry),
                        buttonAction = {
                            viewModel.onRetryButtonPressed()
                        }
                    )
                )
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