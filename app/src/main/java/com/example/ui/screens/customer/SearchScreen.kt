package com.example.ui.screens.customer

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.StoreYellowAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.GroceryViewModel

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val wishlistIds by viewModel.wishlistProductIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showVoiceDialog by remember { mutableStateOf(false) }

    val filteredProducts = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        val q = searchQuery.trim().lowercase()
        allProducts.filter {
            it.nameEn.lowercase().contains(q) ||
            it.nameHi.lowercase().contains(q) ||
            it.tags.lowercase().contains(q) ||
            it.subCategory.lowercase().contains(q)
        }
    }

    val trendingKeywords = listOf(
        "Amul Milk", "Aashirvaad Atta", "Tomatoes", "Maggi", "Tata Tea",
        "Onions", "Haldiram", "Butter", "Ghee", "Parle-G", "Potato"
    )

    Surface(
        modifier = modifier.fillMaxSize().testTag("search_screen"),
        color = Color(0xFFF7F9F8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text(if (isHindi) "उत्पाद खोजें..." else "Search for groceries...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = StoreGreenPrimary)
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                }
                            }
                            IconButton(onClick = { showVoiceDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Search",
                                    tint = StoreGreenPrimary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_text_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StoreGreenPrimary,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
            }

            // Body: If no query, show trending keywords chips; else show results
            if (searchQuery.isBlank()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Text(
                            text = if (isHindi) "लोकप्रिय खोजें 🔥" else "Popular Searches 🔥",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            trendingKeywords.forEach { keyword ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White)
                                        .clickable { viewModel.searchQuery.value = keyword }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = keyword,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        Text(
                            text = if (isHindi) "सुझाए गए उत्पाद" else "Recommended for you",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    val recPairs = allProducts.take(6).chunked(2)
                    items(recPairs) { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { prod ->
                                val qty = cartProducts.find { it.first.id == prod.id }?.second ?: 0
                                val isWish = wishlistIds.contains(prod.id)
                                ProductCard(
                                    product = prod,
                                    quantityInCart = qty,
                                    isWishlisted = isWish,
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
            } else {
                // Search Results
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    item {
                        Text(
                            text = "${filteredProducts.size} results found for \"$searchQuery\"",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    if (filteredProducts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "🔍", fontSize = 48.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isHindi) "कोई उत्पाद नहीं मिला" else "No matching products found",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Try searching for milk, atta, oil or biscuits",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    } else {
                        val pairs = filteredProducts.chunked(2)
                        items(pairs) { pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pair.forEach { prod ->
                                    val qty = cartProducts.find { it.first.id == prod.id }?.second ?: 0
                                    val isWish = wishlistIds.contains(prod.id)
                                    ProductCard(
                                        product = prod,
                                        quantityInCart = qty,
                                        isWishlisted = isWish,
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

        // Voice Search Dialog
        if (showVoiceDialog) {
            AlertDialog(
                onDismissRequest = { showVoiceDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = StoreGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isHindi) "बोलकर खोजें" else "Voice Search")
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = StoreGreenPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Listening... Speak now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "e.g., \"Amul Milk\", \"Tata Salt\", \"Atta\"",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Amul Milk", "Aashirvaad Atta", "Tomatoes").forEach { phrase ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF3F4F6))
                                        .clickable {
                                            viewModel.searchQuery.value = phrase
                                            showVoiceDialog = false
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "🗣️ $phrase", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showVoiceDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
