package cardgame.derek.ui.playboard

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import cardgame.derek.model.Card
import timber.log.Timber


/**
 * User: derek
 * Date: 10/10/18 8:10 PM
 */

class CardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, val viewModel: PlayBoardViewModel, val index: Int = -1)
    : CardView(context, attrs) {

    companion object {
        const val ANIMATION_DURATION: Long = 200
    }

    private val backgroundImageView = ImageView(context)
    private val frontTextView = TextView(context)

    private val animatorSet = AnimatorSet().apply {
        playSequentially(
                ObjectAnimator.ofFloat(this@CardView, "scaleX", 1f, 0f)
                        .apply { addListener(onStart = {
                            Timber.d("animatorSet ani:1 onStart")
                        }, onEnd = {
                            Timber.d("animatorSet ani:1 onEnd")
                            flipped = !flipped
                            invalidate()
                        }) },
                ObjectAnimator.ofFloat(this@CardView, "scaleX", 0f, 1f)
                        .apply { addListener(onStart = {
                            Timber.d("animatorSet ani:2 onStart")
                        }, onEnd = {
                            Timber.d("animatorSet ani:2 onEnd")
                        })}
        )
        duration = ANIMATION_DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addListener(onStart = {
            animating = true
            Timber.d("animatorSet onStart")
        }, onEnd = {
            animating = false
            Timber.d("animatorSet onEnd")
        })
    }

    private var animating = false

    private var flipped: Boolean = true

    private var mCard : Card? = null

    var card: Card
        get() = mCard!!
        set(value) {
            if (animating) {
                animatorSet.end()
            }

            // skip animation for new card
            val newCard = mCard != value
            val skipFlip = flipped == value.flipped

            mCard = value

            frontTextView.text = value.cardFrontText
            frontTextView.setTextColor(value.cardFrontTextColor)
            invalidate()

            if (!skipFlip) {
                animatorSet.start()
            }
            setOnClickListener {
                viewModel.flipCard(value)
            }
        }

    init {
        addView(backgroundImageView)

        frontTextView.apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.CENTER
            addView(this)
        }
    }

    override fun invalidate() {
        super.invalidate()
        backgroundImageView.setBackgroundResource(
                if (flipped) Card.CARD_BACK_BACKGROUND else Card.CARD_FRONT_BACKGROUND)
        frontTextView.visibility = if (flipped) View.INVISIBLE else View.VISIBLE
    }
}