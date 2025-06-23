package com.example.kangmakan

data class MenuCategory(
    val id: String,
    val name: String,
    val emoji: String,
    val items: List<MenuItem>
)

data class MenuItem(
    val title: String,
    val price: Int,
    val imageRes: Int,
    val category: String
)

object MenuData {
    val categories = listOf(
        MenuCategory(
            id = "food",
            name = "MAKANAN",
            emoji = "🍽️",
            items = listOf(
                MenuItem("Beef Sandwich", 22000, R.drawable.beef_sandwich, "food"),
                MenuItem("Butter Milk Pancakes", 23000, R.drawable.buttermilk_pancakes, "food"),
                MenuItem("Samosa", 23000, R.drawable.samosa, "food"),
                MenuItem("Dimsum", 19000, R.drawable.dimsum, "food"),
                MenuItem("Popcorn Chicken", 25000, R.drawable.popcorn_chicken, "food"),
                MenuItem("Waffle", 22000, R.drawable.waffle, "food"),
                MenuItem("French Fries", 17000, R.drawable.french_fries, "food"),
                MenuItem("Onion Ring", 20000, R.drawable.onion_ring, "food"),
                MenuItem("Ayam Bakar", 29000, R.drawable.ayambuakar, "food"),
                MenuItem("Ayam Goreng", 27000, R.drawable.ayamgoweng, "food"),
                MenuItem("Ayam Serundeng", 28000, R.drawable.ayamserundeng, "food"),
                MenuItem("Bebek Goreng", 32000, R.drawable.bebekgoweng, "food"),
                MenuItem("Beef Burger", 35000, R.drawable.beefburger, "food"),
                MenuItem("Beef Ramen", 28000, R.drawable.beeframeng, "food"),
                MenuItem("Chicken Burger", 30000, R.drawable.chickiinburger, "food"),
                MenuItem("Chicken Gimbap", 25000, R.drawable.chickiingimbap, "food"),
                MenuItem("Chicken Rameng", 25000, R.drawable.chickiinramen, "food"),
                MenuItem("Curry Katsu", 28000, R.drawable.currykatsu, "food"),
                MenuItem("Curry Ramen", 24000, R.drawable.curryrameng, "food"),
                MenuItem("Fish & Chips", 25000, R.drawable.fishnchips, "food"),
                MenuItem("Mentai Rice", 24000, R.drawable.mentairice, "food"),
                MenuItem("Mie Goreng", 20000, R.drawable.miegoweng, "food"),
                MenuItem("Nasgor Seafood", 29000, R.drawable.nasgorseafood, "food"),
                MenuItem("Nasi Goreng", 22000, R.drawable.nasigoweng, "food"),
                MenuItem("Pasta Bolognese", 26000, R.drawable.pastabolonyeis, "food"),
                MenuItem("Roasted Chicken", 26000, R.drawable.roastedchickiin, "food"),
                MenuItem("Salmon Sushi", 29000, R.drawable.salmongsushi, "food"),
                MenuItem("Spaghetti Bolognese", 26000, R.drawable.spagolonyeis, "food"),
                MenuItem("Spaghetti Carbonara", 26000, R.drawable.spagcarbonara, "food"),
                MenuItem("Teriyaki", 24000, R.drawable.teriyiaki, "food"),
                MenuItem("Wonton", 19000, R.drawable.wonton, "food")
            )
        ),
        MenuCategory(
            id = "drink",
            name = "MINUMAN",
            emoji = "🥤",
            items = listOf(
                MenuItem("Caramel Macchiato", 25000, R.drawable.caramel_machiato, "drink"),
                MenuItem("Strawberry Milkshake", 22000, R.drawable.strawberry_milkshake, "drink"),
                MenuItem("Iced Americano", 21000, R.drawable.iced_americano, "drink"),
                MenuItem("Mint Lemonade", 19000, R.drawable.mint_lemonade, "drink"),
                MenuItem("Oreo Milkshake", 15000, R.drawable.oreo_milkshake, "drink"),
                MenuItem("Hot Coffee", 25000, R.drawable.hot_coffe, "drink"),
                MenuItem("Raspberry Ice", 19000, R.drawable.raspberry_ice, "drink"),
                MenuItem("Caramel Hazelnut", 23000, R.drawable.caramelhazelnut, "drink"),
                MenuItem("Choco Milkshake", 22000, R.drawable.chomilkshake, "drink"),
                MenuItem("Hot Americano", 20000, R.drawable.hotamericano, "drink"),
                MenuItem("Hot Cappucino", 21000, R.drawable.hotcappucino, "drink"),
                MenuItem("Hot Choco", 19000, R.drawable.hotchoco, "drink"),
                MenuItem("Hot Espresso", 20000, R.drawable.hotespresso, "drink"),
                MenuItem("Hot Matcha", 23000, R.drawable.hotmatcha, "drink"),
                MenuItem("Hot Redvelvet", 21000, R.drawable.hotredvelvet, "drink"),
                MenuItem("Lychee Tea", 17000, R.drawable.lycheetea, "drink"),
                MenuItem("Matcha Espresso", 25000, R.drawable.matchaespresso, "drink"),
                MenuItem("Mojito", 20000, R.drawable.mojito, "drink"),
                MenuItem("Redvelvet Milkshake", 22000, R.drawable.redvelvetshake, "drink"),
                MenuItem("Strawberry Matcha", 24000, R.drawable.strawmatcha, "drink"),
                MenuItem("Thai Tea", 18000, R.drawable.thaitea, "drink"),
                MenuItem("Vanilla Latte", 22000, R.drawable.vanillalatte, "drink"),
                MenuItem("Vanilla Milkshake", 22000, R.drawable.vanillashake, "drink")
            )
        ),
        MenuCategory(
            id = "snack",
            name = "SNACK",
            emoji = "🍿",
            items = listOf(
                MenuItem("Mozzarella Stick", 20000, R.drawable.mozarella, "snack"),
                MenuItem("Churros", 22000, R.drawable.churros, "snack"),
                MenuItem("Baked Sausage", 17000, R.drawable.bakedsausage, "snack"),
                MenuItem("Choco Cromboloni", 22000, R.drawable.chocromboloni, "snack"),
                MenuItem("Cinnamon Roll", 18000, R.drawable.cinammonr, "snack"),
                MenuItem("Cookies", 16000, R.drawable.cookies, "snack"),
                MenuItem("Croissant", 18000, R.drawable.croissant, "snack"),
                MenuItem("Ebi Fry", 19000, R.drawable.ebifry, "snack"),
                MenuItem("Enoki Crispy", 18000, R.drawable.enokicrispy, "snack"),
                MenuItem("Jamur Crispy", 15000, R.drawable.jamurcrispy, "snack"),
                MenuItem("Matcha Cromboloni", 22000, R.drawable.matcromboloni, "snack"),
                MenuItem("Pudding", 15000, R.drawable.pudding, "snack"),
                MenuItem("Waffle Fries", 17000, R.drawable.wafflefries, "snack"),
                MenuItem("Wings Fries", 27000, R.drawable.wingsfries, "snack")
            )
        )
    )

    fun getAllItems(): List<MenuItem> {
        return categories.flatMap { it.items }
    }

    fun getItemsByCategory(categoryId: String): List<MenuItem> {
        return categories.find { it.id == categoryId }?.items ?: emptyList()
    }

    fun getCategoryById(categoryId: String): MenuCategory? {
        return categories.find { it.id == categoryId }
    }

    fun searchItems(query: String): List<MenuItem> {
        return getAllItems().filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}