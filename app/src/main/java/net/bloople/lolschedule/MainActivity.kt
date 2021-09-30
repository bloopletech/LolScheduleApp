package net.bloople.lolschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val model: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val matchesView: RecyclerView = findViewById(R.id.matches_view);
        val matchesLayoutManager = LinearLayoutManager(this)
        matchesView.layoutManager = matchesLayoutManager

        val matchesAdapter = MatchesAdapter();
        matchesView.adapter = matchesAdapter;

        model.getSearchResults().observe(this) { matches -> matchesAdapter.update(matches) };

        model.load();
    }
}