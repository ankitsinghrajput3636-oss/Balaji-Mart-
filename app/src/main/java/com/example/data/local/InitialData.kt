package com.example.data.local

import com.example.data.model.Category
import com.example.data.model.Coupon
import com.example.data.model.DeliveryPartner
import com.example.data.model.NotificationItem
import com.example.data.model.Product
import com.example.data.model.StoreSettings

object InitialData {

    val categories = listOf(
        Category("fruits_veg", "Fruits & Vegetables", "फल एवं सब्जियां", "🥦", 1),
        Category("atta_rice_dal", "Atta, Rice & Dal", "आटा, चावल और दाल", "🌾", 2),
        Category("dairy_bakery", "Dairy & Bakery", "डेयरी और बेकरी", "🥛", 3),
        Category("oil_ghee", "Oil & Ghee", "तेल और घी", "🫒", 4),
        Category("spices_masala", "Spices & Masala", "मसाले और तड़का", "🌶️", 5),
        Category("snacks_namkeen", "Snacks & Namkeen", "स्नैक्स और नमकीन", "🍿", 6),
        Category("biscuits_chocolates", "Biscuits & Chocolates", "बिस्कुट और चॉकलेट", "🍪", 7),
        Category("drinks_juices", "Cold Drinks & Juices", "कोल्ड ड्रिंक्स और जूस", "🧃", 8),
        Category("tea_coffee", "Tea & Coffee", "चाय और कॉफी", "☕", 9),
        Category("personal_care", "Personal Care", "पर्सनल केयर", "🧴", 10),
        Category("beauty_products", "Beauty Products", "ब्यूटी प्रोडक्ट्स", "💄", 11),
        Category("household_cleaning", "Household Cleaning", "सफाई का सामान", "🧹", 12),
        Category("kitchen_essentials", "Kitchen Essentials", "किचन का सामान", "🍳", 13),
        Category("baby_care", "Baby Care", "बेबी केयर", "🍼", 14),
        Category("pooja_samagri", "Pooja Samagri", "पूजा सामग्री", "🪔", 15),
        Category("dry_fruits", "Dry Fruits & Nuts", "सूखे मेवे", "🥜", 16),
        Category("frozen_foods", "Frozen Foods", "फ्रोजन फूड्स", "🧊", 17),
        Category("pet_supplies", "Pet Supplies", "पेट सप्लाइज", "🐾", 18),
        Category("packaged_foods", "Packaged Foods", "पैकेज्ड फूड्स", "🥫", 19)
    )

