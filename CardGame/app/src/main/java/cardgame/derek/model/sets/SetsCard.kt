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

enum class Number{ One, Two, Three }

data class SetsCard(val number: Number, val symbol: Symbol, val shading: Shading, val color: Color) : Card() {

    companion object : CardCompanionMethods<SetsCard> {

        // 3 ^ 4 = 81
        override val totalSize: Int get() = Symbol.values().size * Shading.values().size * Color.values().size * Number.values().size

        // all the available cards
        val cards by lazy {
            val cards = ArrayList<SetsCard>()
            for (symbol in Symbol.values()){
                for (shading in Shading.values()) {
                    for (color in Color.values()) {
                        for (num in Number.values()) {
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

    override val cardFrontText: String get() = "${number.name}\n${shading.name}\n${symbol.name}"

    @get:ColorInt
    override val cardFrontTextColor: Int get() = color.color
}