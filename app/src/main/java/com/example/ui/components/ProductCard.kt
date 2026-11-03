package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Product
import com.example.ui.theme.StoreGreenDark
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.StoreYellowAccent
import com.example.ui.theme.StoreYellowContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProductCard(
    product: Product,
    quantityInCart: Int,
    isWishlisted: Boolean,
    isHindi: Boolean,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onIncreaseQty: () -> Unit,
    onDecreaseQty: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Image container with Discount badge and Wishlist heart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF3F5F4))
            ) {
                // If product has imageUrl load AsyncImage, otherwise show emoji/icon graphic
                if (product.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.nameEn,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback visual tile
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (product.categoryId) {
                                "fruits_veg" -> "🥦"
                                "atta_rice_dal" -> "🌾"
                                "dairy_bakery" -> "🥛"
                                "oil_ghee" -> "🫒"
                                "spices_masala" -> "🌶️"
                                "snacks_namkeen" -> "🍿"
                                "biscuits_chocolates" -> "🍪"
                                "drinks_juices" -> "🧃"
                                "tea_coffee" -> "☕"
                                "personal_care" -> "🧴"
                                "beauty_products" -> "💄"
                                "household_cleaning" -> "🧹"
                                "kitchen_essentials" -> "🍳"
                                "baby_care" -> "🍼"
                                "pooja_samagri" -> "🪔"
                                "dry_fruits" -> "🥜"
                                "frozen_foods" -> "🧊"
                                "pet_supplies" -> "🐾"
                                else -> "🥫"
                            },
                            fontSize = 42.sp
                        )
                    }
                }

                // Discount badge
                if (product.discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(StoreGreenDark)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${product.discountPercent}% OFF",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Wishlist heart
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .testTag("wishlist_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) Color(0xFFE53935) else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Delivery time badge bottom-left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "⚡ 10 MINS",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StoreGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Unit/Weight
            Text(
                text = product.unit,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            // Title
            Text(
                text = if (isHindi) product.nameHi else product.nameEn,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontSize = 13.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(36.dp)
            )

            // Rating & Stock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(StoreYellowContainer)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", product.rating),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                if (product.stock <= 5) {
                    Text(
                        text = "Only ${product.stock} left!",
                        fontSize = 9.sp,
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Price row & Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${product.price.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    )
                    if (product.originalMrp > product.price) {
                        Text(
                            text = "₹${product.originalMrp.toInt()}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }

                // Add or Quantity Controller
                if (quantityInCart == 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, StoreGreenPrimary, RoundedCornerShape(6.dp))
                            .background(Color(0xFFE8F5E9))
                            .clickable { onAddToCart() }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("add_to_cart_${product.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isHindi) "जोड़ें" else "ADD",
                            fontWeight = FontWeight.ExtraBold,
                            color = StoreGreenPrimary,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StoreGreenPrimary)
                            .padding(horizontal = 4.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onDecreaseQty() }
                                .testTag("decrease_qty_${product.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "$quantityInCart",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onIncreaseQty() }
                                .testTag("increase_qty_${product.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
