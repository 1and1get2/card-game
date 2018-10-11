package cardgame.derek.ui.playboard

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cardgame.derek.R


class PlayBoardFragment : Fragment() {

    companion object {
        fun newInstance() = PlayBoardFragment()
    }

    private lateinit var viewModel : PlayBoardViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : RecyclerView.Adapter<CardViewHolder>
    private lateinit var layoutManager: GridLayoutManager
    private var gridVerticalSpace: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        layoutManager = GridLayoutManager(inflater.context, 4)
        adapter = Adapter()
        gridVerticalSpace = resources.getDimension(R.dimen.grid_cell_vertical_space).toInt()
        recyclerView = RecyclerView(inflater.context).apply {
            setHasFixedSize(true)
            adapter = this@PlayBoardFragment.adapter
            layoutManager = this@PlayBoardFragment.layoutManager
            addItemDecoration(SpacesItemDecoration(gridVerticalSpace))
        }
        return recyclerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ViewModelProviders.of(this)
        viewModel = activity?.run { ViewModelProviders.of(this).get(PlayBoardViewModel::class.java) }
                ?: throw Exception("Invalid Activity")

        viewModel.cards.observe(viewLifecycleOwner, Observer { adapter.notifyDataSetChanged() })
    }


    inner class Adapter : RecyclerView.Adapter<CardViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val cardView = CardView(parent.context)

            // calculate the height of each card
            viewModel.grid?.run {
                val lp = GridLayoutManager.LayoutParams(parent.layoutParams)
                lp.height = (parent.measuredHeight - (this.second + 1) * gridVerticalSpace) / this.second
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
            (holder.itemView as? CardView)?.let {
                it.card = card
            }
        }
    }

    class CardViewHolder(itemView : CardView) : RecyclerView.ViewHolder(itemView)


    inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override
        fun getItemOffsets(outRect: Rect, view: View,
                           parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) < viewModel.grid?.first ?: 4) {
                outRect.top = space
            } else {
                outRect.top = 0
            }
        }
    }
}
