package cardgame.derek.model

/**
 * User: derek
 * Date: 10/10/18 1:03 PM
 */
interface GameType <C : Card> {
    val grid: Pair<out Int, out Int>

    companion object {
        val a = "A"
    }

    fun getCards() : Array<Array<C>>


}