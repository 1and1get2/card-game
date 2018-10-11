package cardgame.derek

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cardgame.derek.databinding.ActivityMainBinding
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

        if (viewModel.gameType.value == null) {selectGameType()}

        viewModel.gameType.observe(this, Observer {
            supportActionBar?.title = it?.name ?: "Card Game"
            if (it == null) { selectGameType() }
        })



    }

    private fun selectGameType() {
        val options = viewModel.gameTypes
        val names = options.map { it.name }.toTypedArray()
        AlertDialog.Builder(this).apply {
            setTitle("select game type:")
            setCancelable(false)
            setItems(names) { dialog, which ->
                val gameType = options[which]
                Timber.d("selected: $which, ${gameType.name}")
                viewModel.selectGameType(gameType)
                dialog.dismiss()
            }
            show()
        }
    }


}
