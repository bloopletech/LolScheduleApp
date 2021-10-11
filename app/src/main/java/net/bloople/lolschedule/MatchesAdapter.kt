package net.bloople.lolschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.ZonedDateTime
import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager


internal class MatchesAdapter(private val matchRevealedVods: MatchRevealedVods) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    private var matches: List<Match> = ArrayList();

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

    fun getMatches(): List<Match> {
        return matches;
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var leagueView: TextView;
        var timeView: TextView;
        var participantsView: View;
        var participant1View: TextView;
        var participant1LogoView: ImageView;
        var participant2View: TextView;
        var participant2LogoView: ImageView;
        var vodsView: RecyclerView;

        init {
            leagueView = view.findViewById(R.id.league_view);
            timeView = view.findViewById(R.id.time_view);
            participantsView = view.findViewById(R.id.participants_view);
            participant1View = view.findViewById(R.id.participant_1_view);
            participant1LogoView = view.findViewById(R.id.participant_1_logo_view);
            participant2View = view.findViewById(R.id.participant_2_view);
            participant2LogoView = view.findViewById(R.id.participant_2_logo_view);
            vodsView = view.findViewById(R.id.vods_view);

            participantsView.setOnClickListener { v -> v.alpha = 1f }

            val vodsLayoutManager = LinearLayoutManager(vodsView.context)
            vodsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL;
            vodsView.layoutManager = vodsLayoutManager

            vodsView.itemAnimator = null
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.match_view, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]
        holder.leagueView.setText(match.league);
        holder.timeView.setText(TimeUtils.formatDate(match.time, ZonedDateTime.now()));

        holder.participantsView.alpha = if(match.spoiler) 0f else 1f;

        holder.participant1View.setText(match.participant1);
        if(match.participant1Logo != null) {
            holder.participant1LogoView.setImageBitmap(match.participant1Logo);
            holder.participant1LogoView.visibility = View.VISIBLE;
        }
        else {
            holder.participant1LogoView.visibility = View.GONE;
        }

        holder.participant2View.setText(match.participant2);
        if(match.participant2Logo != null) {
            holder.participant2LogoView.setImageBitmap(match.participant2Logo);
            holder.participant2LogoView.visibility = View.VISIBLE;
        }
        else {
            holder.participant2LogoView.visibility = View.GONE;
        }

        holder.vodsView.adapter = VodsAdapter(match, matchRevealedVods);
    }

    init {
        setHasStableIds(true);
    }
}