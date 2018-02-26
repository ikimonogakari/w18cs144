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

// read
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
	});
}

// create
exports.postDoc = function (collName, doc, callback) {
	let db = null;
	promise_count = connect()
	.then(dbClient => {
		db = dbClient;
		return db.db(dbName).collection(collName);
	})
	.then(collection => {
		var lookup = {};
		lookup['username'] = doc['username'];
		lookup['postid'] = doc['postid'];
		console.log('find query:');
		console.log(lookup);
		return collection.count(lookup);
	})
	.catch(err => {
		console.log('error: mongodb operations');
		console.error(err);
	})
	.then(count => {
		console.log('find docs: ' + count);
		pro = new Promise((resovle, reject) => {
			if (count === 0) {
				resovle();
			} else {
				reject("existed");
			}
		})
		return pro;
	});

	var success = function() {
		console.log('start inserting');
		console.log(doc);
		insert = new Promise((resovle) => {
			collection = db.db(dbName).collection(collName);
			console.log('The inserted docs are:');
			console.log(doc);
			resovle(collection.insertOne(doc));
		})
		insert
		.then(result => {
			console.log(result.insertedCount);
			callback(result.insertedCount);
			db.close();
		}).catch(err => {
			console.log('error: mongodb operations');
			console.error(err);
		});
	}

	promise_count 
	.then(success, () => {
		db.close();
		callback(0);
	});
}

// delete
exports.deleteDoc = function (collName, doc, callback) {
	let db = null;
	connect()
	.then((dbClient) => {
		db = dbClient;
		return db.db(dbName).collection(collName);
	})
	.then((collection) => {
		console.log('query patterns are:');
		console.log(doc);
		return collection.findOneAndDelete(doc);
	})
	.then((result) => {
		console.log(result);
		callback(result.value);
		db.close();
	})
	.catch((err) => {
		console.log('error: mongodb operations');
		console.error(err);
	});
}

// update
exports.updateDoc = function (collName, doc, callback) {
	let db = null;
	var filter = {'username': doc['username'],
				  'postid': doc['postid']};
	var update = {$set: {
		'modified': doc['modified'],
		'body': doc['body'],
		'title': doc['title']
	}}
	connect()
	.then((dbClient) => {
		db = dbClient;
		return db.db(dbName).collection(collName);
	})
	.then((collection) => {
		console.log('query patterns are:');
		console.log(doc);
		return collection.findOneAndUpdate(filter, update);
	})
	.then((result) => {
		console.log(result);
		callback(result.value);
		db.close();
	})
	.catch((err) => {
		console.log('error: mongodb operations');
		console.error(err);
	});
}