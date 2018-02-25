var express = require('express');
var router = express.Router();
// var mongo = require('mongodb')
var mongo = require('../db');

// Read operation
var findDocuments = function(collName, queryParams, callback){
	let db = null;
	
	var collection = db.get().db(dbName).collection(collName);
	console.log(queryParams);
	collection.find(queryParams).toArray(function(err, docs){
		if(err){
			console.log('Error happens in read process.');
			callback();
		} else {
			console.log(docs);
			callback(docs);
		}
    });
};

router.get('/:username/:postid', function(req, res){
	var username = req.params.username;
	var postid = req.params.postid;
	var params = {};
	if(username != null){
		params['username'] = username;
	}
	if(postid != null){
		params['postid'] = Number(postid);
	}
	
	mongo.findDocuments('Posts', params, function(docs){
			console.log('prepare to render blogpost.ejs');
			res.render('blogpost', {'posts' : docs});
	})
});

router.get('/:username', function(req, res){
	var username = req.params.username;
	var offset = req.query.start;

	var params = {};
	if(username != null){
		params['username'] = username;
	}
	var start = 0;
	if(offset != null){
		start = Number(offset);
	}
	console.log("offset is:" + offset);

	params['postid'] = {$gt: start, $lt: start+6};
	mongo.findDocuments('Posts', params, function(docs){
		console.log('prepare to render personBlog.ejs');
		var nextStartUrl = 'localhost:3000/blog/'+username+'?start='+String(start+5);
		console.log(nextStartUrl);
		if(docs.length < 5){
			nextStartUrl = 'javascript:void(0);';
		}
		res.render('bloglist', {'posts' : docs, 'nextUrl': nextStartUrl});
	});
});

module.exports = router;