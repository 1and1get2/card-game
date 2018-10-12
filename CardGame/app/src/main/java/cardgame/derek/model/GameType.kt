package cardgame.derek.model

/**
 * User: derek
 * Date: 10/10/18 1:03 PM
 *
 * todo: https://kotlinlang.org/docs/reference/typecasts.html
 */
interface GameType<C : Card> {
    val grid: Pair<out Int, out Int>
    val size: Int get() = grid.first * grid.second
    val name: String

    fun getCards(): List<C>

    fun shouldCheckMatch(cards: List<Card>): Boolean

    fun checkMatch(cards: List<Card>): Int

    fun revealCard(card: Card): Int
}