package cardgame.derek.ui.playboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cardgame.derek.model.playingcards.PlayCardGameType
import timber.log.Timber





class PlayBoardFragment : Fragment() {

    companion object {
        fun newInstance() = PlayBoardFragment()
    }

    private lateinit var viewModel : PlayBoardViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : RecyclerView.Adapter<CardViewHolder>
    private lateinit var layoutManager: GridLayoutManager

    private val cards = PlayCardGameType().getCards()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        layoutManager = GridLayoutManager(inflater.context, 4)
        adapter = Adapter()
        recyclerView = RecyclerView(inflater.context).apply {
            setHasFixedSize(true)
            adapter = this@PlayBoardFragment.adapter
            layoutManager = this@PlayBoardFragment.layoutManager
        }
        return recyclerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ViewModelProviders.of(this)
        viewModel = activity?.run { ViewModelProviders.of(this).get(PlayBoardViewModel::class.java) }
                ?: throw Exception("Invalid Activity")

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
            Timber.d("itemCount has changed, card size: ${adapter.itemCount}, it:${it?.size}")
        })
    }


    inner class Adapter : RecyclerView.Adapter<CardViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val cardView = CardView(parent.context)

            // calculate the height of each card
            viewModel.grid?.run {
                val lp = GridLayoutManager.LayoutParams(parent.layoutParams)
                lp.height = parent.measuredHeight / this.second
                lp.width = GridLayoutManager.LayoutParams.MATCH_PARENT
                cardView.layoutParams = lp
            }

            return CardViewHolder(cardView)
        }

        override fun getItemCount(): Int = (viewModel.cards.value?.size ?: 0).also {
//            Timber.d("Adapter get item count: $it")
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val card = viewModel.cards.value?.get(position)
            (holder.itemView as CardView).card = card
        }
    }

    class CardViewHolder(itemView : CardView) : RecyclerView.ViewHolder(itemView)
}
