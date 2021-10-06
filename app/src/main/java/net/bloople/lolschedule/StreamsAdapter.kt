package net.bloople.lolschedule

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


internal class StreamsAdapter : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {
    private var streams: List<Stream> = ArrayList();

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return streams.size;
    }

    fun update(streams: List<Stream>) {
        this.streams = streams;
        notifyDataSetChanged();
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameView: TextView;
        var watchView: TextView;

        init {
            nameView = view.findViewById(R.id.name_view);
            watchView = view.findViewById(R.id.watch_view);

            watchView.setOnClickListener { v: View ->
                val stream = streams[adapterPosition];
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stream.url));
                ContextCompat.startActivity(v.context, browserIntent, null);
            }
            watchView.paintFlags = watchView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.stream_view, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]
        holder.nameView.setText(stream.name);
    }

    init {
        setHasStableIds(true);
    }
}
