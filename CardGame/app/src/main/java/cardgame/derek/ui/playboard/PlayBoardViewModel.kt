package cardgame.derek.ui.playboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cardgame.derek.BuildConfig
import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.model.playingcards.PlayCardGameType
import cardgame.derek.model.sets.SetsGameType
import cardgame.derek.util.SingleLiveEvent
import timber.log.Timber

/**
 * User: derek
 * Date: 10/10/18 10:26 PM
 */

class PlayBoardViewModel(private val context: Application) : AndroidViewModel(context) {

    companion object {
        val GAME_TYPES : Array<GameType<out Card>> by lazy { arrayOf(PlayCardGameType(), SetsGameType()) }
    }

    private val gameTypes get() = PlayBoardViewModel.GAME_TYPES

    val gameStoped = MutableLiveData<Boolean>().apply { value = true }

    val gameType = MutableLiveData<GameType<out Card>>()

    val cards = MutableLiveData<List<Card>>()

    val score = MutableLiveData<Int>().apply { value = 0 }

    val gameTypeSelectEvent = SingleLiveEvent<Array<GameType<out Card>>>()

    val grid : Pair<Int, Int>? get() = gameType.value?.grid



    fun flipCard(card: Card) {
        var list = cards.value?.filter { !card.flipped }
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

    fun setGameType(gameType : GameType<out Card>? = null) {
        this.gameType.value = gameType
        stopGame()
        getNewCards()
    }

    fun selectGameType() {
        gameTypeSelectEvent.call(gameTypes)
    }

    fun startOrStopGame() {
        if (gameStoped.value == false) {
            stopGame()
        } else {
            startGame()
        }
    }

    fun stopGame() {
        gameStoped.value = true
        Timber.d("stopGame score:${score.value}")
    }

    fun startGame() {
        if (gameType.value == null) {
            selectGameType()
        }
        gameStoped.value = false
        getNewCards()
    }


    fun onStart() {
        selectGameType()
    }


    override fun onCleared() {
        super.onCleared()
    }


}