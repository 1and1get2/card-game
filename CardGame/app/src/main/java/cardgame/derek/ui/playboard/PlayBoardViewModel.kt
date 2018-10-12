package cardgame.derek.ui.playboard

import android.app.Application
import android.os.Handler
import androidx.core.os.postDelayed
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cardgame.derek.BuildConfig
import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.model.playingcards.PlayCardGameType
import cardgame.derek.model.sets.SetsGameType
import cardgame.derek.util.SingleLiveEvent
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * User: derek
 * Date: 10/10/18 10:26 PM
 */

class PlayBoardViewModel(private val context: Application) : AndroidViewModel(context) {

    companion object {
        const val REVEAL_DELAY = 1500L
        const val MATCH_DELAY = 500L // delay before greyout matched cards
        val HANDLER = Handler()
        val GAME_TYPES : Array<GameType<out Card>> by lazy { arrayOf(PlayCardGameType(), SetsGameType()) }
    }

    private val gameTypes get() = PlayBoardViewModel.GAME_TYPES

    val gameStopped = MutableLiveData<Boolean>().apply { value = true }

    val gameType = MutableLiveData<GameType<out Card>>()

    val cards = MutableLiveData<List<Card>>()

    val score = MutableLiveData<Int>().apply { value = 0 }

    val gameTypeSelectEvent = SingleLiveEvent<Array<GameType<out Card>>>()

    val grid : Pair<Int, Int>? get() = gameType.value?.grid

    var mPending = AtomicBoolean(false)


    var index = 0

    fun flipCard(card: Card) : Boolean {

        if (mPending.get()) return false
        // ignore on matched cards
        if (card.matched) return false

        var currentCards = cards.value!!.filter { !it.matched && (!it.flipped || it == card) }

        val shouldCheckMatch = gameType.value!!.shouldCheckMatch(currentCards)
        Timber.d("current cards:$currentCards, shouldCheckMatch:$shouldCheckMatch")
        if (shouldCheckMatch) {
            val ifMatch = gameType.value!!.checkMatch(currentCards)
            updateScore(ifMatch.second)
            if (ifMatch.first) {
                refreshCards{ card.flipped = !card.flipped }
                HANDLER.postDelayed(MATCH_DELAY) {
                    refreshCards{
                        currentCards.forEach{it.matched = true}
                    }
                }
            } else {
                // flip cards after delay
                revealCardsTemporary(card, currentCards.filter { it != card })
            }
        } else {
            if (card.flipped) {
                // reveal a card
                updateScore(gameType.value!!.revealCard(card))
            }
            refreshCards{ card.flipped = !card.flipped }
//            HANDLER.postDelayed({refreshCards{ card.flipped = !card.flipped }}, REVEAL_DELAY)
        }

/*        score.value!!.let {
            val gaT: GameType<out Card> = PlayCardGameType()
            val cards = gaT.getCards()
            gaT.revealCard(card)
            gaT.shouldCheckMatch(cards = cards)
            val a: Int = gameType.value?.revealCard(card) ?: 0
            score.value = it + a
        }*/

//        card.flipped = !card.flipped
//        cards.value = cards.value
        return true
    }

    private fun revealCardsTemporary(reveal: Card, reHideCards: List<Card>, delay: Long = REVEAL_DELAY) {
        refreshCards {reveal.flipped = false}
        mPending.set(true)
        HANDLER.postDelayed(delay){
            refreshCards {
                reHideCards.forEach{ it.flipped = true }
                mPending.set(false)
            }
        }
    }

    private fun refreshCards(run: (() -> Unit)? = null) {
        if (run != null) {
            run()
        }
        cards.value = cards.value
    }

    private fun updateScore(change: Int) {
        score.value = score.value!!.plus(change)
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