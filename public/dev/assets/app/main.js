'use strict';

angular.module('app', ['ngRoute', 'ngResource', 'ngLodash', 'app.util', 'app.containerManagement'])

  .config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.useXDomain = true
    delete $httpProvider.defaults.headers.common['X-Requested-With']
  }])
