package app.xl.androidapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.xl.androidapp.domain.entity.Repo
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepositoryInterface
) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    private val _readmeState = MutableLiveData<ReadmeState>(ReadmeState.Loading)
    val readmeState: LiveData<ReadmeState> = _readmeState

    private val _actions = MutableSharedFlow<Action>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val actions: Flow<Action> = _actions

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    sealed interface Action {
        object Logout : Action
        object RouteBack : Action
    }
}