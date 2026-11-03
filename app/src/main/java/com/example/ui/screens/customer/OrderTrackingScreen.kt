package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusOrange
import com.example.ui.theme.StatusRed
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
fun OrderTrackingScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val trackingOrderId by viewModel.trackingOrderId.collectAsState()

    val currentOrder = allOrders.find { it.orderId == trackingOrderId } ?: allOrders.firstOrNull()

    if (currentOrder == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No active order found", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
                    Text("Go to Home")
                }
            }
        }
        return
    }

    Surface(
        modifier = modifier.fillMaxSize().testTag("order_tracking_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = if (isHindi) "ऑर्डर ट्रैकिंग" else "Order Tracking",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "#${currentOrder.orderId}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Status pill
                val (statusBg, statusFg) = when (currentOrder.orderStatus) {
                    "DELIVERED" -> StatusGreen to Color.White
                    "CANCELLED" -> StatusRed to Color.White
                    "OUT_FOR_DELIVERY" -> StoreYellowAccent to StoreGreenDark
                    else -> Color(0xFFE8F5E9) to StoreGreenDark
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = currentOrder.orderStatus.replace("_", " "),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = statusFg
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                // Estimated Delivery & ETA Card
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StoreGreenPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (currentOrder.orderStatus == "DELIVERED") {
                                    if (isHindi) "ऑर्डर सफलतापूर्वक डिलीवर हुआ! 🎉" else "Order Delivered Successfully! 🎉"
                                } else if (currentOrder.orderStatus == "CANCELLED") {
                                    if (isHindi) "ऑर्डर रद्द कर दिया गया है" else "Order has been Cancelled"
                                } else {
                                    if (isHindi) "डिलीवरी अनुमानित समय" else "Estimated Delivery Time"
                                },
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (currentOrder.orderStatus == "DELIVERED") "DELIVERED" else "~${currentOrder.estimatedDeliveryMinutes} MINUTES",
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = StoreYellowAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Balaji Mart • 2 KM Superfast Express",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Delivery Timeline
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isHindi) "ऑर्डर की स्थिति" else "Order Status Timeline",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val steps = listOf(
                                "PLACED" to ("Order Confirmed" to "Store received your order"),
                                "ACCEPTED" to ("Store Accepted" to "Balaji Store is preparing items"),
                                "PACKED" to ("Packed & Inspected" to "Items bagged and ready for dispatch"),
                                "OUT_FOR_DELIVERY" to ("Out for Delivery" to "Delivery partner on the way 🛵"),
                                "DELIVERED" to ("Delivered" to "Delivered to your doorstep")
                            )

                            val currentIdx = when (currentOrder.orderStatus) {
                                "PLACED" -> 0
                                "ACCEPTED" -> 1
                                "PACKED" -> 2
                                "OUT_FOR_DELIVERY" -> 3
                                "DELIVERED" -> 4
                                else -> -1
                            }

                            steps.forEachIndexed { idx, (statusKey, textPair) ->
                                val isDone = currentIdx >= idx && currentOrder.orderStatus != "CANCELLED"
                                val isCurrent = currentIdx == idx

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isDone) StoreGreenPrimary else Color(0xFFE5E7EB)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isDone) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.Gray)
                                                )
                                            }
                                        }

                                        if (idx < steps.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(28.dp)
                                                    .background(
                                                        if (currentIdx > idx) StoreGreenPrimary else Color(0xFFE5E7EB)
                                                    )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.padding(bottom = if (idx < steps.size - 1) 16.dp else 0.dp)) {
                                        Text(
                                            text = textPair.first,
                                            fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = if (isDone) TextPrimary else TextMuted
                                        )
                                        Text(
                                            text = textPair.second,
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Delivery Partner Card
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "डिलीवरी पार्टनर" else "Delivery Partner",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE8F5E9)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsBike,
                                            contentDescription = null,
                                            tint = StoreGreenPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = currentOrder.deliveryPartnerName ?: "Ramesh Kumar (Assigned)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = currentOrder.deliveryPartnerPhone ?: "+91 98765 43210 • Bike DL 04 EF 2021",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.toastMessage.value = "Calling ${currentOrder.deliveryPartnerName ?: "Rider"}..."
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call rider",
                                        tint = StoreGreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Order Items Summary
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "सामान सूची" else "Items in Order",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentOrder.itemsSummary,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Payment Mode: ${currentOrder.paymentMethod}", fontSize = 11.sp, color = TextSecondary)
                                Text(text = "Total: ₹${currentOrder.totalAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StoreGreenDark)
                            }
                            if (currentOrder.orderNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Note: ${currentOrder.orderNotes}", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }

                // Cancel Order button (if still PLACED or ACCEPTED)
                if (currentOrder.orderStatus in listOf("PLACED", "ACCEPTED")) {
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.updateOrderStatus(currentOrder.orderId, "CANCELLED")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isHindi) "ऑर्डर रद्द करें" else "Cancel Order")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
