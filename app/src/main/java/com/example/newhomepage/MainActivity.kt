package com.example.newhomepage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newhomepage.CategoryAdapter
import com.example.newhomepage.CategoryModel
import com.example.newhomepage.EventAdapter
import com.example.newhomepage.EventModel
import com.example.newhomepage.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventList: List<EventModel>
    private lateinit var filteredEventList: MutableList<EventModel>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: List<CategoryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ตั้งค่าข้อมูลหมวดหมู่กิจกรรม
        categoryList = listOf(
            CategoryModel("กีฬา", R.drawable.image1),
            CategoryModel("การศึกษา", R.drawable.image2),
            CategoryModel("ศิลปะ", R.drawable.image3),
            CategoryModel("ดนตรี", R.drawable.image4)
        )

        // ตั้งค่าข้อมูลกิจกรรมล่าสุด
        eventList = listOf(
            EventModel("KKU Run 2025", "9-03-2025", R.drawable.banner1, "กีฬา"),
            EventModel("Tech Talk AI", "12-03-2025", R.drawable.event2, "การศึกษา"),
            EventModel("KKU Music Fest", "12-03-2025", R.drawable.event3, "ดนตรี"),
            EventModel("KKU Game Fest", "15-03-2025", R.drawable.event1, "กีฬา"),
            EventModel("KKU Game Fest", "15-03-2025", R.drawable.event3, "กีฬา")
        )

        filteredEventList = eventList.toMutableList()


        val latestEventsRecyclerView: RecyclerView = findViewById(R.id.latestEventsRecyclerView)
        latestEventsRecyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = EventAdapter(filteredEventList) { event ->

            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("eventName", event.eventName)
            intent.putExtra("eventDate", event.eventDate)
            intent.putExtra("eventImageResId", event.imageResId)
            startActivity(intent)
        }

        latestEventsRecyclerView.adapter = eventAdapter


        val categoryRecyclerView: RecyclerView = findViewById(R.id.categoryRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter = CategoryAdapter(categoryList) { category ->
            filterEventsByCategory(category)
        }
        categoryRecyclerView.adapter = categoryAdapter


        val themeToggleButton: ImageButton = findViewById(R.id.themeToggleButton)


        updateThemeIcon(themeToggleButton)

        themeToggleButton.setOnClickListener {

            val currentMode = AppCompatDelegate.getDefaultNightMode()

            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeToggleButton.setImageResource(R.drawable.sun1)  // เปลี่ยนรูปเมื่อโหมดสว่าง
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeToggleButton.setImageResource(R.drawable.moon1) // เปลี่ยนรูปเมื่อโหมดมืด
            }

            recreate()
        }


        val searchBar: EditText = findViewById(R.id.searchBar)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterEvents(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.subscriptions -> {
                    val intent = Intent(this, AnnouncementActivity::class.java)

                    intent.putExtra("eventList", ArrayList(eventList)) // ส่งข้อมูล eventList
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun filterEventsByCategory(category: CategoryModel) {
        filteredEventList.clear()


        for (event in eventList) {
            if (event.category == category.categoryName) {
                filteredEventList.add(event)
            }
        }

        eventAdapter.notifyDataSetChanged()
    }


    private fun filterEvents(query: String) {
        filteredEventList.clear()

        if (query.isEmpty()) {
            filteredEventList.addAll(eventList)
        } else {
            for (event in eventList) {
                if (event.eventName.contains(query, ignoreCase = true)) {
                    filteredEventList.add(event)
                }
            }
        }

        eventAdapter.notifyDataSetChanged()
    }

   
    private fun updateThemeIcon(themeToggleButton: ImageButton) {
        val currentMode = AppCompatDelegate.getDefaultNightMode()

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeToggleButton.setImageResource(R.drawable.moon1)
        } else {
            themeToggleButton.setImageResource(R.drawable.sun1)
        }
    }
}
