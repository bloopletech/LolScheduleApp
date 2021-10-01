package net.bloople.lolschedule

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


internal class YearsAdapter : RecyclerView.Adapter<YearsAdapter.ViewHolder>() {
    private var years: List<Int> = ArrayList();

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return years.size;
    }

    fun update(years: List<Int>) {
        this.years = years;
        notifyDataSetChanged();
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var yearView: TextView;

        init {
            yearView = view.findViewById(R.id.year_view);
            yearView.paintFlags = yearView.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

            yearView.setOnClickListener { v: View ->
                val year = years[adapterPosition]
                var mainActivity = v.context as MainActivity;
                mainActivity.filterYear(year);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.year_view, parent, false);
        return ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val year: Int = years[position]
        holder.yearView.setText(year.toString());
    }

    init {
        setHasStableIds(true);
    }
}