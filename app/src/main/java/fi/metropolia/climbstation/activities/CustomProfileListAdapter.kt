package fi.metropolia.climbstation.activities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.TerrainProfile

interface ItemHelper {
    fun onItemDismiss(position: Int)
}

class CustomProfileListAdapter(
    private val profiles: List<TerrainProfile>,
    private val context: Context,
    private val listener: OnItemLongClickListener
) : RecyclerView.Adapter<CustomProfileListAdapter.CustomProfileListViewHolder>(), ItemHelper {

    inner class CustomProfileListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener {
        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(position)
            }

            return true

        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomProfileListViewHolder {
        return CustomProfileListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_profile_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomProfileListViewHolder, position: Int) {
        val profile = profiles[position]
        var totalLength = 0
        profile.phases.forEach { totalLength += it.distance }
        holder.itemView.findViewById<TextView>(R.id.profile_name).text = profile.name
        holder.itemView.findViewById<TextView>(R.id.steps_value).text = context.resources.getQuantityString(R.plurals.phase_amount, profile.phases.size,profile.phases.size)
        holder.itemView.findViewById<TextView>(R.id.length_value).text = context.getString(R.string.distance, totalLength)
        holder.itemView.findViewById<ConstraintLayout>(R.id.custom_profile_container)
            .setOnClickListener {
                val profileId = profile.id
                val intent = Intent(context, ModifyProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("profileId", profileId)
                context.startActivity(intent)
            }
    }

    override fun getItemCount(): Int = profiles.size

    override fun onItemDismiss(position: Int) = notifyItemRemoved(position)

}