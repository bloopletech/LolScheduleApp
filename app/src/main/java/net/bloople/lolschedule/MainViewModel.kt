package net.bloople.lolschedule
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.lang.Exception
import java.net.URL
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    private val searchResults: MutableLiveData<List<Match>> = MutableLiveData<List<Match>>()
    //private val searcher: BooksSearcher = BooksSearcher()
    //private val sorter: BooksSorter = BooksSorter()
    //private var sorterDescription: MutableLiveData<String>? = null

//    fun getLibrary(): Library? {
//        return library
//    }
//
    fun getSearchResults(): LiveData<List<Match>> {
        return searchResults
    }
//
//    fun getSorterDescription(): LiveData<String> {
//        if(sorterDescription == null) {
//            sorterDescription = MutableLiveData(sorter.description())
//        }
//        return sorterDescription
//    }
//
//    val sortMethod: Int
//        get() = sorter.getSortMethod()
//    val sortDirectionAsc: Boolean
//        get() = sorter.getSortDirectionAsc()
//
//    fun setSearchText(searchText: String?) {
//        searcher.setSearchText(searchText)
//        resolve()
//    }
//
//    fun setSort(sortMethod: Int, sortDirectionAsc: Boolean) {
//        sorter.setSortMethod(sortMethod)
//        sorter.setSortDirectionAsc(sortDirectionAsc)
//        sorterDescription!!.value = sorter.description()
//        resolve()
//    }
//
//    fun useList(list: BookList?) {
//        if(list == null) searcher.setFilterIds(null) else searcher.setFilterIds(list.bookIds(application))
//        resolve()
//    }

//    private fun resolve() {
//        val service = Executors.newSingleThreadExecutor()
//        service.submit {
//            val books: ArrayList<Book> = searcher.search(library)
//            sorter.sort(application, books)
//            searchResults.postValue(books)
//        }
//    }
    fun load() {
        val service: ExecutorService = Executors.newSingleThreadExecutor();
        service.submit {
            try {
                val connection = URL("https://lol.bloople.net/data.json").openConnection();
                val schedule = Json.decodeFromStream<Schedule>(connection.getInputStream());

                searchResults.postValue(schedule.matches.sortedBy { match -> match.time });
            }
            catch(e: Exception) {
                e.printStackTrace();
            }
        }
    }
}
