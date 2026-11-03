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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.theme.StoreYellowContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel

@Composable
fun ProfileScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val settings by viewModel.storeSettings.collectAsState()

    var showOtpDialog by remember { mutableStateOf(false) }
    var inputPhone by remember { mutableStateOf(userPhone) }
    var inputOtp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize().testTag("profile_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "मेरी प्रोफाइल" else "My Account & Profile",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Profile Avatar & Details Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(StoreGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userName.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "+91 $userPhone",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = userEmail,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }

                            IconButton(
                                onClick = {
                                    showOtpDialog = true
                                    otpSent = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit phone",
                                    tint = StoreGreenPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Store Delivery Radius Info
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StoreYellowContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Balaji Mart Policy",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "Fastest 10-min delivery strictly within 2.0 KM radius. Free delivery on orders above ₹${settings?.freeDeliveryThreshold?.toInt() ?: 199}!",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Settings & Shortcuts Group
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column {
                            ProfileMenuItem(
                                icon = Icons.Default.ReceiptLong,
                                title = if (isHindi) "मेरे ऑर्डर" else "My Orders",
                                subtitle = "Active and past deliveries",
                                onClick = { viewModel.navigateTo(AppScreen.ORDERS) }
                            )
                            Divider(color = Color(0xFFF3F4F6))

                            ProfileMenuItem(
                                icon = Icons.Default.Favorite,
                                title = if (isHindi) "मेरी पसंदीदा वस्तुएं" else "My Wishlist",
                                subtitle = "Saved items for instant ordering",
                                onClick = { viewModel.navigateTo(AppScreen.WISHLIST) }
                            )
                            Divider(color = Color(0xFFF3F4F6))

                            ProfileMenuItem(
                                icon = Icons.Default.Language,
                                title = if (isHindi) "भाषा बदलें (हिंदी / English)" else "Change Language (English / हिंदी)",
                                subtitle = if (isHindi) "वर्तमान: हिंदी" else "Current: English",
                                onClick = { viewModel.toggleLanguage() }
                            )
                            Divider(color = Color(0xFFF3F4F6))

                            ProfileMenuItem(
                                icon = Icons.Default.Notifications,
                                title = if (isHindi) "सूचनाएं और अलर्ट" else "Notifications & Alerts",
                                subtitle = "Order status & discount notifications",
                                onClick = { viewModel.navigateTo(AppScreen.NOTIFICATIONS) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Store Owner Admin Panel Entry
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.STORE_LOGIN) }
                            .testTag("admin_panel_entry_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StoreGreenPrimary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(StoreGreenPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = StoreYellowAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isHindi) "दुकानदार एडमिन पैनल" else "Store Owner Admin Dashboard",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = StoreGreenDark
                                    )
                                    Text(
                                        text = "Manage inventory, orders, prices & delivery",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = StoreGreenPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // Phone Number OTP Dialog
        if (showOtpDialog) {
            val generatedOtp by viewModel.generatedOtp.collectAsState()
            AlertDialog(
                onDismissRequest = { showOtpDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = StoreGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (!otpSent) "Mobile OTP Login" else "Verify OTP")
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (!otpSent) {
                            Text(
                                text = "Enter 10-digit mobile number for instant OTP authentication:",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = inputPhone,
                                onValueChange = { if (it.length <= 10) inputPhone = it },
                                label = { Text("Phone Number") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = "SMS OTP code sent to +91 $inputPhone: ${generatedOtp ?: "1234"}",
                                fontSize = 12.sp,
                                color = StoreGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = inputOtp,
                                onValueChange = { if (it.length <= 4) inputOtp = it },
                                label = { Text("Enter 4-Digit OTP") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TextButton(
                                onClick = { inputOtp = generatedOtp ?: "1234" },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Auto-fill OTP (${generatedOtp ?: "1234"})", fontSize = 11.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (!otpSent) {
                                if (inputPhone.length >= 10) {
                                    viewModel.requestPhoneOtp(inputPhone)
                                    otpSent = true
                                } else {
                                    viewModel.toastMessage.value = "Enter valid 10-digit phone number"
                                }
                            } else {
                                viewModel.verifyPhoneOtp(inputOtp) {
                                    showOtpDialog = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text(if (!otpSent) "Send OTP" else "Verify & Login")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOtpDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
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
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = StoreGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, fontSize = 11.sp, color = TextMuted)
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
