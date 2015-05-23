# Cloudharbor Front-end

## Requirements
- [Node.js](https://nodejs.org) 0.10 or up
- [npm](https://www.npmjs.com) (included with Node.js)
- [Scala](http://www.scala-lang.org) 2.11.4 or up
- [Play framework](https://www.playframework.com)
- [Activator](https://www.typesafe.com/community/core-tools/activator-and-sbt) (included with Play framework)
- [Gulp](http://gulpjs.com)

## Install/run the app

Before running the app make sure you've installed all the requirements.

```
npm install -g bower gulp-cli
```

After installing the requirements run the following commands:

```
//installs all necessary npm modules defined in package.json
npm install
//installs all necessary bower packages defined in bower.json
bower install
//compiles all JS and Sass and injects bower packages in main.scala.html
gulp build:dev
//compiles the Play application
activator compile
```

To run the application run the following commands:

```
activator run
//if you wish to use Browsersync to inject CSS styles use gulp serve, otherwise just use gulp watch
gulp serve
```
