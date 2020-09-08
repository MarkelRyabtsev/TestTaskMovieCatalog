package com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory

@EpoxyModelClass(layout = R.layout.header_movies_category)
abstract class CategoryHeaderHolder : EpoxyModelWithHolder<CategoryHeaderHolder.Holder>() {

    @EpoxyAttribute
    lateinit var category: MoviesCategory
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: OnHeaderClickListener? = null

    interface OnHeaderClickListener {
        fun onViewAllClicked(category: MoviesCategory)
    }

    override fun bind(holder: Holder) {
        holder.textTitle.setText(category.strRes)
        holder.buttonViewAll.setOnClickListener { clickListener?.onViewAllClicked(category) }
    }

    override fun unbind(holder: Holder) {
        holder.buttonViewAll.setOnClickListener(null)
    }

    class Holder : EpoxyHolder() {
        lateinit var textTitle: TextView
        lateinit var buttonViewAll: Button

        override fun bindView(itemView: View) {
            textTitle = itemView.findViewById(R.id.textView_categoryHeader_headerMoviesCategory)
            buttonViewAll = itemView.findViewById(R.id.button_viewAll_headerMoviesCategory)
        }
    }
}