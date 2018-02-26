var express = require('express');
var router = express.Router();
var mongo = require('./db');

// get all the user's posts
router.get('/:username', function(req, res, next) {
    let username = req.params.username;
    let params = {};
    params['username'] = username;
    mongo.findDocuments('Posts', params, function(docs){
        let plainRes = JSON.stringify(docs);
        res.send(plainRes);
    });
});

router.get('/:username/:postid', function(req, res, next){
    let username = req.params.username;
    let postid = req.params.postid;
    let params = {};
    params['username'] = username;
    params['postid'] = Number(postid);
    mongo.findDocuments('Posts', params, function(docs){
        if (docs.length === 0) {
            res.sendStatus(404);
        } else {
            let plainRes = JSON.stringify(docs);
            res.send(plainRes);
        }
    });
});

// new a blog post
router.post('/:username/:postid', function(req, res, next){
    // body only contains username, postid, title and body
    console.log('body');
    console.log(req.body);
    // let plainRes = '[{"postid":99,"username":"cs144","created":1518669658420,"modified":1518669658420,"title":"Title 544","body":"!!!I am here."}]';
    var doc = req.body;
    doc['created'] = Date.now();
    doc['modified'] = Date.now();
    if (doc['username'] == null) {
        doc['username'] = req.params.username;
    }
    if (doc['postid'] == null) {
        doc['postid'] = Number(req.params.postid);
    }
    mongo.postDoc('Posts', doc, result => {
        console.log('result: ');
        console.log(result);
        if (result === 0) {
            res.sendStatus(400);
        } else {
            res.sendStatus(201);
        }
    });
});

// edit a blog post
router.put('/:username/:postid', function(req, res, next){
    // body only contains title and body
    // let plainRes = '[{"postid":99,"username":"cs144","created":1518669658420,"modified":1518669658420,"title":"Title 544","body":"New Body&*&*(^(*&^(*&"}]';
    var doc = req.body;
    doc['modified'] = Date.now();
    doc['username'] = req.params.username;
    doc['postid'] = Number(req.params.postid);
    mongo.updateDoc('Posts', doc, result => {
        console.log('result: ');
        console.log(result);
        if (result === null) {
            res.sendStatus(400);
        } else {
            res.sendStatus(200);
        }
    });
});

// delete a blog post
router.delete('/:username/:postid', function(req, res, next){
    let username = req.params.username;
    let postid = req.params.postid;
    let doc = {};
    doc['username'] = username;
    doc['postid'] = Number(postid);
    mongo.deleteDoc('Posts', doc, function(docs){
        if (docs == null) {
            res.sendStatus(400);
        } else {
            res.sendStatus(204);
        }
    });
});

module.exports = router;