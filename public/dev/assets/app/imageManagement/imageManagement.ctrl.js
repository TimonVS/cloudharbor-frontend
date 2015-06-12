'use strict';

function imageManagementCtrl ($scope, $log, $modal, Server, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  vm.createImage = createImage

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function createImage (server) {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'app/imageManagement/create/imageCreateForm.tpl.html',
      controller: 'imageCreateForm',
      controllerAs: 'vm',
      size: 'lg',
      resolve: {
        server: function () {
          return server
        }
      }
    })

    modalInstance.result
      .then(function (selectedItem) {

      })
      .catch(function (error) {

      })
  }

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

}

angular
  .module('app.imageManagement')
  .controller('imageManagementCtrl', imageManagementCtrl);
