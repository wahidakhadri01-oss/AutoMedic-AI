package com.example.data

object DiagnosticEngine {

    val manufacturers = listOf(
        "Peugeot", "Renault", "Citroën", "Volkswagen", "Audi", "BMW", "Mercedes-Benz",
        "Opel", "Ford", "Fiat", "Seat", "Skoda", "Toyota", "Honda", "Hyundai", "Kia",
        "Nissan", "Mazda", "Mitsubishi", "Suzuki", "Dacia", "Chevrolet", "Jeep", "Volvo",
        "Porsche", "Lexus", "Land Rover", "Mini", "Tesla", "Alfa Romeo", "Chery", "Geely",
        "BYD", "MG", "Great Wall", "Isuzu", "Subaru"
    )

    private val modelsMap = mapOf(
        "Peugeot" to listOf("206", "207", "208", "301", "307", "308", "2008", "3008", "5008"),
        "Renault" to listOf("Clio", "Megane", "Symbol", "Captur", "Kadjar", "Duster"),
        "Citroën" to listOf("C3", "C4", "C5", "Berlingo"),
        "Volkswagen" to listOf("Golf", "Polo", "Passat", "Jetta", "Tiguan"),
        "Audi" to listOf("A3", "A4", "A6", "Q3", "Q5"),
        "BMW" to listOf("Series 3", "Series 5", "X1", "X3", "X5"),
        "Mercedes-Benz" to listOf("C180", "C200", "E200", "A-Class", "S-Class"),
        "Opel" to listOf("Astra", "Corsa", "Insignia"),
        "Ford" to listOf("Focus", "Fiesta", "Explorer", "Mustang"),
        "Fiat" to listOf("500", "Panda", "Tipo", "Punto"),
        "Seat" to listOf("Ibiza", "Leon", "Ateca"),
        "Skoda" to listOf("Octavia", "Fabia", "Superb"),
        "Toyota" to listOf("Corolla", "Yaris", "Hilux", "RAV4", "Camry", "Land Cruiser"),
        "Honda" to listOf("Civic", "Accord", "CR-V", "City"),
        "Hyundai" to listOf("Elantra", "Accent", "Tucson", "Santa Fe", "Creta", "i10", "i20"),
        "Kia" to listOf("Sportage", "Rio", "Cerato", "Picanto", "Sorento", "Optima"),
        "Nissan" to listOf("Sunny", "Qashqai", "X-Trail", "Patrol", "Altima"),
        "Dacia" to listOf("Logan", "Sandero", "Duster"),
        "Suzuki" to listOf("Swift", "Vitara", "Jimny"),
        "Mitsubishi" to listOf("Lancer", "Pajero", "Outlander"),
        "Mazda" to listOf("CX-5", "Mazda 3", "Mazda 6"),
        "Tesla" to listOf("Model 3", "Model Y", "Model S", "Model X")
    )

    fun getModelsFor(manufacturer: String): List<String> {
        return modelsMap[manufacturer] ?: listOf("أخرى", "M1", "M2")
    }

    val fuelTypes = listOf("بنزين", "ديزل", "هجين", "كهربائية")
    val transmissionTypes = listOf("يدوي", "أوتوماتيك")

    val symptomsList = listOf(
        "السيارة لا تعمل",
        "المحرك يدور ولا يشتغل",
        "تنطفئ بعد السخونة",
        "تنطفئ أثناء القيادة",
        "تأخير في التشغيل",
        "صعوبة تشغيل صباحًا",
        "ضعف في العزم",
        "بطء التسارع",
        "استهلاك وقود مرتفع",
        "رائحة وقود",
        "رائحة احتراق",
        "رائحة زيت",
        "ارتفاع حرارة المحرك",
        "انخفاض حرارة المحرك",
        "اهتزاز المحرك",
        "اهتزاز السيارة",
        "دخان أبيض",
        "دخان أسود",
        "دخان أزرق",
        "أصوات طقطقة",
        "أصوات صفير",
        "أصوات احتكاك",
        "لمبة المحرك",
        "لمبة ABS",
        "لمبة الوسائد الهوائية",
        "لمبة الزيت",
        "لمبة البطارية",
        "المكيف لا يبرد",
        "المروحة لا تعمل",
        "تسريب ماء",
        "تسريب زيت",
        "تسريب وقود",
        "ضعف الفرامل",
        "الدركسيون ثقيل",
        "الدركسيون يهتز",
        "صعوبة تغيير السرعات",
        "ضعف شحن البطارية",
        "عدم عمل الأنوار",
        "عدم عمل المساحات",
        "عدم عمل البوق"
    )

