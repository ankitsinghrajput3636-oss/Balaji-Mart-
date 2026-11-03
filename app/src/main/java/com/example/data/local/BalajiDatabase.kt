package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Category::class,
        Product::class,
        CartItem::class,
        WishlistItem::class,
        RecentlyViewed::class,
        Order::class,
        Coupon::class,
        DeliveryPartner::class,
        StoreSettings::class,
        NotificationItem::class,
        Review::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BalajiDatabase : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao

    companion object {
        @Volatile
        private var INSTANCE: BalajiDatabase? = null

        fun getInstance(context: Context): BalajiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BalajiDatabase::class.java,
                    "balaji_grocery.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate in background
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    val dao = database.groceryDao()
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
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
