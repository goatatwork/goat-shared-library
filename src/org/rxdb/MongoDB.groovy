package org.rxdb

@Grab(group='org.mongodb', module='mongo-java-driver', version='3.12.7')

import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document

class MongoDB {

    static MongoClient mongoClient

    static void connect(Map params) {
        def serverAddress = new ServerAddress(params.server, params.port)
        def credentials = MongoCredential.createCredential(params.username, params.authDB, params.password.toCharArray())

        mongoClient = new MongoClient(serverAddress, [credentials] as List)
    }
    
    static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close()
            mongoClient = null
        }
    }
    
    static void execute(Map params, Closure closure) {
        if (mongoClient == null) {
            connect(params)
        }

        def database = mongoClient.getDatabase(params.databaseName)
        def collection = database.getCollection(params.collectionName, Document)

        // Iterate over documents and call closure
        def cursor = collection.find().iterator()
        while (cursor.hasNext()) {
            closure.call(cursor.next())
        }
    }
    
    static void find(Map params, Map query, Closure closure) {
        if (mongoClient == null) {
            connect(params)
        }

        def database = mongoClient.getDatabase(params.databaseName)
        def collection = database.getCollection(params.collectionName, Document)

        // Execute find query and call closure for each result
        def cursor = collection.find(new Document(query)).iterator()
        while (cursor.hasNext()) {
            closure.call(cursor.next())
        }
    }
    
}
