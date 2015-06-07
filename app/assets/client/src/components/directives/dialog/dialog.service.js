'use strict'

function dialogService ($modal, $rootScope) {
  this.alert = function (data) {
    var scope = $rootScope.$new()
    angular.extend(scope, data)

    return $modal.open({
      scope: scope,
      size: 'sm',
      animation: false,
      templateUrl: 'components/directives/dialog/dialog.tpl.html'
    }).result
  }

  this.confirm = function (data) {
    var scope = $rootScope.$new()
    angular.extend(scope, data)

    return $modal.open({
      scope: scope,
      size: 'sm',
      animation: false,
      templateUrl: 'components/directives/dialog/dialog.tpl.html'
    }).result
  }
}

angular
  .module('app.util')
  .service('Dialog', dialogService)
