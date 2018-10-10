package cardgame.derek.model.playingcards

import cardgame.derek.model.GameType

/**
 * User: derek
 * Date: 10/10/18 1:23 PM
 */

class PlayCardGameType : GameType<PlayCard> {
    override val grid: Pair<Int, Int> get() = Pair(first = 4, second = 4)

    /**
     * return a 2 dimensional array that represents the card set
     */
    override fun getCards(): Array<Array<PlayCard>> {
        val cards = PlayCard.getCards(grid.first * grid.second)
        return Array(grid.second) { y ->
            Array(grid.first) {x ->
                cards[y * grid.first + x]
            }
        }.also {
            // debug
            assert(it.size == grid.second)
            assert(it[0].size == grid.first)
        }
    }
}