'use strict'

function panel () {
  return {
    restrict: 'AE',
    scope: {},
    templateUrl: 'components/panel/panel.tpl.html',
    transclude: true,
    replace: true
  }
}

function panelGroup () {
  return {
    restrict: 'AE',
    scope: {},
    templateUrl: 'components/panel/panelGroup.tpl.html',
    transclude: true,
    replace: true
  }
}

function panelHeading () {
  return {
    restrict: 'AE',
    scope: {},
    templateUrl: 'components/panel/panelHeading.tpl.html',
    transclude: true,
    replace: true,
    require: '^panel'
  }
}

function panelBody () {
  return {
    restrict: 'AE',
    scope: {},
    templateUrl: 'components/panel/panelBody.tpl.html',
    transclude: true,
    replace: true,
    require: '^panel'
  }
}

angular
  .module('app.components')
  .directive('chPanel', panel)
  .directive('chPanelGroup', panelGroup)
  .directive('chPanelHeading', panelHeading)
  .directive('chPanelBody', panelBody)
