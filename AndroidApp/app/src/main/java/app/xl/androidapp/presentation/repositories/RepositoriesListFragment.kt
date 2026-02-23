package app.xl.androidapp.presentation.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.xl.androidapp.databinding.FragmentRepositoriesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
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
        bindRepositoriesListState()
    }

    private fun bindRepositoriesListState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                RepositoriesListViewModel.State.Empty -> {
                    // TODO: - show empty placeholder
                }

                RepositoriesListViewModel.State.Loading -> {
                    // TODO: - show loader
                }

                is RepositoriesListViewModel.State.Loaded -> {
                    // TODO: - show content
                }

                is RepositoriesListViewModel.State.Error -> {
                    // TODO: - show error placeholder
                }
            }
        }
    }
}