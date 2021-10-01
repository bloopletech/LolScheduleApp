package net.bloople.lolschedule

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    private lateinit var model: MainViewModel;
    private lateinit var matchesView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProvider(this).get(MainViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val yearsView: RecyclerView = findViewById(R.id.years_view);
        val yearsLayoutManager = LinearLayoutManager(this)
        yearsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        yearsView.layoutManager = yearsLayoutManager

        val yearsAdapter = YearsAdapter();
        yearsView.adapter = yearsAdapter;

        model.getYears().observe(this) { years -> yearsAdapter.update(years); };

        model.getTitle().observe(this) { title -> toolbar.title = title };

        matchesView = findViewById(R.id.matches_view);
        val matchesLayoutManager = LinearLayoutManager(this)
        matchesView.layoutManager = matchesLayoutManager

        val matchesAdapter = MatchesAdapter();
        matchesView.adapter = matchesAdapter;

        model.getSearchResults().observe(this) { matches -> matchesAdapter.update(matches) };

        val jumpToTodayView: TextView = findViewById(R.id.jump_to_today_view);
        jumpToTodayView.setOnClickListener { v: View ->
            val firstMatch = matchesAdapter.getMatches().indexOfFirst { match -> match.todayish }
            if(firstMatch >= 0) matchesLayoutManager.scrollToPositionWithOffset(firstMatch, 0);
        }
        jumpToTodayView.paintFlags = jumpToTodayView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

        val jumpToTopView: TextView = findViewById(R.id.jump_to_top_view);
        jumpToTopView.setOnClickListener { v: View ->
            matchesView.scrollToPosition(0);
        }
        jumpToTopView.paintFlags = jumpToTopView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

        val jumpToBottomView: TextView = findViewById(R.id.jump_to_bottom_view);
        jumpToBottomView.setOnClickListener { v: View ->
            matchesView.scrollToPosition(matchesAdapter.itemCount - 1);
        }
        jumpToBottomView.paintFlags = jumpToBottomView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

        var loadingDialog = ProgressDialog.show(
            this,
            "Loading schedule",
            "Please wait while the schedule is loaded...",
            true);

        model.getSearchResults().observe(this) { matches ->
            loadingDialog?.let {
                it.dismiss();
                loadingDialog = null;
            }
        }

        model.load();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.info -> {
                AlertDialog.Builder(this)
                    .setTitle("Information")
                    .setMessage("""
                        League of Legends® and LCS® are registered trademarks of Riot Games, Inc.
                        This app is not associated with or sponsored by Riot Games, Inc.
                    """.trimIndent())
                    .setPositiveButton("OK") { dialogInterface, _ -> dialogInterface.cancel() }
                    .show();
            }
        }

        return true
    }

    fun filterYear(year: Int) {
        matchesView.scrollToPosition(0);
        model.filterYear(year);
    }
}