'use strict'

function apiKeysCtrl ($http, CloudService, flash) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.updateApiKey = updateApiKey

  // Get api key
  activate()

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function activate () {
    CloudService.apiKey().$promise
      .then(function (data) {
        vm.apiKey = data.success
      })
      .catch(function () {
        flash('error', 'Something went wrong while retrieving the data')
      })
  }

  function updateApiKey () {
    return CloudService.addInfo({ apiKey: vm.apiKey }).$promise
      .then(function (data) {
        flash('success', 'API key successfully updated')
      })
      .catch(function (error) {
        vm.error = error.data.error
      })
  }

}

angular
  .module('app.user')
  .controller('apiKeysCtrl', apiKeysCtrl)
