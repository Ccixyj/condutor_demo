package me.xyj.conduct.demo.glide.support

/**
 * this interface must with Controller
 */
interface IGlideSupportProvider {

    val provider: GlideSupport

    fun glide() = provider.requestManager
        ?: throw IllegalArgumentException("You cannot start a load until the Controller has been bound to a Context and postCreateView.")
}


