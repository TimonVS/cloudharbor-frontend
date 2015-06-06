angular.module("app").run(["$templateCache", function($templateCache) {$templateCache.put("app/containerManagement/overview.tpl.html","<h1 class=\"page-title\">Container overview</h1><div class=\"management-overview panel-group\"><div class=\"panel panel-default\" ng-repeat=\"server in containerManagement.servers\"><div class=\"panel-heading\"><h4 class=\"panel-title\"><span ng-bind=\"::server.name\"></span> <a href=\"\" class=\"pull-right\" ng-click=\"containerManagement.getContainers(server)\"><span class=\"mdi mdi-refresh\"></span></a> <a href=\"\" class=\"pull-right\" ng-click=\"containerManagement.createContainer(server)\"><span class=\"mdi mdi-plus\"></span></a></h4></div><div class=\"containers-list\"><div class=\"panel-body\" ng-show=\"!server.containers.length || server.containers.$error\"><p class=\"no-margin\" ng-show=\"!server.containers.length && !server.containers.$error\">No containers available, <a href=\"\" ng-click=\"containerManagement.createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"server.containers.$error\"><span ng-bind=\"server.containers.$error\"></span>, <a href=\"\" ng-click=\"containerManagement.getContainers(server)\">try again</a>.</p></div><a ng-repeat=\"container in server.containers\" href=\"\" class=\"containers-list-item\"><span ng-bind=\"::container.Id\" class=\"item-name\"></span> <span class=\"item-status\">Running</span> <span class=\"item-created\"><i class=\"mdi mdi-clock\"></i> 5 hours ago</span></a></div></div></div>");
$templateCache.put("app/content/content.tpl.html","<div ui-view=\"\"></div>");
$templateCache.put("app/dashboard/dashboard.tpl.html","<h1 class=\"page-title\">Dashboard</h1>");
$templateCache.put("app/serverManagement/overview.tpl.html","<h1 class=\"page-title\">Server overview</h1><div ng-include=\"\'app/serverManagement/create/serverManagementForm.tpl.html\'\"></div><div class=\"table-responsive\"><table class=\"table table-hover\"><tr><th>ID</th><th>Name</th><th>Locked</th><th>Status</th><th>Image</th><th>Operating System</th><th>IP address</th><th>Region</th></tr><tr ng-repeat=\"server in serverManagement.servers\"><td ng-bind=\"server.id\"></td><td ng-bind=\"server.name\"></td><td ng-bind=\"server.locked\"></td><td ng-bind=\"server.status\"></td><td ng-bind=\"server.image.name\"></td><td ng-bind=\"server.image.distribution\"></td><td ng-bind=\"server.networks.v4[0].ip_address\"></td><td ng-bind=\"server.region.name\"></td><td><a href=\"\" ui-sref=\".show({ id: server.id })\">Go</a></td></tr></table><ch-spinner ng-show=\"serverManagement.busy\"></ch-spinner></div>");
$templateCache.put("app/containerManagement/create/containerCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create container</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\"><div class=\"btn-group\"><label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'Docker Hub image\'\">Docker Hub image</label> <label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'DockerFile\'\">DockerFile</label></div></div><ch-image-search ng-model=\"vm.container.image\"></ch-image-search><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.containerName.$invalid && !vm.form.containerName.$pristine }\"><label for=\"containerName\">Container name</label> <input type=\"text\" class=\"form-control\" name=\"containerName\" id=\"containerName\" placeholder=\"Enter container name\" ng-model=\"vm.container.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\" required=\"\"><div ng-show=\"!vm.form.containerName.$pristine && vm.form.containerName.$error\"><p ng-show=\"vm.form.containerName.$error.pattern\">Your container name must only contain alphanumeric characters, dashes, and periods.</p></div></div><div class=\"form-group\"><label for=\"cpuShares\">CPU shares</label> <input type=\"number\" class=\"form-control\" name=\"cpuShares\" id=\"cpuShares\" placeholder=\"Enter amount of CPU shares\" ng-model=\"vm.container.cpuShares\" required=\"\"></div><div class=\"form-group\"><label for=\"memoryLimit\">Memory limit</label> <input type=\"number\" class=\"form-control\" name=\"memoryLimit\" id=\"memoryLimit\" placeholder=\"Enter memory limit\" ng-model=\"vm.container.memory\" required=\"\"></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"autorestartEnabled\"><input type=\"checkbox\" name=\"autorestartEnabled\" id=\"autorestartEnabled\" ng-model=\"vm.container.autorestart\"> Enable autorestart</label></div></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"ipv6Enabled\"><input type=\"checkbox\" name=\"autodestroyEnabled\" id=\"ipv6Enabled\" ng-model=\"vm.form.autodestroy\"> Enable autodestroy</label></div></div></form></div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\" ng-click=\"vm.submit(vm.form)\">Create container</button> <button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/serverManagement/create/serverManagementForm.tpl.html","<div ng-controller=\"serverManagementFormCtrl as vm\"><div class=\"well\"><form name=\"vm.form\" ng-submit=\"vm.onSubmit(vm.form)\" novalidate=\"\"><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.serverName.$invalid && !vm.form.serverName.$pristine }\"><label for=\"serverName\">Server name</label> <input type=\"text\" class=\"form-control\" name=\"serverName\" id=\"serverName\" placeholder=\"Enter server name\" ng-model=\"vm.server.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\" required=\"\"><div ng-show=\"!vm.form.serverName.$pristine && vm.form.serverName.$error\"><p ng-show=\"vm.form.serverName.$error.pattern\">Your server name must only contain alphanumeric characters, dashes, and periods.</p></div></div><div class=\"form-group\"><label for=\"serverRegion\">Server region</label><select name=\"serverRegion\" class=\"form-control\" id=\"serverRegion\" ng-model=\"vm.server.region\" ng-disabled=\"!vm.serverOptions\" ng-options=\"value.name group by value.group disable when !value.available for (label, value) in vm.serverOptions.regions\" required=\"\"></select></div><div class=\"form-group\"><label for=\"serverSize\">Server size</label><select name=\"serverSize\" class=\"form-control\" id=\"serverSize\" ng-model=\"vm.server.size\" ng-disabled=\"!vm.serverOptions || !vm.server.region || !vm.server.region.sizes.length\" ng-options=\"option for option in vm.server.region.sizes\" required=\"\"></select></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.discoveryToken.$invalid && !vm.form.discoveryToken.$pristine }\"><label for=\"discoveryToken\">Discovery token</label><div class=\"input-group\"><input type=\"url\" class=\"form-control\" name=\"discoveryToken\" id=\"discoveryToken\" placeholder=\"Enter a discovery token\" ng-model=\"vm.server.discovery_token\"> <span class=\"input-group-btn\"><button class=\"btn btn-default\" type=\"button\" ng-click=\"vm.generateToken()\"><span class=\"mdi mdi-refresh\"></span></button></span></div><p class=\"help-block\">Use an existing token to .... A new token can be generated on <a href=\"https://discovery.etcd.io/new\" target=\"_blank\">https://discovery.etcd.io/new</a>. Leaving this field empty will result in a newly generated token.</p></div><div class=\"form-group\"><label for=\"sshKeyId\">SSH key</label><select class=\"form-control\" name=\"sshKeyId\" id=\"sshKeyId\" ng-model=\"vm.server.ssh_keys\" ng-options=\"item.name for item in vm.sshKeys\" required=\"\"></select><p class=\"help-block\">Select a SSH key to ...</p></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"backupsEnabled\"><input type=\"checkbox\" name=\"backupsEnabled\" id=\"backupsEnabled\" ng-model=\"vm.server.backups\"> Enable backups</label></div></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"ipv6Enabled\"><input type=\"checkbox\" name=\"ipv6Enabled\" id=\"ipv6Enabled\" ng-model=\"vm.server.ipv6\"> Enable IPv6</label></div></div><button type=\"submit\" class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\">Create server</button></form></div></div>");
$templateCache.put("app/serverManagement/show/serverShow.tpl.html","<h1 class=\"page-title\" ng-bind=\"::vm.server.name\"></h1><div class=\"alert alert-warning\" role=\"alert\" ng-show=\"vm.server.locked\">This server is currently being provisioned, and thus locked from any interaction. Please wait a moment.</div><div class=\"row\"><div class=\"col-sm-4\"><div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\">Server details</h4></div><ul class=\"list-group\"><li class=\"list-group-item\"><span class=\"mdi mdi-cloud\"></span> Digital Ocean</li><li class=\"list-group-item\"><span class=\"mdi mdi-play-circle\"></span> Server active</li><li class=\"list-group-item\"><span class=\"mdi mdi-clock\"></span> Created 5 hours ago</li><li class=\"list-group-item\"><span class=\"mdi mdi-layers\"></span> <span ng-bind=\"::vm.server.image.distribution\"></span> <span ng-bind=\"::vm.server.image.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-map-marker\"></span> <span ng-bind=\"::vm.server.region.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-server-network\"></span> <span ng-bind=\"::vm.server.getIp()\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-cube-outline\"></span> <span ng-bind=\"vm.server.containers.length\"></span> containers</li></ul></div></div><div class=\"col-sm-8\"><div class=\"\" style=\"margin-bottom: 20px;\"><a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-refresh\"></span> Refresh containers</a> <a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-plus\"></span> Create new container</a></div><div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\">Containers</h4></div><div class=\"containers-list\"><div class=\"panel-body\" ng-show=\"!vm.server.containers.length || vm.server.containers.$error\"><p class=\"no-margin\" ng-show=\"!vm.server.containers.length && !vm.server.containers.$error\">No containers available, <a href=\"\" ng-click=\"vm.createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"vm.server.containers.$error\"><span ng-bind=\"vm.server.containers.$error\"></span>, <a href=\"\" ng-click=\"vm.getContainers(server)\">try again</a>.</p></div><a ng-repeat=\"container in vm.server.containers\" href=\"\" class=\"containers-list-item\"><span ng-bind=\"::container.Id\" class=\"item-name\"></span> <span class=\"item-status\">Running</span> <span class=\"item-created\"><i class=\"mdi mdi-clock\"></i> 5 hours ago</span></a></div></div></div></div>");
$templateCache.put("components/directives/navMenu/navMenu.tpl.html","<nav class=\"navmenu navmenu-fixed-left offcanvas-sm\" role=\"navigation\"><a href=\"\" ui-sref=\"dashboard\" class=\"navmenu-brand\"><img src=\"/images/logo-small.png\" alt=\"Cloudharbor logo\"></a><ul class=\"nav navmenu-nav\"><li ng-class=\"{ active: $state.includes(\'dashboard\') }\"><a href=\"\" ui-sref=\"dashboard\"><span class=\"mdi mdi-speedometer\"></span> Dashboard</a></li><li ng-class=\"{ active: $state.includes(\'servers\') }\"><a href=\"\" ui-sref=\"servers\"><span class=\"mdi mdi-dns\"></span> Servers</a></li><li ng-class=\"{ active: $state.includes(\'containers\') }\"><a href=\"\" ui-sref=\"containers\"><span class=\"mdi mdi-cube-outline\"></span> Containers</a></li><li ng-class=\"{ active: $state.includes(\'statistics\') }\"><a href=\"\" ui-sref=\"statistics\"><span class=\"mdi mdi-information\"></span> Statistics</a></li><li ng-class=\"{ active: $state.includes(\'settings\') }\"><a href=\"\" ui-sref=\"settings\"><span class=\"mdi mdi-settings\"></span> Settings</a></li><li ng-class=\"{ active: $state.includes(\'help\') }\"><a href=\"\" ui-sref=\"help\"><span class=\"mdi mdi-help-circle\"></span> Help</a></li></ul></nav>");
$templateCache.put("components/directives/imageSearch/imageSearch.tpl.html","<div class=\"docker-image-search\"><form name=\"imageSearch\" novalidate=\"\"><div class=\"form-group\"><label for=\"search\">Image name</label> <input ng-model=\"search.input\" ng-model-options=\"{debounce: 500}\" type=\"text\" class=\"form-control\" id=\"search\" name=\"searchInput\" placeholder=\"Search by image name\" required=\"\"></div></form><div class=\"search-results\"><div class=\"search-results-box\"><ch-spinner ng-show=\"busy\"></ch-spinner><div ng-show=\"search.error && !results && !busy\"><p>An error has occurred. <a ng-click=\"search(search.error.query, search.error.params)\" href=\"\">Try again?</a></p></div><div ng-hide=\"busy\" class=\"list-group search-results-box\"><a href=\"\" class=\"list-group-item\" ng-click=\"selectImage(result)\" ng-repeat=\"result in results.results\"><span ng-bind=\"result.name\"></span><div ng-if=\"result.star_count > 0\" class=\"pull-right\"><span class=\"mdi mdi-star\"></span> <span ng-bind=\"result.star_count\"></span></div></a></div></div><ch-pagination ng-if=\"results.num_pages > 1\" total-items=\"{{ results.num_results }}\" items-per-page=\"25\" pagination-range=\"9\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div></div>");
$templateCache.put("components/directives/pagination/pagination.tpl.html","<nav><ul class=\"pagination\"><li><a ng-click=\"goToPage(1)\" href=\"\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li><li ng-repeat=\"page in pages\" ng-class=\"{active: page === currentPage}\"><a ng-bind=\"page\" ng-click=\"goToPage(page)\" href=\"\"></a></li><li><a ng-click=\"goToPage(numPages)\" href=\"\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li></ul></nav>");
$templateCache.put("components/directives/spinner/spinner.tpl.html","<div class=\"spinner\"><div class=\"spinner-container container1\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container2\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container3\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div></div>");}]);