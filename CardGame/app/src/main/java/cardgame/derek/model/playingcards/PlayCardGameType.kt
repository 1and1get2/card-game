package cardgame.derek.model.playingcards

import cardgame.derek.model.GameType

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

    override fun revealCard(card: PlayCard): Int = -1

    override fun shouldCheckMatch(vararg cards: PlayCard): Boolean = cards.size == 2

    override fun checkMatch(vararg cards: PlayCard): Int {
        if (cards.size == 2) {
            if (cards[0].rank == cards[1].rank) return 16
            if (cards[0].suit == cards[1].suit) return 4
        }
        return -2
    }
}