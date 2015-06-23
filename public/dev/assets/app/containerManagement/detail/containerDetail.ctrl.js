'use strict'

function containerDetail ($stateParams, container, Container, flash) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.container = container
  var params = { id: container.Id, serverUrl: $stateParams.serverUrl }

  // Function assignment
  vm.startContainer = startContainer
  vm.stopContainer = stopContainer
  vm.refreshTop = top

  top()

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function startContainer () {
    return Container.start(params).$promise
      .then(function (data) {
        console.log(data)
        flash('success', data.success)
      })
      .catch(function (error) {
        console.log(error)
        flash('danger', error.data.error)
      })
  }

  function stopContainer () {
    return Container.stop(params).$promise
      .then(function (data) {
        console.log(data)
        flash('success', data.success)
      })
      .catch(function (error) {
        console.log(error)
        flash('danger', error.error)
      })
  }

  function top () {
    return Container.top(params).$promise
      .then(function (data) {
        vm.processes = data
      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

}

angular
  .module('app.containerManagement')
  .controller('containerDetailCtrl', containerDetail)
