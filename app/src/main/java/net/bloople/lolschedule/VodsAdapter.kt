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
    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return vods.size;
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameView: TextView;

        init {
            nameView = view.findViewById(R.id.name_view);
            nameView.paintFlags = nameView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

            nameView.setOnClickListener { v: View ->
                var vod = vods.get(adapterPosition);
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(vod));
                startActivity(v.context, browserIntent, null);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.vod_view, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vod: String = vods[position];
        holder.nameView.setText("vod ${position + 1}");
    }

    init {
        setHasStableIds(true);
    }
}