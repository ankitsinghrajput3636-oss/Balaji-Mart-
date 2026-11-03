package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Category
import com.example.ui.components.ProductCard
import com.example.ui.theme.StoreGreenContainer
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.GroceryViewModel

@Composable
fun CategoriesScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val wishlistIds by viewModel.wishlistProductIds.collectAsState()

    val currentCategory = selectedCategory ?: allCategories.firstOrNull()
    val categoryProducts = if (currentCategory != null) {
        allProducts.filter { it.categoryId == currentCategory.id }
    } else {
        allProducts
    }

    Row(modifier = modifier.fillMaxSize().testTag("categories_screen")) {
        // Left Column: All 19 Categories
        LazyColumn(
            modifier = Modifier
                .width(105.dp)
                .fillMaxHeight()
                .background(Color(0xFFF3F5F4))
        ) {
            items(allCategories) { category ->
                val isSelected = category.id == currentCategory?.id
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectedCategory.value = category }
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .padding(vertical = 12.dp, horizontal = 6.dp)
                        .testTag("cat_menu_${category.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) StoreGreenContainer else Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = category.iconEmoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) category.nameHi else category.nameEn,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StoreGreenPrimary else TextSecondary,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Divider(color = Color(0xFFE5E7EB), thickness = 0.5.dp)
            }
        }

        // Right Pane: Products in the selected category
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White)
                .padding(10.dp)
        ) {
            // Category Header Title
            if (currentCategory != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = currentCategory.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isHindi) currentCategory.nameHi else currentCategory.nameEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${categoryProducts.size} items available",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (categoryProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🛒", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isHindi) "इस श्रेणी में कोई उत्पाद उपलब्ध नहीं है" else "No products found in this category",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val pairs = categoryProducts.chunked(2)
                    items(pairs) { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { product ->
                                val qty = cartProducts.find { it.first.id == product.id }?.second ?: 0
                                val isWish = wishlistIds.contains(product.id)
                                ProductCard(
                                    product = product,
                                    quantityInCart = qty,
                                    isWishlisted = isWish,
                                    isHindi = isHindi,
                                    onProductClick = { viewModel.selectProduct(product) },
                                    onAddToCart = { viewModel.addToCart(product) },
                                    onIncreaseQty = { viewModel.updateCartQuantity(product.id, qty + 1) },
                                    onDecreaseQty = { viewModel.updateCartQuantity(product.id, qty - 1) },
                                    onToggleWishlist = { viewModel.toggleWishlist(product) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
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
}
