package app.xl.androidapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepositoryInterface
) : ViewModel() {

    private val _token = MutableLiveData<String>("")
    val token: LiveData<String> = _token

    private val _state = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> = _state

    private val _actions = MutableSharedFlow<Action>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val actions: Flow<Action> = _actions

    private val githubTokenRegex = Regex("^[A-Za-z0-9_-]*$")

    fun onTokenChanged(text: String) {
        _token.value = text
        _state.value = when {
            text.isEmpty() -> State.Idle
            !githubTokenRegex.matches(text) -> State.InvalidInput
            else -> State.Idle
        }
    }

    fun onSignInButtonPressed() {
        val tokenValue = _token.value.orEmpty()

        if (tokenValue.isBlank()) {
            viewModelScope.launch {
                _actions.emit(Action.FocusOnTokenField)
            }
            return
        }

        viewModelScope.launch {
            _state.value = State.Loading
            try {
                repository.signIn(tokenValue)
                _state.value = State.Idle
                _actions.emit(Action.RouteToMain)
            } catch (error: AppError) {
                _state.value = State.Idle
                handleError(error)
            }
        }
    }

    private suspend fun handleError(error: AppError) {
        when (error) {
            is AppError.Http -> {
                _actions.emit(
                    Action.ShowError(
                        code = error.code,
                        message = error.errorMessage ?: ""
                    )
                )
            }

            is AppError.Network -> {
                _actions.emit(
                    Action.ShowError(
                        code = null,
                        message = null
                    )
                )
            }

            else -> {
                Unit
            }
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        object InvalidInput : State
    }

    sealed interface Action {
        data class ShowError(val code: Int?, val message: String?) : Action
        object RouteToMain : Action
        object FocusOnTokenField : Action
    }
}