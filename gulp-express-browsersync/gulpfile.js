'use strict';

var gulp        = require('gulp'),
    browserSync = require('browser-sync'),
    nodemon     = require('gulp-nodemon');

gulp.task('default', ['browser-sync'], function() {
});

gulp.task('browser-sync', ['nodemon'], function() {
    browserSync.init(null, {
        proxy: 'http://localhost:5000',
        files: ['public/**/*'],
        port: 7000
    });
});

gulp.task('nodemon', function(f) {
    var started = false;               // to avoid nodemon being started multiple times
    return nodemon({
        script: 'index.js'
    }).on('start', function() {
        if (!started) {
            f();
            started = true;
        }
    });
});