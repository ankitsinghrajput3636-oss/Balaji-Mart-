package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextDecoration
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
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    viewModel: GroceryViewModel,
    onDismiss: () -> Unit
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val qtyInCart = cartProducts.find { it.first.id == product.id }?.second ?: 0
    var newReviewRating by remember { mutableIntStateOf(5) }
    var newReviewComment by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("product_detail_sheet")
        ) {
            // Header with Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "उत्पाद विवरण" else "Product Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_detail_sheet")) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Large Visual Tile
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3F5F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (product.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.nameEn,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(text = "🛒", fontSize = 64.sp)
                        }

                        // 10 Min express guarantee badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(StoreYellowAccent)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "⚡ 10 MINS SUPERFAST DELIVERY",
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = StoreGreenDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Name, Unit & Price
                item {
                    Text(
                        text = product.unit,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (isHindi) product.nameHi else product.nameEn,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹${product.price.toInt()}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (product.originalMrp > product.price) {
                            Text(
                                text = "₹${product.originalMrp.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextMuted,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StoreGreenDark)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${product.discountPercent}% OFF",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating chip
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(StoreYellowContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${product.rating} (${product.reviewCount} reviews)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (product.stock > 0) "In Stock: ${product.stock} items" else "Out of Stock",
                            color = if (product.stock > 0) StoreGreenPrimary else Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Description
                item {
                    Text(
                        text = if (isHindi) "विवरण" else "Description",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isHindi) product.descriptionHi else product.descriptionEn,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Trust Badges
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TrustBadge(icon = Icons.Default.FlashOn, title = "10 Min Delivery", subtitle = "Direct from Store")
                        TrustBadge(icon = Icons.Default.Security, title = "100% Quality", subtitle = "Farm Handpicked")
                        TrustBadge(icon = Icons.Default.LocalShipping, title = "Instant Return", subtitle = "No questions asked")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Customer Review Section
                item {
                    Text(
                        text = if (isHindi) "ग्राहक समीक्षाएं" else "Customer Reviews",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Rate this product:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                (1..5).forEach { star ->
                                    IconButton(
                                        onClick = { newReviewRating = star },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "$star stars",
                                            tint = if (star <= newReviewRating) Color(0xFFF59E0B) else Color.LightGray
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = newReviewComment,
                                onValueChange = { newReviewComment = it },
                                placeholder = { Text("Write your review...", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    if (newReviewComment.isNotBlank()) {
                                        viewModel.addProductReview(product.id, newReviewRating, newReviewComment)
                                        newReviewComment = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Submit Review", fontSize = 11.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Bottom Sticky Bar: Add to Cart and Buy Now
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCart(product)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_add_to_cart_btn"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StoreGreenPrimary)
                    ) {
                        Text(
                            text = if (qtyInCart > 0) "In Cart ($qtyInCart)" else "Add to Cart",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            if (qtyInCart == 0) {
                                viewModel.addToCart(product)
                            }
                            onDismiss()
                            viewModel.navigateTo(AppScreen.CHECKOUT)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_buy_now_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = StoreGreenPrimary)
                    ) {
                        Text(
                            text = if (isHindi) "अभी खरीदें" else "Buy Now",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrustBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(95.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StoreGreenPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = TextMuted
        )
    }
}
