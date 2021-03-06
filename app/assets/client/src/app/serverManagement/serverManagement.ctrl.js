'use strict';

function serverManagementCtrl ($scope, $modal, $state, Server, servers, notifications, flash) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  // Sorting and search
  vm.sortType = 'name'
  vm.sortReverse = false
  vm.searchQuery = ''

  // Pagination
  vm.pagination = {
    from: 0,
    limit: 10,
    ranges: [10, 25, 50, 100]
  }

  vm.changePage = function (pageNum) {
    vm.pagination.from = (pageNum - 1) * vm.pagination.limit
  }

  vm.getServers = getServers
  vm.createServer = createServer
  vm.orderBy = orderBy
  vm.goTo = goTo

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function getServers () {
    vm.busy = true

    Server.query().$promise.then(function (data) {
      vm.servers = data
      vm.busy = false
    })
  }

  function createServer () {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'app/serverManagement/create/serverCreateForm.tpl.html',
      controller: 'serverCreateFormCtrl',
      controllerAs: 'vm',
      size: 'lg'
    })

    modalInstance.result
      .then(function (data) {
        // Get server data with id and add the server to the servers array
        Server.get({ id: data.success }).$promise
          .then(function (response) {
            vm.servers.unshift(response)
          })
          .catch(function(error) {
            flash('danger', error)
          })
      })
  }

  function orderBy (name) {
    if (name === vm.sortType) {
      vm.sortReverse = !vm.sortReverse
    } else {
      vm.sortReverse = false
      vm.sortType = name
    }
  }

  function goTo (server) {
    if (server.locked) return

    $state.go('servers.show', { id: server.id })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

  notifications.get('notification', function (event, notification) {
    if (notification.message.body === 'Server created') {
      vm.servers.some(function (server) {
        if (parseInt(notification.message.id, 10) === server.id) {

          $scope.$apply(function () {
            server.status = 'Active'
            server.locked = false
          })

          return true
        }
      })
    }
  })

}

angular
  .module('app.serverManagement')
  .controller('serverManagementCtrl', serverManagementCtrl);
