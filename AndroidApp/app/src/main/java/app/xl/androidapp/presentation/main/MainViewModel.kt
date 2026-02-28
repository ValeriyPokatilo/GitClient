package app.xl.androidapp.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.xl.androidapp.R
import app.xl.androidapp.data.storage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    val startDestination = MutableLiveData<Int>()

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        val destination = if (tokenManager.getToken().isNullOrEmpty()) {
            R.id.authFragment
        } else {
            R.id.repositoriesListFragment
        }
        startDestination.value = destination
    }
}