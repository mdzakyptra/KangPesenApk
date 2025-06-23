package com.example.kangmakan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import com.example.kangmakan.ui.theme.KangMakanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KangMakanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFCF0)
                ) {
                    MainContentScreen()
                }
            }
        }
    }
}

@Composable
fun MainContentScreen() {
    var showSidebar by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            AppLogoHeader()
            Spacer(modifier = Modifier.height(12.dp))
            TableSelector()
            Spacer(modifier = Modifier.height(12.dp))
            NavigationBarWithSearch(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onMenuClick = { showSidebar = true }
            )

            Spacer(modifier = Modifier.height(12.dp))
            MenuListByCategory(
                listState = listState,
                searchQuery = searchQuery,
                onItemAdded = { itemName ->
                    if (itemName == "ERROR_NO_TABLE") {
                        notificationMessage = "⚠️ Pilih meja terlebih dahulu"
                        showNotification = true
                    } else {
                        notificationMessage = "✅ $itemName ditambahkan ke keranjang"
                        showNotification = true
                    }
                }
            )
        }
        CartFloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        if (showSidebar) {
            CategorySidebar(
                onDismiss = { showSidebar = false },
                onCategoryClick = { categoryIndex ->
                    showSidebar = false
                    searchQuery = ""
                    coroutineScope.launch {
                        val targetIndex = categoryIndex * 2
                        listState.animateScrollToItem(
                            index = targetIndex,
                            scrollOffset = 0
                        )
                    }
                }
            )
        }

        if (showNotification) {
            val isError = notificationMessage.startsWith("⚠️")

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isError) Color(0xFFFFEBEE) else Color(0xFFF0F9FF)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isError) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = if (isError) "Warning" else "Success",
                            tint = if (isError) Color(0xFFE57373) else Color(0xFF64B5F6),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = notificationMessage.removePrefix("✅ ").removePrefix("⚠️ "),
                            color = if (isError) Color(0xFFC62828) else Color(0xFF1976D2),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            LaunchedEffect(showNotification) {
                kotlinx.coroutines.delay(if (isError) 2500 else 1500)
                showNotification = false
            }
        }
    }
}