    val products = listOf(
        // Fruits & Veg
        Product(
            id = "fv_01",
            nameEn = "Fresh Farm Tomatoes (Hybrid)",
            nameHi = "ताजे टमाटर",
            categoryId = "fruits_veg",
            subCategory = "Vegetables",
            price = 34.0,
            originalMrp = 50.0,
            unit = "1 kg",
            descriptionEn = "Farm-fresh ripe red juicy tomatoes sourced directly from local farmers.",
            descriptionHi = "खेत से सीधे लाए गए ताजे और रसीले टमाटर।",
            stock = 40,
            rating = 4.8f,
            reviewCount = 142,
            isTrending = true,
            isFrequentlyBought = true,
            tags = "tomato, tamatar, sabji, fresh, vegetable"
        ),
        Product(
            id = "fv_02",
            nameEn = "Fresh Potato (Aloo)",
            nameHi = "ताजा आलू",
            categoryId = "fruits_veg",
            subCategory = "Vegetables",
            price = 28.0,
            originalMrp = 40.0,
            unit = "1 kg",
            descriptionEn = "Clean, firm fresh new crop potatoes ideal for curries and snacks.",
            descriptionHi = "साफ और अच्छे आलू हर तरह की सब्जी के लिए।",
            stock = 65,
            rating = 4.7f,
            reviewCount = 98,
            isBestSeller = true,
            isFrequentlyBought = true,
            tags = "potato, aloo, alu, vegetable, sabji"
        ),
        Product(
            id = "fv_03",
            nameEn = "Nashik Red Onions (Pyaz)",
            nameHi = "ताजा प्याज",
            categoryId = "fruits_veg",
            subCategory = "Vegetables",
            price = 45.0,
            originalMrp = 60.0,
            unit = "1 kg",
            descriptionEn = "Grade-A quality crunchy red onions with rich pungent aroma.",
            descriptionHi = "नासिक के उच्च गुणवत्ता वाले लाल प्याज।",
            stock = 50,
            rating = 4.9f,
            reviewCount = 210,
            isBestSeller = true,
            isFrequentlyBought = true,
            tags = "onion, pyaz, pyaaz, sabji"
        ),
        Product(
            id = "fv_04",
            nameEn = "Fresh Shimla Green Apples",
            nameHi = "शिमला सेब",
            categoryId = "fruits_veg",
            subCategory = "Fruits",
            price = 140.0,
            originalMrp = 180.0,
            unit = "4 pcs (approx 600g)",
            descriptionEn = "Sweet and crisp orchard apples handpicked from Himachal.",
            descriptionHi = "मीठे और कुरकुरे ताजे सेब।",
            stock = 25,
            rating = 4.9f,
            reviewCount = 85,
            isRecommended = true,
            tags = "apple, seb, fruits, himachal"
        ),

        // Atta, Rice, Dal
        Product(
            id = "ard_01",
            nameEn = "Aashirvaad Shudh Chakki Atta",
            nameHi = "आशीर्वाद शुद्ध चक्की आटा",
            categoryId = "atta_rice_dal",
            subCategory = "Atta & Flours",
            price = 245.0,
            originalMrp = 275.0,
            unit = "5 kg",
            descriptionEn = "100% whole wheat flour ground to perfection for soft rotis.",
            descriptionHi = "100% साबुत गेहूं से बना चक्की ताजा आटा।",
            stock = 30,
            rating = 4.9f,
            reviewCount = 340,
            isBestSeller = true,
            isFrequentlyBought = true,
            tags = "atta, flour, aashirvaad, gehu, roti"
        ),
        Product(
            id = "ard_02",
            nameEn = "Fortune Rozana Basmati Rice",
            nameHi = "फॉर्च्यून रोजमर्रा बासमती चावल",
            categoryId = "atta_rice_dal",
            subCategory = "Rice & Poha",
            price = 115.0,
            originalMrp = 145.0,
            unit = "1 kg",
            descriptionEn = "Aromatic long grain basmati rice, pristine pearly white.",
            descriptionHi = "सुगंधित लंबे दाने वाला बासमती चावल।",
            stock = 45,
            rating = 4.7f,
            reviewCount = 115,
            isRecommended = true,
            tags = "rice, chawal, basmati, fortune"
        ),
        Product(
            id = "ard_03",
            nameEn = "Tata Sampann Unpolished Toor Dal",
            nameHi = "टाटा सम्पन्न तुअर दाल",
            categoryId = "atta_rice_dal",
            subCategory = "Dals & Pulses",
            price = 168.0,
            originalMrp = 195.0,
            unit = "1 kg",
            descriptionEn = "Pure protein-rich unpolished arhar dal with natural wholesome nutrients.",
            descriptionHi = "बिना पॉलिश की शुद्ध और पौष्टिक अरहर दाल।",
            stock = 38,
            rating = 4.8f,
            reviewCount = 176,
            isFrequentlyBought = true,
            tags = "dal, toor dal, arhar, pulses, tata sampann"
        ),

        // Dairy & Bakery
        Product(
            id = "db_01",
            nameEn = "Amul Taaza Homogenised Toned Milk",
            nameHi = "अमूल ताजा टोन्ड दूध",
            categoryId = "dairy_bakery",
            subCategory = "Milk",
            price = 27.0,
            originalMrp = 28.0,
            unit = "500 ml",
            descriptionEn = "Pasteurised toned milk with 3.0% fat, rich taste and essential calcium.",
            descriptionHi = "अमूल ताजा शुद्ध और स्वादिष्ट दूध।",
            stock = 90,
            rating = 4.9f,
            reviewCount = 520,
            isBestSeller = true,
            isTrending = true,
            isFrequentlyBought = true,
            tags = "milk, doodh, amul, dairy, fresh"
        ),
        Product(
            id = "db_02",
            nameEn = "Amul Salted Butter",
            nameHi = "अमूल मक्खन",
            categoryId = "dairy_bakery",
            subCategory = "Butter & Cheese",
            price = 56.0,
            originalMrp = 60.0,
            unit = "100 g",
            descriptionEn = "Utterly butterly delicious classic salted butter.",
            descriptionHi = "अमूल मक्खन - भारत का पसंदीदा स्वाद।",
            stock = 50,
            rating = 4.9f,
            reviewCount = 310,
            isBestSeller = true,
            tags = "butter, makkhan, amul, dairy"
        ),
        Product(
            id = "db_03",
            nameEn = "Harvest Gold 100% Whole Wheat Bread",
            nameHi = "हॉर्वेस्ट गोल्ड होल व्हीट ब्रेड",
            categoryId = "dairy_bakery",
            subCategory = "Bakery",
            price = 45.0,
            originalMrp = 50.0,
            unit = "400 g",
            descriptionEn = "Soft, freshly baked fiber-packed whole wheat bread loaf.",
            descriptionHi = "ताजी और मुलायम होल व्हीट ब्रेड।",
            stock = 25,
            rating = 4.6f,
            reviewCount = 89,
            tags = "bread, wheat bread, bakery, breakfast"
        ),

        // Oil & Ghee
        Product(
            id = "og_01",
            nameEn = "Fortune Sunlite Refined Sunflower Oil",
            nameHi = "फॉर्च्यून रिफाइंड सनफ्लावर तेल",
            categoryId = "oil_ghee",
            subCategory = "Cooking Oils",
            price = 138.0,
            originalMrp = 165.0,
            unit = "1 L pouch",
            descriptionEn = "Enriched with Vitamins A & D, light and easily digestible cooking oil.",
            descriptionHi = "हल्का और सेहतमंद रिफाइंड सूरजमुखी तेल।",
            stock = 35,
            rating = 4.8f,
            reviewCount = 145,
            isBestSeller = true,
            tags = "oil, tel, sunflower, cooking oil, fortune"
        ),
        Product(
            id = "og_02",
            nameEn = "Amul Pure Cow Ghee",
            nameHi = "अमूल शुद्ध गाय का घी",
            categoryId = "oil_ghee",
            subCategory = "Ghee",
            price = 620.0,
            originalMrp = 675.0,
            unit = "1 L Ceka Pack",
            descriptionEn = "Granular aromatic golden cow ghee with traditional purity.",
            descriptionHi = "दानेदार और सुगंधित शुद्ध गाय का देसी घी।",
            stock = 18,
            rating = 4.9f,
            reviewCount = 260,
            isRecommended = true,
            tags = "ghee, cow ghee, desi ghee, amul"
        ),

        // Spices & Masala
        Product(
            id = "sm_01",
            nameEn = "MDH Deggi Mirch Powder",
            nameHi = "एमडीएच देगी मिर्च",
            categoryId = "spices_masala",
            subCategory = "Powdered Spices",
            price = 88.0,
            originalMrp = 100.0,
            unit = "100 g",
            descriptionEn = "Famous Indian spice blend for natural rich red color and mild warmth.",
            descriptionHi = "प्राकृतिक लाल रंग और स्वादिष्ट स्वाद देने वाली देगी मिर्च।",
            stock = 40,
            rating = 4.9f,
            reviewCount = 190,
            isBestSeller = true,
            tags = "mirch, deggi mirch, mdh, masala, chilli"
        ),
        Product(
            id = "sm_02",
            nameEn = "Tata Salt Vacuum Evaporated Iodised",
            nameHi = "टाटा नमक आयोडीन युक्त",
            categoryId = "spices_masala",
            subCategory = "Salt & Sugar",
            price = 28.0,
            originalMrp = 30.0,
            unit = "1 kg",
            descriptionEn = "Desh ka namak, vacuum evaporated pure white iodised salt.",
            descriptionHi = "देश का नमक, शुद्ध आयोडीन युक्त टाटा नमक।",
            stock = 80,
            rating = 4.9f,
            reviewCount = 670,
            isFrequentlyBought = true,
            tags = "salt, namak, tata salt, iodine"
        ),

        // Snacks & Namkeen
        Product(
            id = "sn_01",
            nameEn = "Haldiram's Nagpur Aloo Bhujia",
            nameHi = "हल्दीराम आलू भुजिया",
            categoryId = "snacks_namkeen",
            subCategory = "Namkeen",
            price = 52.0,
            originalMrp = 60.0,
            unit = "200 g",
            descriptionEn = "Crispy, spicy potato sev with authentic mint and spice punch.",
            descriptionHi = "कुरकुरी और तीखी स्वादिष्ट आलू भुजिया।",
            stock = 55,
            rating = 4.8f,
            reviewCount = 280,
            isTrending = true,
            isBestSeller = true,
            tags = "namkeen, aloo bhujia, haldiram, snacks"
        ),
        Product(
            id = "sn_02",
            nameEn = "Lay's India's Magic Masala Chips",
            nameHi = "लेज मैजिक मसाला चिप्स",
            categoryId = "snacks_namkeen",
            subCategory = "Chips & Crisps",
            price = 20.0,
            originalMrp = 20.0,
            unit = "50 g",
            descriptionEn = "Crunchy ridged potato chips seasoned with tangy Indian spices.",
            descriptionHi = "मसालेदार और चटपटे आलू चिप्स।",
            stock = 75,
            rating = 4.7f,
            reviewCount = 310,
            tags = "lays, chips, potato chips, magic masala"
        ),

        // Biscuits & Chocolates
        Product(
            id = "bc_01",
            nameEn = "Parle-G Gold Glucose Biscuits",
            nameHi = "पारले-जी गोल्ड बिस्कुट",
            categoryId = "biscuits_chocolates",
            subCategory = "Biscuits",
            price = 25.0,
            originalMrp = 30.0,
            unit = "250 g",
            descriptionEn = "Bigger and richer original milk & wheat glucose biscuits for tea time.",
            descriptionHi = "दूध और गेहूं की शक्ति वाला पारले-जी गोल्ड।",
            stock = 60,
            rating = 4.9f,
            reviewCount = 410,
            isFrequentlyBought = true,
            tags = "parle g, biscuit, chai, glucose"
        ),
        Product(
            id = "bc_02",
            nameEn = "Cadbury Dairy Milk Silk Chocolate Bar",
            nameHi = "कैडबरी डेयरी मिल्क सिल्क",
            categoryId = "biscuits_chocolates",
            subCategory = "Chocolates",
            price = 85.0,
            originalMrp = 90.0,
            unit = "60 g",
            descriptionEn = "Velvety smooth, melting milk chocolate indulgence.",
            descriptionHi = "बेहद मुलायम और स्वादिष्ट सिल्क चॉकलेट।",
            stock = 30,
            rating = 4.9f,
            reviewCount = 330,
            isTrending = true,
            tags = "cadbury, chocolate, silk, dairy milk"
        ),

        // Cold Drinks & Juices
        Product(
            id = "dj_01",
            nameEn = "Coca-Cola Zero Sugar Soft Drink Can",
            nameHi = "कोका-कोला जीरो शुगर",
            categoryId = "drinks_juices",
            subCategory = "Carbonated Drinks",
            price = 40.0,
            originalMrp = 40.0,
            unit = "300 ml Can",
            descriptionEn = "Chilled sparkling refreshment with the iconic Coke taste and 0 calories.",
            descriptionHi = "ठंडी और ताजगी भरी जीरो कैलोरी कोका-कोला।",
            stock = 35,
            rating = 4.7f,
            reviewCount = 120,
            tags = "coke, coca cola, cold drink, soda, beverage"
        ),
        Product(
            id = "dj_02",
            nameEn = "Real Fruit Power Mixed Fruit Juice",
            nameHi = "रियल फ्रूट जूस मिक्स्ड फ्रूट",
            categoryId = "drinks_juices",
            subCategory = "Fruit Juices",
            price = 110.0,
            originalMrp = 130.0,
            unit = "1 L Tetra Pak",
            descriptionEn = "Rich fruit juice goodness with real blends of apple, mango, and guava.",
            descriptionHi = "फलों के रस से भरपूर मिक्स्ड फ्रूट जूस।",
            stock = 25,
            rating = 4.8f,
            reviewCount = 95,
            tags = "juice, real juice, mixed fruit, cold drink"
        ),

        // Tea & Coffee
        Product(
            id = "tc_01",
            nameEn = "Tata Tea Gold Royal Taste & Aroma",
            nameHi = "टाटा टी गोल्ड",
            categoryId = "tea_coffee",
            subCategory = "Tea",
            price = 285.0,
            originalMrp = 330.0,
            unit = "500 g",
            descriptionEn = "Delightful blend of fine Assam CTC teas with 15% long leaves.",
            descriptionHi = "कड़क और खुशबूदार असम की स्पेशल चाय।",
            stock = 40,
            rating = 4.9f,
            reviewCount = 220,
            isBestSeller = true,
            isFrequentlyBought = true,
            tags = "tea, chai, tata tea, tata gold"
        ),
        Product(
            id = "tc_02",
            nameEn = "Nescafé Classic Instant Coffee Jar",
            nameHi = "नेस्कैफे क्लासिक कॉफी",
            categoryId = "tea_coffee",
            subCategory = "Coffee",
            price = 185.0,
            originalMrp = 210.0,
            unit = "50 g Glass Jar",
            descriptionEn = "100% pure instant coffee made from selected Robusta beans.",
            descriptionHi = "प्योर और ताजगी भरी स्वादिष्ट नेस्कैफे कॉफी।",
            stock = 28,
            rating = 4.8f,
            reviewCount = 160,
            tags = "coffee, nescafe, classic, brew"
        ),

        // Personal Care
        Product(
            id = "pc_01",
            nameEn = "Dettol Original Germ Protection Soap (Buy 4 Get 1)",
            nameHi = "डेटॉल ओरिजिनल साबुन",
            categoryId = "personal_care",
            subCategory = "Bath & Body",
            price = 145.0,
            originalMrp = 175.0,
            unit = "125 g x 5 pcs",
            descriptionEn = "Proven antibacterial protection for the whole family.",
            descriptionHi = "कीटाणुओं से 100% सुरक्षा देने वाला डेटॉल साबुन।",
            stock = 30,
            rating = 4.9f,
            reviewCount = 290,
            isBestSeller = true,
            tags = "soap, dettol, bathing, hygiene, bath"
        ),
        Product(
            id = "pc_02",
            nameEn = "Colgate Strong Teeth Dental Cream",
            nameHi = "कोलगेट स्ट्रॉन्ग टीथ टूथपेस्ट",
            categoryId = "personal_care",
            subCategory = "Oral Care",
            price = 110.0,
            originalMrp = 130.0,
            unit = "300 g Saver Pack",
            descriptionEn = "With amino power formula for 4x stronger enamel protection.",
            descriptionHi = "मजबूत दांतों और ताजगी के लिए कोलगेट टूथपेस्ट।",
            stock = 40,
            rating = 4.8f,
            reviewCount = 185,
            tags = "toothpaste, colgate, oral care, brush"
        ),

        // Beauty Products
        Product(
            id = "bp_01",
            nameEn = "Nivea Soft Light Moisturizing Cream",
            nameHi = "निविया सॉफ्ट लाइट क्रीम",
            categoryId = "beauty_products",
            subCategory = "Skincare",
            price = 199.0,
            originalMrp = 240.0,
            unit = "100 ml",
            descriptionEn = "Fast-absorbing light moisturizer enriched with Vitamin E and Jojoba Oil.",
            descriptionHi = "त्वचा को नर्म और मुलायम रखने वाली निविया क्रीम।",
            stock = 22,
            rating = 4.8f,
            reviewCount = 110,
            tags = "nivea, cream, beauty, moisturizer, face"
        ),

        // Household Cleaning
        Product(
            id = "hc_01",
            nameEn = "Surf Excel Quick Wash Detergent Powder",
            nameHi = "सर्फ एक्सेल क्विक वॉश पाउडर",
            categoryId = "household_cleaning",
            subCategory = "Laundry",
            price = 195.0,
            originalMrp = 230.0,
            unit = "1 kg",
            descriptionEn = "Removes tough stains like tea, curry and gravy in 1 wash.",
            descriptionHi = "जिद्दी दाग आसानी से हटाने वाला सर्फ एक्सेल।",
            stock = 35,
            rating = 4.9f,
            reviewCount = 270,
            isFrequentlyBought = true,
            tags = "surf excel, detergent, washing, powder, laundry"
        ),
        Product(
            id = "hc_02",
            nameEn = "Vim Dishwash Gel Lemon",
            nameHi = "विम डिशवॉश जेल नींबू",
            categoryId = "household_cleaning",
            subCategory = "Dishwash",
            price = 99.0,
            originalMrp = 120.0,
            unit = "750 ml Pouch",
            descriptionEn = "Degreases 100 tough oily utensils with just one spoon of gel.",
            descriptionHi = "नींबू की शक्ति से बर्तनों की चिकनाई साफ करें।",
            stock = 45,
            rating = 4.8f,
            reviewCount = 195,
            tags = "vim, dishwash, bartan, cleaning, lemon"
        ),

        // Kitchen Essentials
        Product(
            id = "ke_01",
            nameEn = "Freshwrapp Aluminum Kitchen Foil",
            nameHi = "फ्रेशव्रैप एल्युमिनियम फॉयल",
            categoryId = "kitchen_essentials",
            subCategory = "Food Wrap",
            price = 125.0,
            originalMrp = 150.0,
            unit = "18 meters",
            descriptionEn = "Food grade 11 microns thick hygienic wrapping foil for fresh rotis.",
            descriptionHi = "खाने को ताजा रखने वाला एल्युमिनियम फॉयल।",
            stock = 20,
            rating = 4.7f,
            reviewCount = 80,
            tags = "foil, wrap, kitchen, roti wrap"
        ),

        // Baby Care
        Product(
            id = "bc_baby_01",
            nameEn = "Pampers All Round Protection Baby Pants (M)",
            nameHi = "पैम्पर्स बेबी डायपर पैंट्स",
            categoryId = "baby_care",
            subCategory = "Diapers",
            price = 549.0,
            originalMrp = 649.0,
            unit = "44 Count Pack",
            descriptionEn = "Up to 12 hours absorption with anti-rash lotion and aloe vera.",
            descriptionHi = "बच्चे की नाजुक त्वचा के लिए सुरक्षित पैम्पर्स डायपर।",
            stock = 15,
            rating = 4.9f,
            reviewCount = 140,
            tags = "pampers, diaper, baby, care"
        ),

        // Pooja Samagri
        Product(
            id = "ps_01",
            nameEn = "Mangaldeep Sandalwood Agarbatti (Incense Sticks)",
            nameHi = "मंगलदीप चंदन अगरबत्ती",
            categoryId = "pooja_samagri",
            subCategory = "Incense & Dhoop",
            price = 50.0,
            originalMrp = 60.0,
            unit = "Pack of 100 sticks",
            descriptionEn = "Divinely fragrant natural sandalwood incense sticks for daily prayers.",
            descriptionHi = "मन को शांति और भक्ति का अहसास देने वाली चंदन अगरबत्ती।",
            stock = 50,
            rating = 4.9f,
            reviewCount = 230,
            isBestSeller = true,
            tags = "agarbatti, pooja, dhoop, chandan, incense"
        ),
        Product(
            id = "ps_02",
            nameEn = "Pure Cow Ghee Pooja Diya Batti (Wicks)",
            nameHi = "शुद्ध घी दीया बत्ती",
            categoryId = "pooja_samagri",
            subCategory = "Diya & Wicks",
            price = 75.0,
            originalMrp = 90.0,
            unit = "50 Ready-to-light wicks",
            descriptionEn = "Ready made organic cotton wicks soaked in pure cow ghee.",
            descriptionHi = "पूजा के लिए तैयार शुद्ध घी की दीया बत्तियां।",
            stock = 30,
            rating = 4.8f,
            reviewCount = 95,
            tags = "diya, batti, pooja, ghee wicks"
        ),

        // Dry Fruits
        Product(
            id = "df_01",
            nameEn = "Nutraj California Premium Whole Almonds (Badam)",
            nameHi = "कैलिफ़ोर्निया बादाम गिरी",
            categoryId = "dry_fruits",
            subCategory = "Almonds",
            price = 399.0,
            originalMrp = 499.0,
            unit = "500 g Zip Pack",
            descriptionEn = "Handpicked crunchy Californian sweet whole almonds packed with Vitamin E.",
            descriptionHi = "दिमाग और सेहत के लिए उत्तम कैलिफोर्निया बादाम।",
            stock = 25,
            rating = 4.9f,
            reviewCount = 160,
            isRecommended = true,
            tags = "badam, almond, dry fruit, nutraj"
        ),
        Product(
            id = "df_02",
            nameEn = "Royal Premium Cashew Nuts (Kaju W320)",
            nameHi = "शाही काजू दानेदार",
            categoryId = "dry_fruits",
            subCategory = "Cashews",
            price = 425.0,
            originalMrp = 520.0,
            unit = "500 g",
            descriptionEn = "Crisp whole king size white cashew nuts, ideal for sweets and curries.",
            descriptionHi = "सफेद और बड़े दाने वाले शाही काजू।",
            stock = 20,
            rating = 4.8f,
            reviewCount = 110,
            tags = "kaju, cashew, dry fruit"
        ),

        // Frozen Foods
        Product(
            id = "ff_01",
            nameEn = "McCain French Fries Crispy",
            nameHi = "मैक्केन क्रिस्पी फ्रेंच फ्राइज़",
            categoryId = "frozen_foods",
            subCategory = "Snacks",
            price = 115.0,
            originalMrp = 135.0,
            unit = "420 g Frozen Pack",
            descriptionEn = "Golden, crunchy restaurant-style fries ready in just 3 minutes.",
            descriptionHi = "स्वादिष्ट और कुरकुरी फ्रेंच फ्राइज।",
            stock = 15,
            rating = 4.7f,
            reviewCount = 85,
            tags = "mccain, fries, frozen, french fries"
        ),

        // Pet Supplies
        Product(
            id = "pets_01",
            nameEn = "Pedigree Adult Chicken & Vegetables Dry Dog Food",
            nameHi = "पेडिग्री डॉग फूड",
            categoryId = "pet_supplies",
            subCategory = "Dog Food",
            price = 340.0,
            originalMrp = 380.0,
            unit = "1.2 kg Bag",
            descriptionEn = "100% complete and balanced nutrition for active adult dogs.",
            descriptionHi = "कुत्तों के स्वास्थ्य और पोषण के लिए पेडिग्री।",
            stock = 12,
            rating = 4.8f,
            reviewCount = 65,
            tags = "pedigree, dog food, pet, animal"
        ),

        // Packaged Foods
        Product(
            id = "pf_01",
            nameEn = "Maggi 2-Minute Instant Masala Noodles (Pack of 4)",
            nameHi = "मैगी 2-मिनट मसाला नूडल्स",
            categoryId = "packaged_foods",
            subCategory = "Noodles & Pasta",
            price = 56.0,
            originalMrp = 60.0,
            unit = "280 g (4 x 70g)",
            descriptionEn = "India's favorite instant noodles enriched with Iron and roasted spices.",
            descriptionHi = "भारत की मनपसंद 2 मिनट में बनने वाली स्वादिष्ट मैगी।",
            stock = 80,
            rating = 4.9f,
            reviewCount = 850,
            isBestSeller = true,
            isFrequentlyBought = true,
            isTrending = true,
            tags = "maggi, noodles, instant food, snacks"
        ),
        Product(
            id = "pf_02",
            nameEn = "Kissan Fresh Tomato Ketchup Bottle",
            nameHi = "किसान फ्रेश टोमैटो केचप",
            categoryId = "packaged_foods",
            subCategory = "Sauces & Spreads",
            price = 120.0,
            originalMrp = 145.0,
            unit = "950 g Squeezy Bottle",
            descriptionEn = "Made from 100% real ripe sun-ripened tomatoes for luscious taste.",
            descriptionHi = "स्वादिष्ट और गाढ़ा किसान टोमैटो केचप।",
            stock = 30,
            rating = 4.8f,
            reviewCount = 140,
            tags = "ketchup, kissan, sauce, tomato sauce"
        )
    )

