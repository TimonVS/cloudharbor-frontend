# Cloudharbor Front-end

## Requirements
- [Node.js](https://nodejs.org) 0.10 or up
- [npm](https://www.npmjs.com) (included with Node.js)
- [Scala](http://www.scala-lang.org) 2.11.4 or up
- [Play framework](https://www.playframework.com)
- [Activator](https://www.typesafe.com/community/core-tools/activator-and-sbt) (included with Play framework)
- [Gulp](http://gulpjs.com)

## Run/install the app for the first time

Before running the app make sure you've installed all the requirements.

```
npm install -g bower gulp-cli
```

After installing the requirements run the following commands:

```javascript
//installs all necessary npm modules defined in package.json
npm install

//installs all necessary bower packages defined in bower.json
bower install

//compiles all JS and Sass and injects bower packages in main.scala.html
gulp build:dev

//compiles the Play application
activator compile
```

## Run the app

To run the application run `activator run` and in a separate terminal window or tab run either `gulp watch` or `gulp serve`. Use `gulp watch` if you just want to compile client assets (JS, Sass etc.), use `gulp serve` if you also want the browser to refresh when you've made changes to client assets.