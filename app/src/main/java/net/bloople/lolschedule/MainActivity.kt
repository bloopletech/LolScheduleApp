package net.bloople.lolschedule

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    private lateinit var model: MainViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProvider(this).get(MainViewModel::class.java)

        val yearsView: RecyclerView = findViewById(R.id.years_view);
        val yearsLayoutManager = LinearLayoutManager(this)
        yearsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        yearsView.layoutManager = yearsLayoutManager

        val yearsAdapter = YearsAdapter();
        yearsView.adapter = yearsAdapter;

        model.getYears().observe(this) { years -> yearsAdapter.update(years); };

        val titleView: TextView = findViewById(R.id.title);
        model.getTitle().observe(this) { title -> titleView.text = title };

        val matchesView: RecyclerView = findViewById(R.id.matches_view);
        val matchesLayoutManager = LinearLayoutManager(this)
        matchesView.layoutManager = matchesLayoutManager

        val matchesAdapter = MatchesAdapter();
        matchesView.adapter = matchesAdapter;

        model.getSearchResults().observe(this) { matches -> matchesAdapter.update(matches) };

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

        model.load();
    }

    fun filterYear(year: Int) {
        model.filterYear(year);
    }
}