    val coupons = listOf(
        Coupon(
            code = "BALAJI50",
            discountAmount = 50.0,
            minOrderValue = 199.0,
            description = "Flat ₹50 OFF on orders above ₹199 at Balaji Mart"
        ),
        Coupon(
            code = "WELCOME20",
            discountPercentage = 20,
            minOrderValue = 149.0,
            description = "20% OFF on your grocery order (Max ₹100)"
        ),
        Coupon(
            code = "FREEDEL",
            discountAmount = 25.0,
            minOrderValue = 99.0,
            description = "Free express delivery on all orders above ₹99"
        )
    )

    val deliveryPartners = listOf(
        DeliveryPartner(
            id = "dp_01",
            name = "Ramesh Kumar",
            phone = "+91 98765 43210",
            vehicleNumber = "DL 04 EF 2021",
            status = "AVAILABLE",
            rating = 4.9f
        ),
        DeliveryPartner(
            id = "dp_02",
            name = "Suresh Verma",
            phone = "+91 98112 34567",
            vehicleNumber = "DL 07 GH 4590",
            status = "AVAILABLE",
            rating = 4.8f
        ),
        DeliveryPartner(
            id = "dp_03",
            name = "Anil Sharma",
            phone = "+91 99554 11223",
            vehicleNumber = "HR 26 AB 8812",
            status = "ON_DUTY",
            rating = 5.0f
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            title = "Welcome to Balaji Mart! 🙏",
            message = "Get lightning fast 10-minute grocery delivery within 2 KM radius. Use code BALAJI50 for ₹50 off!",
            type = "OFFER",
            isRead = false
        ),
        NotificationItem(
            title = "Fresh Stock Alert 🥦",
            message = "Fresh fruits and farm vegetables arrived this morning. Order now before stock runs out!",
            type = "ALERT",
            isRead = false
        )
    )
}