    data class OBDCode(
        val code: String,
        val title: String,
        val explanation: String,
        val causes: String,
        val solution: String,
        val severity: String // "high", "medium", "low"
    )

    val obdCodes = listOf(
        OBDCode(
            code = "P0100",
            title = "عطل في دائرة حساس تدفق الهواء (MAF)",
            explanation = "يشير هذا الرمز إلى وجود مشكلة في الإشارة المرسلة من حساس تدفق الهواء (MAF) إلى كمبيوتر السيارة (ECU). الحساس مسؤول عن قياس كمية الهواء الداخل للمحرك.",
            causes = "اتساخ الحساس بالأتربة، تلف الأسلاك أو التوصيلات، تلف الحساس نفسه، أو وجود تسريب في مجرى الهواء.",
            solution = "تنظيف الحساس ببخاخ خاص سريع التطاير، فحص التوصيلات الكهربائية، أو استبدال الحساس إذا كان تالفًا.",
            severity = "medium"
        ),
        OBDCode(
            code = "P0115",
            title = "عطل في دائرة حساس حرارة سائل التبريد (ECT)",
            explanation = "يدل على أن كمبيوتر السيارة يتلقى إشارات غير طبيعية أو خارج النطاق المعتاد من حساس درجة حرارة المحرك (سائل التبريد).",
            causes = "تلف حساس الحرارة، انخفاض مستوى سائل التبريد، تلف الثرموستات، أو تآكل وتلف الأسلاك.",
            solution = "فحص مستوى سائل التبريد وتعبئته، فحص أو استبدال الثرموستات وحساس الحرارة ECT.",
            severity = "high"
        ),
        OBDCode(
            code = "P0171",
            title = "خليط الوقود فقير جدًا (البنك 1)",
            explanation = "يعني أن المحرك يعمل بكمية هواء أكثر من اللازم مقارنة بنسبة الوقود (خليط فقير/لين). الكمبيوتر يحاول تعويض النقص بزيادة الوقود.",
            causes = "تسريب في مجرى الهواء بعد الحساس (Vacuum leak)، ضعف مضخة الوقود، انسداد فلتر الوقود، أو اتساخ البخاخات.",
            solution = "البحث عن تسريبات الهواء وإصلاحها، قياس ضغط مضخة الوقود، تنظيف البخاخات، أو استبدال فلتر الوقود.",
            severity = "medium"
        ),
        OBDCode(
            code = "P0300",
            title = "ميسفاير عشوائي/متعدد في الأسطوانات (Misfire)",
            explanation = "يدل على فشل أو تأخر الاحتراق داخل أسطوانات المحرك بشكل عشوائي، مما يؤدي لاهتزاز المحرك وضعف العزم وتلف الكاتاليزر.",
            causes = "تلف البواجي (شمعات الإشعال)، تلف الكويلات (ملفات الإشعال)، انسداد البخاخات، أو ضعف ضغط الأسطوانات.",
            solution = "استبدال البواجي التالفة، فحص واختبار الكويلات والبخاخات، وفحص التوصيلات الكهربائية.",
            severity = "high"
        ),
        OBDCode(
            code = "P0301",
            title = "ميسفاير في الأسطوانة رقم 1",
            explanation = "فشل الاحتراق محدد بالأسطوانة الأولى من أسطوانات المحرك.",
            causes = "تلف شمعة الإشعال أو كويل الأسطوانة 1، تلف بخاخ الأسطوانة 1، أو مشكلة ميكانيكية في صمامات هذه الأسطوانة.",
            solution = "نقل الكويل لأسطوانة أخرى لمعرفة إن كان العيب منه، استبدال البوجيه للأسطوانة 1، أو فحص البخاخ.",
            severity = "high"
        ),
        OBDCode(
            code = "P0302",
            title = "ميسفاير في الأسطوانة رقم 2",
            explanation = "فشل الاحتراق محدد بالأسطوانة الثانية من أسطوانات المحرك.",
            causes = "تلف شمعة الإشعال أو كويل الأسطوانة 2، تلف بخاخ الأسطوانة 2.",
            solution = "فحص واستبدال شمعة الإشعال أو الكويل الخاص بالأسطوانة الثانية.",
            severity = "high"
        ),
        OBDCode(
            code = "P0420",
            title = "كفاءة نظام الكاتاليزر أقل من الحد المسموح (البنك 1)",
            explanation = "يشير إلى أن علبة البيئة (دبة التلوث/الكاتاليزر) لم تعد تعمل بكفاءة في تنقية الغازات السامة الخارجة من العادم.",
            causes = "تلف الكاتاليزر بسبب ميسفاير مستمر أو استهلاك زيت، تلف حساس الأكسجين الخلفي، وجود تسريب في الشكمان.",
            solution = "إصلاح مشاكل ميسفاير المحرك أولاً، فحص حساس الأكسجين، أو استبدال علبة البيئة.",
            severity = "medium"
        ),
        OBDCode(
            code = "P0505",
            title = "عطل في نظام التحكم بهواء الخامل (IAC)",
            explanation = "يدل على وجود خلل في التحكم بسرعة خمول المحرك (السرعة أثناء التوقف)، مما يؤدي لتذبذب RPM أو انطفاء المحرك.",
            causes = "اتساخ أو تلف صمام بوابة الهواء (الثروتل/IAC)، وجود تسريب هواء فوتوم.",
            solution = "تنظيف صمام الخمول IAC وبوابة الهواء ببخاخ تنظيف الثروتل، أو استبدال الصمام.",
            severity = "medium"
        ),
        OBDCode(
            code = "P0700",
            title = "خلل في نظام التحكم بنقل الحركة (الفتيس)",
            explanation = "يدل على أن كمبيوتر ناقل الحركة (TCM) قد رصد عطلاً وأرسل إشارة إلى كمبيوتر المحرك لإشعال لمبة الأعطال.",
            causes = "تلف حساسات السرعة بالفتيس، انخفاض مستوى زيت القير، أو تلف بلوف الفتيس الداخلية (Solenoids).",
            solution = "فحص مستوى وجودة زيت ناقل الحركة، وقراءة أكواد كمبيوتر القير (TCM) التفصيلية.",
            severity = "high"
        ),
        OBDCode(
            code = "P0113",
            title = "إشارة مرتفعة في دائرة حساس درجة حرارة الهواء الداخل (IAT)",
            explanation = "حساس IAT يقيس درجة حرارة الهواء الداخل للمحرك، وإشارته مرتفعة مما يربك نسبة الوقود المتوقعة.",
            causes = "توصيلات كهربائية مرتخية، تلف الحساس نفسه، أو قصر في الدائرة الكهربائية.",
            solution = "تنظيف أو تغيير الحساس، وفحص الأسلاك والتوصيلات المتصلة به.",
            severity = "low"
        )
    )

