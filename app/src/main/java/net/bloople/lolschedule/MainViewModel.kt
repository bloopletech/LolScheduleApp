package net.bloople.lolschedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime


class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    private lateinit var schedule: Schedule;

    private val years: MutableLiveData<List<Int>> = MutableLiveData();
    private val selectedYear: MutableLiveData<Int> = MutableLiveData();
    private val streams: MutableLiveData<List<Stream>> = MutableLiveData();
    private val searchResults: MutableLiveData<List<Match>> = MutableLiveData();

    //private val searcher: BooksSearcher = BooksSearcher()
    //private val sorter: BooksSorter = BooksSorter()
    //private var sorterDescription: MutableLiveData<String>? = null

    fun getYears(): LiveData<List<Int>> {
        return years
    }

    fun getSelectedYear(): LiveData<Int> {
        return selectedYear
    }

    fun getStreams(): LiveData<List<Stream>> {
        return streams
    }

    fun getSearchResults(): LiveData<List<Match>> {
        return searchResults
    }

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

    fun setSelectedYear(year: Int) {
        selectedYear.value = year
        resolve()
    }

    private fun resolve() {
        selectedYear.value?.let {
            streams.value = schedule.getStreams(it);
            searchResults.value = schedule[it]!!;
        }
    }

    fun load() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                schedule = Schedule.download();

                val now = ZonedDateTime.now();
                for(matches in schedule.matches.values) {
                    for(match in matches) MatchTagger(match).tag(now)
                };
            }

            years.value = schedule.years;

            if(!schedule.years.contains(selectedYear.value)) selectedYear.value = schedule.currentYear
            resolve()
        }
    }
}
