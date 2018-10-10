package cardgame.derek.ui.playboard

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import cardgame.derek.model.Card

/**
 * User: derek
 * Date: 10/10/18 10:26 PM
 */

class PlayBoardViewModel<C : Card>(private val context: Application) : AndroidViewModel(context) {

    val score = ObservableInt(0)
    val cards = ObservableField<Array<Array<C>>>()



}