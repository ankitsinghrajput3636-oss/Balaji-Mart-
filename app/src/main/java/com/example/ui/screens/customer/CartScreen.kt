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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusGreen
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
fun CartScreen(
    viewModel: GroceryViewModel,
    onSelectAddressClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val totalMrp by viewModel.totalMrpValue.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponError by viewModel.couponError.collectAsState()
    val allCoupons by viewModel.allCoupons.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val settings by viewModel.storeSettings.collectAsState()
    val isWithinRadius = viewModel.isAddressWithinRadius()

    var couponInput by remember { mutableStateOf("") }

    val freeThreshold = settings?.freeDeliveryThreshold ?: 199.0
    val stdDeliveryFee = settings?.standardDeliveryFee ?: 25.0
    val deliveryFee = if (subtotal >= freeThreshold) 0.0 else stdDeliveryFee
    val couponDiscount = viewModel.calculateCouponDiscount(subtotal, appliedCoupon)
    val packagingFee = 5.0
    val finalTotal = (subtotal - couponDiscount + deliveryFee + packagingFee).coerceAtLeast(0.0)

    if (cartProducts.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("empty_cart_view"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = StoreGreenPrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isHindi) "आपकी कार्ट खाली है" else "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isHindi) "दैनिक ताजी सब्जियां, फल और किराना ऑर्डर करें!" else "Add fresh vegetables, milk, atta & groceries to order in 10 mins!",
                    color = TextMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                    modifier = Modifier.testTag("start_shopping_btn")
                ) {
                    Text(if (isHindi) "खरीदारी शुरू करें" else "Browse Products", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Box(modifier = modifier.fillMaxSize().testTag("cart_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .padding(horizontal = 14.dp)
        ) {
            // Cart Header with Item Count
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "आपकी शॉपिंग कार्ट (${cartProducts.sumOf { it.second }})" else "Shopping Cart (${cartProducts.sumOf { it.second }})",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = if (isHindi) "खाली करें" else "Clear All",
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.clearCart() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Delivery Address Card with 2 KM radius alert
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAddressClick() },
                    colors = CardDefaults.cardColors(containerColor = if (isWithinRadius) Color.White else Color(0xFFFEF2F2)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isWithinRadius) StoreGreenPrimary else StatusRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Delivery to: ${selectedAddress.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = "Change",
                                color = StoreGreenPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = selectedAddress.fullAddress,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Radius Status
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isWithinRadius) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (isWithinRadius) StatusGreen else StatusRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isWithinRadius) {
                                    "Store distance: ${selectedAddress.distanceKm} KM (Deliverable in 10-12 mins)"
                                } else {
                                    "Distance: ${selectedAddress.distanceKm} KM. Restricted to 2 KM radius!"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isWithinRadius) StatusGreen else StatusRed
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Free Delivery progress notification
            item {
                val neededForFree = (freeThreshold - subtotal).coerceAtLeast(0.0)
                if (neededForFree > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(StoreYellowContainer)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "₹${neededForFree.toInt()} का और सामान जोड़ें और पाएं मुफ़्त डिलीवरी!" else "Add items worth ₹${neededForFree.toInt()} more for FREE Delivery!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Cart Items List
            items(cartProducts) { (product, qty) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF3F5F4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (product.categoryId) {
                                        "fruits_veg" -> "🥦"
                                        "atta_rice_dal" -> "🌾"
                                        "dairy_bakery" -> "🥛"
                                        "oil_ghee" -> "🫒"
                                        "spices_masala" -> "🌶️"
                                        else -> "🥫"
                                    },
                                    fontSize = 22.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) product.nameHi else product.nameEn,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = product.unit,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹${product.price.toInt()}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    if (product.originalMrp > product.price) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "₹${product.originalMrp.toInt()}",
                                            fontSize = 11.sp,
                                            color = TextMuted,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }
                                }
                            }
                        }

                        // Quantity +/- Controller
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StoreGreenPrimary)
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { viewModel.updateCartQuantity(product.id, qty - 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (qty == 1) Icons.Default.Delete else Icons.Default.Remove,
                                    contentDescription = "Minus",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "$qty",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { viewModel.updateCartQuantity(product.id, qty + 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Plus",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Coupon Code Card
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = StoreGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "कूपन और ऑफर्स" else "Coupons & Offers",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (appliedCoupon != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Coupon ${appliedCoupon!!.code} Applied! 🎉",
                                        fontWeight = FontWeight.Bold,
                                        color = StoreGreenDark,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = appliedCoupon!!.description,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = "Remove",
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { viewModel.removeCoupon() }
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponInput,
                                    onValueChange = { couponInput = it.uppercase() },
                                    placeholder = { Text("Enter coupon (e.g. BALAJI50)", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (couponInput.isNotBlank()) {
                                            viewModel.applyCoupon(couponInput)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                                ) {
                                    Text("Apply", fontSize = 12.sp)
                                }
                            }
                            if (couponError != null) {
                                Text(
                                    text = couponError!!,
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // Quick apply suggestion chips
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                allCoupons.take(2).forEach { c ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .border(1.dp, StoreGreenPrimary, RoundedCornerShape(4.dp))
                                            .clickable {
                                                couponInput = c.code
                                                viewModel.applyCoupon(c.code)
                                            }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = c.code, fontSize = 10.sp, color = StoreGreenPrimary, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bill Breakdown Details
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isHindi) "बिल विवरण" else "Bill Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        BillRow(title = "Item Total (Subtotal)", value = "₹${subtotal.toInt()}")
                        if (couponDiscount > 0) {
                            BillRow(title = "Coupon Discount", value = "-₹${couponDiscount.toInt()}", valueColor = StatusGreen)
                        }
                        BillRow(
                            title = "Delivery Fee",
                            value = if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                            valueColor = if (deliveryFee == 0.0) StatusGreen else TextPrimary
                        )
                        BillRow(title = "Handling & Packing Charge", value = "₹${packagingFee.toInt()}")

                        Divider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isHindi) "कुल राशि" else "Grand Total",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Inclusive of all taxes",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = "₹${finalTotal.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = StoreGreenDark
                            )
                        }

                        if (totalSavings > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎉 You saved ₹${totalSavings.toInt()} on this order!",
                                    fontWeight = FontWeight.Bold,
                                    color = StoreGreenDark,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bottom Sticky Proceed to Checkout Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
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
                        text = "₹${finalTotal.toInt()}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = if (isWithinRadius) "⚡ 10 Mins Delivery" else "⚠️ Outside 2KM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isWithinRadius) StoreGreenPrimary else StatusRed
                    )
                }

                Button(
                    onClick = {
                        if (!isWithinRadius) {
                            viewModel.toastMessage.value = "⚠️ Please choose an address within the 2 KM delivery radius!"
                        } else {
                            viewModel.navigateTo(AppScreen.CHECKOUT)
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("proceed_to_checkout_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                    enabled = isWithinRadius
                ) {
                    Text(
                        text = if (isHindi) "चेकआउट करें >" else "Proceed to Pay >",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BillRow(
    title: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
