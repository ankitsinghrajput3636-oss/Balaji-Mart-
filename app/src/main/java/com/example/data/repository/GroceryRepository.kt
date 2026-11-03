package com.example.data.repository

import com.example.data.local.InitialData
import com.example.data.local.dao.GroceryDao
import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.Coupon
import com.example.data.model.DeliveryPartner
import com.example.data.model.NotificationItem
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.RecentlyViewed
import com.example.data.model.Review
import com.example.data.model.StoreSettings
import com.example.data.model.WishlistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GroceryRepository(private val dao: GroceryDao) {

    val allCategories: Flow<List<Category>> = dao.getAllCategories()
    val allProducts: Flow<List<Product>> = dao.getAllProducts()
    val cartItems: Flow<List<CartItem>> = dao.getCartItems()
    val wishlistItems: Flow<List<WishlistItem>> = dao.getWishlist()
    val recentlyViewed: Flow<List<RecentlyViewed>> = dao.getRecentlyViewed()
    val allOrders: Flow<List<Order>> = dao.getAllOrders()
    val allCoupons: Flow<List<Coupon>> = dao.getAllCoupons()
    val allDeliveryPartners: Flow<List<DeliveryPartner>> = dao.getAllDeliveryPartners()
    val storeSettings: Flow<StoreSettings?> = dao.getStoreSettings()
    val notifications: Flow<List<NotificationItem>> = dao.getAllNotifications()
    val trendingProducts: Flow<List<Product>> = dao.getTrendingProducts()
    val bestSellers: Flow<List<Product>> = dao.getBestSellers()
    val frequentlyBought: Flow<List<Product>> = dao.getFrequentlyBought()
    val recommendedProducts: Flow<List<Product>> = dao.getRecommended()
    val lowStockProducts: Flow<List<Product>> = dao.getLowStockProducts()

    suspend fun checkAndSeedDatabase() {
        val existingCats = dao.getAllCategories().firstOrNull()
        if (existingCats.isNullOrEmpty()) {
            dao.insertCategories(InitialData.categories)
            dao.insertProducts(InitialData.products)
            dao.insertCoupons(InitialData.coupons)
            dao.insertDeliveryPartners(InitialData.deliveryPartners)
            dao.saveStoreSettings(StoreSettings())
            InitialData.initialNotifications.forEach {
                dao.insertNotification(it)
            }
        }
    }

    fun getProductsByCategory(categoryId: String): Flow<List<Product>> =
        dao.getProductsByCategory(categoryId)

    fun searchProducts(query: String): Flow<List<Product>> =
        dao.searchProducts(query)

    fun getProductById(productId: String): Flow<Product?> =
        dao.getProductById(productId)

    fun getReviews(productId: String): Flow<List<Review>> =
        dao.getReviewsForProduct(productId)

    // --- Cart Actions ---
    suspend fun addToCart(productId: String, quantity: Int = 1) {
        dao.insertCartItem(CartItem(productId, quantity))
    }

    suspend fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            dao.deleteCartItem(productId)
        } else {
            dao.insertCartItem(CartItem(productId, quantity))
        }
    }

    suspend fun removeFromCart(productId: String) {
        dao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    // --- Wishlist Actions ---
    suspend fun toggleWishlist(productId: String, isWishlisted: Boolean) {
        if (isWishlisted) {
            dao.removeFromWishlist(productId)
        } else {
            dao.addToWishlist(WishlistItem(productId))
        }
    }

    // --- Recently Viewed ---
    suspend fun logRecentlyViewed(productId: String) {
        dao.addRecentlyViewed(RecentlyViewed(productId))
    }

    // --- Order Placement ---
    suspend fun placeOrder(
        order: Order,
        purchasedItems: List<Pair<Product, Int>>
    ) {
        dao.insertOrder(order)
        // Deduct inventory
        purchasedItems.forEach { (product, quantity) ->
            dao.reduceStock(product.id, quantity)
        }
        // Clear cart
        dao.clearCart()

        // Create notification
        dao.insertNotification(
            NotificationItem(
                title = "Order Placed Successfully! 🛒",
                message = "Order #${order.orderId.takeLast(6).uppercase()} of ₹${order.totalAmount.toInt()} has been confirmed. Delivery in ~${order.estimatedDeliveryMinutes} mins.",
                type = "ORDER"
            )
        )
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        dao.updateOrderStatus(orderId, status)
        val statusText = when (status) {
            "ACCEPTED" -> "accepted and is being prepared"
            "PACKED" -> "packed and ready for pickup"
            "OUT_FOR_DELIVERY" -> "out for delivery! 🛵"
            "DELIVERED" -> "delivered. Enjoy your fresh groceries! 🎉"
            "CANCELLED" -> "cancelled"
            else -> status
        }
        dao.insertNotification(
            NotificationItem(
                title = "Order Status: $status",
                message = "Your order #${orderId.takeLast(6).uppercase()} is now $statusText.",
                type = "ORDER"
            )
        )
    }

    suspend fun updatePaymentStatus(orderId: String, status: String, txnId: String = "") {
        dao.updatePaymentStatus(orderId, status, txnId)
    }

    suspend fun assignDeliveryPartner(orderId: String, partnerName: String, partnerPhone: String) {
        dao.assignDeliveryPartner(orderId, partnerName, partnerPhone)
        dao.insertNotification(
            NotificationItem(
                title = "Delivery Partner Assigned 🛵",
                message = "$partnerName ($partnerPhone) is bringing your order #${orderId.takeLast(6).uppercase()}.",
                type = "ORDER"
            )
        )
    }

    // --- Product Admin ---
    suspend fun insertProduct(product: Product) = dao.insertProduct(product)
    suspend fun updateProduct(product: Product) = dao.updateProduct(product)
    suspend fun deleteProduct(productId: String) = dao.deleteProduct(productId)
    suspend fun updateStock(productId: String, newStock: Int) = dao.updateStock(productId, newStock)

    // --- Category Admin ---
    suspend fun insertCategory(category: Category) = dao.insertCategory(category)
    suspend fun deleteCategory(categoryId: String) = dao.deleteCategory(categoryId)

    // --- Coupon Admin ---
    suspend fun insertCoupon(coupon: Coupon) = dao.insertCoupon(coupon)
    suspend fun deleteCoupon(code: String) = dao.deleteCoupon(code)

    // --- Delivery Partner Admin ---
    suspend fun insertDeliveryPartner(partner: DeliveryPartner) = dao.insertDeliveryPartner(partner)
    suspend fun updatePartnerStatus(partnerId: String, status: String) = dao.updatePartnerStatus(partnerId, status)

    // --- Store Settings Admin ---
    suspend fun saveStoreSettings(settings: StoreSettings) = dao.saveStoreSettings(settings)

    // --- Notifications ---
    suspend fun markNotificationsRead() = dao.markAllNotificationsRead()

    // --- Reviews ---
    suspend fun addReview(review: Review) = dao.insertReview(review)
}
