'use strict';

var gulp = require('gulp');
var paths = gulp.paths;

gulp.task('watch', ['inject'], function () {
  gulp.watch([
    paths.src + '/*.html',
    paths.src + '/{app,components}/**/*.tpl.html',
    paths.src + '/{app,components,stylesheets}/**/*.scss',
    paths.src + '/{app,components}/**/*.js',
    'bower.json'
  ], ['inject']);
});
