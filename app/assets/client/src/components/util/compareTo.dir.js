function compareTo () {
  return {
    require: 'ngModel',
    scope: {
      value: '=compareTo'
    },
    link: function(scope, element, attributes, ngModel) {

      ngModel.$validators.compareTo = function(modelValue) {
        return modelValue == scope.value
      }

      scope.$watch('value', function() {
        ngModel.$validate()
      })
    }
  }
}

angular
  .module('app.components')
  .directive('compareTo', compareTo);
