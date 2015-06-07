'use strict';

function header () {
  return {
    restrict: 'E',
    templateUrl: 'app/content/header/header.tpl.html',
    scope: {
      title: '@'
    }
  }
}

angular
  .module('app.content')
  .directive('chHeader', header);
