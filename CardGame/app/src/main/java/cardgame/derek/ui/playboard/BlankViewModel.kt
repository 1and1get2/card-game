package cardgame.derek.ui.playboard

import androidx.lifecycle.ViewModel
import cardgame.derek.model.Card
import io.reactivex.subjects.PublishSubject



class BlankViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var searchPublishSubject =  PublishSubject.create<Card>()


}
