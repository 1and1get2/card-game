package cardgame.derek

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cardgame.derek.databinding.ActivityMainBinding
import cardgame.derek.model.Card
import cardgame.derek.model.GameType
import cardgame.derek.ui.playboard.PlayBoardViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val restart : Button by lazy { findViewById<Button>(R.id.playboard_restart_button) }
    private val stop : Button by lazy { findViewById<Button>(R.id.playboard_stop_button) }

    private lateinit var viewModel : PlayBoardViewModel
    private lateinit var viewDataBinding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PlayBoardViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply{
            viewDataBinding = this
            viewmodel = this@MainActivity.viewModel
            setLifecycleOwner(this@MainActivity)
        }

        viewModel.gameType.observe(this, Observer {
            supportActionBar?.title = it?.name ?: "Card Game"

        })


        viewModel.gameTypeSelectEvent.observe(this, Observer {
            Timber.d("viewModel.gameTypeSelectEvent $it")
            if (it != null && it.isNotEmpty()) { selectGameType(it) }
        })

        viewModel.matchEndedEvent.observe(this, Observer {
            AlertDialog.Builder(this).apply {
                setTitle("Game has ended with score ${viewModel.score.value}")
                setCancelable(false)
                setPositiveButton("Restart") {_, _ ->
                    viewModel.restartGame()
                }
                setNegativeButton("Ok") {_, _ ->
                    viewModel.stopGame()
                }
                show()
            }
        })

        viewModel.gameNotStartedMessageEvent.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_game_type -> {
                viewModel.selectGameType()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onStart()
    }

    private fun selectGameType(gameTypes: Array<GameType<out Card>>) {
        val names = gameTypes.map { it.name }.toTypedArray()
        AlertDialog.Builder(this).apply {
            setTitle("select game type:")
            setCancelable(false)
            setItems(names) { dialog, which ->
                val gameType = gameTypes[which]
                Timber.d("selected: $which, ${gameType.name}")
                viewModel.setGameType(gameType)
                dialog.dismiss()
            }
            show()
        }
    }


}
