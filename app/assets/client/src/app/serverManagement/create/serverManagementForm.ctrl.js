'use strict';

function serverManagementFormCtrl ($scope, $http, CloudService, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.generateToken = generateToken
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.form = {}
  vm.server = {}

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
      vm.form.discovery_token = response.data
      generateActive = false
    })
  }

  function onSubmit (form) {
    if (form.$invalid) return

    vm.busy = true

    var request = {
      'name': vm.server.name,
      'image': 'coreos-stable',
      'region': vm.server.region.slug,
      'size': vm.server.size,
      'ipv6': vm.server.ipv6 || false,
      'backups': vm.server.backups || false,
      'ssh_keys': [vm.server.ssh_keys.id] //TODO
    }

    var server = new Server(request)

    server.$create()
      .then(function (data) {
        vm.busy = false
        $scope.$emit('serverCreated', data)
      })
      .catch(function (error) {
        vm.busy = false
        console.log(error)
      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.serverManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
