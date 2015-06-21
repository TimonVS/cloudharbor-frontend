'use strict';

function containerCreateFormCtrl ($scope, $modalInstance, server, Container, lodash) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.server = server
  vm.form = {}
  vm.container = {}

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function splitCommands (command) {
    var commands = command.split(' ')
    var result = []
    var begin = 0

    commands.forEach(function (cmd, index) {
      if (cmd.charAt(0) === '"') {
        begin = index
        return
      }
      else if (cmd.charAt(cmd.length - 1) === '"') {
        result.push(commands.slice(begin, index + 1).join(' '))
        begin = 0
        return
      }
      else if (begin) return
      result.push(cmd)
    })

    vm.commands = result

    return result
  }

  function cancel () {
    $modalInstance.dismiss('cancel');
  }

  function submit (form) {
    if (form.$invalid) return

    vm.busy = true

    var request = {
      Image: vm.container.image.RepoTags[0],
      cpuShares: vm.container.cpuShares,
      memory: vm.container.memory
    }

    if (vm.container.command) {
      angular.extend(request, splitCommands(vm.container.command))
    }

    var container = new Container(request)

    var params = { serverUrl: server.getIp() }

    if (vm.container.name) {
      params = angular.extend(params, { name: vm.container.name })
    }

    container.$create(params)
      .then(function (data) {
        vm.busy = false
        $modalInstance.close(data)
      })
      .catch(function (error) {
        vm.error = error.data.error
        vm.busy = false
      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('containerCreateForm', containerCreateFormCtrl);
