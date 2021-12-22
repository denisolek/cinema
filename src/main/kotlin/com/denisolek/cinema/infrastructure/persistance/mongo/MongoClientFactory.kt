package com.denisolek.cinema.infrastructure.persistance.mongo

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation.JAVA_LEGACY
import org.litote.kmongo.KMongo

// TODO it should be something similar to HttpClientProperties.kt - configurable properties/timeouts per client
object MongoClientFactory {
    fun mongodb(): MongoDatabase {
        val settings = MongoClientSettings.builder().uuidRepresentation(JAVA_LEGACY).build()
        val client = KMongo.createClient(settings)
        return client.getDatabase("cinema")
    }
}

