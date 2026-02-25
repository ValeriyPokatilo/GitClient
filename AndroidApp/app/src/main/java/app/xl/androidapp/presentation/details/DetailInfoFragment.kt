package app.xl.androidapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import app.xl.androidapp.databinding.FragmentDetailInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private var _binding: FragmentDetailInfoBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoryInfoViewModel by viewModels()

    private val args: DetailInfoFragmentArgs by navArgs()

    private val repositoryFullName: String
        get() = args.repositoryFullName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToViewModel() {
        bindState()
        bindReadmeState()
        bindActions()
    }

    private fun bindState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RepositoryInfoViewModel.State.Error -> {}
                is RepositoryInfoViewModel.State.Loaded -> {}
                RepositoryInfoViewModel.State.Loading -> {}
            }
        }
    }

    private fun bindReadmeState() {
        viewModel.readmeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RepositoryInfoViewModel.ReadmeState.Empty -> {}
                is RepositoryInfoViewModel.ReadmeState.Error -> {}
                is RepositoryInfoViewModel.ReadmeState.Loaded -> {}
                RepositoryInfoViewModel.ReadmeState.Loading -> {}
            }
        }
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        RepositoryInfoViewModel.Action.Logout -> {}
                        RepositoryInfoViewModel.Action.RouteBack -> {}
                    }
                }
            }
        }
    }
}