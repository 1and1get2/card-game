package cardgame.derek

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cardgame.derek.ui.playboard.PlayBoardViewModel
import cardgame.derek.util.obtainViewModel

class MainActivity : AppCompatActivity() {
    val viewModel by lazy { obtainViewModel(PlayBoardViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
