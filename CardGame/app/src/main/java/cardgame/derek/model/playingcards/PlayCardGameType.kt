package cardgame.derek.model.playingcards

import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.util.asListOfType

/**
 * User: derek
 * Date: 10/10/18 1:23 PM
 */

class PlayCardGameType : GameType<PlayCard> {
    override val name: String get() = "Playing cards"
    override val grid: Pair<Int, Int> get() = Pair(first = 4, second = 4)

    /**
     * return a 2 dimensional array that represents the card set
     */
    override fun getCards(): List<PlayCard> = PlayCard.getCards(grid.first * grid.second)

    override fun revealCard(card: Card): Int = -1

    override fun shouldCheckMatch(cards: List<Card>): Boolean = cards.size == 2

    override fun checkMatch(cards: List<Card>): Int {
        castList(cards).also {
            if (it.size == 2) {
                if (it[0].rank == it[1].rank) return 16
                if (it[0].suit == it[1].suit) return 4
            }
        }
        return -2
    }


    private fun castList(cards: List<Card>) : List<PlayCard> =
        cards.asListOfType() ?: throw TypeCastException("unable to cast type to List<SetsCard>")
}