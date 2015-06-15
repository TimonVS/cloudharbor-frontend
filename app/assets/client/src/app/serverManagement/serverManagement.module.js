'use strict';

angular
  .module('app.serverManagement', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('servers', {
        url: '/app/servers',
        parent: 'content',
        abstract: true
      })

      .state('servers.overview', {
        url: '/overview',
        views: {
          'content@content': {
            templateUrl: 'app/serverManagement/overview.tpl.html',
            controller: 'serverManagementCtrl',
            controllerAs: 'serverManagement'
          },
          'header@content': {
            template: '<ch-header title="Server management">'
          }
        },
        resolve: {
          servers: function (Server) {
            return Server.query().$promise
          }
        }
      })

      .state('servers.show', {
        url: '/show/:id',
        views: {
          'content@content': {
            templateUrl: 'app/serverManagement/show/serverShow.tpl.html',
            controller: 'serverShowCtrl',
            controllerAs: 'vm'
          },
          'header@content': {
            template: '<ch-header title="Server: {{ ::vm.server.name }}">',
            controller: 'serverShowCtrl',
            controllerAs: 'vm'
          }
        },
        resolve: {
          server: function ($stateParams, Server) {
            return Server.get({ id: $stateParams.id }).$promise
          }
        },
        redirectTo: 'servers.show.containers'
      })
      .state('servers.show.containers', {
        url: '/containers',
        views: {
          'tab-content': {
            template: '<container-list server="vm.server"></container-list>'
          }
        }
      })
      .state('servers.show.images', {
        url: '/images',
        views: {
          'tab-content': {
            template: '<image-list server="vm.server"></image-list>'
          }
        }
      })
  })
