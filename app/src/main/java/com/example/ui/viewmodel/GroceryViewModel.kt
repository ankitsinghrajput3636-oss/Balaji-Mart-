package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.Coupon
import com.example.data.model.DeliveryPartner
import com.example.data.model.NotificationItem
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.StoreSettings
import com.example.data.repository.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

enum class AppScreen {
    HOME,
    CATEGORIES,
    CART,
    ORDERS,
    PROFILE,
    SEARCH,
    WISHLIST,
    NOTIFICATIONS,
    CHECKOUT,
    ORDER_TRACKING,
    ADMIN_DASHBOARD,
    ADMIN_PRODUCTS,
    ADMIN_ORDERS,
    ADMIN_INVENTORY,
    ADMIN_SETTINGS,
    ADMIN_COUPONS,
    ADMIN_DELIVERY_PARTNERS
}

enum class HomeTab {
    ALL,
    TRENDING,
    BEST_SELLERS,
    FREQUENT,
    RECOMMENDED
}

data class SavedAddress(
    val title: String,
    val fullAddress: String,
    val distanceKm: Double,
    val isDefault: Boolean = false
)

class GroceryViewModel(private val repository: GroceryRepository) : ViewModel() {

    // --- Navigation ---
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<AppScreen>()

    // --- Language ---
    private val _isHindi = MutableStateFlow(false)
    val isHindi: StateFlow<Boolean> = _isHindi.asStateFlow()

    fun toggleLanguage() {
        _isHindi.value = !_isHindi.value
    }

    // --- Search & Tabs ---
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<Category?>(null)
    val selectedHomeTab = MutableStateFlow(HomeTab.ALL)
    val selectedProduct = MutableStateFlow<Product?>(null)

    // --- User & Location ---
    val userPhone = MutableStateFlow("9876543210")
    val userName = MutableStateFlow("Rahul Sharma")
    val userEmail = MutableStateFlow("rahul.sharma@example.com")

    val savedAddresses = listOf(
        SavedAddress("Home (Within 2KM)", "Flat 302, Krishna Heights, Sector 14", 0.8, isDefault = true),
        SavedAddress("Shop (Balaji Chowk)", "Shop 12, Main Market, Balaji Chowk", 0.3),
        SavedAddress("Office (Within 2KM)", "Tower B, IT Park, Sector 15", 1.6),
        SavedAddress("Far Away (Outside 2KM)", "Villa 9, Greater Green City, Sector 58", 4.5)
    )

    val selectedAddress = MutableStateFlow(savedAddresses[0])

