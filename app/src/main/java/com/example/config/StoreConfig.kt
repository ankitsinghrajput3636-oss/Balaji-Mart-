package com.example.config

/**
 * =========================================================================
 * BALAJI MART - CENTRAL STORE & CREDENTIAL CONFIGURATION REPOSITORY
 * =========================================================================
 * This dedicated file organizes all operational constants, store identity,
 * payment gateways, administrative access, and placeholders for credentials
 * to be provided by the store owner for production activation.
 *
 * NOTE: Sensitive server secrets (such as Razorpay Secret Key, Database admin keys)
 * MUST remain exclusively on the secure backend/Cloud Functions, never in the APK.
 */
object StoreConfig {

    // -------------------------------------------------------------------------
    // 1. RAZORPAY PAYMENT GATEWAY CONFIGURATION
    // -------------------------------------------------------------------------
    object Razorpay {
        /**
         * Razorpay Key ID (Public Key):
         * Provided from the Razorpay Dashboard (Settings -> API Keys).
         * Can be provided via BuildConfig / AI Studio Secrets: RAZORPAY_KEY_ID
         */
        const val KEY_ID: String = "" // Placeholder: Provide rzp_live_xxx or rzp_test_xxx

        /**
         * Server-Side Payment Verification Note:
         * Razorpay Key Secret is NEVER stored in this Android client.
         * The client receives razorpay_payment_id, razorpay_order_id, razorpay_signature
         * and posts them to your backend endpoint for HMAC-SHA256 signature verification.
         */
        const val BACKEND_VERIFICATION_ENDPOINT: String = "https://api.balajimart.com/verify-payment"

        /**
         * Checks whether real Razorpay credentials have been injected.
         * When false, online payment falls back cleanly to Direct UPI & Cash on Delivery (COD).
         */
        val isConfigured: Boolean
            get() = KEY_ID.isNotBlank() && !KEY_ID.startsWith("YOUR_")
    }

    // -------------------------------------------------------------------------
    // 2. STORE OWNER & ADMIN AUTHENTICATION CONFIGURATION
    // -------------------------------------------------------------------------
    object AdminAuth {
        /**
         * Reserved Store Admin ID and Email for role-based authorization.
         * In production, this maps to Firebase Auth UID with custom admin claims.
         */
        const val STORE_ADMIN_EMAIL: String = "owner@balajimart.com"
        const val STORE_ADMIN_PHONE: String = "9876543210"

        /**
         * Default Store Owner Dashboard Passcode (Configurable).
         * Used to gate physical in-store access and owner controls.
         */
        const val DEFAULT_ADMIN_PASSCODE: String = "1234"

        fun verifyPasscode(enteredPasscode: String): Boolean {
            return enteredPasscode.trim() == DEFAULT_ADMIN_PASSCODE
        }
    }

    // -------------------------------------------------------------------------
    // 3. STORE IDENTITY & CONTACT CONFIGURATION
    // -------------------------------------------------------------------------
    object Identity {
        const val STORE_NAME_EN: String = "Balaji Mart"
        const val STORE_NAME_HI: String = "श्री बालाजी जनरल स्टोर"
        const val TAGLINE_EN: String = "10 Mins Superfast Grocery Delivery"
        const val TAGLINE_HI: String = "10 मिनट में ताज़ा किराना आपके घर"

        /**
         * Store Address placeholder (To be finalized by store owner).
         */
        const val STORE_ADDRESS: String = "Balaji General Store, Shop #4, Main Market, Balaji Chowk"

        /**
         * Store GPS Coordinates:
         * Used to accurately compute distance from customer delivery locations.
         * Default center: (28.6139, 77.2090) or Bangalore / Delhi NCR location.
         */
        const val STORE_LATITUDE: Double = 28.6139
        const val STORE_LONGITUDE: Double = 77.2090

        /**
         * Maximum Delivery Radius (strictly enforced 2.0 KM promise).
         */
        const val DELIVERY_RADIUS_KM: Double = 2.0

        /**
         * Operating Hours
         */
        const val OPENING_TIME: String = "06:00 AM"
        const val CLOSING_TIME: String = "11:00 PM"

        /**
         * Merchant UPI VPA for Direct NPCI UPI Intent & QR payments
         */
        const val UPI_MERCHANT_VPA: String = "balajimart@upi"
        const val UPI_MERCHANT_NAME: String = "Balaji Mart Grocery"

        /**
         * Customer Support Phone & WhatsApp
         */
        const val SUPPORT_PHONE: String = "+91 98765 43210"
        const val SUPPORT_EMAIL: String = "support@balajimart.com"
    }

    // -------------------------------------------------------------------------
    // 4. GOOGLE MAPS API CONFIGURATION
    // -------------------------------------------------------------------------
    object Maps {
        /**
         * Google Maps SDK / Directions API Key placeholder.
         * Can be provided via BuildConfig / AI Studio Secrets: MAPS_API_KEY
         */
        const val MAPS_API_KEY: String = "" // Placeholder: Provide AIzaSy...
    }

    // -------------------------------------------------------------------------
    // 5. ORDER & COMMERCE THRESHOLDS
    // -------------------------------------------------------------------------
    object Commerce {
        const val MIN_ORDER_AMOUNT: Double = 99.0
        const val FREE_DELIVERY_THRESHOLD: Double = 199.0
        const val STANDARD_DELIVERY_FEE: Double = 25.0
        const val PACKAGING_FEE: Double = 5.0
    }
}
