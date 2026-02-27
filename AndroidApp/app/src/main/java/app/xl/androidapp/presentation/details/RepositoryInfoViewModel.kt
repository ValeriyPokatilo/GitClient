package app.xl.androidapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.RepoDetails
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepositoryInterface,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val owner: String = checkNotNull(savedStateHandle["owner"])
    private val repositoryName: String = checkNotNull(savedStateHandle["repositoryName"])
    private val branch: String = checkNotNull(savedStateHandle["branch"])

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    private val _actions = MutableSharedFlow<Action>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val actions: Flow<Action> = _actions

    init {
        loadRepositoryInfo()
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            _actions.emit(Action.RouteBack)
        }
    }

    fun onLogoutPressed() {
        viewModelScope.launch {
            repository.logout()
            _actions.emit(Action.Logout)
        }
    }

    fun onRetryButtonPressed() {
        loadRepositoryInfo()
    }

    private fun loadRepositoryInfo() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val details = repository.getRepository(
                    ownerName = owner,
                    repositoryName = repositoryName
                )

                _state.value = State.Loaded(
                    githubRepo = details,
                    readmeState = ReadmeState.Loading
                )

                loadReadme(details)
            } catch (error: AppError) {
                _state.value = State.Error(error)
            }
        }
    }

    private suspend fun loadReadme(details: RepoDetails) {
        try {
            val readme = repository.getRepositoryReadme(
                ownerName = owner,
                repositoryName = repositoryName,
                branchName = branch
            )

            val readmeState = if (readme == null) {
                ReadmeState.Empty
            } else {
                ReadmeState.Loaded(readme)
            }

            _state.value = State.Loaded(
                githubRepo = details,
                readmeState = readmeState
            )
        } catch (error: AppError) {
            _state.value = State.Loaded(
                githubRepo = details,
                readmeState =  ReadmeState.Error(error)
            )
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: AppError) : State

        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: AppError) : ReadmeState
        data class Loaded(val markdown: String?) : ReadmeState
    }

    sealed interface Action {
        object Logout : Action
        object RouteBack : Action
    }
}