package cardgame.derek.ui.playboard

import cardgame.derek.BasePresenter
import cardgame.derek.BaseView
import cardgame.derek.model.Card
import cardgame.derek.model.GameType

/**
 * User: derek
 * Date: 10/10/18 12:56 PM
 */
interface PlayBoardContract {

    interface View : BaseView<Presenter> {

        fun <C : Card, T : GameType<C>> showGameTypeSelection(gameTypes: List<T>)
        fun updateScore(score: Int)
    }

    interface Presenter : BasePresenter {
        fun <C : Card, T : GameType<C>> gameTypeSelected(gameType: T)

        /**
         * onTab on the card
         *
         * @return true if card can be flipped,
         *  false if card has been matched and thus greyed out
         *
         */
        fun <C : Card> flipCard(card: C): Boolean

        fun restartGame()

        fun stopGame()

    }

}

