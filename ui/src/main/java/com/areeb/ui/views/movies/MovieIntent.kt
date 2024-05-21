package com.areeb.ui.views.movies

import com.areeb.common.SortType
import com.areeb.ui.base.BaseIntent

sealed class MovieIntent : BaseIntent() {

    data object LoadMovies : MovieIntent()
    class ChangeSortType(var sortType: SortType) : MovieIntent()
}