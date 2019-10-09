package me.xyj.conduct.demo

import android.app.Application
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import leakcanary.AppWatcher
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
        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = true)
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
            loadKoinModules(MainActivity.mainModule)
            loadKoinModules(controllerModuel)
            loadKoinModules(repos)
        }
    }
}