package com.rhtyme.coroutinevideocompressor.view.editvideo

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rhtyme.coroutinevideocompressor.model.VideoResolution


class SpinAdapter(context: Context, textViewResourceId: Int, val values: ArrayList<VideoResolution>)
    : ArrayAdapter<VideoResolution>(context, textViewResourceId, values) {

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): VideoResolution {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        // Then you can get the current item using the values array (Users array) and the current position
// You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].toString())
        // And finally return your dynamic (or custom) view for each spinner item
        return label

    }

    // And here is when the "chooser" is popped up
// Normally is the same view, but you can customize it if you want
    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.setText(values[position].toString())
        return label
    }
}
