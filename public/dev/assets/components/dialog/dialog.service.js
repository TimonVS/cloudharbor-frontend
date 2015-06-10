'use strict'

function dialogService ($modal, $rootScope) {
  this.alert = function (data) {
    var scope = $rootScope.$new()
    angular.extend(scope, data)

    return $modal.open({
      scope: scope,
      size: 'sm',
      animation: false,
      templateUrl: 'components/dialog/dialog.tpl.html'
    }).result
  }

  this.confirm = function (data) {
    var scope = $rootScope.$new()
    angular.extend(scope, data)

    return $modal.open({
      scope: scope,
      size: 'sm',
      animation: false,
      templateUrl: 'components/dialog/dialog.tpl.html'
    }).result
  }
}

angular
  .module('app.components')
  .service('Dialog', dialogService)
