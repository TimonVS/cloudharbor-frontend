'use strict';

function imageCreateFormCtrl ($scope, $modalInstance, server, DockerImage, notifications) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this
  console.log(server)

  // Function assignment
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.form = {}
  vm.image = {}

  var params = { serverUrl: server.getIp() }

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function cancel () {
    $modalInstance.dismiss('cancel')
  }

  function submit (form) {
    if (form.$invalid) return

    vm.busy = true

    var image = new DockerImage()

    var img = vm.image.image.name.split('/')
    var repo = img[0]
    var name = img[1]

    // in case of an image without a repo
    if (!img[1]) {
      name = img[0]
      repo = 'library'
    }

    var extendedParams = { name: name, repo: repo}

    angular.extend(params, extendedParams)

    image.$create(params)
      .then(function (data) {
        vm.busy = false
        $modalInstance.close(data)
      })
      .catch(function (error) {
        vm.busy = false
      })
  }

}

angular
  .module('app.imageManagement')
  .controller('imageCreateForm', imageCreateFormCtrl);
