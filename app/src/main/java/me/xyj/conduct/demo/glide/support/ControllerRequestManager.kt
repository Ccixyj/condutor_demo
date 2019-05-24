package me.xyj.conduct.demo.glide.support

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bumptech.glide.RequestManager
import me.xyj.conduct.demo.MainActivity

object ControllerRequestManager {
    fun with(glideController: Controller): RequestManager {
        if (glideController !is HasGlideSupport) {
            throw ClassCastException("glideController must implement HasGlideSupport")
        }

        if (glideController.activity == null) {
            throw IllegalArgumentException("You cannot start a load until the Controller has been bound to a Context.")
        }

        return (glideController as HasGlideSupport).glideSupport.requestManager
            ?: throw UninitializedPropertyAccessException("requestManager not yet initialized for the given controller")
    }

}
