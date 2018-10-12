package cardgame.derek.model

/**
 * User: derek
 * Date: 10/10/18 1:03 PM
 *
 * todo: https://kotlinlang.org/docs/reference/typecasts.html
 */
interface GameType<C : Card> {
    val grid: Pair<Int, Int>
    val size: Int get() = grid.first * grid.second
    val name: String


    fun getCards(): List<C>

    fun shouldCheckMatch(cards: List<Card>): Boolean

    /**
     * check the matching result of given cards
     * @return Pair<Boolean, Int>
     *     Boolean: whether it's a match or not
     *     Int: changes on the score
     */
    fun checkMatch(cards: List<Card>): Pair<List<Card>?, Int>

    /**
     *
     */
    fun revealCard(card: Card): Int

    /**
     * cant find any more matches in remaining cards
     */
    fun checkNoMoreMatches(cards: List<Card>): Boolean



    companion object {
        fun getAllActiveSelectedCard(cards: List<Card>) = cards.filter { !it.matched && !it.flipped }
        fun getAllNotMatchedCard(cards: List<Card>) = cards.filter { !it.matched && it.flipped }
    }
}

