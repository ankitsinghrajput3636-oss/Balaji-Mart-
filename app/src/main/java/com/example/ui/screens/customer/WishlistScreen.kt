package com.example.ui.screens.customer

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.ProductCard
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel

@Composable
fun WishlistScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val wishlistIds by viewModel.wishlistProductIds.collectAsState()

    val wishlistedProducts = allProducts.filter { wishlistIds.contains(it.id) }

    Surface(
        modifier = modifier.fillMaxSize().testTag("wishlist_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (isHindi) "मेरी पसंदीदा वस्तुएं (${wishlistedProducts.size})" else "My Wishlist (${wishlistedProducts.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (wishlistedProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isHindi) "आपकी पसंदीदा सूची खाली है" else "Your Wishlist is Empty",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Save items you love and reorder anytime with one tap",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.HOME) },
                            colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                        ) {
                            Text("Explore Groceries")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    val pairs = wishlistedProducts.chunked(2)
                    items(pairs) { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { prod ->
                                val qty = cartProducts.find { it.first.id == prod.id }?.second ?: 0
                                ProductCard(
                                    product = prod,
                                    quantityInCart = qty,
                                    isWishlisted = true,
                                    isHindi = isHindi,
                                    onProductClick = { viewModel.selectProduct(prod) },
                                    onAddToCart = { viewModel.addToCart(prod) },
                                    onIncreaseQty = { viewModel.updateCartQuantity(prod.id, qty + 1) },
                                    onDecreaseQty = { viewModel.updateCartQuantity(prod.id, qty - 1) },
                                    onToggleWishlist = { viewModel.toggleWishlist(prod) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
