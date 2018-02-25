var express = require('express');
var router = express.Router();
var mongo = require('mongodb')
var db = require('./db');

// get all the user's posts
router.get('/:username', function(req, res, next) {
    let username = req.params.username;
});

router.get('/:username/:postid', function(req, res, next){

});

// new a blog post
router.post('/:username/:postid', function(req, res, next){

});

// edit a blog post
router.put('/:username/:postid', function(req, res, next){

});

router.delete('/:username/:postid', function(req, res, next){

});
module.exports = router;
