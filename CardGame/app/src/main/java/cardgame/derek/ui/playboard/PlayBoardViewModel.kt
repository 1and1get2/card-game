package cardgame.derek.ui.playboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cardgame.derek.BuildConfig
import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.model.playingcards.PlayCardGameType
import cardgame.derek.model.sets.SetsGameType
import timber.log.Timber

/**
 * User: derek
 * Date: 10/10/18 10:26 PM
 */

class PlayBoardViewModel(private val context: Application) : AndroidViewModel(context) {

    companion object {
        val GAME_TYPES : Array<GameType<out Card>> by lazy { arrayOf(PlayCardGameType(), SetsGameType()) }
    }

    val gameTypes get() = PlayBoardViewModel.GAME_TYPES

    val gameType = MutableLiveData<GameType<out Card>>()

    val cards = MutableLiveData<List<Card>>()

    val score = MutableLiveData<Int>().apply { value = 0 }


    val grid : Pair<Int, Int>? get() = gameType.value?.grid



    fun flipCard(card: Card) {

    }

    private fun getNewCards() {
        //cards.value = gameType.value?.getCards()

        cards.value = gameType.value?.getCards()?.apply {

            // sanity check, make sure the cards are clean and flipped from previous games
            // refer to card.getCards()

            forEach{
                if (BuildConfig.DEBUG) {
                    if (!it.flipped) throw RuntimeException("dirty card found")
                } else { it.flipped = true } // dirty fix
            }
        }

        Timber.d("got new cards: ${cards.value?.size}")
    }

    fun restartGame() {
        getNewCards()
        score.postValue(0)
    }

    fun selectGameType(gameType : GameType<out Card>? = null) {
        this.gameType.value = gameType
        getNewCards()
    }

    fun stopGame() {
        score.value?.let {
            score.value = it + 1
        }
        Timber.d("stopGame score:${score.value}")

//        selectGameType()
    }


    override fun onCleared() {
        super.onCleared()
    }


}