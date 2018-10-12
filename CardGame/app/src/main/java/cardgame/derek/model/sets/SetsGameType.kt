package cardgame.derek.model.sets

import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.util.asListOfType
import timber.log.Timber

/**
 * User: derek
 * Date: 10/10/18 1:36 PM
 */
class SetsGameType : GameType<SetsCard> {
    override val name: String get() = "Sets"
    override val grid: Pair<Int, Int> get() = Pair(first = 4, second = 4)

    companion object {
        const val MATCHING_BINARY_MASK = 1 + 2 + 4 //2.pow(0) or 2.pow(1) or 2.pow(2)

    }

    /**
     * return a 2 dimensional array that represents the card set
     */
    override fun getCards(): List<SetsCard> = SetsCard.getCards(grid.first * grid.second)

    override fun shouldCheckMatch(cards: List<Card>): Boolean = GameType.getAllActiveSelectedCard(cards).size == 3


    override fun checkMatch(cards: List<Card>): Pair<List<Card>?, Int> {
        val current = GameType.getAllActiveSelectedCard(cards)
        if (isSet(castList(current))) {
            return Pair(current, 16)
        }
        return Pair(null, -2)
    }

    private fun castList(cards: List<Card>) : List<SetsCard> =
            cards.asListOfType() ?: throw TypeCastException("unable to cast type to List<SetsCard>")

    override fun revealCard(card: Card): Int = -1


    /**
     * using this algorithm in average takes as long as 7668927 nanoseconds
     * in comparision, to achieve 120fps, a methods should take 8333333 nanoseconds or less
     * so we are fine
     */
    override fun checkNoMoreMatches(cards: List<Card>): Boolean {
        val list = castList(GameType.getAllNotMatchedCard(cards))


        for (i in 0..(list.size - 1)) {
            for (j in (i + 1)..(list.size - 1)) {
                for (k in (j + 1)..(list.size - 1)) {
                    val match = listOf(list[i], list[j], list[k])
                    if (isSet(match)) {
                        Timber.d("Found a match: $match")
                        return false
                    }
                }
            }
        }
        return true
    }

    /**
     * A set consists of three cards which satisfy all of these conditions:
     *      They all have the same number, or they have three different numbers.
     *      They all have the same symbol, or they have three different symbols.
     *      They all have the same shading, or they have three different shadings.
     *      They all have the same color, or they have three different colors.
     *
     */
    private fun isSet(cards: List<SetsCard>) : Boolean {
        if (cards.size == 3) {
            val array = (0..3).map { ArrayList<Int>() }
            for(card in cards) {
                array[0].add(card.color.ordinal)
                array[1].add(card.number.ordinal)
                array[2].add(card.symbol.ordinal)
                array[3].add(card.shading.ordinal)
            }

            for (list in array) {
                val distinctSize = list.distinct().size
                if (distinctSize == 1 || distinctSize == 3) return true
            }
        }
        return false
    }


}