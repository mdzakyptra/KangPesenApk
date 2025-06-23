package com.example.kangmakan

import androidx.compose.runtime.mutableStateListOf

object CartManager {
    val items = mutableStateListOf<CartItem>()

    fun addItem(item: CartItem) {
        val existingItem = items.find { it.title == item.title }
        if (existingItem != null) {
            val index = items.indexOf(existingItem)
            items[index] = existingItem.copy(quantity = existingItem.quantity + item.quantity)
        } else {
            items.add(item)
        }
    }

    fun removeItem(title: String) {
        val existingItem = items.find { it.title == title }
        if (existingItem != null) {
            val index = items.indexOf(existingItem)
            val newQuantity = existingItem.quantity - 1
            if (newQuantity <= 0) {
                items.removeAt(index)
            } else {
                items[index] = existingItem.copy(quantity = newQuantity)
            }
        }
    }

    fun removeItemCompletely(title: String) {
        items.removeAll { it.title == title }
    }

    fun clearCart() {
        items.clear()
    }

    fun getItemQuantity(title: String): Int {
        return items.find { it.title == title }?.quantity ?: 0
    }

    fun getTotalPrice(): Int {
        return items.sumOf { it.price * it.quantity }
    }

    fun getTotalQuantity(): Int {
        return items.sumOf { it.quantity }
    }

    fun getItemsByCategory(category: String): List<CartItem> {
        return items.filter { cartItem ->
            // Find the menu item that matches this cart item
            val menuItem = MenuData.getAllItems().find { it.title == cartItem.title }
            menuItem?.category == category
        }
    }

    fun getCategoryTotal(category: String): Int {
        return getItemsByCategory(category).sumOf { it.price * it.quantity }
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun hasItems(): Boolean {
        return items.isNotEmpty()
    }
}