'use strict';

angular
  .module('app.containerManagement', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('containers', {
        url: '/app/containers',
        parent: 'content',
        abstract: true
      })
      .state('containers.overview', {
        url: '/overview',
        views: {
          'content@content': {
            templateUrl: 'app/containerManagement/overview.tpl.html',
            controller: 'containerManagementCtrl',
            controllerAs: 'containerManagement'
          },
          'header@content': {
            template: '<ch-header title="Container management">'
          }
        },
        resolve: {
          servers: function (Server) {
            return Server.query().$promise
          }
        }
      })
  })
