package app.xl.androidapp.presentation.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.xl.androidapp.data.storage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _destination = Channel<Destination>(Channel.BUFFERED)
    val destination = _destination.receiveAsFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val target = if (tokenManager.getToken() != null) {
                Destination.Repositories
            } else {
                Destination.Auth
            }
            _destination.send(target)
        }
    }

    sealed class Destination {
        object Auth : Destination()
        object Repositories : Destination()
    }
}