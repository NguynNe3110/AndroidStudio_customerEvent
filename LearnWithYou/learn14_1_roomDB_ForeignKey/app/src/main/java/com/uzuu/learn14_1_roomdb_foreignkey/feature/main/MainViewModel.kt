package com.uzuu.learn14_1_roomdb_foreignkey.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.learn14_1_roomdb_foreignkey.data.repository.DbRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: DbRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MainUiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeUsersWithClass()
        observeClasses()
    }

    // ---------- Observe ----------
    private fun observeUsersWithClass() {
        viewModelScope.launch {
            repo.observeUsersWithClass().collect { list ->
                val text = if (list.isEmpty()) {
                    "No students!"
                } else {
                    list.joinToString("\n") { u ->
                        "SV(id=${u.id}, name=${u.nameStudent})  |  Class(id=${u.idClass}, name=${u.className})"
                    }
                }
                _uiState.update { it.copy(textInfUser = text) }
            }
        }
    }

    private fun observeClasses() {
        viewModelScope.launch {
            repo.observeClasses().collect { list ->
                val text = if (list.isEmpty()) {
                    "No classes!"
                } else {
                    list.joinToString("\n") { c ->
                        "Class(id=${c.id}) - ${c.name}"
                    }
                }
                _uiState.update { it.copy(textClass = text) }
            }
        }
    }

    // ---------- Actions from UI ----------
    fun onAddClass(idText: String, name: String) {
        val id = idText.toIntOrNull()
        if (id == null) return toastAndStatus("Class id không hợp lệ")
        if (name.isBlank()) return toastAndStatus("Class name rỗng")

        viewModelScope.launch {
            runCatching {
                repo.addClass(id, name)
            }.onSuccess {
                toastAndStatus("Add class OK (id=$id)")
            }.onFailure { e ->
                toastAndStatus("Add class FAIL: ${e.message}")
            }
        }
    }

    fun onUpdateClass(idText: String, name: String) {
        val id = idText.toIntOrNull()
        if (id == null) return toastAndStatus("Class id không hợp lệ")
        if (name.isBlank()) return toastAndStatus("Class name rỗng")

        viewModelScope.launch {
            runCatching {
                repo.updClass(id, name)
            }.onSuccess { rows ->
                toastAndStatus("Update class rows=$rows (id=$id)")
            }.onFailure { e ->
                toastAndStatus("Update class FAIL: ${e.message}")
            }
        }
    }

    fun onDeleteClass(idText: String) {
        val id = idText.toIntOrNull()
        if (id == null) return toastAndStatus("Class id không hợp lệ")

        viewModelScope.launch {
            runCatching {
                repo.deleteClassById(id)
            }.onSuccess { rows ->
                toastAndStatus("Delete class rows=$rows (id=$id)")
            }.onFailure { e ->
                toastAndStatus("Delete class FAIL: ${e.message}")
            }
        }
    }

    fun onAddStudent(idStudentText: String, idClassText: String, name: String) {
        val idStudent = idStudentText.toIntOrNull()
        val idClass = idClassText.toIntOrNull()
        if (idStudent == null) return toastAndStatus("Student id không hợp lệ")
        if (idClass == null) return toastAndStatus("Class id (FK) không hợp lệ")
        if (name.isBlank()) return toastAndStatus("Student name rỗng")

        viewModelScope.launch {
            runCatching {
                repo.addUser(idStudent, idClass, name)
            }.onSuccess {
                // nếu idClass không tồn tại -> FAIL do FK (đúng mục tiêu bạn học)
                toastAndStatus("Add student OK (id=$idStudent, classId=$idClass)")
            }.onFailure { e ->
                toastAndStatus("Add student FAIL: ${e.message}")
            }
        }
    }

    fun onUpdateStudent(idStudentText: String, idClassText: String, name: String) {
        val idStudent = idStudentText.toIntOrNull()
        val idClass = idClassText.toIntOrNull()
        if (idStudent == null) return toastAndStatus("Student id không hợp lệ")
        if (idClass == null) return toastAndStatus("Class id (FK) không hợp lệ")
        if (name.isBlank()) return toastAndStatus("Student name rỗng")

        viewModelScope.launch {
            runCatching {
                repo.updUser(idStudent, idClass, name)
            }.onSuccess { rows ->
                toastAndStatus("Update student rows=$rows (id=$idStudent)")
            }.onFailure { e ->
                toastAndStatus("Update student FAIL: ${e.message}")
            }
        }
    }

    fun onDeleteStudent(idStudentText: String) {
        val idStudent = idStudentText.toIntOrNull()
        if (idStudent == null) return toastAndStatus("Student id không hợp lệ")

        viewModelScope.launch {
            runCatching {
                repo.deleteUserById(idStudent)
            }.onSuccess { rows ->
                toastAndStatus("Delete student rows=$rows (id=$idStudent)")
            }.onFailure { e ->
                toastAndStatus("Delete student FAIL: ${e.message}")
            }
        }
    }

    fun onClearAll() {
        viewModelScope.launch {
            runCatching { repo.clearAll() }
                .onSuccess { toastAndStatus("Clear all OK") }
                .onFailure { e -> toastAndStatus("Clear all FAIL: ${e.message}") }
        }
    }

    private fun toastAndStatus(message: String) {
        _uiState.update { it.copy(error = message) }
        _uiEvent.tryEmit(MainUiEvent.Toast(message))
    }
}
