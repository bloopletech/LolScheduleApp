package net.bloople.lolschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.ZonedDateTime
import java.util.ArrayList
import android.widget.ArrayAdapter

import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager


internal class MatchesAdapter : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    private var matches: List<Match> = ArrayList();
    private var timeRenderer = TimeRenderer()

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return matches.size;
    }

    fun update(matches: List<Match>) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var leagueView: TextView;
        var timeView: TextView;
        var participant1View: TextView;
        var participant2View: TextView;
        var vodsView: RecyclerView;

        init {
            leagueView = view.findViewById(R.id.league_view);
            timeView = view.findViewById(R.id.time_view);
            participant1View = view.findViewById(R.id.participant_1_view);
            participant2View = view.findViewById(R.id.participant_2_view);
            vodsView = view.findViewById(R.id.vods_view);
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.match_view, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match: Match = matches[position]
        holder.leagueView.setText(match.league);
        holder.timeView.setText(timeRenderer.formatDate(match.local_time, ZonedDateTime.now()));
        holder.participant1View.setText(match.participant_1);
        holder.participant2View.setText(match.participant_2);

        val vodsLayoutManager = LinearLayoutManager(holder.vodsView.context)
        vodsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL;
        holder.vodsView.layoutManager = vodsLayoutManager

        val vodsAdapter = VodsAdapter(match.vods ?: ArrayList());
        holder.vodsView.adapter = vodsAdapter;
    }

    init {
        setHasStableIds(true);
    }
}