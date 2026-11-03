package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.BalajiDatabase
import com.example.data.repository.GroceryRepository
import com.example.ui.components.AddressSelectorSheet
import com.example.ui.components.ProductDetailSheet
import com.example.ui.components.TopStoreBar
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.customer.CartScreen
import com.example.ui.screens.customer.CategoriesScreen
import com.example.ui.screens.customer.CheckoutScreen
import com.example.ui.screens.customer.HomeScreen
import com.example.ui.screens.customer.NotificationsScreen
import com.example.ui.screens.customer.OrderTrackingScreen
import com.example.ui.screens.customer.OrdersHistoryScreen
import com.example.ui.screens.customer.ProfileScreen
import com.example.ui.screens.customer.SearchScreen
import com.example.ui.screens.customer.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StoreGreenDark
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.StoreYellowAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel
import com.example.ui.viewmodel.GroceryViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = BalajiDatabase.getInstance(applicationContext)
        val repository = GroceryRepository(database.groceryDao())
        viewModel = ViewModelProvider(
            this,
            GroceryViewModelFactory(repository)
        )[GroceryViewModel::class.java]

        setContent {
            MyApplicationTheme {
                BalajiGroceryApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BalajiGroceryApp(viewModel: GroceryViewModel) {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1200)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
        return
    }

    val currentScreen by viewModel.currentScreen.collectAsState()
    val isHindi by viewModel.isHindi.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val totalCartItems = cartProducts.sumOf { it.second }

    var showAddressSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    // Hardware back navigation handler
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    val showBottomNav = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.CATEGORIES,
        AppScreen.CART,
        AppScreen.ORDERS,
        AppScreen.PROFILE
    )

    val showTopStoreBar = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.CATEGORIES,
        AppScreen.WISHLIST
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (showTopStoreBar) {
                TopStoreBar(
                    viewModel = viewModel,
                    onAddressClick = { showAddressSheet = true }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar"),
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        Triple(AppScreen.HOME, if (isHindi) "होम" else "Home", Icons.Filled.Home to Icons.Outlined.Home),
                        Triple(AppScreen.CATEGORIES, if (isHindi) "श्रेणियां" else "Categories", Icons.Filled.Category to Icons.Outlined.Category),
                        Triple(AppScreen.CART, if (isHindi) "कार्ट" else "Cart", Icons.Filled.ShoppingBag to Icons.Outlined.ShoppingBag),
                        Triple(AppScreen.ORDERS, if (isHindi) "ऑर्डर" else "Orders", Icons.Filled.ReceiptLong to Icons.Outlined.ReceiptLong),
                        Triple(AppScreen.PROFILE, if (isHindi) "प्रोफ़ाइल" else "Profile", Icons.Filled.Person to Icons.Outlined.Person)
                    )

                    navItems.forEach { (screen, label, icons) ->
                        val isSelected = currentScreen == screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(screen) },
                            icon = {
                                if (screen == AppScreen.CART && totalCartItems > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = StoreYellowAccent,
                                                contentColor = Color.Black
                                            ) {
                                                Text(
                                                    text = "$totalCartItems",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) icons.first else icons.second,
                                            contentDescription = label
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) icons.first else icons.second,
                                        contentDescription = label
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = StoreGreenDark,
                                selectedTextColor = StoreGreenDark,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = Color(0xFFE8F5E9)
                            ),
                            modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.CATEGORIES -> CategoriesScreen(viewModel = viewModel)
                AppScreen.CART -> CartScreen(
                    viewModel = viewModel,
                    onSelectAddressClick = { showAddressSheet = true }
                )
                AppScreen.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                AppScreen.ORDERS -> OrdersHistoryScreen(viewModel = viewModel)
                AppScreen.ORDER_TRACKING -> OrderTrackingScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                AppScreen.SEARCH -> SearchScreen(viewModel = viewModel)
                AppScreen.WISHLIST -> WishlistScreen(viewModel = viewModel)
                AppScreen.NOTIFICATIONS -> NotificationsScreen(viewModel = viewModel)
                AppScreen.ADMIN_DASHBOARD,
                AppScreen.ADMIN_PRODUCTS,
                AppScreen.ADMIN_ORDERS,
                AppScreen.ADMIN_INVENTORY,
                AppScreen.ADMIN_SETTINGS,
                AppScreen.ADMIN_COUPONS,
                AppScreen.ADMIN_DELIVERY_PARTNERS -> AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }

    // Address Selector Modal Sheet
    if (showAddressSheet) {
        AddressSelectorSheet(
            viewModel = viewModel,
            onDismiss = { showAddressSheet = false }
        )
    }

    // Product Detail Bottom Sheet
    selectedProduct?.let { product ->
        ProductDetailSheet(
            product = product,
            viewModel = viewModel,
            onDismiss = { viewModel.dismissProductDetail() }
        )
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StoreGreenPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🛒", fontSize = 48.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Balaji Mart",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(StoreYellowAccent)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "⚡ 10 MINS SUPERFAST GROCERY DELIVERY",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = StoreGreenDark
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Fresh Vegetables • Dairy • Atta • Pulses & Spices",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator(
                color = StoreYellowAccent,
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
