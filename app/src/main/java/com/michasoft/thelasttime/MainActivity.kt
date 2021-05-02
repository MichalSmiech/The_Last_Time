package com.michasoft.thelasttime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.adapter.EventTypeListAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}