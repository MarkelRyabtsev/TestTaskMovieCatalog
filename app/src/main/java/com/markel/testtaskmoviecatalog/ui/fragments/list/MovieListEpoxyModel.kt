package com.markel.testtaskmoviecatalog.ui.fragments.list

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.markel.testtaskmoviecatalog.R

@EpoxyModelClass(layout = R.layout.item_movie_movielist)
abstract class MovieListEpoxyModel : EpoxyModelWithHolder<MovieListEpoxyModel.Holder>() {

    @EpoxyAttribute
    var movieId = ""
    @EpoxyAttribute
    var imagePoster = ""
    @EpoxyAttribute
    var textTitle = "--"
    @EpoxyAttribute
    var rating = 0.0f
    @EpoxyAttribute
    var voteCount = ""
    @EpoxyAttribute
    var releaseDate = ""
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var itemClickListener: (String) -> Unit = {}

    override fun bind(holder: Holder) {
        Glide.with(holder.imagePoster)
            .load(imagePoster)
            .error(R.color.darkBlue)
            .placeholder(R.color.darkBlue)
            .into(holder.imagePoster)
        holder.textTitle.text = textTitle
        holder.ratingBar.rating = rating
        holder.textVoteCount.text = voteCount
        holder.textReleaseDate.text = releaseDate
        holder.itemView.setOnClickListener { itemClickListener(movieId) }
    }

    class Holder : EpoxyHolder() {

        lateinit var itemView: View
        lateinit var imagePoster: ImageView
        lateinit var textTitle: TextView
        lateinit var ratingBar: RatingBar
        lateinit var textVoteCount: TextView
        lateinit var textReleaseDate: TextView

        override fun bindView(itemView: View) {
            this.itemView = itemView
            imagePoster = itemView.findViewById(R.id.imageView_poster_itemMovieMovieList)
            textTitle = itemView.findViewById(R.id.textView_title_itemMovieMovieList)
            ratingBar = itemView.findViewById(R.id.ratingBar_rating_itemMovieMovieList)
            textVoteCount = itemView.findViewById(R.id.textView_voteCount_itemMovieMovieList)
            textReleaseDate = itemView.findViewById(R.id.textView_releaseDate_itemMovieMovieList)
        }

    }
}