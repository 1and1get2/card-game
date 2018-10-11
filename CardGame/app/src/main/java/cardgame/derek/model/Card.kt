package cardgame.derek.model

import androidx.annotation.ColorInt
import cardgame.derek.R

/**
 * User: derek
 * Date: 10/10/18 1:27 PM
 */
abstract class Card {
    companion object {
        const val CARD_FRONT_BACKGROUND = R.drawable.cardfront_background
        const val CARD_BACK_BACKGROUND = R.drawable.cardback
    }

    var flipped = true

    abstract val cardFrontText: String

    @get:ColorInt abstract val cardFrontTextColor: Int
}

interface CardCompanionMethods <C : Card> {

    /**
     * totally available cards
     */
    val totalSize : Int

    /**
     * generate random cards based on game's grid
     */
    fun getCards(total: Int) : List<C>
}