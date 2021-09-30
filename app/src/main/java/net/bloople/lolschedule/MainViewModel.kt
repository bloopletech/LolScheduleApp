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
    private var matches: List<Match> = ArrayList();

    private val years: MutableLiveData<List<Int>> = MutableLiveData<List<Int>>();
    private var title: MutableLiveData<String> = MutableLiveData<String>();
    private val searchResults: MutableLiveData<List<Match>> = MutableLiveData<List<Match>>();
    private var filterYear: Int = 0
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

    fun getYears(): LiveData<List<Int>> {
        return years
    }

    fun getTitle(): LiveData<String> {
        return title
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

    fun filterYear(year: Int) {
        this.filterYear = year
        resolve()
    }

//    private fun resolve() {
//        val service = Executors.newSingleThreadExecutor()
//        service.submit {
//            val books: ArrayList<Book> = searcher.search(library)
//            sorter.sort(application, books)
//            searchResults.postValue(books)
//        }
//    }
    private fun resolve() {
        searchResults.postValue(matches.filter { it.local_time.year == filterYear });
        title.postValue("$filterYear League of Legends eSports Schedule");
    }

    fun load() {
        val service: ExecutorService = Executors.newSingleThreadExecutor();
        service.submit {
            try {
                val connection = URL("https://lol.bloople.net/data.json").openConnection();
                val schedule = Json.decodeFromStream<Schedule>(connection.getInputStream());

                matches = schedule.matches.sortedBy { match -> match.time };
                val yearsData = matches.map { match -> match.local_time.year }.distinct().sorted();
                years.postValue(yearsData);

                filterYear = yearsData.last();
                resolve();
            }
            catch(e: Exception) {
                e.printStackTrace();
            }
        }
    }
}
