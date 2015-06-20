'use strict';

function serverCreateForm ($scope, $http, $modalInstance, CloudService, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.generateToken = generateToken
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.form = {}
  vm.server = {}

  // Requests
  vm.sshKeys = CloudService.sshKeys()
  vm.serverOptions = CloudService.serverOptions()

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function generateToken () {
    if (generateActive) return

    var generateActive = true

    // todo: implement in back-end
    $http.get('https://cloudharbor-reverse-proxy.herokuapp.com/https://discovery.etcd.io/new').then(function (response) {
      vm.server.discovery_token = response.data
      generateActive = false
    })
  }

  function cancel () {
    $modalInstance.dismiss()
  }

  function submit (form) {
    if (form.$invalid) return

    vm.busy = true

    var request = {
      'name': vm.server.name,
      'image': 'coreos-stable',
      'region': vm.server.region.slug,
      'size': vm.server.size,
      'etcd': vm.server.discovery_token,
      'ipv6': vm.server.ipv6 || false,
      'backups': vm.server.backups || false,
      'ssh_keys': [vm.server.ssh_keys.id] //TODO
    }

    var server = new Server(request)

    return server.$create()
      .then(function (data) {
        vm.busy = false
        $modalInstance.close(data)
      })
      .catch(function (error) {
        vm.busy = false
        // todo: error handling
      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.serverManagement')
  .controller('serverCreateFormCtrl', serverCreateForm);
