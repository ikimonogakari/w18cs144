const
	MongoClient = require('mongodb').MongoClient,
	dbUrl = 'mongodb://localhost:27017',
	dbName = 'BlogServer';

var connect = function(){
	return new Promise((resovle, reject) => {
		MongoClient.connect(dbUrl, (err, db) => {
			if (err) {
				reject(err);
			} else {
				console.log('Successfully connected !');
				resovle(db);
			}
		});
	});
}

exports.findDocuments = function (collName, queryParams, callback) {
	let db = null;
	connect()
	.then((dbClient) => {
		db = dbClient;
		return db.db(dbName).collection(collName);
	})
	.then((collection) => {
		console.log('query patterns are:');
		console.log(queryParams);
		return collection.find(queryParams);
	})
	.then(cursor => cursor.toArray())
	.then((result) => {
		console.log(result);
		callback(result);
		db.close();
	})
	.catch((err) => {
		console.log('error: mongodb operations');
		console.error(err);
	}) 
}