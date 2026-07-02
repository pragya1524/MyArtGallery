package com.example.polytone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.polytone.data.model.ChatMessage
import com.example.polytone.data.model.DummyData
import com.example.polytone.data.model.UserPreferences
import com.example.polytone.data.repository.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val preferences: UserPreferences,
    val messages: List<ChatMessage>,
    val input: String,
    val isSending: Boolean,
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: TranslationRepository
) : ViewModel() {
    private val input = MutableStateFlow("")
    private val isSending = MutableStateFlow(false)

    val uiState = combine(
        repository.observePreferences(),
        repository.observeMessages(),
        input,
        isSending,
    ) { preferences, messages, currentInput, sending ->
        ChatUiState(
            preferences = preferences,
            messages = messages,
            input = currentInput,
            isSending = sending,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChatUiState(
            preferences = DummyData.defaultPreferences(),
            messages = DummyData.initialMessages(),
            input = "",
            isSending = false,
        ),
    )

    fun updateInput(value: String) {
        input.value = value
    }

    fun sendMessage() {
        val text = input.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            input.value = ""
            isSending.value = true
            repository.sendMessage(text)
            isSending.value = false
        }
    }

    fun retryMessage(messageId: String) {
        viewModelScope.launch {
            isSending.value = true
            repository.retryMessage(messageId)
            isSending.value = false
        }
    }

    fun swapLanguages() {
        viewModelScope.launch { repository.swapLanguages() }
    }
}
