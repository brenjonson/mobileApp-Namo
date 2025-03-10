package com.example.newhomepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private var eventList: MutableList<EventModel>, private val onClick: (EventModel) -> Unit) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event, onClick)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    // เพิ่มฟังก์ชันเพื่ออัปเดตข้อมูลใน RecyclerView
    fun updateEventList(newEventList: MutableList<EventModel>) {
        eventList = newEventList
        notifyDataSetChanged() // อัปเดต RecyclerView
    }

    // ViewHolder ที่ใช้แสดงข้อมูลกิจกรรม
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.eventName)
        private val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        private val eventImage: ImageView = itemView.findViewById(R.id.eventImage)

        fun bind(event: EventModel, onClick: (EventModel) -> Unit) {
            eventName.text = event.eventName
            eventDate.text = event.eventDate
            eventImage.setImageResource(event.imageResId)

            itemView.setOnClickListener {
                onClick(event)
            }
        }
    }
}
