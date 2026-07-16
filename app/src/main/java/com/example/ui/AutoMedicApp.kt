package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DiagnosticEngine
import com.example.data.database.DiagnosisEntity
import com.example.data.database.MaintenanceEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoMedicApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Force Arabic RTL direction for authentic automotive Arabic app styling
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "AutoMedic AI",
                            fontWeight = FontWeight.Bold,
                            color = CarPrimary
                        )
                    },
                    navigationIcon = {
                        if (currentScreen != Screen.Home) {
                            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "رجوع",
                                    tint = CarPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CarSurface,
                        titleContentColor = TextPrimary
                    ),
                    actions = {
                        IconButton(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "الإعدادات",
                                tint = TextSecondary
                            )
                        }
                    }
                )
            },
            containerColor = CarBackground
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        is Screen.Home -> HomeScreen(viewModel)
                        is Screen.CarSelection -> CarSelectionScreen(viewModel)
                        is Screen.SymptomSelection -> SymptomSelectionScreen(viewModel)
                        is Screen.DiagnosisResult -> DiagnosisResultScreen(viewModel, screen.results)
                        is Screen.OBDLookup -> OBDLookupScreen(viewModel)
                        is Screen.MaintenanceRecord -> MaintenanceRecordScreen(viewModel)
                        is Screen.SmartAssistant -> SmartAssistantScreen(viewModel)
                        is Screen.MaintenanceTips -> MaintenanceTipsScreen(viewModel)
                        is Screen.Settings -> SettingsScreen(viewModel)
                        is Screen.DiagnosisHistory -> DiagnosisHistoryScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Image Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CarSurface)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_dashboard_hero_1784201688445),
                    contentDescription = "AutoMedic Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Ambient Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "شخّص عطل سيارتك بذكاء",
                        color = CarPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "قبل الذهاب إلى الميكانيكي وتوفير التكاليف",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large CTA Button - Diagnosis
        Button(
            onClick = {
                viewModel.resetCarSelection()
                viewModel.navigateTo(Screen.CarSelection)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CarPrimary),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "ابدأ التشخيص",
                    tint = TextOnPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "ابدأ التشخيص الذكي",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnPrimary
                    )
                    Text(
                        text = "فحص متكامل بحسب الأعراض",
                        fontSize = 11.sp,
                        color = TextOnPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "الأدوات والخدمات",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Start
        )

        // Grid of options
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HomeOptionCard(
                    modifier = Modifier.weight(1f),
                    title = "رموز أعطال OBD-II",
                    subtitle = "البحث عن أكواد كمبيوتر السيارة",
                    icon = Icons.Default.Search,
                    color = CarSecondary,
                    onClick = { viewModel.navigateTo(Screen.OBDLookup) }
                )
                HomeOptionCard(
                    modifier = Modifier.weight(1f),
                    title = "سجل الصيانة",
                    subtitle = "تتبع مواعيد الزيت والقطع",
                    icon = Icons.Default.DateRange,
                    color = SeverityLow,
                    onClick = { viewModel.navigateTo(Screen.MaintenanceRecord) }
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HomeOptionCard(
                    modifier = Modifier.weight(1f),
                    title = "المساعد الذكي",
                    subtitle = "دردش مع خبير ميكانيكي بالذكاء الاصطناعي",
                    icon = Icons.AutoMirrored.Filled.Message,
                    color = CarPrimary,
                    onClick = { viewModel.navigateTo(Screen.SmartAssistant) }
                )
                HomeOptionCard(
                    modifier = Modifier.weight(1f),
                    title = "نصائح الصيانة",
                    subtitle = "كيف تحافظ على سيارتك",
                    icon = Icons.Default.Info,
                    color = SeverityMedium,
                    onClick = { viewModel.navigateTo(Screen.MaintenanceTips) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History Row Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(Screen.DiagnosisHistory) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CarSurface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CarSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "السجل",
                        tint = CarPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "سجل التقارير والتشخيصات السابقة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "عرض التقارير المحفوظة لسيارتك",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "فتح السجل",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
fun HomeOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CarSurface),
        border = BorderStroke(1.dp, CarSurfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarSelectionScreen(viewModel: MainViewModel) {
    val brand by viewModel.selectedBrand.collectAsState()
    val model by viewModel.selectedModel.collectAsState()
    val year by viewModel.selectedYear.collectAsState()
    val fuelType by viewModel.selectedFuelType.collectAsState()
    val transmission by viewModel.selectedTransmission.collectAsState()
    val capacity by viewModel.engineCapacity.collectAsState()
    val kms by viewModel.mileage.collectAsState()

    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val yearsList = (currentYear downTo 1995).map { it.toString() }
    var yearExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "الخطوة الأولى: تفاصيل السيارة",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Brand dropdown
        ExposedDropdownMenuBox(
            expanded = brandExpanded,
            onExpandedChange = { brandExpanded = !brandExpanded }
        ) {
            OutlinedTextField(
                value = brand,
                onValueChange = {},
                readOnly = true,
                label = { Text("الشركة المصنعة") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = CarPrimary,
                    unfocusedLabelColor = TextSecondary,
                    focusedBorderColor = CarPrimary,
                    unfocusedBorderColor = CarSurfaceVariant
                )
            )
            ExposedDropdownMenu(
                expanded = brandExpanded,
                onDismissRequest = { brandExpanded = false },
                modifier = Modifier.background(CarSurface)
            ) {
                DiagnosticEngine.manufacturers.forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m, color = TextPrimary) },
                        onClick = {
                            viewModel.selectedBrand.value = m
                            viewModel.selectedModel.value = "" // reset model
                            brandExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Model dropdown
        if (brand.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = modelExpanded,
                onExpandedChange = { modelExpanded = !modelExpanded }
            ) {
                OutlinedTextField(
                    value = model,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("الموديل") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = CarPrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedBorderColor = CarPrimary,
                        unfocusedBorderColor = CarSurfaceVariant
                    )
                )
                ExposedDropdownMenu(
                    expanded = modelExpanded,
                    onDismissRequest = { modelExpanded = false },
                    modifier = Modifier.background(CarSurface)
                ) {
                    DiagnosticEngine.getModelsFor(brand).forEach { m ->
                        DropdownMenuItem(
                            text = { Text(m, color = TextPrimary) },
                            onClick = {
                                viewModel.selectedModel.value = m
                                modelExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Year dropdown
        ExposedDropdownMenuBox(
            expanded = yearExpanded,
            onExpandedChange = { yearExpanded = !yearExpanded }
        ) {
            OutlinedTextField(
                value = year,
                onValueChange = {},
                readOnly = true,
                label = { Text("سنة الصنع") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = CarPrimary,
                    unfocusedLabelColor = TextSecondary,
                    focusedBorderColor = CarPrimary,
                    unfocusedBorderColor = CarSurfaceVariant
                )
            )
            ExposedDropdownMenu(
                expanded = yearExpanded,
                onDismissRequest = { yearExpanded = false },
                modifier = Modifier.background(CarSurface)
            ) {
                yearsList.forEach { y ->
                    DropdownMenuItem(
                        text = { Text(y, color = TextPrimary) },
                        onClick = {
                            viewModel.selectedYear.value = y
                            yearExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fuel Type Selector
        Text(
            text = "نوع الوقود",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DiagnosticEngine.fuelTypes.forEach { fuel ->
                val selected = fuelType == fuel
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable { viewModel.selectedFuelType.value = fuel },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) CarPrimary else CarSurface
                    ),
                    border = if (selected) null else BorderStroke(1.dp, CarSurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fuel,
                            color = if (selected) TextOnPrimary else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transmission Selector
        Text(
            text = "ناقل الحركة",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DiagnosticEngine.transmissionTypes.forEach { trans ->
                val selected = transmission == trans
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable { viewModel.selectedTransmission.value = trans },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) CarPrimary else CarSurface
                    ),
                    border = if (selected) null else BorderStroke(1.dp, CarSurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = trans,
                            color = if (selected) TextOnPrimary else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Engine Capacity & Mileage Inputs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = capacity,
                onValueChange = { viewModel.engineCapacity.value = it },
                label = { Text("سعة المحرك (مثلا: 1.6)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = CarPrimary,
                    unfocusedLabelColor = TextSecondary,
                    focusedBorderColor = CarPrimary,
                    unfocusedBorderColor = CarSurfaceVariant
                )
            )
            OutlinedTextField(
                value = kms,
                onValueChange = { viewModel.mileage.value = it },
                label = { Text("عدد الكيلومترات") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = CarPrimary,
                    unfocusedLabelColor = TextSecondary,
                    focusedBorderColor = CarPrimary,
                    unfocusedBorderColor = CarSurfaceVariant
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Navigation CTA
        Button(
            onClick = { viewModel.navigateTo(Screen.SymptomSelection) },
            enabled = brand.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CarPrimary)
        ) {
            Text(
                text = "متابعة لاختيار الأعراض",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextOnPrimary
            )
        }
    }
}

@Composable
fun SymptomSelectionScreen(viewModel: MainViewModel) {
    val selectedSymptoms by viewModel.selectedSymptoms.collectAsState()
    val context = LocalContext.current

    // Categorized symptoms for an extremely professional layout
    val engineNotStarting = listOf("السيارة لا تعمل", "المحرك يدور ولا يشتغل", "تنطفئ بعد السخونة", "تنطفئ أثناء القيادة", "تأخير في التشغيل", "صعوبة تشغيل صباحًا")
    val enginePerformance = listOf("ضعف في العزم", "بطء التسارع", "استهلاك وقود مرتفع", "اهتزاز المحرك", "ارتفاع حرارة المحرك", "انخفاض حرارة المحرك")
    val unusualSmellsAndLeaks = listOf("رائحة وقود", "رائحة احتراق", "رائحة زيت", "تسريب ماء", "تسريب زيت", "تسريب وقود")
    val exhaustAndNoises = listOf("دخان أبيض", "دخان أسود", "دخان أزرق", "أصوات طقطقة", "أصوات صفير", "أصوات احتكاك")
    val warningLights = listOf("لمبة المحرك", "لمبة ABS", "لمبة الوسائد الهوائية", "لمبة الزيت", "لمبة البطارية")
    val generalSms = listOf("المكيف لا يبرد", "المروحة لا تعمل", "ضعف الفرامل", "الدركسيون ثقيل", "الدركسيون يهتز", "صعوبة تغيير السرعات", "ضعف شحن البطارية", "عدم عمل الأنوار", "عدم عمل المساحات", "عدم عمل البوق")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "الخطوة الثانية: حدد الأعراض التي تلاحظها",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "يمكنك اختيار عَرَض واحد أو أكثر لتشخيص المشكلة بدقة",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Scrollable list of categories
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SymptomCategorySection(
                    title = "مشاكل التشغيل والإنطفاء",
                    symptoms = engineNotStarting,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
            item {
                SymptomCategorySection(
                    title = "أداء وعزم المحرك",
                    symptoms = enginePerformance,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
            item {
                SymptomCategorySection(
                    title = "الروائح الغريبة والتسريبات",
                    symptoms = unusualSmellsAndLeaks,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
            item {
                SymptomCategorySection(
                    title = "ألوان العادم والأصوات",
                    symptoms = exhaustAndNoises,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
            item {
                SymptomCategorySection(
                    title = "لمبات التحذير في الطبلون",
                    symptoms = warningLights,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
            item {
                SymptomCategorySection(
                    title = "الفرامل والتكييف والأنظمة الأخرى",
                    symptoms = generalSms,
                    selectedList = selectedSymptoms,
                    onToggle = { viewModel.toggleSymptom(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CTA Panel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.clearSymptoms() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SeverityHigh),
                border = BorderStroke(1.dp, SeverityHigh)
            ) {
                Text("مسح الكل")
            }

            Button(
                onClick = { viewModel.performDiagnosis() },
                enabled = selectedSymptoms.isNotEmpty(),
                modifier = Modifier
                    .weight(2f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CarPrimary)
            ) {
                Text(
                    text = "عرض نتيجة التشخيص (${selectedSymptoms.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOnPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymptomCategorySection(
    title: String,
    symptoms: List<String>,
    selectedList: Set<String>,
    onToggle: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CarSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            symptoms.forEach { symptom ->
                val isSelected = selectedList.contains(symptom)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggle(symptom) },
                    label = { Text(symptom, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CarSurface,
                        labelColor = TextSecondary,
                        selectedContainerColor = CarPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CarPrimary
                    ),
                    border = if (isSelected) {
                        BorderStroke(1.dp, CarPrimary)
                    } else {
                        BorderStroke(1.dp, CarSurfaceVariant)
                    }
                )
            }
        }
    }
}

@Composable
fun DiagnosisResultScreen(viewModel: MainViewModel, results: List<DiagnosticEngine.FaultDiagnosis>) {
    val brand by viewModel.selectedBrand.collectAsState()
    val model by viewModel.selectedModel.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "تقرير تشخيص الأعطال",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CarPrimary
                )
                Text(
                    text = "$brand $model",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CarSecondary.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "عدد الأعطال: ${results.size}",
                    color = CarSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Faults List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(results) { fault ->
                FaultItemCard(fault)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom CTA Panel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CarPrimary),
                border = BorderStroke(1.dp, CarPrimary)
            ) {
                Text("الصفحة الرئيسية")
            }

            Button(
                onClick = { viewModel.navigateTo(Screen.SmartAssistant) },
                modifier = Modifier
                    .weight(1.5f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CarSecondary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Message,
                    contentDescription = "استفسر من المساعد",
                    tint = TextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "استفسر من المساعد الذكي",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
fun FaultItemCard(fault: DiagnosticEngine.FaultDiagnosis) {
    val severityColor = when (fault.severity.lowercase()) {
        "high" -> SeverityHigh
        "medium" -> SeverityMedium
        else -> SeverityLow
    }

    val severityText = when (fault.severity.lowercase()) {
        "high" -> "خطورة عالية"
        "medium" -> "خطورة متوسطة"
        else -> "خطورة منخفضة"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CarSurface),
        border = BorderStroke(1.dp, CarSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fault.faultName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "نسبة الاحتمال: %${fault.probability}",
                        color = CarPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(severityColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = severityText,
                        color = severityColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CarSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // Details list
            FaultDetailRow(label = "شرح المشكلة:", value = fault.explanation)
            FaultDetailRow(label = "سبب الحدوث:", value = fault.reason)
            FaultDetailRow(label = "كيفية الفحص والتحقق:", value = fault.inspection)
            FaultDetailRow(label = "طريقة الإصلاح:", value = fault.repair)

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CarSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // Additional stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("الوقت المتوقع", color = TextSecondary, fontSize = 11.sp)
                    Text(fault.repairTime, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("التكلفة التقريبية", color = TextSecondary, fontSize = 11.sp)
                    Text(fault.cost, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("هل يمكن مواصلة القيادة؟", color = TextSecondary, fontSize = 10.sp)
                    Text(fault.canContinueDriving, color = if (fault.severity == "high") SeverityHigh else TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CarSurfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "قطع الغيار",
                        tint = CarSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "القطع المطلوبة: ${fault.partsToReplace}",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun FaultDetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CarSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = TextPrimary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun OBDLookupScreen(viewModel: MainViewModel) {
    val query by viewModel.obdSearchQuery.collectAsState()
    val results by viewModel.obdSearchResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "البحث عن أكواد OBD-II",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "أدخل كود العطل الظاهر في فحص الكمبيوتر (مثلا: P0300) لمعرفة تفاصيله المباشرة",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.searchOBDCodes(it) },
            placeholder = { Text("ابحث برقم الكود، مثل P0100...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchOBDCodes("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "مسح")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = CarPrimary,
                unfocusedLabelColor = TextSecondary,
                focusedBorderColor = CarPrimary,
                unfocusedBorderColor = CarSurfaceVariant
            )
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (results.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "لا نتائج",
                            tint = TextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لم يتم العثور على الكود المطلوب في الكاش المحلي.",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "يمكنك سؤال المساعد الذكي عن الكود مباشرة للحصول على إجابة عبر الإنترنت!",
                            color = CarSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.navigateTo(Screen.SmartAssistant)
                                if (query.isNotBlank()) {
                                    viewModel.sendChatMessage("أريد شرح تفصيلي عن كود عطل السيارة: $query")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CarSecondary)
                        ) {
                            Text("اسأل المساعد الذكي")
                        }
                    }
                }
            } else {
                items(results) { code ->
                    OBDCodeCard(code)
                }
            }
        }
    }
}

@Composable
fun OBDCodeCard(obd: DiagnosticEngine.OBDCode) {
    val severityColor = when (obd.severity.lowercase()) {
        "high" -> SeverityHigh
        "medium" -> SeverityMedium
        else -> SeverityLow
    }
    val severityText = when (obd.severity.lowercase()) {
        "high" -> "خطورة عالية"
        "medium" -> "خطورة متوسطة"
        else -> "خطورة منخفضة"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CarSurface),
        border = BorderStroke(1.dp, CarSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CarPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = obd.code,
                        color = CarPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(severityColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = severityText,
                        color = severityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = obd.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = obd.explanation,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CarSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Text("الأسباب المحتملة:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CarSecondary)
            Text(obd.causes, fontSize = 12.sp, color = TextPrimary, modifier = Modifier.padding(top = 2.dp, bottom = 8.dp))

            Text("طريقة الإصلاح الموصى بها:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CarSecondary)
            Text(obd.solution, fontSize = 12.sp, color = TextPrimary, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
fun MaintenanceRecordScreen(viewModel: MainViewModel) {
    val records by viewModel.maintenanceRecords.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    val categories = listOf("تغيير الزيت", "تغيير فلتر الزيت", "فلتر الهواء", "فلتر الوقود", "فلتر المقصورة", "البواجي", "البطارية", "الإطارات", "الفرامل", "سير التوقيت", "سائل التبريد", "زيت علبة السرعات")
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var currentKms by remember { mutableStateOf("") }
    var nextKms by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "سجل صيانة السيارة",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CarPrimary
                )
                Text(
                    text = "تتبع فترات صيانة القطع الاستهلاكية",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { showForm = !showForm },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CarPrimary)
            ) {
                Icon(
                    imageVector = if (showForm) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "إضافة صيانة",
                    tint = TextOnPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Slide/Fade Form
        if (showForm) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CarSurface),
                border = BorderStroke(1.dp, CarSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "إضافة إجراء صيانة جديد",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CarPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Category scrollable chips
                    Text("نوع الصيانة:", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CarPrimary,
                                    selectedLabelColor = TextOnPrimary,
                                    containerColor = CarSurfaceVariant,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = currentKms,
                            onValueChange = { currentKms = it },
                            label = { Text("الممشى الحالي (كم)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = CarPrimary,
                                unfocusedBorderColor = CarSurfaceVariant
                            )
                        )
                        OutlinedTextField(
                            value = nextKms,
                            onValueChange = { nextKms = it },
                            label = { Text("الممشى القادم (كم)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = CarPrimary,
                                unfocusedBorderColor = CarSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("ملاحظات إضافية") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CarPrimary,
                            unfocusedBorderColor = CarSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val curr = currentKms.toIntOrNull() ?: 0
                            val next = nextKms.toIntOrNull() ?: 0
                            viewModel.addMaintenanceRecord(selectedCategory, curr, next, notes)
                            // Reset
                            currentKms = ""
                            nextKms = ""
                            notes = ""
                            showForm = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CarPrimary)
                    ) {
                        Text("حفظ وتفعيل التذكير", color = TextOnPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (records.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "السجل فارغ",
                            tint = TextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "لا توجد سجلات صيانة حالية.",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "اضغط على الزر (+) بالأعلى لتدوين أول صيانة لسيارتك.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(records) { record ->
                    MaintenanceRecordCard(record, onDelete = { viewModel.deleteMaintenanceRecord(record) })
                }
            }
        }
    }
}

@Composable
fun MaintenanceRecordCard(record: MaintenanceEntity, onDelete: () -> Unit) {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val formattedDate = sdf.format(Date(record.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CarSurface),
        border = BorderStroke(1.dp, CarSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CarSecondary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = record.category,
                            tint = CarSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = record.category,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف السجل",
                        tint = SeverityHigh.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("الممشى عند التغيير", color = TextSecondary, fontSize = 10.sp)
                    Text("${record.currentMileage} كم", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("الصيانة القادمة", color = TextSecondary, fontSize = 10.sp)
                    Text("${record.nextMileage} كم", color = CarPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("تاريخ الإجراء", color = TextSecondary, fontSize = 10.sp)
                    Text(formattedDate, color = TextPrimary, fontSize = 12.sp)
                }
            }

            if (record.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CarSurfaceVariant.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "ملاحظات: ${record.notes}",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SmartAssistantScreen(viewModel: MainViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isAssistantLoading.collectAsState()
    var userPrompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CarPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = "المساعد الذكي",
                    tint = CarPrimary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "المساعد الميكانيكي الذكي",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "مدعوم بـ Gemini AI للرد الفوري",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Messages area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CarSurfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = CarPrimary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "جاري تحليل المشكلة وصياغة الحل...",
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Input Panel
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                placeholder = { Text("مثلا: ليش ترتفع حرارة التوسان 2018 وهي واقفة؟") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = CarPrimary,
                    unfocusedBorderColor = CarSurfaceVariant
                ),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = {
                    viewModel.sendChatMessage(userPrompt)
                    userPrompt = ""
                },
                enabled = userPrompt.isNotBlank() && !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CarPrimary),
                modifier = Modifier.height(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "إرسال",
                    tint = TextOnPrimary
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isUser) 12.dp else 0.dp,
                bottomEnd = if (isUser) 0.dp else 12.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) CarPrimary else CarSurface
            ),
            border = if (isUser) null else BorderStroke(1.dp, CarSurfaceVariant),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isUser) TextOnPrimary else TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun MaintenanceTipsScreen(viewModel: MainViewModel) {
    val tips = listOf(
        Pair("فحص مستوى زيت المحرك", "يجب قياس زيت المحرك بانتظام كل أسبوعين تقريباً والسيارة متوقفة على سطح مستوٍ ومحركها بارد. نقصه عن علامة Min يضر بالمكونات الداخلية فوراً!"),
        Pair("مراقبة سائل تبريد الرادياتير", "لا تفتح غطاء الرادياتير أبداً والمحرك ساخن لتفادي حروق المياه المغلية. استخدم دائماً مياه تبريد ملونة مخصصة ومطابقة لكتالوج سيارتك لعدم تكون الصدأ."),
        Pair("الحفاظ على ضغط الإطارات", "فحص ضغط الهواء بانتظام يضمن سلامتك، ويطيل عمر الإطارات، ويقلل استهلاك الوقود بنسبة تصل لـ %3. افحصها وهي باردة صباحاً."),
        Pair("فحص سماكة تيل الفرامل", "عند سماع صوت صفير خفيف أو اهتزاز أثناء الضغط على الفرامل، بادر بفحص تيل الفرامل فوراً، فتأخير الاستبدال يسبب تلفاً مكلفاً للأقراص (الهوبات)."),
        Pair("علامات تلف شمعات الإشعال", "إذا لاحظت رجفة أو اهتزاز في المحرك أثناء الوقوف، أو بطء في تسارع السيارة، أو زيادة مفاجئة في استهلاك الوقود، فقد يكون قد حان وقت استبدال البواجي.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "نصائح وإرشادات الصيانة الوقائية",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "إرشادات وقائية بسيطة توفر عليك زيارات الميكانيكي المكلفة",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(tips) { tip ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CarSurface),
                    border = BorderStroke(1.dp, CarSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CarSecondary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "نصيحة",
                                tint = CarSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = tip.first,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tip.second,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "الإعدادات والنظام",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // General Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CarSurface),
            border = BorderStroke(1.dp, CarSurfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_automedic_logo_1784201702250),
                        contentDescription = "AutoMedic Logo",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "AutoMedic AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "الإصدار 1.0 (تجريبي ذكي)",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CarSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "نبذة عن التطبيق:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = CarSecondary
                )
                Text(
                    text = "تطبيق ذكي متكامل مصمم خصيصاً لمساعدة ملاك السيارات على الفحص والتشخيص الفوري للأعطال بذكاء قبل الذهاب إلى ورش الصيانة لسلامة الجميع وتجنب الاحتيال.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History Log option
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(Screen.DiagnosisHistory) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CarSurface),
            border = BorderStroke(1.dp, CarSurfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "تاريخ التشخيصات",
                        tint = CarPrimary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "عرض تشخيصاتي السابقة",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "فتح السجل",
                    tint = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gemini Status Info Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CarSurface),
            border = BorderStroke(1.dp, CarSurfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ربط الذكاء الاصطناعي (Gemini AI)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CarPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "يتم قراءة مفتاح API الخاص بـ Gemini تلقائياً وبأمان من خزنة الأسرار في AI Studio.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SeverityLow)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "الحالة: متصل وجاهز للتشغيل",
                        color = SeverityLow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DiagnosisHistoryScreen(viewModel: MainViewModel) {
    val reports by viewModel.diagnosisReports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "أرشيف التشخيصات السابقة",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CarPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "تاريخ التقارير المحفوظة تلقائياً عند التشخيص",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (reports.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "السجل فارغ",
                            tint = TextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "لا توجد تقارير تشخيصية محفوظة بعد.",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "ابدأ عملية تشخيص جديدة لحفظ التقارير تلقائياً هنا.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                items(reports) { report ->
                    DiagnosisHistoryCard(report)
                }
            }
        }
    }
}

@Composable
fun DiagnosisHistoryCard(report: DiagnosisEntity) {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    val formattedDate = sdf.format(Date(report.date))

    val severityColor = when (report.severity.lowercase()) {
        "high" -> SeverityHigh
        "medium" -> SeverityMedium
        else -> SeverityLow
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CarSurface),
        border = BorderStroke(1.dp, CarSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "${report.carBrand} ${report.carModel} (${report.carYear})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "تاريخ الحفظ: $formattedDate",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(severityColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when (report.severity.lowercase()) {
                            "high" -> "خطورة عالية"
                            "medium" -> "خطورة متوسطة"
                            else -> "خطورة منخفضة"
                        },
                        color = severityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = CarSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "العطل المحتمل الأساسي:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CarPrimary
            )
            Text(
                text = "${report.faultName} (بنسبة %${report.probability})",
                fontSize = 13.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "الأعراض المشكو منها:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CarSecondary
            )
            Text(
                text = report.symptoms,
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("التكلفة التقريبية", color = TextSecondary, fontSize = 10.sp)
                    Text(report.cost, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("وقت الإصلاح المتوقع", color = TextSecondary, fontSize = 10.sp)
                    Text(report.estimatedTime, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
