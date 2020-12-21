package io.github.quwac.how_to_use_jetpack_datastore.ui.main

//import androidx.datastore.createDataStoreではないことに注意。
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository = Repository(getApplication())
    val exampleText = repository.getExampleTextFlow_preference().asLiveData()

    fun saveText(newValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save_preference(newValue)
        }
    }

    val exampleNumber = repository.getExampleNumberFlow_proto().asLiveData()

    fun saveNumber(newValue: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save_proto(newValue)
        }
    }
}