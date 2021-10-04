package net.bloople.lolschedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.ZonedDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    private lateinit var schedule: Schedule;

    private val years: MutableLiveData<List<Int>> = MutableLiveData<List<Int>>();
    private val searchResults: MutableLiveData<YearSchedule> = MutableLiveData<YearSchedule>();
    private var filterYear: Int = 0
    //private val searcher: BooksSearcher = BooksSearcher()
    //private val sorter: BooksSorter = BooksSorter()
    //private var sorterDescription: MutableLiveData<String>? = null

//    fun getLibrary(): Library? {
//        return library
//    }
//
    fun getSearchResults(): LiveData<YearSchedule> {
        return searchResults
    }

    fun getYears(): LiveData<List<Int>> {
        return years
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
        searchResults.postValue(YearSchedule(filterYear, schedule[filterYear]!!));
    }

    fun load() {
        val service: ExecutorService = Executors.newSingleThreadExecutor();
        service.submit {
            schedule = Schedule.download();

            val now = ZonedDateTime.now();
            for(matches in schedule.matches.values) {
                for(match in matches) MatchTagger(match).tag(now)
            };

            val yearsData = schedule.matches.keys.toList()
            years.postValue(yearsData);

            filterYear = yearsData.last();
            resolve();
        }
    }
}

data class YearSchedule(val year: Int, val matches: List<Match>)
