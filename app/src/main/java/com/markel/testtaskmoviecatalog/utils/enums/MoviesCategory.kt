package com.markel.testtaskmoviecatalog.utils.enums

import androidx.annotation.StringRes
import com.markel.testtaskmoviecatalog.R

enum class MoviesCategory(val key: String, @StringRes val strRes: Int) {

    NOW_PLAYING("now_playing", R.string.category_now_playing),
    TOP_RATED("top_rated", R.string.category_top_rated),
    POPULAR("popular", R.string.category_popular),
    UPCOMING("upcoming", R.string.category_upcoming),
    BY_FILTER("byFilter", R.string.by_filter)
}