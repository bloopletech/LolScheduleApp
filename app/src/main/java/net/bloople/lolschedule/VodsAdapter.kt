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


internal class VodsAdapter(private var vods: List<String>) : RecyclerView.Adapter<VodsAdapter.ViewHolder>() {
    private var revealedVods: ArrayList<String> = ArrayList();

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return Math.min(revealedVods.size + 1, vods.size);
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameView: TextView;

        init {
            nameView = view.findViewById(R.id.name_view);

            nameView.setOnClickListener { v: View ->
                when(getItemViewType(adapterPosition)) {
                    VOD_VIEW_TYPE -> {
                        var vod = revealedVods.get(adapterPosition);
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(vod));
                        startActivity(v.context, browserIntent, null);
                    }
                    REVEAL_VIEW_TYPE -> {
                        if(adapterPosition < vods.size) {
                            revealedVods.add(vods.get(adapterPosition));
                            notifyDataSetChanged();
                        }
                    }
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == revealedVods.size && position < vods.size) REVEAL_VIEW_TYPE else VOD_VIEW_TYPE;
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
                val vod: String = revealedVods[position];
                holder.nameView.setText("vod ${position + 1}");
                holder.nameView.paintFlags = holder.nameView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
            }
            REVEAL_VIEW_TYPE -> {
                holder.nameView.setText("...");
                holder.nameView.paintFlags = holder.nameView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv();
            }
            else -> throw IllegalArgumentException()
        }
    }

    init {
        setHasStableIds(true);
        if(!vods.isEmpty()) revealedVods.add(vods.first());
    }

    companion object {
        private const val VOD_VIEW_TYPE = 0;
        private const val REVEAL_VIEW_TYPE = 1;
    }
}