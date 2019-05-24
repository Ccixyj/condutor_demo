package me.xyj.conduct.demo.glide.support

/**
 * Must be implemented by [com.bluelinelabs.conductor.Controller]s to have controller-scoped Glide 
 * resource management (image request pausing, cancelation, cleanup, etc.)
 */
interface HasGlideSupport {
    val glideSupport: GlideSupport
}