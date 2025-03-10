package com.example.newhomepage

import LocationImagesAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.newhomepage.R

class EventDetailActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: LocationImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }


        val eventName = intent.getStringExtra("eventName")
        val eventDate = intent.getStringExtra("eventDate")
        val eventImageResId = intent.getIntExtra("eventImageResId", R.drawable.moon)


        val eventNameTextView: TextView = findViewById(R.id.eventNameTextView)
        val eventDateTextView: TextView = findViewById(R.id.eventDateTextView)
        val eventImageView: ImageView = findViewById(R.id.eventImageView)


        eventNameTextView.text = eventName ?: "กิจกรรมไม่ระบุ"
        eventDateTextView.text = eventDate ?: "วันที่ไม่ระบุ"
        eventImageView.setImageResource(eventImageResId)


        val images = listOf(R.drawable.event1, R.drawable.event2, R.drawable.event3)


        viewPager2 = findViewById(R.id.locationImagesViewPager)
        adapter = LocationImagesAdapter(images)
        viewPager2.adapter = adapter


        tabLayout = findViewById(R.id.tabDots)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
        }.attach()

        autoScrollImages()
    }

    private fun autoScrollImages() {
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager2.currentItem
                val nextItem = if (currentItem + 1 < viewPager2.adapter?.itemCount ?: 0) {
                    currentItem + 1
                } else {
                    0 //
                }
                viewPager2.setCurrentItem(nextItem, true)
                handler.postDelayed(this, delay)
            }
        }
        handler.postDelayed(runnable, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
