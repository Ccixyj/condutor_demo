package me.xyj.conduct.demo.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.controller_recycle.view.*
import me.xyj.conduct.demo.R
import me.xyj.conduct.demo.glide.support.ControllerRequestManager
import me.xyj.conduct.demo.glide.support.GlideSupport
import me.xyj.conduct.demo.glide.support.HasGlideSupport
import me.xyj.conduct.demo.vm.base.ViewModelController

class RecycleController() : ViewModelController(), HasGlideSupport {

    override val glideSupport: GlideSupport = GlideSupport(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_recycle, container, false)
        Log.d("onCreateView", "${v.rv_imgs} -> ${v.rv_imgs.layoutManager}")
        val rv = v.rv_imgs
        rv.layoutManager = FlexboxLayoutManager(applicationContext).apply {
            flexWrap = FlexWrap.WRAP
        }
        rv.adapter = CatAdapter()
        return v
    }

    inner class CatAdapter : RecyclerView.Adapter<CatViewHolder>() {

        private val CAT_IMAGE_IDS = listOf(
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_1.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_2.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_3.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_4.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_5.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_6.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_7.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_8.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_9.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_10.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_11.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_12.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_13.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_14.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_15.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_16.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_17.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_18.jpg",
            "https://raw.githubusercontent.com/google/flexbox-layout/master/demo-cat-gallery/src/main/res/drawable/cat_19.jpg"
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_cat, parent, false)
            return CatViewHolder(view)
        }

        override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
            val pos = position % CAT_IMAGE_IDS.size
            holder.bindTo(CAT_IMAGE_IDS[pos])
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)

        }

        override fun getItemCount() = CAT_IMAGE_IDS.size * 4
    }

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_cat)


        internal fun bindTo(drawableRes: String) {
            println("bind $drawableRes ${imageView.layoutParams}")
            ControllerRequestManager.with(this@RecycleController)
                .load(drawableRes)
                .skipMemoryCache(true)
                .override(Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
            val lp = imageView.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams) {
                lp.flexGrow = 1f
            }
        }
    }
}