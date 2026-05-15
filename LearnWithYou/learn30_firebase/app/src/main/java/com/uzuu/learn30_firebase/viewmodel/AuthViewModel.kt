package com.uzuu.learn30_firebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.uzuu.learn30_firebase.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun login(email: String, password: String) {
        repo.login(email, password) { user, error ->
            if (user != null) {
                _user.postValue(user)
            } else {
                _error.postValue(error)
            }
        }
    }

    fun register(email: String, password: String) {
        repo.register(email, password) { user, error ->
            if (user != null) {
                _user.postValue(user)
            } else {
                _error.postValue(error)
            }
        }
    }

    fun checkLogin() {
        _user.value = repo.getCurrentUser()
    }
}