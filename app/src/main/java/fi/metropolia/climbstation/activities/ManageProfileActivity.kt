package fi.metropolia.climbstation.activities

import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingHistoryBinding
import java.util.*

class ManageProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityClimbingHistoryBinding
    lateinit var adapter: CustomProfileListAdapter
    lateinit var customProfiles: LiveData<List<TerrainProfile>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listContainer.backgroundTintList = ContextCompat.getColorStateList(this,R.color.white)
        binding.listContainer.backgroundTintMode = PorterDuff.Mode.ADD
        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        customProfiles = terrainProfileViewModel.getCustomTerrainProfiles()

        binding.textViewTitle.text = getString(R.string.manage_custom_terrain_profile)
        binding.textViewTitle.textSize = 24.0f

        val profileListView = binding.listView
        profileListView.layoutManager = LinearLayoutManager(this)
        customProfiles.observe(this, {
            adapter = CustomProfileListAdapter(it, this)
            profileListView.adapter = adapter
        })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(profileListView)

        binding.bottomNav.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        ItemTouchHelper.LEFT
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition
            customProfiles.observe(this@ManageProfileActivity, {
                Collections.swap(it, startPosition, endPosition)
                recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            })
            return true
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {
            val foregroundView = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.custom_profile_container)
            getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
        }

        override fun onChildDrawOver(
            c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {
            val foregroundView = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.custom_profile_container)
            getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            Log.d("pos", position.toString())
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
                    val profileId = customProfiles.value?.get(position)
                    Log.d("id", profileId.toString())
                    profileId?.let { terrainProfileViewModel.deleteTerrainProfile(it) }
//                    it.toMutableList().removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
    }


}