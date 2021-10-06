package net.bloople.lolschedule

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri


internal class VodsAdapter(private var match: Match) : RecyclerView.Adapter<VodsAdapter.ViewHolder>() {
    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return Math.min(match.vodsRevealed + 1, match.vods.size);
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameView: TextView;

        init {
            nameView = view.findViewById(R.id.name_view);

            nameView.setOnClickListener { v: View ->
                when(getItemViewType(adapterPosition)) {
                    VOD_VIEW_TYPE -> {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(match.vods[adapterPosition]));
                        startActivity(v.context, browserIntent, null);
                    }
                    REVEAL_VIEW_TYPE -> {
                        match.vodsRevealed += 1;
                        if(adapterPosition == match.vods.size - 1) notifyItemChanged(adapterPosition);
                        else notifyItemInserted(adapterPosition);
                    }
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == match.vodsRevealed) REVEAL_VIEW_TYPE else VOD_VIEW_TYPE;
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewId = when(viewType) {
            VOD_VIEW_TYPE -> R.layout.vod_view
            REVEAL_VIEW_TYPE -> R.layout.reveal_view
            else -> throw IllegalArgumentException()
        }
        val view: View = LayoutInflater.from(parent.context).inflate(viewId, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType) {
            VOD_VIEW_TYPE -> {
                holder.nameView.text = "vod ${position + 1}";
                holder.nameView.paintFlags = holder.nameView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
            }
            REVEAL_VIEW_TYPE -> {
                holder.nameView.text = "...";
                holder.nameView.paintFlags = holder.nameView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv();
            }
            else -> throw IllegalArgumentException()
        }
    }

    init {
        setHasStableIds(true);
    }

    companion object {
        private const val VOD_VIEW_TYPE = 0;
        private const val REVEAL_VIEW_TYPE = 1;
    }
}