    data class FaultDiagnosis(
        val faultName: String,
        val probability: Int,
        val explanation: String,
        val reason: String,
        val inspection: String,
        val repair: String,
        val repairTime: String,
        val cost: String,
        val severity: String, // "high", "medium", "low"
        val canContinueDriving: String,
        val partsToReplace: String
    )

    fun diagnose(selectedSymptoms: List<String>, carBrand: String = "السيارة"): List<FaultDiagnosis> {
        if (selectedSymptoms.isEmpty()) return emptyList()

        val results = mutableListOf<FaultDiagnosis>()

        // Look for symptoms combinations
        val hasNoStart = "السيارة لا تعمل" in selectedSymptoms || "المحرك يدور ولا يشتغل" in selectedSymptoms
        val hasLowPower = "ضعف في العزم" in selectedSymptoms || "بطء التسارع" in selectedSymptoms
        val hasBatterySymptom = "ضعف شحن البطارية" in selectedSymptoms || "لمبة البطارية" in selectedSymptoms || "عدم عمل الأنوار" in selectedSymptoms
        val hasEngineHeat = "ارتفاع حرارة المحرك" in selectedSymptoms || "المروحة لا تعمل" in selectedSymptoms || "تسريب ماء" in selectedSymptoms
        val hasVibration = "اهتزاز المحرك" in selectedSymptoms || "اهتزاز السيارة" in selectedSymptoms
        val hasSmoke = "دخان أبيض" in selectedSymptoms || "دخان أسود" in selectedSymptoms || "دخان أزرق" in selectedSymptoms
        val hasBrakes = "ضعف الفرامل" in selectedSymptoms || "لمبة ABS" in selectedSymptoms
        val hasSteering = "الدركسيون ثقيل" in selectedSymptoms || "الدركسيون يهتز" in selectedSymptoms
        val hasGear = "صعوبة تغيير السرعات" in selectedSymptoms
        val hasLeak = "تسريب زيت" in selectedSymptoms || "تسريب ماء" in selectedSymptoms || "تسريب وقود" in selectedSymptoms

        if (hasNoStart) {
            if (hasBatterySymptom) {
                results.add(
                    FaultDiagnosis(
                        faultName = "تلف أو ضعف البطارية (Battery Failure)",
                        probability = 95,
                        explanation = "البطارية هي مصدر الطاقة الكهربائية الأولي لبدء تشغيل المحرك. ضعفها يمنع بادئ الحركة (السلف) من تدوير المحرك بسرعة كافية.",
                        reason = "انتهاء العمر الافتراضي للبطارية (عادة سنتين إلى 3 سنوات)، أو وجود تسريب كهربائي، أو خلل في نظام الشحن (الدينامو).",
                        inspection = "قياس جهد البطارية بجهاز الملتيميتر (يجب أن يكون بين 12.4 و 12.7 فولت والمحرك مطفأ)، وفحص أقطاب البطارية من التآكل.",
                        repair = "تنظيف أقطاب البطارية وإحكام ربطها، أو شحنها، أو استبدالها ببطارية جديدة مطابقة للمواصفات.",
                        repairTime = "15 - 30 دقيقة",
                        cost = "$50 - $120",
                        severity = "high",
                        canContinueDriving = "لا، يجب حل المشكلة لتشغيل السيارة.",
                        partsToReplace = "بطارية جديدة"
                    )
                )
            } else {
                results.add(
                    FaultDiagnosis(
                        faultName = "تلف بادئ الحركة / السلف (Starter Motor)",
                        probability = 80,
                        explanation = "بادئ الحركة هو محرك كهربائي صغير يقوم بتدوير محرك السيارة الأساسي عند تدوير المفتاح لبدء التشغيل.",
                        reason = "تآكل الفحمات الداخلية، تلف التروس، أو وجود خلل في التوصيلات الكهربائية والفيوزات المغذية له.",
                        inspection = "سماع صوت طقطقة خفيفة عند محاولة التشغيل دون دوران المحرك، قياس وصول التيار الكهربائي إلى السلف.",
                        repair = "فك بادئ الحركة وإصلاح الأجزاء الداخلية (الفحمات والتروس) أو استبداله بالكامل.",
                        repairTime = "1 - 2 ساعة",
                        cost = "$80 - $200",
                        severity = "high",
                        canContinueDriving = "لا، السيارة لن تعمل دون إصلاحه.",
                        partsToReplace = "بادئ حركة (سلف) أو طقم فحمات داخلي"
                    )
                )
                results.add(
                    FaultDiagnosis(
                        faultName = "عطل في مضخة الوقود (Fuel Pump)",
                        probability = 70,
                        explanation = "مضخة الوقود مسؤولة عن إيصال الوقود بضغط معين من الخزان إلى البخاخات في المحرك. تلفها يمنع وصول الوقود تمامًا.",
                        reason = "تراكم الأوساخ نتيجة قيادة السيارة بخزان وقود شبه فارغ باستمرار، أو انتهاء عمرها الافتراضي.",
                        inspection = "عدم سماع صوت ويز خفيف عند فتح السويتش (On)، قياس ضغط الوقود في مجرى الوقود بالمحرك.",
                        repair = "استبدال مضخة الوقود التالفة وتنظيف خزان الوقود من الشوائب واستبدال فلتر الوقود.",
                        repairTime = "1 - 3 ساعات",
                        cost = "$100 - $350",
                        severity = "high",
                        canContinueDriving = "لا، لن يدور المحرك أو سيتوقف فورًا.",
                        partsToReplace = "مضخة الوقود (طرمبة البنزين)، فلتر الوقود"
                    )
                )
            }
        }

        if (hasLowPower || "استهلاك وقود مرتفع" in selectedSymptoms) {
            results.add(
                FaultDiagnosis(
                    faultName = "انسداد فلتر الهواء أو فلتر الوقود (Filter Clogging)",
                    probability = 85,
                    explanation = "تراكم الأتربة في فلتر الهواء يمنع المحرك من التنفس بحرية، وانسداد فلتر الوقود يقلل من تدفق البنزين، مما يؤدي لضعف العزم وزيادة الاستهلاك.",
                    reason = "عدم الالتزام بجدول الصيانة الدورية وتأخير تغيير الفلاتر في بيئة مليئة بالأتربة.",
                    inspection = "إخراج فلتر الهواء وفحصه بالنظر (إذا كان مظلمًا ومليئًا بالأتربة فهو تالف)، فحص ضغط الوقود لمعرفة حالة فلتر الوقود.",
                    repair = "استبدال فلتر الهواء وفلتر الوقود بقطع جديدة أصلية.",
                    repairTime = "20 - 45 دقيقة",
                    cost = "$15 - $50",
                    severity = "low",
                    canContinueDriving = "نعم، ولكن يفضل التغيير لتجنب إجهاد المحرك وتلف الحساسات.",
                    partsToReplace = "فلتر هواء، فلتر وقود"
                )
            )
            results.add(
                FaultDiagnosis(
                    faultName = "تلف شمعات الإشعال / البواجي (Spark Plugs)",
                    probability = 75,
                    explanation = "البواجي تولد الشرارة اللازمة لإشعال خليط الهواء والوقود داخل الأسطوانات. تلفها يسبب احتراقًا غير كامل وضعفًا كبيرًا في العزم.",
                    reason = "تراكم الكربون أو الزيت على رأس الشمعة، أو تآكل قطب الشمعة بسبب الاستخدام الطويل.",
                    inspection = "فك البواجي وفحص رأسها لمعرفة مدى تآكله ووجود رواسب سوداء أو تآكل شديد.",
                    repair = "تنظيف فجوة البواجي أو استبدال طقم البواجي بالكامل بطقم جديد.",
                    repairTime = "30 - 60 دقيقة",
                    cost = "$20 - $80",
                    severity = "medium",
                    canContinueDriving = "نعم لمسافة قصيرة، لكن يسبب اهتزازًا وتلفًا لعلبة البيئة مستقبلاً.",
                    partsToReplace = "طقم بواجي (شمعات إشعال)"
                )
            )
        }

        if (hasEngineHeat) {
            results.add(
                FaultDiagnosis(
                    faultName = "خلل في نظام التبريد / الثرموستات (Cooling System / Thermostat)",
                    probability = 90,
                    explanation = "الثرموستات ينظم مرور سائل التبريد من المحرك للرادياتير. إذا علق مغلقًا، ترتفع حرارة المحرك بشكل خطر وسريع.",
                    reason = "تلف زنبرك الثرموستات الداخلي، أو صدأ وتراكم شوائب في سائل التبريد نتيجة استخدام مياه عادية.",
                    inspection = "ملاحظة ارتفاع مؤشر الحرارة، مع بقاء خرطوم الرادياتير العلوي ساخنًا والسفلي باردًا.",
                    repair = "استبدال الثرموستات، وتنظيف نظام التبريد بالكامل وإضافة سائل تبريد مخصص.",
                    repairTime = "1 - 2 ساعة",
                    cost = "$40 - $120",
                    severity = "high",
                    canContinueDriving = "إطلاقًا لا! مواصلة القيادة تؤدي لتلف وجه رأس المحرك (جوان الكولاس) وتكلفة إصلاح باهظة.",
                    partsToReplace = "ثرموستات جديد، سائل تبريد أصلي"
                )
            )
            results.add(
                FaultDiagnosis(
                    faultName = "تلف مروحة التبريد أو طرمبة الماء (Cooling Fan / Water Pump)",
                    probability = 80,
                    explanation = "المروحة تسحب الهواء لتبريد الماء بالرادياتير، وطرمبة الماء تدير السائل داخل المحرك. تلف أي منهما يسبب غليان الماء وارتفاع الحرارة.",
                    reason = "تآكل ريش طرمبة الماء، تلف محرك مروحة التبريد الكهربائي، أو تلف في ريليه (مرحل) المروحة.",
                    inspection = "عدم دوران المروحة عند وصول المحرك لدرجة حرارة التشغيل، أو سماع صوت حشرجة من طرمبة الماء أو تهريب ماء منها.",
                    repair = "استبدال مروحة التبريد التالفة أو طرمبة الماء المتآكلة وفحص سير المحرك.",
                    repairTime = "2 - 4 ساعات",
                    cost = "$70 - $250",
                    severity = "high",
                    canContinueDriving = "لا، توقف فورًا عند مكان آمن وأطفئ المحرك.",
                    partsToReplace = "مروحة تبريد أو مضخة ماء"
                )
            )
        }

        if (hasVibration) {
            results.add(
                FaultDiagnosis(
                    faultName = "تلف كراسي المحرك (Engine Mounts)",
                    probability = 85,
                    explanation = "كراسي المحرك مصنوعة من المعدن والمطاط لتثبيت المحرك وامتصاص الاهتزازات الناتجة عن احتراقه. تلف المطاط يسبب اهتزاز السيارة بالكامل.",
                    reason = "تصلب وتلف الجزء المطاطي بكراسي المحرك بسبب الحرارة والعمر الطويل.",
                    inspection = "ملاحظة اهتزاز شديد بالسيارة وهي متوقفة (على وضع D أو N)، واختفاء أو قلة الاهتزاز عند الحركة.",
                    repair = "استبدال كرسي أو كراسي المحرك التالفة بكراسي جديدة.",
                    repairTime = "1 - 3 ساعات",
                    cost = "$60 - $200",
                    severity = "medium",
                    canContinueDriving = "نعم، ولكن الاهتزاز المستمر قد يضر بقطع أخرى وتوصيلات العادم.",
                    partsToReplace = "كرسي محرك (كراسي المكينة)"
                )
            )
        }

        if (hasBrakes) {
            results.add(
                FaultDiagnosis(
                    faultName = "تآكل تيل أو فحمات الفرامل (Brake Pads Wear)",
                    probability = 95,
                    explanation = "فحمات الفرامل هي القطع الاحتكاكية التي تضغط على الهوبات (الأقراص) لإيقاف السيارة. تآكلها يضعف الفرامل ويسبب أصوات احتكاك معدنية.",
                    reason = "تآكل المادة الاحتكاكية بالاستخدام الطبيعي للفرامل.",
                    inspection = "سماع صوت صفير أو حك معدني عند الضغط على الفرامل، وانخفاض مستوى زيت الفرامل بالعلبة.",
                    repair = "استبدال فحمات الفرامل فورا، وعمل خرط (تسوية) لأقراص الفرامل إذا كان بها خدوش.",
                    repairTime = "45 - 90 دقيقة",
                    cost = "$30 - $100",
                    severity = "high",
                    canContinueDriving = "نعم ببطء شديد وبحذر للذهاب للورشة، فالقيادة بها خطرة جدًا.",
                    partsToReplace = "طقم فحمات فرامل أمامي/خلفي"
                )
            )
        }

        if (hasSteering) {
            results.add(
                FaultDiagnosis(
                    faultName = "نقص زيت الدركسيون أو خلل في علبة القيادة (Power Steering Defect)",
                    probability = 90,
                    explanation = "نظام التوجيه الهيدروليكي يعتمد على زيت الباور لتسهيل حركة الدركسيون. نقصه يجعله ثقيلًا جدًا ويصدر أصواتًا عند الالتفاف.",
                    reason = "وجود تسريب في خراطيم أو علبة الدركسيون (الدودة)، أو تلف طرمبة الباور.",
                    inspection = "فحص مستوى زيت الباور في العلبة المخصصة، سماع صوت أنين عند لف الدركسيون.",
                    repair = "إصلاح التهريب وتعبئة زيت باور أصلي، أو تغيير طرمبة الباور إذا كانت تالفة.",
                    repairTime = "1 - 2 ساعة",
                    cost = "$40 - $180",
                    severity = "medium",
                    canContinueDriving = "نعم، ولكن توجيه السيارة سيكون ثقيلاً ومجهداً ويتطلب قوة بدنية.",
                    partsToReplace = "زيت باور، أو طرمبة باور، أو خراطيم توجيه"
                )
            )
        }

        if (hasSmoke) {
            if ("دخان أزرق" in selectedSymptoms) {
                results.add(
                    FaultDiagnosis(
                        faultName = "تسريب زيت داخل غرف الاحتراق (Engine Oil Burning / Valve Seals)",
                        probability = 85,
                        explanation = "ظهور دخان أزرق من الشكمان يدل على احتراق زيت المحرك داخل غرف الاحتراق مع الوقود، مما يسبب نقص الزيت.",
                        reason = "تلف جلود الصمامات (الصبابات) أو تآكل شنابر المكبس (Piston Rings).",
                        inspection = "خروج دخان أزرق خاصة عند بدء التشغيل صباحاً أو عند الضغط على دواسة الوقود، مع نقص مستمر بزيت المحرك.",
                        repair = "تغيير جلود الصمامات أو عمل توضيب (عمرة) جزئي أو كلي للمحرك.",
                        repairTime = "1 - 3 أيام",
                        cost = "$150 - $800",
                        severity = "high",
                        canContinueDriving = "نعم مؤقتاً مع مراقبة مستوى الزيت وزيادته باستمرار حتى الإصلاح.",
                        partsToReplace = "جلود صبابات، شنابر مكبس، وجه رأس المحرك"
                    )
                )
            } else if ("دخان أسود" in selectedSymptoms) {
                results.add(
                    FaultDiagnosis(
                        faultName = "زيادة نسبة الوقود في الاحتراق (Rich Mixture / Black Smoke)",
                        probability = 90,
                        explanation = "الدخان الأسود يشير إلى احتراق كمية وقود زائدة لعدم وجود هواء كافٍ، مما يسبب هدراً كبيراً للبنزين وضعفاً بالعزم.",
                        reason = "اتساخ أو تلف البخاخات (تسيل بنزين)، تلف حساس الأكسجين، أو انسداد تام لفلتر الهواء.",
                        inspection = "ملاحظة خروج دخان أسود كربوني من العادم وتراكم السواد على فوهة الشكمان.",
                        repair = "تنظيف أو استبدال البخاخات، تغيير فلتر الهواء، أو استبدال حساس الأكسجين.",
                        repairTime = "1 - 2 ساعة",
                        cost = "$50 - $250",
                        severity = "medium",
                        canContinueDriving = "نعم، ولكن مع استهلاك كبير جداً للوقود وتأثير سلبي على علبة البيئة.",
                        partsToReplace = "بخاخات وقود، حساس أكسجين، فلتر هواء"
                    )
                )
            } else {
                results.add(
                    FaultDiagnosis(
                        faultName = "تسريب سائل التبريد داخل المحرك (Coolant Leak to Cylinder / White Smoke)",
                        probability = 80,
                        explanation = "الدخان الأبيض الكثيف المستمر (وليس بخار الصباح المؤقت) يعني اختلاط ماء التبريد بغرف الاحتراق بالمحرك.",
                        reason = "تلف حشية رأس المحرك (جوان رأس الكولاس) نتيجة ارتفاع سابق ومفاجئ لحرارة المحرك.",
                        inspection = "خروج دخان أبيض كثيف له رائحة حلوة، نقصان سائل التبريد باستمرار، تغير لون زيت المحرك للون شبيه بالحليب والقهوة.",
                        repair = "فك رأس المحرك واستبدال الحشية (الجوان) ومسح رأس المحرك بالخرط لإعادة استوائه.",
                        repairTime = "1 - 2 يوم",
                        cost = "$200 - $600",
                        severity = "high",
                        canContinueDriving = "إطلاقاً لا! قد يتسبب ذلك في تلف كامل للمحرك (خلط ماء وزيت وتصلب المحرك).",
                        partsToReplace = "جوان رأس المحرك، سائل تبريد جديد، زيت محرك وفلتر"
                    )
                )
            }
        }

        // Default catch-all diagnostic report if no specific match is found but some symptoms are checked
        if (results.isEmpty()) {
            results.add(
                FaultDiagnosis(
                    faultName = "خلل عام بحاجة للفحص المباشر (Check Engine Diagnostic Required)",
                    probability = 75,
                    explanation = "الأعراض المدخلة تشير إلى مشكلة كهربائية أو ميكانيكية عامة تتطلب قراءة كود العطل عبر جهاز كاشف الأعطال (OBD-II).",
                    reason = "خلل مؤقت في أحد الحساسات الفرعية أو التوصيلات.",
                    inspection = "توصيل جهاز OBD-II بالسيارة وقراءة الكود المخزن في ذاكرة الكمبيوتر لتحديد العطل بدقة.",
                    repair = "إجراء الصيانة بحسب الكود الذي يظهره جهاز الفحص.",
                    repairTime = "30 - 60 دقيقة",
                    cost = "$20 - $70 (تكلفة فحص الكمبيوتر)",
                    severity = "medium",
                    canContinueDriving = "نعم بحذر حتى الذهاب لأقرب ورشة لتشخيص الكود.",
                    partsToReplace = "بحسب نتيجة فحص الكمبيوتر"
                )
            )
        }

        return results
    }
}
