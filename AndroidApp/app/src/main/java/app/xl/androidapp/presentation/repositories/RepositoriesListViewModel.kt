package app.xl.androidapp.presentation.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.xl.androidapp.data.repository.toEntity
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.Repo
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoriesListViewModel@Inject constructor(
    private val repository: AppRepositoryInterface
) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> = _actions

    init {
        loadRepositories()
    }

    fun onLogoutButtonPressed() {
        viewModelScope.launch {
            repository.logout()
            _actions.emit(Action.Logout)
        }
    }

    private fun loadRepositories() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val repositories = repository.getRepositories()

                _state.value = if (repositories.isEmpty()) {
                    State.Empty
                } else {
                    State.Loaded(repositories.map { it.toEntity() })
                }
            } catch (error: AppError) {
                _state.value = State.Error(error.message.toString())
                handleError(error)
            }
        }
    }

    private fun handleError(error: AppError) {
        // TODO: - fill error placeholder
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repositories: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    sealed interface Action {
        object Logout : Action
        object RouteToDetail : Action
    }
}