'use strict';

angular.module('app', [
  'ngResource',
  'ngLodash',
  'ngAnimate',
  'ui.router',
  'ui.bootstrap',
  'angular-loading-bar',
  'app.util',
  'app.containerManagement',
  'app.serverManagement'
])

  .config(function($httpProvider, $stateProvider, $urlRouterProvider, $locationProvider, cfpLoadingBarProvider) {

    // $http config
    $httpProvider.defaults.useXDomain = true
    delete $httpProvider.defaults.headers.common['X-Requested-With']

    // loading bar config
    cfpLoadingBarProvider.includeSpinner = false

    // router config
    $urlRouterProvider.otherwise('/app/dashboard')

    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    })

    $stateProvider
      .state('content', {
        abstract: true,
        templateUrl: 'app/content/content.tpl.html'
      })

      .state('dashboard', {
        url: '/app/dashboard',
        templateUrl: 'app/dashboard/dashboard.tpl.html'
      })

      .state('servers', {
        url: '/app/servers',
        templateUrl: 'app/serverManagement/overview.tpl.html',
        controller: 'serverManagementCtrl',
        controllerAs: 'serverManagement',
        resolve: {
          servers: function (Server) {
            return Server.query().$promise
          }
        }
      })
      .state('servers.show', {
        url: '/app/servers/show/:id',
        templateUrl: 'app/serverManagement/show/serverShow.tpl.html',
        controller: 'serverShowCtrl',
        controllerAs: 'vm',
        parent: 'content',
        resolve: {
          server: function ($stateParams, Server) {
            return Server.get({ id: $stateParams.id }).$promise
          }
        }
      })

      .state('containers', {
        url: '/app/containers',
        templateUrl: 'app/containerManagement/overview.tpl.html',
        controller: 'containerManagementCtrl',
        controllerAs: 'containerManagement',
        resolve: {
          servers: function (Server) {
            return Server.query().$promise
          }
        }
      })
  })

  .run(function ($rootScope, $state, $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams
  })

  // ------------------------------------------------------------------
  // Mock API
  // ------------------------------------------------------------------

  /*.run(function($httpBackend) {
    var serverNames = ['devbit.nl', 'back-end', 'Docker test']
    var containerNames = ['container-1', 'container-2', 'container-3']

    var servers = serverNames.map(function (name) {
      return {
        'id': 3164444,
        'name': name,
        'memory': 512,
        'vcpus': 1,
        'disk': 20,
        'locked': false,
        'status': 'active',
        'kernel': {
          'id': 2233,
          'name': 'Ubuntu 14.04 x64 vmlinuz-3.13.0-37-generic',
          'version': '3.13.0-37-generic'
        },
        'created_at': '2014-11-14T16:29:21Z',
        'features': [
          'backups',
          'ipv6',
          'virtio'
        ],
        'backup_ids': [
          7938002
        ],
        'snapshot_ids': [],
        'image': {
          'id': 6918990,
          'name': '14.04 x64',
          'distribution': 'Ubuntu',
          'slug': 'ubuntu-14-04-x64',
          'public': true,
          'regions': [
            'nyc1',
            'ams1',
            'sfo1',
            'nyc2',
            'ams2',
            'sgp1',
            'lon1',
            'nyc3',
            'ams3',
            'nyc3'
          ],
          'created_at': '2014-10-17T20:24:33Z',
          'type': 'snapshot',
          'min_disk_size': 20
        }
      }
    })

    var containers = containerNames.map(function (name) {
      return {
        'Id': name,
        'Image': 'training/webapp:latest',
        'Command': 'python app.py',
        'Created': 1432541130,
        'Status': 'Up 5 hours',
        'Ports': [
          {
            'IP': '0.0.0.0',
            'PrivatePort': 5000,
            'PublicPort': 32770,
            'Type': 'tcp'
          }
        ]
      }
    })

    // mocks current list of servers
    $httpBackend.whenGET('/mocks/servers').respond(servers)

    // mocks current list of containers
    $httpBackend.whenGET('/mocks/containers').respond(containers)

    // dont mock anything else, specify pass through to avoid error.
    $httpBackend.whenGET(/^\w+.*!/).passThrough()
    $httpBackend.whenPOST(/^\w+.*!/).passThrough()
    $httpBackend.whenGET(/server-options/).passThrough()
    $httpBackend.whenGET(/servermanagement\/servers/).passThrough()
    $httpBackend.whenPOST(/servermanagement\/servers\/add/).passThrough()
    $httpBackend.whenGET(/servermanagement\/servers\/add/).passThrough()
    $httpBackend.whenGET(/management\/containers/).passThrough()
  })*/

  .constant('accordionConfig', {
    closeOthers: false
  })
