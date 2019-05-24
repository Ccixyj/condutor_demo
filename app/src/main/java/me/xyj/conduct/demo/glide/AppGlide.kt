package me.xyj.conduct.demo.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import me.xyj.conduct.demo.BuildConfig

@GlideModule
class AppGlide : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.DEBUG)
        }

        super.applyOptions(context, builder)
    }

    override fun isManifestParsingEnabled() = false
}