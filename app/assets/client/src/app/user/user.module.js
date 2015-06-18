'use strict';

angular
  .module('app.user', [])
  .config(function ($stateProvider) {
    $stateProvider
      .state('user', {
        url: '/app/user',
        parent: 'content',
        abstract: true,
        views: {
          'content': {
            templateUrl: 'app/user/user.tpl.html'
          }
        }
      })

      .state('user.profile', {
        url: '/profile',
        views: {
          'content': {
            templateUrl: 'app/user/profile/profile.tpl.html',
            controller: 'profileCtrl',
            controllerAs: 'vm'
          },
          'header@content': {
            template: '<ch-header title="Profile settings">'
          }
        },
        resolve: {
          user: function (User) {
            return User.get().$promise
          }
        }
      })
      .state('user.apikeys', {
        url: '/apikeys',
        views: {
          'content': {
            templateUrl: 'app/user/apikeys/apikeys.tpl.html',
            controller: 'apiKeysCtrl',
            controllerAs: 'vm'
          },
          'header@content': {
            template: '<ch-header title="API keys">'
          }
        },
        resolve: {

        }
      })
  })
