package com.example.ui.screens.admin

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Category
import com.example.data.model.Coupon
import com.example.data.model.DeliveryPartner
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.StoreSettings
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
import java.util.UUID

enum class AdminTab {
    ORDERS,
    PRODUCTS,
    INVENTORY,
    PARTNERS,
    COUPONS,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val allOrders by viewModel.allOrders.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val deliveryPartners by viewModel.allDeliveryPartners.collectAsState()
    val coupons by viewModel.allCoupons.collectAsState()
    val storeSettings by viewModel.storeSettings.collectAsState()

    var activeTab by remember { mutableStateOf(AdminTab.ORDERS) }

    // Dialog state for adding/editing product
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var pNameEn by remember { mutableStateOf("") }
    var pNameHi by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pMrp by remember { mutableStateOf("") }
    var pUnit by remember { mutableStateOf("") }
    var pStock by remember { mutableStateOf("50") }
    var pCatId by remember { mutableStateOf(allCategories.firstOrNull()?.id ?: "fruits_veg") }
    var pDescEn by remember { mutableStateOf("") }

    // Dialog for adding coupon
    var showCouponDialog by remember { mutableStateOf(false) }
    var cCode by remember { mutableStateOf("") }
    var cDiscount by remember { mutableStateOf("50") }
    var cMinOrder by remember { mutableStateOf("199") }
    var cDesc by remember { mutableStateOf("") }

    // Dialog for adding delivery partner
    var showPartnerDialog by remember { mutableStateOf(false) }
    var dpName by remember { mutableStateOf("") }
    var dpPhone by remember { mutableStateOf("") }
    var dpVehicle by remember { mutableStateOf("") }

    val totalRevenue = allOrders.filter { it.orderStatus != "CANCELLED" }.sumOf { it.totalAmount }

