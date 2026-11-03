package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface GroceryDao {

    // --- Categories ---
    @Query("SELECT * FROM categories ORDER BY displayOrder ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: String)

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY nameEn ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductById(productId: String): Flow<Product?>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY nameEn ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE nameEn LIKE '%' || :query || '%' OR nameHi LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' OR subCategory LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isTrending = 1")
    fun getTrendingProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isBestSeller = 1")
    fun getBestSellers(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isFrequentlyBought = 1")
    fun getFrequentlyBought(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isRecommended = 1")
    fun getRecommended(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE stock <= 10 ORDER BY stock ASC")
    fun getLowStockProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)

    @Query("UPDATE products SET stock = MAX(0, stock - :quantity) WHERE id = :productId")
    suspend fun reduceStock(productId: String, quantity: Int)

    @Query("UPDATE products SET stock = :newStock WHERE id = :productId")
    suspend fun updateStock(productId: String, newStock: Int)

    // --- Cart ---
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // --- Wishlist ---
    @Query("SELECT * FROM wishlist_items")
    fun getWishlist(): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlistItem: WishlistItem)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: String)

    // --- Recently Viewed ---
    @Query("SELECT * FROM recently_viewed ORDER BY viewedAt DESC LIMIT 20")
    fun getRecentlyViewed(): Flow<List<RecentlyViewed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed)

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<Order?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("UPDATE orders SET deliveryPartnerName = :partnerName, deliveryPartnerPhone = :partnerPhone, orderStatus = 'OUT_FOR_DELIVERY' WHERE orderId = :orderId")
    suspend fun assignDeliveryPartner(orderId: String, partnerName: String, partnerPhone: String)

    // --- Coupons ---
    @Query("SELECT * FROM coupons ORDER BY code ASC")
    fun getAllCoupons(): Flow<List<Coupon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: Coupon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupons(coupons: List<Coupon>)

    @Query("DELETE FROM coupons WHERE code = :code")
    suspend fun deleteCoupon(code: String)

    // --- Delivery Partners ---
    @Query("SELECT * FROM delivery_partners")
    fun getAllDeliveryPartners(): Flow<List<DeliveryPartner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliveryPartner(partner: DeliveryPartner)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliveryPartners(partners: List<DeliveryPartner>)

    @Query("UPDATE delivery_partners SET status = :status WHERE id = :partnerId")
    suspend fun updatePartnerStatus(partnerId: String, status: String)

    // --- Store Settings ---
    @Query("SELECT * FROM store_settings WHERE id = 1 LIMIT 1")
    fun getStoreSettings(): Flow<StoreSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStoreSettings(settings: StoreSettings)

    // --- Notifications ---
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE isRead = 0")
    suspend fun markAllNotificationsRead()

    // --- Reviews ---
    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY createdAt DESC")
    fun getReviewsForProduct(productId: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)
}
