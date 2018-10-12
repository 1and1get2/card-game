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


    var matched = false

    /**
     * card-back upfront
     */
    var flipped = true

    // this is for testing only, we shouldn't need to worry about the index
    var index = -1

    abstract val cardFrontText: String

    @get:ColorInt abstract val cardFrontTextColor: Int

    override fun toString(): String = "${javaClass.simpleName} flipped:$flipped index:$index"
}

interface CardCompanionMethods <C : Card> {

    /**
     * totally available cards
     */
    val totalSize : Int

    /**
     * generate random cards based on game's grid
     * @return new list
     */
    fun getCards(total: Int) : List<C>
}