@Composable
fun NavigationBarWithSearch(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2B47)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu Button
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                placeholder = {
                    Text(
                        text = "Cari menu...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFA1001E),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun AppLogoHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logokp),
            contentDescription = "Logo App",
            modifier = Modifier
                .size(32.dp)
                .padding(end = 6.dp)
        )

        Text(
            text = "KANG ",
            color = Color(0xFFA1001E),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = "PESEN",
            color = Color(0xFF0F2B47),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
fun TableSelector() {
    val context = LocalContext.current
    var selectedTable by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showFinishDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val tableNum = TableManager.getTableNumberOnly(context)
        selectedTable = tableNum
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(45.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA1001E)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "Pilih Meja",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (selectedTable == null) "Pilih Meja" else "Meja $selectedTable",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            onClick = { showFinishDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTable != null) Color(0xFF22C55E) else Color.Gray
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = selectedTable != null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selesai Makan",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Selesai",
                    color = Color.White,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Pilih Nomor Meja")
            },
            text = {
                Column {
                    val tableNumbers = (1..15).toList().chunked(3)
                    tableNumbers.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { number ->
                                Button(
                                    onClick = {
                                        selectedTable = number
                                        TableManager.saveSelectedTableNumber(context, "Meja $number")
                                        showDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedTable == number) {
                                            Color(0xFFA1001E)
                                        } else {
                                            Color(0xFF0F2B47)
                                        }
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(
                                        text = number.toString(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selesai",
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Konfirmasi Selesai Makan",
                        color = Color(0xFF0F2B47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Apakah Anda yakin telah selesai makan?",
                        color = Color(0xFF666666),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Meja: ${selectedTable?.let { "Meja $it" } ?: ""}",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F2B47)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Tampilkan riwayat pesanan untuk pembayaran",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "• Meja akan tersedia untuk pelanggan lain",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "• Keranjang belanja akan dikosongkan",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row {
                    TextButton(onClick = { showFinishDialog = false }) {
                        Text("Batal", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            showFinishDialog = false
                            showPaymentDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
                    ) {
                        Text("Ya, Selesai", color = Color.White)
                    }
                }
            }
        )
    }
    if (showPaymentDialog) {
        PaymentDialog(
            tableNumber = selectedTable?.let { "Meja $it" } ?: "",
            onPaymentComplete = {
                CartManager.clearCart()
                TableManager.clearSelectedTable(context)
                selectedTable = null
                showPaymentDialog = false
            },
            onDismiss = { showPaymentDialog = false }
        )
    }
}

@Composable
fun PaymentDialog(
    tableNumber: String,
    onPaymentComplete: () -> Unit,
    onDismiss: () -> Unit
) {
    val tableOrders = remember {
        HistoryManager.orders.filter { it.tableNumber == tableNumber && it.status != OrderStatus.CANCELLED }
    }

    val totalAmount = tableOrders.sumOf { it.totalPrice }
    var selectedPaymentMethod by remember { mutableStateOf("Tunai") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "Pembayaran",
                    tint = Color(0xFF0F2B47),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pembayaran di Kasir",
                    color = Color(0xFF0F2B47),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📍 $tableNumber",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F2B47),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${tableOrders.size} pesanan",
                                color = Color(0xFF666666),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (tableOrders.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = "No Orders",
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tidak ada pesanan untuk meja ini",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "Rincian Pesanan:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F2B47),
                            fontSize = 14.sp
                        )
                    }

                    items(tableOrders) { order ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = order.dateTime,
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = formatRupiahConsistent(order.totalPrice),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA1001E)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))

                                val itemsText = order.items.take(2).joinToString(", ") {
                                    "${it.title} (${it.quantity}x)"
                                }
                                val remainingCount = order.items.size - 2

                                Text(
                                    text = if (remainingCount > 0) {
                                        "$itemsText... +$remainingCount lainnya"
                                    } else {
                                        itemsText
                                    },
                                    fontSize = 11.sp,
                                    color = Color(0xFF666666),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFA1001E)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Pembayaran:",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = formatRupiahConsistent(totalAmount),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Metode Pembayaran:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F2B47),
                            fontSize = 14.sp
                        )

                        val paymentMethods = listOf("Tunai", "Kartu Debit", "Kartu Kredit", "QRIS", "E-Wallet")

                        paymentMethods.forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMethod = method }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == method,
                                    onClick = { selectedPaymentMethod = method },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFFA1001E)
                                    )
                                )
                                Text(
                                    text = method,
                                    color = Color(0xFF0F2B47),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (tableOrders.isNotEmpty()) {
                                showSuccessDialog = true
                            } else {
                                onPaymentComplete()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        enabled = tableOrders.isNotEmpty()
                    ) {
                        Text("Proses Pembayaran", color = Color.White)
                    }
                }

                if (tableOrders.isEmpty()) {
                    Text(
                        text = "Tidak ada pesanan untuk diproses",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onPaymentComplete()
            },
            containerColor = Color(0xFF03A9F4),
            shape = RoundedCornerShape(16.dp),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sukses",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pembayaran Dikasir!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Terima kasih telah berkunjung!",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total: ${formatRupiahConsistent(totalAmount)}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Metode: $selectedPaymentMethod",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            onPaymentComplete()
                        }
                    ) {
                        Text("Selesai", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}

@Composable
fun CategorySidebar(
    onDismiss: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(260.dp)
                .align(Alignment.CenterStart)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Menu Kategori",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F2B47)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color(0xFF0F2B47),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                MenuData.categories.forEachIndexed { index, category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2B47)),
                        onClick = { onCategoryClick(index) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${category.emoji} ${category.name}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${category.items.size} items",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuListByCategory(
    listState: LazyListState,
    searchQuery: String,
    onItemAdded: (String) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (searchQuery.isNotEmpty()) {
            val searchResults = MenuData.searchItems(searchQuery)

            if (searchResults.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "No Results",
                                modifier = Modifier.size(40.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tidak ada menu yang ditemukan",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Coba kata kunci lain",
                                color = Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            } else {
                item {
                    MenuCategoryHeader("HASIL PENCARIAN (${searchResults.size})")
                }
                item {
                    MenuItemGrid(items = searchResults, onItemAdded = onItemAdded)
                }
            }
        } else {
            MenuData.categories.forEach { category ->
                if (category.items.isNotEmpty()) {
                    item {
                        MenuCategoryHeader(category.name)
                    }
                    item {
                        MenuItemGrid(items = category.items, onItemAdded = onItemAdded)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemGrid(items: List<MenuItem>, onItemAdded: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height((items.size / 2 * 260 + if(items.size % 2 != 0) 260 else 0).dp),
        contentPadding = PaddingValues(3.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(items) { menuItem ->
            FoodItemCard(
                title = menuItem.title,
                price = menuItem.price,
                imageRes = menuItem.imageRes,
                onItemAdded = onItemAdded
            )
        }
    }
}

@Composable
fun MenuCategoryHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFA1001E),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FoodItemCard(title: String, price: Int, imageRes: Int, onItemAdded: (String) -> Unit) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(0) }
    var selectedTable by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        quantity = CartManager.getItemQuantity(title)
        selectedTable = TableManager.getSelectedTableNumber(context)
    }

    LaunchedEffect(selectedTable) {
        selectedTable = TableManager.getSelectedTableNumber(context)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2B47))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = formatRupiahConsistent(price),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFFFF8E1), // Warna cream
                            shape = CircleShape
                        )
                        .clickable(
                            enabled = quantity > 0,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (quantity > 0) {
                                CartManager.removeItem(title)
                                quantity = CartManager.getItemQuantity(title)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Kurangi",
                        tint = if (quantity > 0) Color(0xFF0F2B47) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(2.dp))
                    if (quantity == 0) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Keranjang",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = quantity.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFFFF8E1), // Warna cream
                            shape = CircleShape
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Re-check table selection right before adding
                            val currentTable = TableManager.getSelectedTableNumber(context)
                            if (currentTable.isNotEmpty()) {
                                CartManager.addItem(CartItem(title, price, 1, imageRes))
                                quantity = CartManager.getItemQuantity(title)
                                onItemAdded(title)
                            } else {
                                onItemAdded("ERROR_NO_TABLE")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = Color(0xFF0F2B47), // Dark blue untuk kontras dengan cream
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CartFloatingButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var totalItems by remember { mutableStateOf(0) }

    LaunchedEffect(CartManager.items.size) {
        totalItems = CartManager.getTotalQuantity()
    }

    val cartSize = CartManager.items.size
    LaunchedEffect(cartSize) {
        totalItems = CartManager.getTotalQuantity()
    }

    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = {
                context.startActivity(Intent(context, KeranjangActivity::class.java))
            },
            containerColor = Color(0xFFA1001E),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Keranjang",
                modifier = Modifier.size(20.dp)
            )
        }

        if (totalItems > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 3.dp, y = 3.dp)
                    .size(18.dp)
                    .background(
                        color = Color(0xFFFFF8E1),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (totalItems > 99) "99+" else totalItems.toString(),
                    color = Color(0xFF8D6E63),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun formatRupiahConsistent(price: Int): String {
    val formatter = java.text.DecimalFormat("#,###")
    return "Rp ${formatter.format(price)}"
}