'use strict';

angular
  .module('app.content', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('content', {
        abstract: true,
        templateUrl: 'app/content/content.tpl.html'
      })
  })
