package cn.lancet.navigation.payment.placeholder

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PaymentContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<PaymentItem> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, PaymentItem> = HashMap()

    private val COUNT = 25

    init {
        for (i in 1..COUNT) {
            addItem(createPaymentItem(i))
        }
    }

    private fun addItem(item: PaymentItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createPaymentItem(position: Int): PaymentItem {
        return PaymentItem(position.toString(), "Item $position", makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PaymentItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}