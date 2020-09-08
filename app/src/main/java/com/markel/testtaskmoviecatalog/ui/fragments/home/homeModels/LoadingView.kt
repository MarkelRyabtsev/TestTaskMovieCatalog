package com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.markel.testtaskmoviecatalog.R

@EpoxyModelClass(layout = R.layout.view_load_init)
abstract class HomeLoadInitView : EpoxyModel<View>()

@EpoxyModelClass(layout = R.layout.view_load_more)
abstract class HomeLoadMoreView : EpoxyModel<View>()