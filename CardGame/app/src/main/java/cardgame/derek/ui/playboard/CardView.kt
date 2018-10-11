package cardgame.derek.ui.playboard

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
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

class CardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : CardView(context, attrs) {

    companion object {
        const val ANIMATION_DURATION: Long = 300
    }

    private val backgroundImageView = ImageView(context)
    private val frontTextView = TextView(context)

    private val oa1 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                addListener(onEnd = {
                    flipped = !flipped
                    invalidate()
                    oa2.start()
                }, onStart = {
                    animating = true
                })
                duration = ANIMATION_DURATION / 2

            }

    private val oa2 = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = ANIMATION_DURATION / 2
                addListener(onEnd = {
                    animating = false
                })
            }


    private var animating = false

    private var flipped : Boolean
        set(value) {
            card?.flipped = value
            invalidate()
        }
        get() = card?.flipped ?: false




    var card: Card? = null
        set(value) {
            field = value
            flipped = card?.flipped ?: true
            frontTextView.text = value?.cardFrontText
            frontTextView.setTextColor(value?.cardFrontTextColor ?: Color.BLACK)
            invalidate()

            this@CardView.setOnClickListener {
                Timber.d("click on card: ${card?.cardFrontText}")
                if (!animating) {
                    oa1.start()
                }
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
        backgroundImageView.setBackgroundResource(if (flipped) Card.CARD_BACK_BACKGROUND else Card.CARD_FRONT_BACKGROUND)
        frontTextView.visibility = if (flipped) View.INVISIBLE else View.VISIBLE
    }
}