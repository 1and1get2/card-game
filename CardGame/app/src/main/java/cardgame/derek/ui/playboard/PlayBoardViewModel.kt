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
import kotlin.system.measureNanoTime

/**
 * User: derek
 * Date: 10/10/18 10:26 PM
 */

class PlayBoardViewModel(private val context: Application) : AndroidViewModel(context) {

    companion object {
        const val REVEAL_DELAY = 1000L
        const val MATCH_DELAY = 400L // delay before grey out matched cards
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

    val matchEndedEvent = SingleLiveEvent<Nothing>()

    val gameNotStartedMessageEvent = SingleLiveEvent<String>()

    private var mPendingHideCard = AtomicBoolean(false)

    private var mPendingCardRefresh = false



    fun flipCard(card: Card) : Boolean {
        if (gameStopped.value == true) {
            gameNotStartedMessageEvent.call("Press start to play")
            return false
        }
        if (mPendingHideCard.get()) return false
        // ignore on matched cards
        if (card.matched) return false

        card.flipped = !card.flipped

        val cards = cards.value!!
        val game = gameType.value!!

        if (game.shouldCheckMatch(cards)) {
            val checkResult = game.checkMatch(cards)
            val matchedCards = checkResult.first
            updateScore(checkResult.second)

            if (matchedCards == null || matchedCards.isEmpty()) {
                // no matched cards, hides all others after delay except for the currently tabbed one
                card.flipped = false
                mPendingCardRefresh = true
                revealCardsTemporary(hideCards = cards.filter { !it.matched && it != card })
            } else {
                // matches, mark as matched
                mPendingCardRefresh = true
                HANDLER.postDelayed(MATCH_DELAY) {
                    refreshCards{matchedCards.forEach{it.matched = true; it.flipped = false} }

                    var end = false

                    measureNanoTime {
                        end = game.checkNoMoreMatches(cards)
                    }.also {
                        Timber.d("calculating end of match took:$it Nanoseconds")
                    }

                    if (end) {
                        Timber.d("NoMoreMatches")
                        matchEndedEvent.call()
                    }
                }
            }
        } else {
            if (!card.flipped) { // the card is now revealed
                // reveal a card
                updateScore(game.revealCard(card))
            }

            mPendingCardRefresh = true
        }

        if (mPendingCardRefresh) {
            refreshCards()
            mPendingCardRefresh = false
        }

        return true
    }

    private fun revealCardsTemporary(reveal: Card? = null, hideCards: List<Card>, delay: Long = REVEAL_DELAY) {
        reveal?.let { refreshCards {it.flipped = false} }
        mPendingHideCard.set(true)
        HANDLER.postDelayed(delay){
            refreshCards {
                hideCards.forEach{ it.flipped = true }
                mPendingHideCard.set(false)
            }
        }
    }

    private fun refreshCards(run: (() -> Unit)? = null) {
        if (run != null) { run() }
        cards.value = cards.value
    }

    private fun updateScore(change: Int) {
        score.value = score.value!!.plus(change)
    }

    private fun getNewCards() {
        cards.value = gameType.value?.getCards()
                ?.apply {
                    // sanity check, make sure the cards are clean and flipped from previous games
                    // refer to card.getCards()
                    forEach {
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
        score.postValue(0)
        gameStopped.value = false
        getNewCards()
    }


    fun onStart() {
        if (gameType.value == null) {
            selectGameType()
        }
    }

}