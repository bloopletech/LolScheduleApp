package net.bloople.lolschedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.time.ZonedDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    private lateinit var schedule: Schedule;

    val years: MutableLiveData<List<Int>> = MutableLiveData();
    val selectedYear: MutableLiveData<Int> = MutableLiveData();
    val streams: MutableLiveData<List<Stream>> = MutableLiveData();
    val searchResults: MutableLiveData<List<Match>> = MutableLiveData();

    //private val searcher: BooksSearcher = BooksSearcher()
    //private val sorter: BooksSorter = BooksSorter()
    //private var sorterDescription: MutableLiveData<String>? = null

//    fun getLibrary(): Library? {
//        return library
//    }
//
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
        selectedYear.postValue(year)
        resolve(year)
    }

    private fun resolve(year: Int) {
        streams.postValue(schedule.getStreams(year));
        searchResults.postValue(schedule[year]!!);
    }

    fun load() {
        val service: ExecutorService = Executors.newSingleThreadExecutor();
        service.submit {
            schedule = Schedule.download();

            val now = ZonedDateTime.now();
            for(matches in schedule.matches.values) {
                for(match in matches) MatchTagger(match).tag(now)
            };

            years.postValue(schedule.years);

            selectedYear.postValue(schedule.currentYear);
            resolve(schedule.currentYear);
        }
    }
}
