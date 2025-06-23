    package com.example.kangmakan

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.runtime.snapshots.SnapshotStateList
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.kangmakan.ui.theme.KangMakanTheme

    class KeranjangActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                KangMakanTheme {
                    KeranjangScreenContent()
                }
            }
        }
    }

    @Composable
    fun KeranjangScreenContent() {
        val context = LocalContext.current
        val cartItems: SnapshotStateList<CartItem> = remember { CartManager.items }
        val totalPrice by remember { derivedStateOf { CartManager.getTotalPrice() } }
        val showConfirmDialog = remember { mutableStateOf(false) }
        val showSuccessDialog = remember { mutableStateOf(false) }
        val showHistoryDialog = remember { mutableStateOf(false) }

        val selectedTableNumber by remember {
            mutableStateOf(TableManager.getSelectedTableNumber(context))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFCF0))
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            KeranjangHeader(onHistoryClick = { showHistoryDialog.value = true })

            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Keranjang Kosong",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Keranjang Kosong",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Silakan pilih menu favorit Anda",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                if (selectedTableNumber.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                    ) {
                        Text(
                            text = "📍 $selectedTableNumber",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F2B47)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    items(cartItems) { item ->
                        KeranjangItemCard(item)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 160.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(cartItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F2B47)
                            )
                            Text(
                                text = "${formatRupiah(item.price)} x${item.quantity}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFA1001E)
                            )
                        }
                    }
                }

                Text(
                    text = "Total: ${formatRupiah(totalPrice)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFF0F2B47)
                )

                Button(
                    onClick = {
                        when {
                            selectedTableNumber.isEmpty() -> {
                            }
                            cartItems.isEmpty() -> {
                            }
                            else -> {
                                showConfirmDialog.value = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTableNumber.isNotEmpty() && cartItems.isNotEmpty()) {
                            Color(0xFFA1001E)
                        } else {
                            Color.Gray
                        }
                    ),
                    enabled = selectedTableNumber.isNotEmpty() && cartItems.isNotEmpty()
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Konfirmasi")
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (selectedTableNumber.isEmpty()) {
                            "Pilih Meja Terlebih Dahulu"
                        } else {
                            "Konfirmasi Pesanan"
                        },
                        color = Color.White
                    )
                }
            }
        }

        if (showConfirmDialog.value) {
            OrderConfirmationDialog(
                onConfirm = {
                    showConfirmDialog.value = false
                    showSuccessDialog.value = true
                },
                onDismiss = { showConfirmDialog.value = false }
            )
        }

        if (showSuccessDialog.value) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog.value = false },
                containerColor = Color(0xFF3B82F6),
                shape = RoundedCornerShape(16.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sukses",
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pesanan Dikonfirmasi!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                text = {
                    Text(
                        text = "Terima kasih! Pesanan Anda sedang diproses.",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog.value = false }) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }

        if (showHistoryDialog.value) {
            OrderHistoryDialog(onDismiss = { showHistoryDialog.value = false })
        }
    }

    @Composable
    fun KeranjangHeader(onHistoryClick: () -> Unit) {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = "KANG ",
                    color = Color(0xFFA1001E),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = "PESEN",
                    color = Color(0xFF0F2B47),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onHistoryClick) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History Pemesanan",
                        tint = Color(0xFF0F2B47),
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { CartManager.clearCart() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Semua",
                        tint = Color(0xFFA1001E),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .clickable {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            context.startActivity(intent)
                        }
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(Color(0xFFA1001E))
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun KeranjangItemCard(item: CartItem) {
        val itemTotal = item.quantity * item.price

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xFF0F2B47), shape = MaterialTheme.shapes.medium)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 12.dp)
                )

                Column {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${formatRupiah(item.price)} / pcs",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                    Text(
                        text = "Subtotal: ${formatRupiah(itemTotal)}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (item.quantity > 1) {
                            CartManager.removeItem(item.title)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Kurangi",
                        tint = Color.White
                    )
                }

                Text(
                    text = "${item.quantity}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = Color.White
                )

                IconButton(
                    onClick = {
                        CartManager.addItem(item.copy(quantity = 1))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun OrderConfirmationDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        val context = LocalContext.current
        val cartItems = CartManager.items
        val totalPrice = CartManager.getTotalPrice()
        val selectedTable = TableManager.getSelectedTableNumber(context)

        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "Order Summary",
                        tint = Color(0xFF0F2B47),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Konfirmasi Pesanan",
                        color = Color(0xFF0F2B47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Meja:",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F2B47)
                        )
                        Text(
                            text = selectedTable,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA1001E)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Pesanan:",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F2B47)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(cartItems) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.title} (${item.quantity}x)",
                                    fontSize = 14.sp,
                                    color = Color(0xFF666666),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = formatRupiah(item.price * item.quantity),
                                    fontSize = 14.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF0F2B47)
                        )
                        Text(
                            text = formatRupiah(totalPrice),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFA1001E)
                        )
                    }
                }
            },
            confirmButton = {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            HistoryManager.addOrder(
                                tableNumber = selectedTable,
                                cartItems = cartItems.toList(),
                                totalPrice = totalPrice
                            )
                            CartManager.clearCart()
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA1001E))
                    ) {
                        Text("Konfirmasi", color = Color.White)
                    }
                }
            }
        )
    }

    @Composable
    fun OrderHistoryDialog(onDismiss: () -> Unit) {
        val historyOrders by remember { mutableStateOf(HistoryManager.orders) }

        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = Color(0xFF0F2B47),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "History Pemesanan",
                        color = Color(0xFF0F2B47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${historyOrders.size} pesanan",
                        color = Color(0xFF666666),
                        fontSize = 12.sp
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (historyOrders.isEmpty()) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "No History",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Belum ada history pemesanan",
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Konfirmasi pesanan untuk melihat history",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(historyOrders) { order ->
                            RealHistoryOrderCard(order)
                        }
                    }
                }
            },
            confirmButton = {
                Row {
                    if (historyOrders.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                HistoryManager.clearHistory()
                            }
                        ) {
                            Text("Hapus Semua", color = Color(0xFFA1001E))
                        }
                    }
                    TextButton(onClick = onDismiss) {
                        Text("Tutup", color = Color(0xFF0F2B47))
                    }
                }
            }
        )
    }

    @Composable
    fun RealHistoryOrderCard(order: HistoryOrder) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = order.dateTime,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = order.tableNumber,
                            fontSize = 14.sp,
                            color = Color(0xFF0F2B47),
                            fontWeight = FontWeight.Medium
                        )

                        Box(
                            modifier = Modifier
                                .background(
                                    color = when(order.status) {
                                        OrderStatus.COMPLETED -> Color(0xFF22C55E)
                                        OrderStatus.PENDING -> Color(0xFFF59E0B)
                                        OrderStatus.PREPARING -> Color(0xFF3B82F6)
                                        OrderStatus.READY -> Color(0xFF8B5CF6)
                                        OrderStatus.CANCELLED -> Color(0xFFEF4444)
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when(order.status) {
                                    OrderStatus.COMPLETED -> "Selesai"
                                    OrderStatus.PENDING -> "Menunggu"
                                    OrderStatus.PREPARING -> "Diproses"
                                    OrderStatus.READY -> "Siap"
                                    OrderStatus.CANCELLED -> "Dibatal"
                                },
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formatRupiah(order.totalPrice),
                            fontSize = 14.sp,
                            color = Color(0xFFA1001E),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${order.items.sumOf { it.quantity }} item",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val itemsText = order.items.take(3).joinToString(", ") {
                    "${it.title} (${it.quantity}x)"
                }
                val remainingItems = order.items.size - 3

                Text(
                    text = if (remainingItems > 0) {
                        "$itemsText... +$remainingItems lainnya"
                    } else {
                        itemsText
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    maxLines = 2
                )
            }
        }
    }

    fun formatRupiah(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return "Rp ${formatter.format(price)}"
    }