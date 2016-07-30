'use strict';

var express = require('express');
var app     = express(),
    router  = express.Router();

app.use(express.static(__dirname + '/public'));
app.use('/node_modules', express.static(__dirname + '/node_modules'));
app.get('/', function(req, res) {
    res.sendfile('./public/index.html');
});

app.listen(5000);