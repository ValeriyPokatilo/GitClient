package app.xl.androidapp.presentation.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.Repo
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepositoryInterface
) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    private val _actions = MutableSharedFlow<Action>(
        replay = 0,
        extraBufferCapacity = 1
    )
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

    fun onRepositoryItemPressed(repository: Repo) {
        viewModelScope.launch {
            _actions.emit(
                Action.RouteToDetail(
                    owner = repository.owner.login,
                    repositoryName = repository.name,
                    branch = repository.defaultBranch
                )
            )
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
                    State.Loaded(repositories.map { it })
                }
            } catch (error: AppError) {
                _state.value = State.Error(error)
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repositories: List<Repo>) : State
        data class Error(val error: AppError) : State
        object Empty : State
    }

    sealed interface Action {
        object Logout : Action
        data class RouteToDetail(
            val owner: String,
            val repositoryName: String,
            val branch: String
        ) : Action
    }
}