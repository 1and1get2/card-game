package cardgame.derek.model

/**
 * User: derek
 * Date: 10/10/18 1:03 PM
 */
interface GameType<C : Card> {
    val grid: Pair<out Int, out Int>
    val name: String

    fun getCards(): List<C>

    fun shouldCheckMatch(vararg cards: C): Boolean

    fun checkMatch(vararg cards: C): Int

    fun revealCard(card: C): Int
}