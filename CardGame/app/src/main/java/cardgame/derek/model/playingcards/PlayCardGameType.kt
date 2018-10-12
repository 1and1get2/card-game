package cardgame.derek.model.playingcards

import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.util.asListOfType
import timber.log.Timber

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

    override fun shouldCheckMatch(cards: List<Card>): Boolean = GameType.getAllActiveSelectedCard(cards).size == 2


    override fun checkNoMoreMatches(cards: List<Card>): Boolean {
        val list = castList(GameType.getAllNotMatchedCard(cards))
        for (i in 0..(list.size - 1)) {
            for (j in (i + 1)..(list.size - 1)) {
                val matchList = listOf(list[i], list[j])
                if (matches(matchList) > 0) {
                    Timber.d("found matches: $matchList")
                    return false
                }
            }
        }
        return true
    }


    override fun checkMatch(cards: List<Card>): Pair<List<Card>?, Int> {
        val list = GameType.getAllActiveSelectedCard(cards)
        castList(list).also {
            if (it.size == 2) {
                val result = matches(it)
                return Pair(if (result > 0) list else null, result)
            }
        }
        return Pair(null, -2)
    }

    private fun matches(list: List<PlayCard>) : Int {
        if (list.size == 2) {
            if (list[0].rank == list[1].rank) return 16
            if (list[0].suit == list[1].suit) return 4
        }
        return -2
    }

    private fun castList(cards: List<Card>) : List<PlayCard> =
        cards.asListOfType() ?: throw TypeCastException("unable to cast type to List<SetsCard>")
}