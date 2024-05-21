package com.areeb.ui.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.areeb.ui.R

class MovieDetailsRow @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null
) : ConstraintLayout(context, attributes) {

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_movie_details_row, this)
        context.theme.obtainStyledAttributes(
            attributes,
            R.styleable.MovieDetailsRow,
            0, 0
        ).apply {
            try {
                val label = getString(R.styleable.MovieDetailsRow_label)
                findViewById<TextView>(R.id.tvLabel).text = label
            } finally {
                recycle()
            }
        }
    }

    companion object {
        fun MovieDetailsRow.setValue(value: String?) {
            findViewById<TextView>(R.id.tvValue).text = value
        }
    }
}