package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DiagnosticEngine
import com.example.data.api.GeminiApiRepository
import com.example.data.database.AppDatabase
import com.example.data.database.DiagnosisEntity
import com.example.data.database.MaintenanceEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object CarSelection : Screen()
    object SymptomSelection : Screen()
    data class DiagnosisResult(val results: List<DiagnosticEngine.FaultDiagnosis>) : Screen()
    object OBDLookup : Screen()
    object MaintenanceRecord : Screen()
    object SmartAssistant : Screen()
    object MaintenanceTips : Screen()
    object Settings : Screen()
    object DiagnosisHistory : Screen()
}

data class ChatMessage(
    val sender: String, // "user" or "assistant"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val dao = database.autoMedicDao()

    // Database flows
    val maintenanceRecords: StateFlow<List<MaintenanceEntity>> = dao.getAllMaintenanceRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val diagnosisReports: StateFlow<List<DiagnosisEntity>> = dao.getAllDiagnosisReports()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Car Selection State
    val selectedBrand = MutableStateFlow("")
    val selectedModel = MutableStateFlow("")
    val selectedYear = MutableStateFlow("")
    val selectedFuelType = MutableStateFlow("بنزين")
    val selectedTransmission = MutableStateFlow("أوتوماتيك")
    val engineCapacity = MutableStateFlow("")
    val mileage = MutableStateFlow("")

    // Symptom Selection State
    val selectedSymptoms = MutableStateFlow<Set<String>>(emptySet())

    // OBD Search State
    val obdSearchQuery = MutableStateFlow("")
    private val _obdSearchResults = MutableStateFlow<List<DiagnosticEngine.OBDCode>>(DiagnosticEngine.obdCodes)
    val obdSearchResults: StateFlow<List<DiagnosticEngine.OBDCode>> = _obdSearchResults.asStateFlow()

    // Smart Assistant State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "assistant",
                text = "مرحباً بك في المساعد الذكي لـ AutoMedic AI! 👋\nأنا هنا لمساعدتك في تشخيص وفحص سيارتك. يمكنك كتابة أي سؤال أو وصف أعراض عطل سيارتك وسأقوم بتحليله وتبسيطه لك."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val isAssistantLoading = MutableStateFlow(false)

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleSymptom(symptom: String) {
        val current = selectedSymptoms.value.toMutableSet()
        if (current.contains(symptom)) {
            current.remove(symptom)
        } else {
            current.add(symptom)
        }
        selectedSymptoms.value = current
    }

    fun clearSymptoms() {
        selectedSymptoms.value = emptySet()
    }

    fun searchOBDCodes(query: String) {
        obdSearchQuery.value = query
        if (query.isBlank()) {
            _obdSearchResults.value = DiagnosticEngine.obdCodes
        } else {
            _obdSearchResults.value = DiagnosticEngine.obdCodes.filter {
                it.code.contains(query, ignoreCase = true) ||
                        it.title.contains(query, ignoreCase = true) ||
                        it.explanation.contains(query, ignoreCase = true)
            }
        }
    }

    fun performDiagnosis() {
        val symptoms = selectedSymptoms.value.toList()
        val brand = selectedBrand.value.ifBlank { "سيارة" }
        val results = DiagnosticEngine.diagnose(symptoms, brand)

        // Save first result to DB as history if available
        if (results.isNotEmpty()) {
            val primary = results.first()
            viewModelScope.launch {
                dao.insertDiagnosisReport(
                    DiagnosisEntity(
                        carBrand = brand,
                        carModel = selectedModel.value,
                        carYear = selectedYear.value,
                        fuelType = selectedFuelType.value,
                        transmission = selectedTransmission.value,
                        symptoms = symptoms.joinToString(", "),
                        date = System.currentTimeMillis(),
                        faultName = primary.faultName,
                        probability = primary.probability,
                        explanation = primary.explanation,
                        inspectionMethod = primary.inspection,
                        repairMethod = primary.repair,
                        estimatedTime = primary.repairTime,
                        cost = primary.cost,
                        severity = primary.severity
                    )
                )
            }
        }

        navigateTo(Screen.DiagnosisResult(results))
    }

    // Maintenance Record CRUD
    fun addMaintenanceRecord(category: String, mileage: Int, nextMileage: Int, notes: String) {
        viewModelScope.launch {
            dao.insertMaintenanceRecord(
                MaintenanceEntity(
                    category = category,
                    date = System.currentTimeMillis(),
                    currentMileage = mileage,
                    nextMileage = nextMileage,
                    notes = notes
                )
            )
        }
    }

    fun deleteMaintenanceRecord(record: MaintenanceEntity) {
        viewModelScope.launch {
            dao.deleteMaintenanceRecord(record.id)
        }
    }

    // Smart Assistant chat message
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return

        // Add user message
        val updated = _chatMessages.value.toMutableList()
        updated.add(ChatMessage(sender = "user", text = text))
        _chatMessages.value = updated

        isAssistantLoading.value = true

        viewModelScope.launch {
            val systemPrompt = """
                أنت مهندس ميكانيك وخبير سيارات ذكي في تطبيق AutoMedic AI.
                مهمتك هي الإجابة عن أسئلة المستخدم حول أعطال السيارات، تحليل الأعراض المقترحة، اقتراح الأعطال المحتملة، شرح خطوات الفحص البسيطة للمبتدئين، وتبسيط المصطلحات الفنية المعقدة باللغة العربية بأسلوب ودود ومفهوم جداً.
                تأكد من تنبيه المستخدم دائماً في حالات الأعطال الخطيرة (مثل ارتفاع حرارة المحرك أو انخفاض ضغط الزيت) بضرورة إطفاء المحرك وعدم القيادة لسلامته.
            """.trimIndent()

            val assistantResponse = GeminiApiRepository.generateContent(text, systemPrompt)
            
            val finalMessages = _chatMessages.value.toMutableList()
            finalMessages.add(ChatMessage(sender = "assistant", text = assistantResponse))
            _chatMessages.value = finalMessages
            isAssistantLoading.value = false
        }
    }

    fun resetCarSelection() {
        selectedBrand.value = ""
        selectedModel.value = ""
        selectedYear.value = ""
        selectedFuelType.value = "بنزين"
        selectedTransmission.value = "أوتوماتيك"
        engineCapacity.value = ""
        mileage.value = ""
        clearSymptoms()
    }
}
