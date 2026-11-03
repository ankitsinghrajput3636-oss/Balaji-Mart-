package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameHi: String,
    val iconEmoji: String,
    val displayOrder: Int = 0
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameHi: String,
    val categoryId: String,
    val subCategory: String,
    val price: Double,
    val originalMrp: Double,
    val unit: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val imageUrl: String = "",
    val stock: Int = 50,
    val rating: Float = 4.8f,
    val reviewCount: Int = 120,
    val isTrending: Boolean = false,
    val isBestSeller: Boolean = false,
    val isFrequentlyBought: Boolean = false,
    val isRecommended: Boolean = false,
    val tags: String = ""
) {
    val discountPercent: Int
        get() = if (originalMrp > price && originalMrp > 0) {
            (((originalMrp - price) / originalMrp) * 100).toInt()
        } else 0
}

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val productId: String,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recently_viewed")
data class RecentlyViewed(
    @PrimaryKey val productId: String,
    val viewedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String,
    val userId: String,
    val itemsSummary: String,
    val subtotal: Double,
    val discount: Double,
    val deliveryFee: Double,
    val taxes: Double,
    val totalAmount: Double,
    val paymentMethod: String, // "COD", "UPI", "CARD"
    val paymentStatus: String, // "PAID", "PENDING"
    val orderStatus: String, // "PLACED", "ACCEPTED", "PACKED", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"
    val orderTime: Long = System.currentTimeMillis(),
    val estimatedDeliveryMinutes: Int = 12,
    val deliveryAddress: String,
    val customerPhone: String,
    val customerName: String,
    val orderNotes: String = "",
    val deliveryPartnerName: String? = null,
    val deliveryPartnerPhone: String? = null
)

@Entity(tableName = "coupons")
data class Coupon(
    @PrimaryKey val code: String,
    val discountAmount: Double = 0.0,
    val discountPercentage: Int = 0,
    val minOrderValue: Double = 150.0,
    val description: String,
    val isActive: Boolean = true
)

@Entity(tableName = "delivery_partners")
data class DeliveryPartner(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val vehicleNumber: String,
    val status: String = "AVAILABLE", // AVAILABLE, ON_DUTY, OFF_DUTY
    val rating: Float = 4.9f
)

@Entity(tableName = "store_settings")
data class StoreSettings(
    @PrimaryKey val id: Int = 1,
    val storeName: String = "Balaji Mart",
    val storeAddress: String = "Main Bazar, Balaji Chowk, Sector 14",
    val storeLatitude: Double = 28.6139,
    val storeLongitude: Double = 77.2090,
    val deliveryRadiusKm: Double = 2.0,
    val minOrderAmount: Double = 99.0,
    val freeDeliveryThreshold: Double = 199.0,
    val standardDeliveryFee: Double = 25.0,
    val isStoreOpen: Boolean = true,
    val openingTime: String = "06:00 AM",
    val closingTime: String = "11:00 PM"
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "ORDER",
    val isRead: Boolean = false
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String,
    val productId: String,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class CartItemWithProduct(
    val cartItem: CartItem,
    val product: Product
)
