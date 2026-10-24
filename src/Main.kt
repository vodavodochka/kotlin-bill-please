fun main() {
    val kotlinBook = Product(
        id = 1,
        name = "Kotlin in Action",
        price = 1_500.0,
        category = Category.BOOKS,
    )

    val coffee = Product(
        id = 2,
        name = "Coffee",
        price = 500.0,
        category = Category.FOOD,
    )

    val headphones = Product(
        id = 3,
        name = "Headphones",
        price = 8_000.0,
        category = Category.ELECTRONICS,
    )

    val customer = Customer(
        name = "Иван",
        email = "ivan@example.com",
        discount = 0.10,
    )

    val orders = listOf(
        Order(
            id = 1,
            customer = customer,
            items = listOf(
                OrderItem(kotlinBook, count = 2),
                OrderItem(coffee, count = 1),
            ),
            status = OrderStatus.Paid(
                transactionId = "TX-123",
            ),
        ),
    )

    //orders.forEach { order -> println(order.status)}

    //val x = 1_500.0

    println(orders.toReceipt())
}

fun List<Order>.toReceipt(): String {
    val sb = StringBuilder()

    for ((id, customer, items, status) in this) {
        sb.append("Заказ #$id\n")
        sb.append("Покупатель: ${customer.name}\n")

        val statusText = when (status) {
            is OrderStatus.Created -> "Создан\n"
            is OrderStatus.Paid -> "Оплачен, транзакция: ${status.transactionId}\n"
            is OrderStatus.Cancelled -> "Отменен, причина: ${status.reason}\n"
            is OrderStatus.Delivered -> "Доставлен\n"
        }

        sb.append(statusText)
        sb.append("\n")

        var total: Double = 0.0

        for (item in items) {
            val price: Double = item.count * item.product.price
            sb.append("${item.product.name} * ${item.count} = ${price}\n")
            total += price
        }

        sb.append("\n")

        val discount = customer.discount

        sb.append("Скидка: ${discount * 100}%\n")
        sb.append("Итого: ${total - total * discount}\n")
    }
    return sb.toString()
}

data class Product(val id: Int, val name: String, val price: Double, val category: Category) {}
data class Customer(val name: String, val email: String, val discount: Double) {}
data class OrderItem(val product: Product, var count: Int) {}
data class Order(val id: Int, val customer: Customer, val items: List<OrderItem>, val status: OrderStatus) {}

sealed class OrderStatus() {
    object Created : OrderStatus()
    data class Paid(val transactionId: String) : OrderStatus()
    data class Cancelled(val reason: String) : OrderStatus()
    object Delivered : OrderStatus()
}

enum class Category {
    FOOD,
    ELECTRONICS,
    BOOKS,
    OTHER;
}