'use strict'

function userDropdown ($rootScope, $window, User) {
  return {
    restrict: 'AE',
    replace: true,
    scope: {},
    templateUrl: 'app/user/dropdown/userDropdown.tpl.html',
    controller: function ($scope) {
      $rootScope.currentUser.then(function (user) {
        $scope.fullName = user.firstName + ' ' + user.prefix + ' ' + user.lastName
      })

      $scope.logout = function () {
        //User.logout() // todo: wait until login has been fixed
        $window.location.href = '/'
      }
    }
  }
}

angular
  .module('app.user')
  .directive('chUserDropdown', userDropdown)
