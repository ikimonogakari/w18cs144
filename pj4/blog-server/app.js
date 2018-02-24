var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongo = require('mongodb')

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();

var db = require('./db');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');


// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/usernames', users);

// catch 404 and forward to error handler
/*
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});
*/

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

// setup mongodb and connection
var dbUrl = 'mongodb://localhost:27017';
var dbName = 'BlogServer';
db.connect(dbUrl, function(err){
	if(err){
		console.log('Something went wrong when connecting to MongoDB');
		process.exit(1);
	} else {
		console.log('Successfully connected!');
	}
});

process.env.PORT = 3000;
console.log('Port number:' + process.env.PORT);

// Read operation
var findDocuments = function(collName, queryParams, callback){
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

app.get('/blog/:username/:postid', function(req, res){
	var username = req.params.username;
	var postid = req.params.postid;
	var params = {};
	if(username != null){
		params['username'] = username;
	}
	if(postid != null){
		params['postid'] = Number(postid);
	}
	findDocuments('Posts', params, function(docs){
		console.log('prepare to render blogpost.ejs');
		res.render('blogpost', {'posts' : docs});
	});
});

app.get('/blog/:username', function(req, res){
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
	findDocuments('Posts', params, function(docs){
		console.log('prepare to render personBlog.ejs');
		var nextStartUrl = 'localhost:3000/blog/'+username+'?start='+String(start+5);
		console.log(nextStartUrl);
		if(docs.length < 5){
			nextStartUrl = 'javascript:void(0);';
		}
		res.render('bloglist', {'posts' : docs, 'nextUrl': nextStartUrl});
	});
});

module.exports = app;
