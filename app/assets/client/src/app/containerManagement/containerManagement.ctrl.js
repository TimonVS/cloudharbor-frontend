'use strict';

function containerManagementCtrl ($scope) {

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

  $scope.$on('imageSelected', function (event, data) {
    event.stopPropagation()
    console.log(data)
  })
}

angular
  .module('app.containerManagement')
  .controller('containerManagementCtrl', containerManagementCtrl);
