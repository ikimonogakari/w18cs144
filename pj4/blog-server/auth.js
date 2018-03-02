var express = require('express');
var bcrypt = require('bcrypt');
var jwt = require('jsonwebtoken');
var router = express.Router();
var mongo = require('./db');

router.use(function(req, res, next){
	var username = req.query.username;
	var password = req.query.password;
	var redirect = req.query.redirect;
	var queryparams = {};
	queryparams['username'] = username;
	mongo.findDocuments('Users', queryparams, function(userEntry){
		var savedpswd = userEntry['password'];
		if(password == savedpswd){
			var payload = {
				"usr": username,
				"exp": "2h"
			};
			var token = jwt.sign(payload, "C-UFRaksvPKhx1txJYFcut3QGxsafPmwCY6SCly3G6c", {"alg":"HS256", "typ":"JWT"});
			res.cookie("jwt", token);
			res.redirect(redirect);
		} else {
			res.render('login', {"notice": "Login in failed!"});
		}
	});
});

module.exports = router;