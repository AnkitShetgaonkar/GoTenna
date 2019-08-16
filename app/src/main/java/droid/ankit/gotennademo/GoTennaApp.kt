package droid.ankit.gotennademo

import android.app.Application
import androidx.room.Room
import droid.ankit.database.PinPointDao
import droid.ankit.database.PinPointDatabase
import droid.ankit.gotennademo.util.PermissionManager
import droid.ankit.network.FakeApi
import droid.ankit.network.GoTennaServiceImpl
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

class GoTennaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(appModule))
    }
}

val appModule: Module = module {
    single { Room.databaseBuilder(get(), PinPointDatabase::class.java, "pinpoint-db")
        .build() }
    single { get<PinPointDatabase>().pinPointDao() }
    single { DataRepository() }
    single { FakeApi(get()) }
    single { GoTennaServiceImpl()}
    single { PermissionManager(get()) }
}