    Surface(
        modifier = modifier.fillMaxSize().testTag("admin_dashboard_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StoreGreenDark)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Exit Admin", tint = Color.White)
                    }
                    Column {
                        Text(
                            text = "Balaji Mart Admin Panel",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Store Operations & Live Orders",
                            color = StoreYellowAccent,
                            fontSize = 11.sp
                        )
                    }
                }

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = StoreYellowAccent),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Customer App",
                        color = StoreGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            // Metric KPI Tiles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    title = "Total Orders",
                    value = "${allOrders.size}",
                    subtitle = "${allOrders.count { it.orderStatus == "PLACED" }} pending",
                    color = StoreGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Revenue",
                    value = "₹${totalRevenue.toInt()}",
                    subtitle = "All time sales",
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Products",
                    value = "${allProducts.size}",
                    subtitle = "${lowStockProducts.size} low stock",
                    color = if (lowStockProducts.isNotEmpty()) StatusRed else Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )
            }

            // Admin Navigation Tabs
            val tabs = listOf(
                AdminTab.ORDERS to "Orders (${allOrders.count { it.orderStatus != "DELIVERED" && it.orderStatus != "CANCELLED" }})",
                AdminTab.PRODUCTS to "Products (${allProducts.size})",
                AdminTab.INVENTORY to "Low Stock (${lowStockProducts.size})",
                AdminTab.PARTNERS to "Delivery Riders (${deliveryPartners.size})",
                AdminTab.COUPONS to "Coupons (${coupons.size})",
                AdminTab.SETTINGS to "Store Settings"
            )

            ScrollableTabRow(
                selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.coerceAtLeast(0),
                edgePadding = 12.dp,
                containerColor = Color.White,
                contentColor = StoreGreenPrimary,
                divider = {}
            ) {
                tabs.forEach { (tab, title) ->
                    val isSelected = activeTab == tab
                    Tab(
                        selected = isSelected,
                        onClick = { activeTab = tab },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StoreGreenPrimary else TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    AdminTab.ORDERS -> AdminOrdersTab(viewModel, allOrders, deliveryPartners)
                    AdminTab.PRODUCTS -> AdminProductsTab(
                        viewModel = viewModel,
                        products = allProducts,
                        onAddClick = {
                            editingProduct = null
                            pNameEn = ""
                            pNameHi = ""
                            pPrice = ""
                            pMrp = ""
                            pUnit = "1 kg"
                            pStock = "50"
                            pDescEn = ""
                            showProductDialog = true
                        },
                        onEditClick = { prod ->
                            editingProduct = prod
                            pNameEn = prod.nameEn
                            pNameHi = prod.nameHi
                            pPrice = prod.price.toInt().toString()
                            pMrp = prod.originalMrp.toInt().toString()
                            pUnit = prod.unit
                            pStock = prod.stock.toString()
                            pCatId = prod.categoryId
                            pDescEn = prod.descriptionEn
                            showProductDialog = true
                        }
                    )
                    AdminTab.INVENTORY -> AdminInventoryTab(viewModel, lowStockProducts)
                    AdminTab.PARTNERS -> AdminPartnersTab(
                        viewModel = viewModel,
                        partners = deliveryPartners,
                        onAddClick = { showPartnerDialog = true }
                    )
                    AdminTab.COUPONS -> AdminCouponsTab(
                        viewModel = viewModel,
                        coupons = coupons,
                        onAddClick = { showCouponDialog = true }
                    )
                    AdminTab.SETTINGS -> AdminSettingsTab(viewModel, storeSettings ?: StoreSettings())
                }
            }
        }

        // Add/Edit Product Dialog
        if (showProductDialog) {
            AlertDialog(
                onDismissRequest = { showProductDialog = false },
                title = { Text(if (editingProduct == null) "Add New Product" else "Edit Product") },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            OutlinedTextField(
                                value = pNameEn,
                                onValueChange = { pNameEn = it },
                                label = { Text("Product Name (English)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = pNameHi,
                                onValueChange = { pNameHi = it },
                                label = { Text("उत्पाद का नाम (हिंदी)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = pPrice,
                                    onValueChange = { pPrice = it },
                                    label = { Text("Price (₹)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = pMrp,
                                    onValueChange = { pMrp = it },
                                    label = { Text("MRP (₹)") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = pUnit,
                                    onValueChange = { pUnit = it },
                                    label = { Text("Unit (1 kg, 500g)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = pStock,
                                    onValueChange = { pStock = it },
                                    label = { Text("Stock Qty") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = pDescEn,
                                onValueChange = { pDescEn = it },
                                label = { Text("Description") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val priceVal = pPrice.toDoubleOrNull() ?: 50.0
                            val mrpVal = pMrp.toDoubleOrNull() ?: priceVal
                            val stockVal = pStock.toIntOrNull() ?: 30

                            if (editingProduct != null) {
                                viewModel.updateProduct(
                                    editingProduct!!.copy(
                                        nameEn = pNameEn,
                                        nameHi = if (pNameHi.isNotBlank()) pNameHi else pNameEn,
                                        price = priceVal,
                                        originalMrp = mrpVal,
                                        unit = pUnit,
                                        stock = stockVal,
                                        descriptionEn = pDescEn
                                    )
                                )
                            } else {
                                viewModel.addProduct(
                                    Product(
                                        id = "prod_" + UUID.randomUUID().toString().take(6),
                                        nameEn = pNameEn,
                                        nameHi = if (pNameHi.isNotBlank()) pNameHi else pNameEn,
                                        categoryId = pCatId,
                                        subCategory = "General Grocery",
                                        price = priceVal,
                                        originalMrp = mrpVal,
                                        unit = pUnit.ifBlank { "1 piece" },
                                        descriptionEn = pDescEn.ifBlank { "Fresh grocery item from Balaji Store" },
                                        descriptionHi = "ताजा किराना उत्पाद",
                                        stock = stockVal
                                    )
                                )
                            }
                            showProductDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text(if (editingProduct == null) "Create" else "Save Changes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showProductDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Add Coupon Dialog
        if (showCouponDialog) {
            AlertDialog(
                onDismissRequest = { showCouponDialog = false },
                title = { Text("Create Discount Coupon") },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = cCode,
                            onValueChange = { cCode = it.uppercase() },
                            label = { Text("Coupon Code (e.g. FESTIVE50)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = cDiscount,
                            onValueChange = { cDiscount = it },
                            label = { Text("Flat Discount (₹)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = cMinOrder,
                            onValueChange = { cMinOrder = it },
                            label = { Text("Min Order Value (₹)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = cDesc,
                            onValueChange = { cDesc = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (cCode.isNotBlank()) {
                                viewModel.addCoupon(
                                    Coupon(
                                        code = cCode,
                                        discountAmount = cDiscount.toDoubleOrNull() ?: 50.0,
                                        minOrderValue = cMinOrder.toDoubleOrNull() ?: 199.0,
                                        description = cDesc.ifBlank { "Save on your grocery order!" }
                                    )
                                )
                                showCouponDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Add Coupon")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCouponDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Add Delivery Partner Dialog
        if (showPartnerDialog) {
            AlertDialog(
                onDismissRequest = { showPartnerDialog = false },
                title = { Text("Register Delivery Partner") },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = dpName,
                            onValueChange = { dpName = it },
                            label = { Text("Partner Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dpPhone,
                            onValueChange = { dpPhone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dpVehicle,
                            onValueChange = { dpVehicle = it },
                            label = { Text("Vehicle (e.g. DL 04 EF 2021)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (dpName.isNotBlank() && dpPhone.isNotBlank()) {
                                viewModel.addDeliveryPartner(
                                    DeliveryPartner(
                                        id = "dp_" + UUID.randomUUID().toString().take(6),
                                        name = dpName,
                                        phone = dpPhone,
                                        vehicleNumber = dpVehicle.ifBlank { "Bike" },
                                        status = "AVAILABLE"
                                    )
                                )
                                showPartnerDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("Register Partner")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPartnerDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 11.sp, color = TextMuted)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = subtitle, fontSize = 9.sp, color = TextSecondary)
        }
    }
}

@Composable
fun AdminOrdersTab(
    viewModel: GroceryViewModel,
    orders: List<Order>,
    deliveryPartners: List<DeliveryPartner>
) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No orders received yet", color = TextMuted)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(orders) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "${order.customerName} • ${order.customerPhone}", fontSize = 11.sp, color = TextMuted)
                        }

                        val (bBg, bFg) = when (order.orderStatus) {
                            "DELIVERED" -> StatusGreen to Color.White
                            "CANCELLED" -> StatusRed to Color.White
                            "OUT_FOR_DELIVERY" -> StoreYellowAccent to StoreGreenDark
                            else -> Color(0xFFE8F5E9) to StoreGreenDark
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(bBg)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = order.orderStatus, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = bFg)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = order.itemsSummary, fontSize = 12.sp, color = TextPrimary)
                    Text(text = "📍 ${order.deliveryAddress}", fontSize = 11.sp, color = TextSecondary, maxLines = 1)
                    Text(text = "Amount: ₹${order.totalAmount.toInt()} (${order.paymentMethod})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StoreGreenDark)

                    Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 8.dp))

                    // Order Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        when (order.orderStatus) {
                            "PLACED" -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.orderId, "ACCEPTED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept Order", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.updateOrderStatus(order.orderId, "CANCELLED") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject", fontSize = 11.sp)
                                }
                            }
                            "ACCEPTED" -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.orderId, "PACKED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark as Packed 📦", fontSize = 11.sp)
                                }
                            }
                            "PACKED" -> {
                                val availableRider = deliveryPartners.firstOrNull { it.status == "AVAILABLE" }
                                Button(
                                    onClick = {
                                        if (availableRider != null) {
                                            viewModel.assignDeliveryPartner(order.orderId, availableRider)
                                        } else {
                                            viewModel.updateOrderStatus(order.orderId, "OUT_FOR_DELIVERY")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Dispatch Out for Delivery 🛵", fontSize = 11.sp)
                                }
                            }
                            "OUT_FOR_DELIVERY" -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.orderId, "DELIVERED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Delivered ✅", fontSize = 11.sp)
                                }
                            }
                            else -> {
                                Text(text = "Order closed: ${order.orderStatus}", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminProductsTab(
    viewModel: GroceryViewModel,
    products: List<Product>,
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Product Catalog (${products.size} items)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Button(
                        onClick = onAddClick,
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Product", fontSize = 11.sp)
                    }
                }
            }

            items(products) { prod ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = prod.nameEn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${prod.nameHi} • ${prod.unit}", fontSize = 11.sp, color = TextMuted)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "₹${prod.price.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = StoreGreenDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Stock: ${prod.stock}", fontSize = 11.sp, color = if (prod.stock <= 5) Color.Red else TextSecondary)
                            }
                        }

                        Row {
                            IconButton(onClick = { onEditClick(prod) }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = StoreGreenPrimary, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = { viewModel.deleteProduct(prod.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminInventoryTab(
    viewModel: GroceryViewModel,
    lowStockList: List<Product>
) {
    if (lowStockList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "✅ All Products Well Stocked!", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "No items under 10 stock threshold", color = TextMuted, fontSize = 12.sp)
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "⚠️ Low Stock Warning (${lowStockList.size} items)",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626),
                fontSize = 14.sp
            )
        }

        items(lowStockList) { prod ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = prod.nameEn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "Remaining Stock: ${prod.stock} units", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = { viewModel.updateProductStock(prod.id, prod.stock + 50) },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text("+50 Restock", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPartnersTab(
    viewModel: GroceryViewModel,
    partners: List<DeliveryPartner>,
    onAddClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Delivery Fleet (${partners.size} Riders)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Rider", fontSize = 11.sp)
                }
            }
        }

        items(partners) { partner ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = partner.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "${partner.phone} • ${partner.vehicleNumber}", fontSize = 11.sp, color = TextMuted)
                        Text(text = "Status: ${partner.status}", fontSize = 11.sp, color = if (partner.status == "AVAILABLE") StatusGreen else Color(0xFFD97706), fontWeight = FontWeight.SemiBold)
                    }

                    Switch(
                        checked = partner.status == "AVAILABLE",
                        onCheckedChange = { isAvailable ->
                            viewModel.updatePartnerStatus(partner.id, if (isAvailable) "AVAILABLE" else "OFF_DUTY")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminCouponsTab(
    viewModel: GroceryViewModel,
    coupons: List<Coupon>,
    onAddClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Promotional Coupons", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Coupon", fontSize = 11.sp)
                }
            }
        }

        items(coupons) { coupon ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = coupon.code, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = StoreGreenPrimary)
                        Text(text = coupon.description, fontSize = 11.sp, color = TextSecondary)
                        Text(text = "Min Order: ₹${coupon.minOrderValue.toInt()}", fontSize = 10.sp, color = TextMuted)
                    }

                    IconButton(onClick = { viewModel.deleteCoupon(coupon.code) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSettingsTab(
    viewModel: GroceryViewModel,
    settings: StoreSettings
) {
    var storeName by remember { mutableStateOf(settings.storeName) }
    var storeAddress by remember { mutableStateOf(settings.storeAddress) }
    var radiusKm by remember { mutableStateOf(settings.deliveryRadiusKm.toFloat()) }
    var minOrder by remember { mutableStateOf(settings.minOrderAmount.toInt().toString()) }
    var freeThreshold by remember { mutableStateOf(settings.freeDeliveryThreshold.toInt().toString()) }
    var deliveryFee by remember { mutableStateOf(settings.standardDeliveryFee.toInt().toString()) }
    var isStoreOpen by remember { mutableStateOf(settings.isStoreOpen) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "Store Operations & Delivery Radius", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Store Status", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = if (isStoreOpen) "Accepting 10-min orders" else "Store closed for orders", fontSize = 11.sp, color = TextMuted)
                        }
                        Switch(
                            checked = isStoreOpen,
                            onCheckedChange = { isStoreOpen = it }
                        )
                    }

                    Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 10.dp))

                    Text(text = "Delivery Radius: ${String.format("%.1f", radiusKm)} KM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "Customer orders outside this distance are restricted", fontSize = 10.sp, color = TextMuted)
                    Slider(
                        value = radiusKm,
                        onValueChange = { radiusKm = it },
                        valueRange = 1f..10f,
                        steps = 8
                    )

                    Divider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = { Text("Store Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = storeAddress,
                        onValueChange = { storeAddress = it },
                        label = { Text("Store Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = minOrder,
                            onValueChange = { minOrder = it },
                            label = { Text("Min Order (₹)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = freeThreshold,
                            onValueChange = { freeThreshold = it },
                            label = { Text("Free Deliv (₹)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryFee,
                        onValueChange = { deliveryFee = it },
                        label = { Text("Standard Delivery Fee (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.saveStoreSettings(
                                settings.copy(
                                    storeName = storeName,
                                    storeAddress = storeAddress,
                                    deliveryRadiusKm = radiusKm.toDouble(),
                                    minOrderAmount = minOrder.toDoubleOrNull() ?: 99.0,
                                    freeDeliveryThreshold = freeThreshold.toDoubleOrNull() ?: 199.0,
                                    standardDeliveryFee = deliveryFee.toDoubleOrNull() ?: 25.0,
                                    isStoreOpen = isStoreOpen
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Store Settings")
                    }
                }
            }
        }
    }
}
