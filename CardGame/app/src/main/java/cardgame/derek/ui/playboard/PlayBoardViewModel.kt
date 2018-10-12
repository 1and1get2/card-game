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

    val gameStopped = MutableLiveData<Boolean>().apply { value = true }

    val gameType = MutableLiveData<GameType<out Card>>()

    val cards = MutableLiveData<List<Card>>()

    val score = MutableLiveData<Int>().apply { value = 0 }

    val gameTypeSelectEvent = SingleLiveEvent<Array<GameType<out Card>>>()

    val grid : Pair<Int, Int>? get() = gameType.value?.grid


    var index = 0

    fun flipCard(card: Card) : Boolean {
        var list = cards.value!!.filter { !card.flipped || it == card }
/*        val cs = gameType.value!!.getCards()
        cs[(index++) % gameType.value!!.size].flipped = false
//        card.flipped = cards
        cards.value = cs
        val flipped = cs.filter { !it.flipped }
        Timber.d("flipCard: ${flipped}")*/

        card.flipped = !card.flipped
        cards.value = cards.value
/*        score.value!!.let {
            val gaT: GameType<out Card> = PlayCardGameType()
            val cards = gaT.getCards()
            gaT.revealCard(card)
            gaT.shouldCheckMatch(cards = cards)
            val a: Int = gameType.value?.revealCard(card) ?: 0
            score.value = it + a
        }*/
        return true
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
        index = 0
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
        if (gameStopped.value == false) {
            stopGame()
        } else {
            startGame()
        }
    }

    fun stopGame() {
        gameStopped.value = true
        Timber.d("stopGame score:${score.value}")
    }

    fun startGame() {
        if (gameType.value == null) {
            selectGameType()
        }
        gameStopped.value = false
        getNewCards()
    }


    fun onStart() {
        selectGameType()
    }


    override fun onCleared() {
        super.onCleared()
    }


}