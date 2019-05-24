package me.xyj.conduct.demo

import android.app.Application
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import leakcanary.LeakSentry
import me.xyj.conduct.demo.vm.DemoRepo
import me.xyj.conduct.demo.vm.SecondViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakSentry.config =
            LeakSentry.config.copy(
                watchFragmentViews = true,
                watchActivities = true,
                enabled = true,
                watchFragments = true,
                watchDurationMillis = 6000
            )
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            val controllerModuel = module {
                single { HorizontalChangeHandler() }
                single { FadeChangeHandler() }
            }
            val repos = module {
                single { DemoRepo() }
                single { SecondViewModel.Factory(get()) }
                viewModel { SecondViewModel(get()) }
            }
            loadKoinModules(MainActivity.mainModule, controllerModuel, repos)
        }
    }
}