package fi.metropolia.climbstation.activities

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.ClimbHistory
import java.util.*

class HistoryListAdapter(private val histories: List<ClimbHistory>):RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder>() {
    class HistoryListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
       return HistoryListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.climb_history,parent,false))
    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
      val history = histories[position]
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        holder.itemView.findViewById<TextView>(R.id.profile_name).text = simpleDateFormat.format(history.dateTime)
        holder.itemView.findViewById<TextView>(R.id.steps).text = history.level
        holder.itemView.findViewById<TextView>(R.id.length).text = "${history.length}m"
    }

    override fun getItemCount(): Int {
       return histories.size
    }
}