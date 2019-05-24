package me.xyj.conduct.demo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import me.xyj.conduct.demo.vm.HomeController
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


class MainActivity : AppCompatActivity() ,LifecycleOwner{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val router =
            currentScope.inject<Router> { parametersOf(this, main_content, savedInstanceState) }
                .value
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(HomeController().apply {
                retainViewMode = Controller.RetainViewMode.RETAIN_DETACH
            }))
        }
        Log.d("MainActivity", "onCreate $router")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d("MainActivity", "onPostCreate $currentScope")
//        Log.d("MainActivity", "onPostCreate $vm ")

    }

    override fun onBackPressed() {
        currentScope.get<Router>().let {
            if (!it.handleBack()) {
                super.onBackPressed()
            }
        }
    }

    override fun getLifecycle(): Lifecycle {
        return super.getLifecycle()
    }

    companion object {
        val mainModule = module {

            scope(named<MainActivity>()) {
                scoped { (activity: Activity, container: ViewGroup, savedInstanceState: Bundle?) ->
                    Conductor.attachRouter(activity, container, savedInstanceState)
                }
            }
        }
    }
}
