package cardgame.derek.model.playingcards

import android.graphics.Color
import androidx.annotation.ColorInt
import cardgame.derek.model.Card
import cardgame.derek.model.CardCompanionMethods
import cardgame.derek.util.random

/**
 * User: derek
 * Date: 10/10/18 12:16 PM
 */


data class PlayCard(val suit: Char, val rank: String) : Card() {


    companion object : CardCompanionMethods<PlayCard> {
        private val suits : Array<Char> get() = arrayOf('♠', '♥', '♦', '♣')
        private val ranks get() = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")

        // 13 * 4 = 52
        override val totalSize: Int get() = suits.size * ranks.size

                // all the available cards
        val cards by lazy {
            val cards = ArrayList<PlayCard>()
            for (suit in suits) {
                for (rank in ranks) { cards.add(PlayCard(suit = suit, rank = rank)) }
            }
            cards
        }


        override fun getCards(total : Int): List<PlayCard> {
            if (total >= totalSize) throw IndexOutOfBoundsException("x * y = $total and exceeded total size: $totalSize")
            return cards.shuffled(random = random).subList(0, total)
        }
    }

    override val cardFrontText: String get() = "$rank$suit"

    @get:ColorInt
    override val cardFrontTextColor: Int get() = Color.BLACK
}