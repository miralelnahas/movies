package com.areeb.ui.views.movies

import com.areeb.ui.base.BaseIntent

sealed class SearchIntent : BaseIntent() {
    class UpdateSearchQuery(val searchQuery: String) : SearchIntent()
    data object SearchMovies : SearchIntent()
}