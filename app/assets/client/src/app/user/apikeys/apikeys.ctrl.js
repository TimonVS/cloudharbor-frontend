'use strict'

function apiKeysCtrl ($http) {

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
    $http.get('/servermanagement/api-key')
      .then(function (response) {
        vm.apiKey = response.data.success
      })
  }

  function updateApiKey () {
    return $http.post('/servermanagement/add-info', { apiKey: 'b3f089e499e30801bdca182867f8b82971cf9dd12b2346e0534301a8e3423d67' })
      .then(function (response) {
        console.log(response)
      })
      .catch(function (error) {

      })
  }

}

angular
  .module('app.user')
  .controller('apiKeysCtrl', apiKeysCtrl)
