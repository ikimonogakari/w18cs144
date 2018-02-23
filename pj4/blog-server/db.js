var MongoClient = require('mongodb').MongoClient;

var conn = {
	db:null,
}

exports.connect = function(url, done){
	if(conn.db) return done();

	MongoClient.connect(url, function(err, dbConn){
		if(err){return done(err);}
		conn.db = dbConn;
		done();
	});
}

exports.get = function(){
	return conn.db;
}

exports.close = function(done){
	if(conn.db){
		conn.db.close(function(err, result){
			conn.db = null
			conn.mode = null
			done(err)
		})
	}
}