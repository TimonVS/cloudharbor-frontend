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
      .state('containers.detail', {
        url: '/detail/:id',
        views: {
          'content@content': {
            templateUrl: 'app/containerManagement/detail/containerDetail.tpl.html',
            controller: 'containerDetailCtrl',
            controllerAs: 'vm'
          },
          'header@content': {
            template: '<ch-header title="Container: {{ ::vm.container.name }}">',
            controller: 'containerDetailCtrl',
            controllerAs: 'vm'
          }
        },
        resolve: {
          container: function ($stateParams, Container) {
            return Container.get({ id: $stateParams.id, serverUrl: $stateParams.serverUrl }).$promise
          }
        }
      })
  })
