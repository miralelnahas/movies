package com.areeb.ui.views.movies

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.areeb.domain.models.movies.Movie
import com.areeb.ui.R
import com.areeb.ui.base.BaseViewHolder

class MoviesAdapter(private val onClickListener: (movie: Movie) -> Unit) :
    PagingDataAdapter<Movie, BaseViewHolder>(MovieDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder.from(parent, R.layout.item_movie)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie).apply {
                root.setOnClickListener {
                    onClickListener(movie)
                }
            }
        }
    }
}