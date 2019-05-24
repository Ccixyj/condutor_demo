package me.xyj.conduct.demo.glide.support

import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.manager.RequestManagerTreeNode

class GlideSupport(private val controller: Controller) : RequestManagerTreeNode {

    private var lifecycle: ControllerLifecycle? = null

    var requestManager: RequestManager? = null
        private set

    init {
        controller.addLifecycleListener(object : Controller.LifecycleListener() {
            var hasDestroyedGlide: Boolean = false
            var hasExited: Boolean = false


            override fun postCreateView(controller: Controller, view: View) {
                lifecycle = ControllerLifecycle()
                requestManager = RequestManager(
                    Glide.get(controller.activity!!),
                    lifecycle!!,
                    this@GlideSupport,
                    controller.activity!!
                )
                hasDestroyedGlide = false
            }

            override fun postAttach(controller: Controller, view: View) {
                lifecycle?.onStart()
            }

            override fun postDetach(controller: Controller, view: View) {
                lifecycle?.onStop()
            }

            override fun postDestroy(controller: Controller) {
                // Last controllers in the backstack may be destroyed without transition (onChangeEnd() not getting called)
                val isLast = !controller.router.hasRootController()
                if ((hasExited || isLast) && !hasDestroyedGlide) {
                    destroyGlide()
                }
            }

            override fun onChangeEnd(
                controller: Controller,
                changeHandler: ControllerChangeHandler,
                changeType: ControllerChangeType
            ) {
                // onChangeEnd() could be called after postDestroy(). We prefer to release Glide as
                // late as possible because releasing Glide clears out all ImageViews and they
                // appear blank during a transition.
                hasExited = !changeType.isEnter
                val viewDestroyed = controller.view == null
                if (hasExited && viewDestroyed && !hasDestroyedGlide) {
                    destroyGlide()
                }
            }

            private fun destroyGlide() {
                lifecycle?.onDestroy()
                lifecycle = null
                requestManager = null
                hasDestroyedGlide = true
            }
        })
    }

    override fun getDescendants(): Set<RequestManager> = collectRequestManagers(controller)

    /**
     * Recursively gathers the [RequestManager]s of a given [Controller] and all its child controllers.
     * The [Controller]s in the hierarchy must implement [HasGlideSupport] in order for their
     * request managers to be collected.
     */
    private fun collectRequestManagers(
        controller: Controller,
        collected: MutableSet<RequestManager> = HashSet()
    ): Set<RequestManager> {

        if (!controller.isDestroyed && !controller.isBeingDestroyed) {
            if (controller is HasGlideSupport) {
                controller.glideSupport.requestManager?.let {
                    collected.add(it)
                }
            }

            controller.childRouters
                .flatMap { childRouter -> childRouter.backstack }
                .map { routerTransaction -> routerTransaction.controller() }
                .forEach { controlr -> collectRequestManagers(controlr, collected) }
        }

        return collected
    }
}