package app.xl.androidapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _token = MutableLiveData<String>("")
    val token: LiveData<String> = _token

    private val _state = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> = _actions

    fun onTokenChanged(text: String) {
        _token.value = text
    }

    fun onSignInButtonPressed() {
        val tokenValue = _token.value.orEmpty()

        if (tokenValue.isBlank()) {
            viewModelScope.launch {
                _actions.emit(Action.FocusOnTokenField)
            }
            return
        }

        // TODO: Sign In request
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
        object FocusOnTokenField : Action
    }
}