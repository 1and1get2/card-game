package cardgame.derek.model.sets

import androidx.annotation.ColorInt
import cardgame.derek.model.Card
import cardgame.derek.model.CardCompanionMethods
import cardgame.derek.util.random

/**
 * User: derek
 * Date: 10/10/18 1:09 PM
 */
enum class Symbol{ Diamond, Squiggle, Oval }

enum class Shading{ Solid, Striped, Open }

enum class Color(@ColorInt val color: Int) {
    Red(android.graphics.Color.RED),
    Green(android.graphics.Color.GREEN),
    Purple(android.graphics.Color.parseColor("#551a8b")),
}

data class SetsCard(val number: Int, val symbol: Symbol, val shading: Shading, val color: Color) : Card() {

    companion object : CardCompanionMethods<SetsCard> {
        private val numbers = arrayOf(1, 2, 3)

        // 3 ^ 3 = 81
        override val totalSize: Int get() = Symbol.values().size * Shading.values().size * Color.values().size * numbers.size

        // all the available cards
        val cards by lazy {
            val cards = ArrayList<SetsCard>()
            for (symbol in Symbol.values()){
                for (shading in Shading.values()) {
                    for (color in Color.values()) {
                        for (num in numbers) {
                            cards.add(SetsCard(number = num, symbol = symbol, shading = shading, color = color))
                        }
                    }
                }
            }
            cards
        }

        override fun getCards(total : Int): List<SetsCard> {
            if (total >= totalSize) throw IndexOutOfBoundsException("x * y = $total and exceeded total size: $totalSize")
            return cards.shuffled(random = random).subList(0, total)
        }
    }
}