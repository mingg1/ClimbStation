package fi.metropolia.climbstation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        customProfiles = terrainProfileViewModel.getCustomTerrainProfiles(1)

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


    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPositon = viewHolder.adapterPosition
            var endPosition = target.adapterPosition
            customProfiles.observe(this@ManageProfileActivity, {
                Collections.swap(it, startPositon, endPosition)
                recyclerView.adapter?.notifyItemMoved(startPositon, endPosition)
            })
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
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