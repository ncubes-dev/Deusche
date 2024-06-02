package com.ndozdev.deuche.di


import android.content.Context
import com.ndozdev.deuche.data. QuestionRO
import com.ndozdev.deuche.data.repository.DataStoreRepositoryImpl
import com.ndozdev.deuche.data.repository.MongoRepositoryImpl
import com.ndozdev.deuche.dormain.repository.DataStoreRepository
import com.ndozdev.deuche.dormain.repository.MongoDBRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

interface AppModule {
    val database: Realm
    val mongodbRepository: MongoDBRepository
    val dataStoreRepository: DataStoreRepository
}

class AppModuleImpl (context:Context): AppModule {
    override val database: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf( QuestionRO::class))
            .compactOnLaunch()
            .build()
        Realm.open(config)
    }
    override val mongodbRepository: MongoDBRepository by lazy {
        MongoRepositoryImpl(database)
    }

    override val dataStoreRepository: DataStoreRepository by lazy {
        DataStoreRepositoryImpl(context)
    }

}