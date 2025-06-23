package com.example.kangmakan

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.*

data class HistoryOrder(
    val id: String = UUID.randomUUID().toString(),
    val dateTime: String,
    val tableNumber: String,
    val totalPrice: Int,
    val items: List<HistoryItem>,
    val status: OrderStatus = OrderStatus.COMPLETED
)

data class HistoryItem(
    val title: String,
    val price: Int,
    val quantity: Int,
    val imageRes: Int
)

enum class OrderStatus {
    PENDING,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}

object HistoryManager {
    val orders = mutableStateListOf<HistoryOrder>()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("id", "ID"))

    fun addOrder(tableNumber: String, cartItems: List<CartItem>, totalPrice: Int) {
        val currentDateTime = dateFormat.format(Date())
        val historyItems = cartItems.map { cartItem ->
            HistoryItem(
                title = cartItem.title,
                price = cartItem.price,
                quantity = cartItem.quantity,
                imageRes = cartItem.imageRes
            )
        }

        val newOrder = HistoryOrder(
            dateTime = currentDateTime,
            tableNumber = tableNumber,
            totalPrice = totalPrice,
            items = historyItems
        )
        orders.add(0, newOrder)
        if (orders.size > 50) {
            orders.removeAt(orders.size - 1)
        }
    }

    fun getRecentOrders(limit: Int = 10): List<HistoryOrder> {
        return orders.take(limit)
    }

    fun getOrdersByDate(date: String): List<HistoryOrder> {
        return orders.filter { it.dateTime.startsWith(date) }
    }

    fun getTotalOrdersToday(): Int {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
        return orders.count { it.dateTime.startsWith(today) }
    }

    fun getTotalRevenueToday(): Int {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
        return orders.filter { it.dateTime.startsWith(today) }
            .sumOf { it.totalPrice }
    }

    fun clearHistory() {
        orders.clear()
    }

    fun removeOrder(orderId: String) {
        orders.removeAll { it.id == orderId }
    }

    fun getOrderById(orderId: String): HistoryOrder? {
        return orders.find { it.id == orderId }
    }

    fun getMostOrderedItems(): List<Pair<String, Int>> {
        val itemCounts = mutableMapOf<String, Int>()

        orders.forEach { order ->
            order.items.forEach { item ->
                itemCounts[item.title] = itemCounts.getOrDefault(item.title, 0) + item.quantity
            }
        }

        return itemCounts.toList().sortedByDescending { it.second }.take(5)
    }

}