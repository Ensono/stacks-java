var gulp = require('gulp');
var inlinesource = require('gulp-inline-source');

gulp.task('inlinesource', function () {
    return gulp.src('jacoco/**/*.html')
        .pipe(inlinesource({attribute: false}))
        .pipe(gulp.dest('./jacoco-inline'));
});
