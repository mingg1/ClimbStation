package fi.metropolia.climbstation.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.ui.Transition
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.ui.feedBackTouchListener
import fi.metropolia.climbstation.ui.scaleAnimation

/**
 * Click listener of an item from recycler view
 *
 * @author Minji Choi
 *
 */
interface RecyclerviewClickListener {
    fun recyclerViewClickListener(programId: Long)
}

/**
 * Adapter for climbing program list
 *
 * @author Minji Choi
 *
 */
class ProgramListAdapter(
    private val profiles: List<TerrainProfile>,
    private val context: Context,
    private val parent: ViewGroup,
    private val listener: RecyclerviewClickListener
) :
    RecyclerView.Adapter<ProgramListAdapter.ProgramListViewHolder>() {

    class ProgramListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramListViewHolder {
        return ProgramListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.climb_program_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProgramListViewHolder, position: Int) {
        val profile = profiles[position]
        holder.itemView.findViewById<TextView>(R.id.program_name).text = profile.name
        val profileNameUri: String = if (profile.custom == 1) {
            "@drawable/custom_profile_character${profile.id % 4 + 1}"
        } else {
            "@drawable/${profile.name.lowercase().replace(" ", "_")}_character"
        }
        val profileDrawable =
            context.resources.getIdentifier(profileNameUri, null, context.packageName)
        val profileImg = profileDrawable.let { ContextCompat.getDrawable(context, it) }
        holder.itemView.findViewById<ImageView>(R.id.program_icon).setImageDrawable(profileImg)

        val programContainer = holder.itemView.findViewById<View>(R.id.program_container)
        val info = parent.findViewById<ConstraintLayout>(R.id.program_info_container)
        val transition = Transition(info, parent)
        programContainer.setOnClickListener { it ->
            it.scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
            it.scaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 500)
            listener.recyclerViewClickListener(profile.id)
            transition.showElement()
            var totalLength = 0
            profile.phases.forEach { totalLength += it.distance }
            parent.findViewById<TextView>(R.id.text_info_level).text =
                context.getString(R.string.level, profile.name)
            parent.findViewById<TextView>(R.id.text_total_length).text =
                context.getString(R.string.distance, totalLength)
            parent.findViewById<TextView>(R.id.text_angle_value).text =
                "${profile.phases.minByOrNull { it.angle }?.angle} to ${profile.phases.maxByOrNull { it.angle }?.angle} degree"
            parent.findViewById<ImageView>(R.id.close_btn).feedBackTouchListener()
            parent.findViewById<ImageView>(R.id.close_btn)
                .setOnClickListener {
                    transition.hideElement()
                }
        }

    }

    override fun getItemCount(): Int {
        return profiles.size
    }

}