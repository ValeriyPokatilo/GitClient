package app.xl.androidapp.presentation.auth

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.xl.androidapp.R
import app.xl.androidapp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        bindState()
        bindActions()
        bindSignInButton()
        bindInputs()
    }

    private fun bindState() {
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
                    binding.signInButton.isEnabled = false
                    binding.tokenInputLayout.error = getString(R.string.invalid_token)
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
                             navigateToMain()
                        }
                        is AuthViewModel.Action.ShowError -> {
                            showErrorDialog(code = action.code, message = action.message)
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

    private fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
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
        }
    }

    private fun showErrorDialog(code: Int?, message: String?) {
        val errorMessageText = if (!message.isNullOrBlank()) {
            message
        } else {
            getString(R.string.check_connection)
        }

        val fullMessage = buildString {
            append(errorMessageText)
            code?.let { append(" / $it") }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setMessage(fullMessage)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    private fun navigateToMain() {
        findNavController().navigate(
            R.id.action_authFragment_to_repositoriesListFragment
        )
    }
}