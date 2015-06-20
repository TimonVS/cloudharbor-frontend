'use strict';

angular
  .module('app.dashboard', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('dashboard', {
        url: '/app/dashboard',
        parent: 'content',
        views: {
          'content': {
            templateUrl: 'app/dashboard/dashboard.tpl.html'
          },
          'header': {
            template: '<ch-header title="Dashboard">'
          }
        }
      })
  })
