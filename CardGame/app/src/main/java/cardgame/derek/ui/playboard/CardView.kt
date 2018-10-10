package cardgame.derek.ui.playboard

import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import cardgame.derek.model.Card







/**
 * User: derek
 * Date: 10/10/18 8:10 PM
 */

class CardView<C : Card> : View {
    private val oa1 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                addListener(onEnd = {
                    card?.cardFrontText
                    oa2.start()
                })
                duration = 1000

            }
    private val oa2 = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 1000
            }


    var card : Card? = null


    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        oa1.start()
    }



}