    // --- Repository Flows ---
    val allCategories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingProducts: StateFlow<List<Product>> = repository.trendingProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestSellers: StateFlow<List<Product>> = repository.bestSellers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val frequentlyBought: StateFlow<List<Product>> = repository.frequentlyBought
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recommendedProducts: StateFlow<List<Product>> = repository.recommendedProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<Product>> = repository.lowStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCoupons: StateFlow<List<Coupon>> = repository.allCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeliveryPartners: StateFlow<List<DeliveryPartner>> = repository.allDeliveryPartners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storeSettings: StateFlow<StoreSettings?> = repository.storeSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StoreSettings())

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItemRaw: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistRaw = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistProductIds: StateFlow<Set<String>> = MutableStateFlow<Set<String>>(emptySet()).apply {
        viewModelScope.launch {
            wishlistRaw.collect { list ->
                value = list.map { it.productId }.toSet()
            }
        }
    }

    // --- Applied Coupon ---
    val appliedCoupon = MutableStateFlow<Coupon?>(null)
    val couponError = MutableStateFlow<String?>(null)

    // --- Tracking Order ---
    val trackingOrderId = MutableStateFlow<String?>(null)

    // --- Admin Auth ---
    val isAdminMode = MutableStateFlow(false)

    // --- UI SnackBar / Alert message ---
    val toastMessage = MutableStateFlow<String?>(null)

    // --- Cart Computation ---
    val cartProducts: StateFlow<List<Pair<Product, Int>>> = combine(
        cartItemRaw,
        allProducts
    ) { items, prods ->
        val prodMap = prods.associateBy { it.id }
        items.mapNotNull { item ->
            prodMap[item.productId]?.let { it to item.quantity }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subtotal: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            cartProducts.collect { pairs ->
                value = pairs.sumOf { (prod, qty) -> prod.price * qty }
            }
        }
    }

    val totalMrpValue: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            cartProducts.collect { pairs ->
                value = pairs.sumOf { (prod, qty) -> prod.originalMrp * qty }
            }
        }
    }

    val totalSavings: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            combine(subtotal, totalMrpValue, appliedCoupon) { sub, mrp, coupon ->
                val mrpDiscount = (mrp - sub).coerceAtLeast(0.0)
                val cDiscount = calculateCouponDiscount(sub, coupon)
                mrpDiscount + cDiscount
            }.collect { value = it }
        }
    }

    init {
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    // --- Navigation Functions ---
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            true
        } else {
            if (_currentScreen.value != AppScreen.HOME) {
                _currentScreen.value = AppScreen.HOME
                true
            } else {
                false
            }
        }
    }

    // --- Location & 2 KM radius check ---
    fun selectAddress(address: SavedAddress) {
        selectedAddress.value = address
        val settings = storeSettings.value ?: StoreSettings()
        if (address.distanceKm > settings.deliveryRadiusKm) {
            toastMessage.value = "⚠️ Address is outside the 2 KM delivery radius (${address.distanceKm} KM)!"
        } else {
            toastMessage.value = "📍 Delivery set to: ${address.title} (${address.distanceKm} KM)"
        }
    }

    fun isAddressWithinRadius(): Boolean {
        val radius = storeSettings.value?.deliveryRadiusKm ?: 2.0
        return selectedAddress.value.distanceKm <= radius
    }

    // --- Cart operations ---
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existing = cartItemRaw.value.find { it.productId == product.id }
            val currentQty = existing?.quantity ?: 0
            if (currentQty + 1 > product.stock) {
                toastMessage.value = "Only ${product.stock} items left in stock!"
                return@launch
            }
            repository.addToCart(product.id, currentQty + 1)
            toastMessage.value = "Added to cart: ${if (_isHindi.value) product.nameHi else product.nameEn}"
        }
    }

    fun updateCartQuantity(productId: String, newQty: Int) {
        viewModelScope.launch {
            val product = allProducts.value.find { it.id == productId }
            if (product != null && newQty > product.stock) {
                toastMessage.value = "Maximum available stock is ${product.stock}!"
                return@launch
            }
            repository.updateCartQuantity(productId, newQty)
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // --- Wishlist ---
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val isWishlisted = wishlistProductIds.value.contains(product.id)
            repository.toggleWishlist(product.id, isWishlisted)
            val msg = if (isWishlisted) "Removed from Wishlist" else "Saved to Wishlist ❤️"
            toastMessage.value = msg
        }
    }

    // --- Product Selection & Recently Viewed ---
    fun selectProduct(product: Product) {
        selectedProduct.value = product
        viewModelScope.launch {
            repository.logRecentlyViewed(product.id)
        }
    }

    fun dismissProductDetail() {
        selectedProduct.value = null
    }

    // --- Coupon Management ---
    fun applyCoupon(code: String) {
        couponError.value = null
        val sub = subtotal.value
        val coupon = allCoupons.value.find { it.code.equals(code.trim(), ignoreCase = true) }
        if (coupon == null || !coupon.isActive) {
            couponError.value = "Invalid or expired coupon code"
            appliedCoupon.value = null
            return
        }
        if (sub < coupon.minOrderValue) {
            couponError.value = "Minimum order value of ₹${coupon.minOrderValue.toInt()} required for this coupon"
            appliedCoupon.value = null
            return
        }
        appliedCoupon.value = coupon
        toastMessage.value = "Coupon ${coupon.code} applied successfully! 🎉"
    }

    fun removeCoupon() {
        appliedCoupon.value = null
        couponError.value = null
    }

    fun calculateCouponDiscount(sub: Double, coupon: Coupon?): Double {
        if (coupon == null || sub < coupon.minOrderValue) return 0.0
        return if (coupon.discountAmount > 0) {
            coupon.discountAmount.coerceAtMost(sub)
        } else if (coupon.discountPercentage > 0) {
            (sub * (coupon.discountPercentage / 100.0)).coerceAtMost(100.0)
        } else 0.0
    }

    // --- Place Order ---
    fun placeOrder(
        paymentMethod: String,
        orderNotes: String,
        onSuccess: (String) -> Unit
    ) {
        val settings = storeSettings.value ?: StoreSettings()
        if (!settings.isStoreOpen) {
            toastMessage.value = "Store is currently closed (${settings.openingTime} - ${settings.closingTime})"
            return
        }
        if (!isAddressWithinRadius()) {
            toastMessage.value = "Cannot deliver outside ${settings.deliveryRadiusKm} KM radius. Please choose an address nearby."
            return
        }
        val currentSubtotal = subtotal.value
        if (currentSubtotal < settings.minOrderAmount) {
            toastMessage.value = "Minimum order value is ₹${settings.minOrderAmount.toInt()}!"
            return
        }

        val items = cartProducts.value
        if (items.isEmpty()) {
            toastMessage.value = "Cart is empty!"
            return
        }

        val cDiscount = calculateCouponDiscount(currentSubtotal, appliedCoupon.value)
        val deliveryFee = if (currentSubtotal >= settings.freeDeliveryThreshold) 0.0 else settings.standardDeliveryFee
        val taxes = 5.0 // Nominal handling & packaging
        val total = (currentSubtotal - cDiscount + deliveryFee + taxes).coerceAtLeast(0.0)

        val newOrderId = "BJ-" + UUID.randomUUID().toString().take(8).uppercase()
        val itemsSummary = items.joinToString(", ") { "${it.first.nameEn} x${it.second}" }

        val newOrder = Order(
            orderId = newOrderId,
            userId = userPhone.value,
            itemsSummary = itemsSummary,
            subtotal = currentSubtotal,
            discount = cDiscount,
            deliveryFee = deliveryFee,
            taxes = taxes,
            totalAmount = total,
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod == "COD") "PENDING" else "PAID",
            orderStatus = "PLACED",
            orderTime = System.currentTimeMillis(),
            estimatedDeliveryMinutes = 10,
            deliveryAddress = selectedAddress.value.fullAddress,
            customerPhone = userPhone.value,
            customerName = userName.value,
            orderNotes = orderNotes
        )

        viewModelScope.launch {
            repository.placeOrder(newOrder, items)
            appliedCoupon.value = null
            trackingOrderId.value = newOrderId
            onSuccess(newOrderId)
        }
    }

    // --- Admin Operations ---
    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
            toastMessage.value = "Product added successfully!"
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
            toastMessage.value = "Product updated!"
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            toastMessage.value = "Product deleted!"
        }
    }

    fun updateProductStock(productId: String, stock: Int) {
        viewModelScope.launch {
            repository.updateStock(productId, stock)
            toastMessage.value = "Stock updated to $stock"
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
            toastMessage.value = "Category added!"
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            toastMessage.value = "Order status updated to $status"
        }
    }

    fun assignDeliveryPartner(orderId: String, partner: DeliveryPartner) {
        viewModelScope.launch {
            repository.assignDeliveryPartner(orderId, partner.name, partner.phone)
            toastMessage.value = "Assigned ${partner.name} to order #${orderId.takeLast(6)}"
        }
    }

    fun saveStoreSettings(settings: StoreSettings) {
        viewModelScope.launch {
            repository.saveStoreSettings(settings)
            toastMessage.value = "Store settings updated!"
        }
    }

    fun addCoupon(coupon: Coupon) {
        viewModelScope.launch {
            repository.insertCoupon(coupon)
            toastMessage.value = "Coupon ${coupon.code} created!"
        }
    }

    fun deleteCoupon(code: String) {
        viewModelScope.launch {
            repository.deleteCoupon(code)
            toastMessage.value = "Coupon deleted!"
        }
    }

    fun addDeliveryPartner(partner: DeliveryPartner) {
        viewModelScope.launch {
            repository.insertDeliveryPartner(partner)
            toastMessage.value = "Partner ${partner.name} added!"
        }
    }

    fun updatePartnerStatus(partnerId: String, status: String) {
        viewModelScope.launch {
            repository.updatePartnerStatus(partnerId, status)
            toastMessage.value = "Rider status updated to $status"
        }
    }

    fun clearToast() {
        toastMessage.value = null
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markNotificationsRead()
        }
    }

    fun addProductReview(productId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            val review = Review(
                id = UUID.randomUUID().toString(),
                productId = productId,
                customerName = userName.value,
                rating = rating,
                comment = comment
            )
            repository.addReview(review)
            toastMessage.value = "Review submitted! Thank you! ⭐"
        }
    }
}
