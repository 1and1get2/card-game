package cardgame.derek.model

/**
 * User: derek
 * Date: 10/10/18 1:27 PM
 */
abstract class Card {
    companion object {
        val frontBackgroundName = "CardFrontBackground.png"
        val CardBackBackgroundName = "CardBack.png"
    }


}

interface CardCompanionMethods <C : Card> {

    /**
     * totally available cards
     */
    val totalSize : Int

    /**
     * generate random cards based on game's grid
     */
    fun getCards(total: Int) : List<C>
}