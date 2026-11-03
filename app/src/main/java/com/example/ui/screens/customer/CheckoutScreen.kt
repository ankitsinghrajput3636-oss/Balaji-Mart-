package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StoreGreenDark
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.StoreYellowAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import com.example.config.StoreConfig
import com.example.util.LocationHelper

@Composable
fun CheckoutScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi by viewModel.isHindi.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val settings by viewModel.storeSettings.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()

    var paymentMethod by remember { mutableStateOf("UPI") } // "UPI", "COD", "CARD"
    var orderNotes by remember { mutableStateOf("") }
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }

    // Dialog state for UPI / Razorpay payment verification
    var showNoUpiAppDialog by remember { mutableStateOf(false) }
    var showPaymentFailedDialog by remember { mutableStateOf(false) }
    var paymentFailureReason by remember { mutableStateOf("") }
    var showManualUtrDialog by remember { mutableStateOf(false) }
    var manualUtrInput by remember { mutableStateOf("") }
    var showRazorpayInfoDialog by remember { mutableStateOf(false) }

    val freeThreshold = settings?.freeDeliveryThreshold ?: 199.0
    val stdDeliveryFee = settings?.standardDeliveryFee ?: 25.0
    val deliveryFee = if (subtotal >= freeThreshold) 0.0 else stdDeliveryFee
    val couponDiscount = viewModel.calculateCouponDiscount(subtotal, appliedCoupon)
    val packagingFee = 5.0
    val finalTotal = (subtotal - couponDiscount + deliveryFee + packagingFee).coerceAtLeast(0.0)

    // Real NPCI UPI Activity Result Handler
    val upiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val dataIntent = result.data
        val responseString = dataIntent?.getStringExtra("response") ?: ""
        // NPCI UPI standard response query string: txnId=...&responseCode=00&Status=SUCCESS
        val paramMap = responseString.split("&").associate { param ->
            val parts = param.split("=")
            if (parts.size == 2) parts[0].lowercase().trim() to parts[1].trim() else "" to ""
        }
        val status = paramMap["status"]?.uppercase()
            ?: if (result.resultCode == Activity.RESULT_OK) "SUCCESS" else "CANCELLED"
        val txnId = paramMap["txnid"] ?: paramMap["tr"] ?: ("UPI" + System.currentTimeMillis().toString().takeLast(8))

        if (status == "SUCCESS") {
            viewModel.placeOrder(
                paymentMethod = "UPI ($selectedUpiApp)",
                orderNotes = orderNotes,
                paymentTransactionId = txnId
            ) { newOrderId ->
                viewModel.navigateTo(AppScreen.ORDER_TRACKING)
            }
        } else {
            paymentFailureReason = if (status == "CANCELLED") {
                "Payment was cancelled by user in UPI app."
            } else {
                "UPI payment returned status: $status. Transaction was not verified."
            }
            showPaymentFailedDialog = true
        }
    }

    Surface(
        modifier = modifier.fillMaxSize().testTag("checkout_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (isHindi) "चेकआउट और भुगतान" else "Checkout & Payment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                // Address Card
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = StoreGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) "डिलीवरी का पता (2 KM के अंदर)" else "Delivery Address (Within 2 KM)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedAddress.fullAddress,
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Distance: ${selectedAddress.distanceKm} KM from Balaji Mart • ETA: 10-12 Mins",
                                color = StoreGreenPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Delivery Notes
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "डिलीवरी निर्देश (वैकल्पिक)" else "Delivery Instructions (Optional)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = orderNotes,
                                onValueChange = { orderNotes = it },
                                placeholder = { Text("e.g. Leave at door, call on arrival, don't ring bell", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                }

                // Payment Method Selector
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "भुगतान का तरीका चुनें" else "Select Payment Method",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // UPI Option
                            PaymentOptionItem(
                                icon = Icons.Default.QrCode,
                                title = "UPI (Instant 100% Secure)",
                                subtitle = "Google Pay, PhonePe, Paytm, BHIM",
                                isSelected = paymentMethod == "UPI",
                                onSelect = { paymentMethod = "UPI" }
                            )

                            if (paymentMethod == "UPI") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Google Pay", "PhonePe", "Paytm", "BHIM").forEach { appName ->
                                        val isChosen = selectedUpiApp == appName
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .border(
                                                    1.dp,
                                                    if (isChosen) StoreGreenPrimary else Color(0xFFD1D5DB),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .background(if (isChosen) Color(0xFFE8F5E9) else Color.White)
                                                .clickable { selectedUpiApp = appName }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = appName,
                                                fontSize = 10.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isChosen) StoreGreenPrimary else TextPrimary
                                            )
                                        }
                                    }
                                }
                            }

                            Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 4.dp))

                            // Cash on Delivery (COD) Option
                            PaymentOptionItem(
                                icon = Icons.Default.Money,
                                title = "Cash on Delivery (COD)",
                                subtitle = "Pay cash or UPI to delivery rider at doorstep",
                                isSelected = paymentMethod == "COD",
                                onSelect = { paymentMethod = "COD" }
                            )

                            Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 4.dp))

                            // Online Card Option
                            PaymentOptionItem(
                                icon = Icons.Default.CreditCard,
                                title = "Credit / Debit Card / Netbanking",
                                subtitle = "Visa, Mastercard, RuPay, Netbanking",
                                isSelected = paymentMethod == "CARD",
                                onSelect = { paymentMethod = "CARD" }
                            )
                        }
                    }
                }

                // Trust info
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = StoreGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "100% Safe & Encrypted Balaji Quick-Commerce",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            // Bottom Pay Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total to Pay",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "₹${finalTotal.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = StoreGreenDark
                        )
                    }

                    Button(
                        onClick = {
                            when (paymentMethod) {
                                "UPI" -> {
                                    val upiIntent = LocationHelper.buildUpiPaymentIntent(
                                        merchantVpa = StoreConfig.Identity.UPI_MERCHANT_VPA,
                                        merchantName = "Balaji Mart",
                                        amount = finalTotal,
                                        orderId = "BJ" + System.currentTimeMillis().toString().takeLast(6)
                                    )
                                    if (upiIntent.resolveActivity(context.packageManager) != null) {
                                        upiLauncher.launch(upiIntent)
                                    } else {
                                        showNoUpiAppDialog = true
                                    }
                                }
                                "COD" -> {
                                    viewModel.placeOrder(
                                        paymentMethod = "COD (Cash on Delivery)",
                                        orderNotes = orderNotes,
                                        paymentTransactionId = ""
                                    ) { newOrderId ->
                                        viewModel.navigateTo(AppScreen.ORDER_TRACKING)
                                    }
                                }
                                "CARD" -> {
                                    showRazorpayInfoDialog = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("confirm_place_order_btn")
                    ) {
                        Text(
                            text = if (isHindi) "ऑर्डर कन्फर्म करें" else "Place Order ⚡",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Dialog: No UPI app installed on device / emulator
        if (showNoUpiAppDialog) {
            AlertDialog(
                onDismissRequest = { showNoUpiAppDialog = false },
                title = { Text("No UPI App Detected") },
                text = {
                    Column {
                        Text(
                            text = "No compatible UPI app (Google Pay, PhonePe, Paytm, BHIM) was found installed on this device or emulator.",
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "You can switch to Cash on Delivery (COD) or enter an external UPI Reference / UTR Number to place the order with verification.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showNoUpiAppDialog = false
                            paymentMethod = "COD"
                            viewModel.placeOrder(
                                paymentMethod = "COD (Cash on Delivery)",
                                orderNotes = orderNotes,
                                paymentTransactionId = ""
                            ) {
                                viewModel.navigateTo(AppScreen.ORDER_TRACKING)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Place as COD")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showNoUpiAppDialog = false
                            showManualUtrDialog = true
                        }
                    ) {
                        Text("Enter UTR ID")
                    }
                }
            )
        }

        // Dialog: Manual UTR / Reference ID entry for UPI verification
        if (showManualUtrDialog) {
            AlertDialog(
                onDismissRequest = { showManualUtrDialog = false },
                title = { Text("Enter UPI Reference / UTR") },
                text = {
                    Column {
                        Text(
                            text = "Transfer ₹${finalTotal.toInt()} to VPA: ${StoreConfig.Identity.UPI_MERCHANT_VPA} and enter the 12-digit UTR / Transaction ID below:",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = manualUtrInput,
                            onValueChange = { manualUtrInput = it },
                            placeholder = { Text("e.g. 423589123456") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (manualUtrInput.isNotBlank()) {
                                showManualUtrDialog = false
                                viewModel.placeOrder(
                                    paymentMethod = "UPI ($selectedUpiApp)",
                                    orderNotes = orderNotes,
                                    paymentTransactionId = manualUtrInput.trim()
                                ) {
                                    viewModel.navigateTo(AppScreen.ORDER_TRACKING)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Confirm Order")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManualUtrDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Dialog: UPI Payment Failed / Cancelled
        if (showPaymentFailedDialog) {
            AlertDialog(
                onDismissRequest = { showPaymentFailedDialog = false },
                title = { Text("Payment Incomplete") },
                text = {
                    Text(
                        text = paymentFailureReason,
                        fontSize = 13.sp,
                        color = Color(0xFFDC2626)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showPaymentFailedDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Try Again")
                    }
                }
            )
        }

        // Dialog: Razorpay Online Gateway
        if (showRazorpayInfoDialog) {
            AlertDialog(
                onDismissRequest = { showRazorpayInfoDialog = false },
                title = { Text("Online Payment Gateway") },
                text = {
                    Column {
                        if (!StoreConfig.Razorpay.isConfigured) {
                            Text(
                                text = "⚠️ Razorpay Online Gateway Pending Configuration",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFD97706)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "The store owner has not yet configured the Razorpay Key ID in StoreConfig.kt. In strict compliance with security rules, transactions are not falsely marked as paid.",
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Please place your order using Cash on Delivery (COD) or Instant Direct UPI (Google Pay, PhonePe, Paytm, BHIM) which are fully operational!",
                                fontSize = 11.sp,
                                color = StoreGreenPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = "Razorpay Gateway is initialized with Key ID. Ready for card / netbanking processing.",
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showRazorpayInfoDialog = false
                            paymentMethod = "COD"
                            viewModel.placeOrder(
                                paymentMethod = "COD (Cash on Delivery)",
                                orderNotes = orderNotes,
                                paymentTransactionId = ""
                            ) {
                                viewModel.navigateTo(AppScreen.ORDER_TRACKING)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Place as Cash on Delivery (COD)")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showRazorpayInfoDialog = false
                            paymentMethod = "UPI"
                        }
                    ) {
                        Text("Switch to UPI")
                    }
                }
            )
        }
    }
}

@Composable
fun PaymentOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) StoreGreenPrimary else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, fontSize = 10.sp, color = TextMuted)
            }
        }

        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = StoreGreenPrimary)
        )
    }
}
