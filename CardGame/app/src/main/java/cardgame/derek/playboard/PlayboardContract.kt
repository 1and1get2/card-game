package cardgame.derek.playboard

import cardgame.derek.BasePresenter
import cardgame.derek.BaseView

/**
 * User: derek
 * Date: 10/10/18 12:56 PM
 */
interface PlayboardContract {

    interface View : BaseView<Presenter> {

        fun showGameTypeSelection()
    }

    interface Presenter : BasePresenter {

    }

}