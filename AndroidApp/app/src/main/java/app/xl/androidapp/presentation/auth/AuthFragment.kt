package app.xl.androidapp.presentation.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentAuthBinding
import kotlinx.coroutines.launch
import kotlin.getValue
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.internal.ViewUtils.showKeyboard

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
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
        bindAuthState()
        bindActions()
        bindSignInButton()
        bindInputs()
    }

    private fun bindAuthState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthViewModel.State.Idle -> {
                    binding.signInButton.isEnabled = true
                    binding.signInButton.setTextColor(Color.WHITE)
                    binding.progressIndicator.hide()
                }

                AuthViewModel.State.Loading -> {
                    binding.signInButton.isEnabled = false
                    binding.signInButton.setTextColor(Color.TRANSPARENT)
                    binding.progressIndicator.show()
                }

                is AuthViewModel.State.InvalidInput -> {
                    binding.tokenInputLayout.error = state.reason
                    binding.signInButton.setTextColor(Color.WHITE)
                    binding.signInButton.isEnabled = true
                    binding.progressIndicator.hide()
                }
            }
        }
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is AuthViewModel.Action.RouteToMain -> {
                             findNavController().navigate(
                                 R.id.action_authFragment_to_repositoriesListFragment
                             )
                        }
                        is AuthViewModel.Action.ShowError -> {
                            val message = action.message
                            binding.tokenInputLayout.error = message
                        }
                        is AuthViewModel.Action.FocusOnTokenField -> {
                            binding.tokenInputEdit.requestFocus()
                            showKeyboard(binding.tokenInputEdit)
                        }
                    }
                }
            }
        }
    }
    private fun bindSignInButton() {
        binding.signInButton.setOnClickListener {
            viewModel.onSignInButtonPressed()
        }
    }

    private fun bindInputs() {
        binding.tokenInputEdit.doAfterTextChanged { text ->
            viewModel.onTokenChanged(text?.toString().orEmpty())
            binding.tokenInputLayout.error = null
        }
    }
}