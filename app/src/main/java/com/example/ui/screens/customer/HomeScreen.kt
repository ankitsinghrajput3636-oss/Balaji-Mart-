package com.example.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Category
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.StoreGreenDark
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.StoreYellowAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GroceryViewModel
import com.example.ui.viewmodel.HomeTab

@Composable
fun HomeScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val trendingProducts by viewModel.trendingProducts.collectAsState()
    val bestSellers by viewModel.bestSellers.collectAsState()
    val frequentlyBought by viewModel.frequentlyBought.collectAsState()
    val recommendedProducts by viewModel.recommendedProducts.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val wishlistIds by viewModel.wishlistProductIds.collectAsState()
    val selectedTab by viewModel.selectedHomeTab.collectAsState()
    val cartCount = cartProducts.sumOf { it.second }
    val cartSubtotal by viewModel.subtotal.collectAsState()

    val displayedProducts = when (selectedTab) {
        HomeTab.ALL -> allProducts
        HomeTab.TRENDING -> if (trendingProducts.isNotEmpty()) trendingProducts else allProducts.take(8)
        HomeTab.BEST_SELLERS -> if (bestSellers.isNotEmpty()) bestSellers else allProducts.take(8)
        HomeTab.FREQUENT -> if (frequentlyBought.isNotEmpty()) frequentlyBought else allProducts.take(8)
        HomeTab.RECOMMENDED -> if (recommendedProducts.isNotEmpty()) recommendedProducts else allProducts.take(8)
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (cartCount > 0) 70.dp else 0.dp)
                .testTag("home_screen_content")
        ) {
            // Search Bar Button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { viewModel.navigateTo(AppScreen.SEARCH) }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("home_search_bar")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = StoreGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "सर्च करें 'दूध', 'आटा', 'टमाटर'..." else "Search 'milk', 'atta', 'tomatoes'...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = StoreGreenPrimary,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.searchQuery.value = "Milk"
                                    viewModel.navigateTo(AppScreen.SEARCH)
                                }
                        )
                    }
                }
            }

            // Hero Grocery Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_grocery_banner),
                            contentDescription = "Balaji Mart Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Quick Commerce Category Row Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "श्रेणियां (19 कैटेगरीज)" else "Explore Categories",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = if (isHindi) "सभी देखें >" else "See All >",
                        color = StoreGreenPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(AppScreen.CATEGORIES) }
                            .testTag("see_all_categories_btn")
                    )
                }
            }

            // Horizontal Categories Scroll
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allCategories) { category ->
                        CategoryIconPill(
                            category = category,
                            isHindi = isHindi,
                            onClick = {
                                viewModel.selectedCategory.value = category
                                viewModel.navigateTo(AppScreen.CATEGORIES)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Filter Tabs (All, Trending, Best Sellers, Frequently Bought, Recommended)
            item {
                val tabTitles = listOf(
                    HomeTab.ALL to (if (isHindi) "सभी" else "All"),
                    HomeTab.TRENDING to (if (isHindi) "ट्रेंडिंग 🔥" else "Trending 🔥"),
                    HomeTab.BEST_SELLERS to (if (isHindi) "बेस्ट सेलर 🏆" else "Best Sellers 🏆"),
                    HomeTab.FREQUENT to (if (isHindi) "अक्सर खरीदे जाने वाले" else "Frequently Bought"),
                    HomeTab.RECOMMENDED to (if (isHindi) "आपके लिए खास ✨" else "Recommended ✨")
                )

                ScrollableTabRow(
                    selectedTabIndex = tabTitles.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0),
                    edgePadding = 14.dp,
                    containerColor = Color.Transparent,
                    contentColor = StoreGreenPrimary,
                    divider = {}
                ) {
                    tabTitles.forEach { (tab, title) ->
                        val isSelected = selectedTab == tab
                        Tab(
                            selected = isSelected,
                            onClick = { viewModel.selectedHomeTab.value = tab },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StoreGreenPrimary else TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Product Grid (Rows of 2)
            val productPairs = displayedProducts.chunked(2)
            items(productPairs) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Floating Cart Peek / Checkout Bar
        if (cartCount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.navigateTo(AppScreen.CART) }
                    .testTag("floating_cart_bar"),
                color = StoreGreenPrimary,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Cart",
                            tint = StoreYellowAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "$cartCount ${if (cartCount == 1) "item" else "items"} | ₹${cartSubtotal.toInt()}",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isHindi) "⚡ 10 मिनट में डिलीवरी" else "⚡ 10 mins express delivery",
                                color = StoreYellowAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isHindi) "कार्ट देखें" else "View Cart",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Proceed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryIconPill(
    category: Category,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable { onClick() }
            .testTag("category_pill_${category.id}")
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.iconEmoji,
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isHindi) category.nameHi else category.nameEn,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
