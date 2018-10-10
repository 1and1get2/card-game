package cardgame.derek.model.sets

import cardgame.derek.model.GameType

/**
 * User: derek
 * Date: 10/10/18 1:36 PM
 */
class SetsGameType : GameType<SetsCard> {
    override val name: String get() = "Sets"
    override val grid: Pair<Int, Int> get() = Pair(first = 4, second = 4)


    /**
     * return a 2 dimensional array that represents the card set
     */
    override fun getCards(): List<SetsCard> = SetsCard.getCards(grid.first * grid.second)

    override fun shouldCheckMatch(vararg cards: SetsCard): Boolean = cards.size == 3

    override fun checkMatch(vararg cards: SetsCard): Int {
        return if (isSet(*cards)) 16 else -2
    }

    /**
     * A set consists of three cards which satisfy all of these conditions:
     *      They all have the same number, or they have three different numbers.
     *      They all have the same symbol, or they have three different symbols.
     *      They all have the same shading, or they have three different shadings.
     *      They all have the same color, or they have three different colors.
     *
     *      todo: wip
     */
    private fun isSet(vararg cards: SetsCard) : Boolean {
        if (cards.size == 3) {
            /*var number = 0
            var symbol = 0
            var shading = 0
            var color = 0
            for (card in cards) {
                number = number or 2.pow(card.number)
                symbol = symbol or 2.pow(card.symbol.ordinal)
                shading = shading or 2.pow(card.shading.ordinal)
                color = color or 2.pow(card.color.ordinal)
            }*/
            if (cards[0].color == cards[1].color && cards[1].color == cards[2].color) return true
            if (cards[0].number == cards[1].number && cards[1].number == cards[2].number) return true
            if (cards[0].symbol == cards[1].symbol && cards[1].symbol == cards[2].symbol) return true
            if (cards[0].shading == cards[1].shading && cards[1].shading == cards[2].shading) return true
        }
        return false
    }

    override fun revealCard(card: SetsCard): Int = -1


}