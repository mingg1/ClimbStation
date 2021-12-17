package fi.metropolia.climbstation.activities

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.ui.scaleAnimation
import java.util.*

class HistoryListAdapter(private val histories: List<ClimbHistory>, private val context: Context) :
    RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder>() {
    class HistoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
        return HistoryListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.climb_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        val history = histories[position]
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        holder.itemView.findViewById<TextView>(R.id.profile_name).text =
            simpleDateFormat.format(history.dateTime)
        holder.itemView.findViewById<TextView>(R.id.steps).text = history.level
        holder.itemView.findViewById<TextView>(R.id.length).text =
            context.getString(R.string.distance, history.climbedLength)

        holder.itemView.findViewById<ConstraintLayout>(R.id.history_info_container)
            .setOnClickListener {
                it.scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
                it.scaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 500)
                val historyId = history.id
                val intent = Intent(context, ClimbHistoryDetailActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("historyId", historyId)
                context.startActivity(intent)
            }
    }

    override fun getItemCount(): Int {
        return histories.size
    }
}