'use strict'

angular
  .module('app.imageManagement', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('images', {
        url: '/app/images',
        parent: 'content',
        abstract: true
      })
      .state('images.overview', {
        url: '/overview',
        views: {
          'content@content': {
            templateUrl: 'app/imageManagement/overview.tpl.html',
            controller: 'imageManagementCtrl',
            controllerAs: 'vm'
          },
          'header@content': {
            template: '<ch-header title="Image management">'
          }
        },
        resolve: {
          servers: function (Server) {
            return Server.query().$promise
          }
        }
      })
  })
