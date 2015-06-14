angular.module("app").run(["$templateCache", function($templateCache) {$templateCache.put("app/containerManagement/overview.tpl.html","<div class=\"management-overview\"><ch-panel-group><ch-panel ng-repeat=\"server in containerManagement.servers | limitTo:containerManagement.pagination.limit:containerManagement.pagination.from\"><ch-panel-heading><a href=\"\" ui-sref=\"servers.show({ id: server.id })\" ng-bind=\"::server.name\"></a> <a href=\"\" class=\"pull-right\" ng-click=\"containerManagement.getContainers(server)\"><span class=\"mdi mdi-refresh\"></span></a> <a href=\"\" class=\"pull-right\" ng-click=\"containerManagement.createContainer(server)\"><span class=\"mdi mdi-plus\"></span></a></ch-panel-heading><ch-panel-body ng-if=\"!server.containers.length || server.containers.$error\"><p class=\"no-margin\" ng-show=\"!server.containers.length && !server.containers.$error\">No containers available, <a href=\"\" ng-click=\"createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"server.containers.$error\"><span ng-bind=\"server.containers.$error\"></span>, <a href=\"\" ng-click=\"getContainers(server)\">try again</a>.</p></ch-panel-body><container-list server=\"server\"></container-list></ch-panel></ch-panel-group><ch-pagination total-items=\"{{ containerManagement.servers.length }}\" items-per-page=\"{{ containerManagement.pagination.limit }}\" on-page-change=\"containerManagements.changePage(pageNumber)\"></ch-pagination></div>");
$templateCache.put("app/dashboard/dashboard.tpl.html","<h1 class=\"page-title\">Dashboard</h1>");
$templateCache.put("app/content/content.tpl.html","<div ui-view=\"header\"></div><div class=\"container\"><div ui-view=\"content\"></div></div>");
$templateCache.put("app/imageManagement/overview.tpl.html","<div class=\"management-overview\"><ch-panel-group><ch-panel ng-repeat=\"server in vm.servers\"><ch-panel-heading><a href=\"\" ui-sref=\"servers.show({ id: server.id })\" ng-bind=\"::server.name\"></a> <a href=\"\" class=\"pull-right\" ng-click=\"vm.getImages(server)\"><span class=\"mdi mdi-refresh\"></span></a> <a href=\"\" class=\"pull-right\" ng-click=\"vm.createImage(server)\"><span class=\"mdi mdi-plus\"></span></a></ch-panel-heading><ch-panel-body ng-if=\"!server.images.length || server.images.$error\"><p class=\"no-margin\" ng-show=\"!server.images.length && !server.images.$error\">No images available, <a href=\"\" ng-click=\"createImage(server)\">create or add a new image</a>.</p><p class=\"no-margin\" ng-show=\"server.images.$error\"><span ng-bind=\"server.images.$error\"></span>, <a href=\"\" ng-click=\"getImages(server)\">try again</a>.</p></ch-panel-body><image-list server=\"server\"></image-list></ch-panel></ch-panel-group></div>");
$templateCache.put("app/serverManagement/overview.tpl.html","<a href=\"\" class=\"btn btn-sm btn-primary\" style=\"margin-bottom: 20px;\" ng-click=\"serverManagement.createServer()\"><span class=\"mdi mdi-plus\"></span> Create server</a> <a href=\"\" class=\"btn btn-sm btn-primary\" style=\"margin-bottom: 20px;\" ng-click=\"serverManagement.getServers()\"><span class=\"mdi mdi-refresh\"></span> Refresh servers</a><div class=\"table-responsive\"><table class=\"table table-hover servers-table\"><tr class=\"header-row\"><th>Name</th><th>Status</th><th>Operating System</th><th>IP address</th><th>Region</th></tr><tr ng-class=\"{ disabled: server.locked }\" ng-repeat=\"server in serverManagement.servers | limitTo:serverManagement.pagination.limit:serverManagement.pagination.from\" class=\"servers-table-item\" ui-sref=\"{{ server.locked ? \'.\' : \'servers.show({ id: server.id })\' }}\"><td><span ng-if=\"server.locked\" class=\"mdi mdi-lock\"></span> <span ng-bind=\"::server.name\"></span></td><td>{{ server.status | capitalize }}</td><td><span ng-bind=\"::server.image.distribution\"></span> <span ng-bind=\"::server.image.name\"></span></td><td ng-bind=\"::server.getIp()\"></td><td ng-bind=\"::server.region.name\"></td></tr></table><ch-spinner ng-show=\"serverManagement.busy\"></ch-spinner></div><div class=\"row\"><div class=\"col-xs-8\"><ch-pagination total-items=\"{{ serverManagement.servers.length }}\" items-per-page=\"{{ serverManagement.pagination.limit }}\" on-page-change=\"serverManagement.changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4\"><select class=\"form-control pull-right\" ng-options=\"range for range in serverManagement.pagination.ranges\" ng-model=\"serverManagement.pagination.limit\"></select></div></div>");
$templateCache.put("app/user/user.tpl.html","<div class=\"row\"><div class=\"col-sm-3\"><ul class=\"nav nav-pills nav-stacked\"><li ng-class=\"{ active: $state.includes(\'user.profile\') }\"><a href=\"\" ui-sref=\"user.profile\">Profile settings</a></li><li ng-class=\"{ active: $state.includes(\'user.apikeys\') }\"><a href=\"\" ui-sref=\"user.apikeys\">API keys</a></li></ul></div><div ui-view=\"content\" class=\"col-sm-9\"></div></div>");
$templateCache.put("components/dialog/dialog.tpl.html","<div class=\"modal-body\"><p ng-bind=\"message\"></p></div><div class=\"modal-footer\"><button type=\"button\" class=\"btn btn-link\" ng-click=\"$dismiss()\">{{ dismiss || \'Cancel\' }}</button> <button type=\"submit\" class=\"btn btn-primary\" ng-click=\"$close()\">{{ action }}</button></div>");
$templateCache.put("components/imageSearch/imageSearch.tpl.html","<div class=\"docker-image-search\"><form name=\"imageSearch\" novalidate=\"\"><div class=\"form-group\"><label for=\"search\">Image name</label> <input ng-model=\"search.input\" ng-model-options=\"{ debounce: 500 }\" type=\"text\" class=\"form-control\" id=\"search\" name=\"searchInput\" placeholder=\"Search by image name\" required=\"\"></div></form><div class=\"search-results\"><div class=\"search-results-box\"><ch-spinner ng-show=\"busy\"></ch-spinner><div ng-show=\"search.error && !results && !busy\"><p>An error has occurred. <a ng-click=\"search(search.error.query, search.error.params)\" href=\"\">Try again?</a></p></div><div ng-hide=\"busy\" class=\"list-group search-results-box\"><a href=\"\" class=\"list-group-item\" ng-click=\"selectImage(result)\" ng-class=\"{ \'active\': result === selected }\" ng-repeat=\"result in results.results\"><span ng-bind=\"result.name\"></span><div ng-if=\"result.star_count > 0\" class=\"pull-right\"><span class=\"mdi mdi-star\"></span> <span ng-bind=\"result.star_count\"></span></div></a></div></div><ch-pagination ng-if=\"results.num_pages > 1\" total-items=\"{{ results.num_results }}\" items-per-page=\"10\" pagination-range=\"9\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div></div>");
$templateCache.put("components/navMenu/navMenu.tpl.html","<nav class=\"navmenu navmenu-fixed-left offcanvas-sm\" role=\"navigation\"><a href=\"\" ui-sref=\"dashboard\" class=\"navmenu-brand\"><img src=\"/images/logo-small.png\" alt=\"Cloudharbor logo\"></a><ul class=\"nav navmenu-nav\"><li ng-class=\"{ active: $state.includes(\'dashboard\') }\"><a href=\"\" ui-sref=\"dashboard\"><span class=\"mdi mdi-speedometer\"></span> Dashboard</a></li><li ng-class=\"{ active: $state.includes(\'servers\') }\"><a href=\"\" ui-sref=\"servers.overview\"><span class=\"mdi mdi-dns\"></span> Servers</a></li><li ng-class=\"{ active: $state.includes(\'images\') }\"><a href=\"\" ui-sref=\"images.overview\"><span class=\"mdi mdi-cube-outline\"></span> Images</a></li><li ng-class=\"{ active: $state.includes(\'containers\') }\"><a href=\"\" ui-sref=\"containers.overview\"><span class=\"mdi mdi-cube-outline\"></span> Containers</a></li><li ng-class=\"{ active: $state.includes(\'statistics\') }\"><a href=\"\" ui-sref=\"statistics\"><span class=\"mdi mdi-information\"></span> Statistics</a></li><li ng-class=\"{ active: $state.includes(\'settings\') }\"><a href=\"\" ui-sref=\"settings\"><span class=\"mdi mdi-settings\"></span> Settings</a></li><li ng-class=\"{ active: $state.includes(\'help\') }\"><a href=\"\" ui-sref=\"help\"><span class=\"mdi mdi-help-circle\"></span> Help</a></li></ul></nav>");
$templateCache.put("components/pagination/pagination.tpl.html","<nav><ul class=\"pagination\"><li><a ng-click=\"goToPage(1)\" href=\"\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li><li ng-repeat=\"page in pages\" ng-class=\"{active: page === currentPage}\"><a ng-bind=\"page\" ng-click=\"goToPage(page)\" href=\"\"></a></li><li><a ng-click=\"goToPage(numPages)\" href=\"\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li></ul></nav>");
$templateCache.put("components/panel/panel.tpl.html","<div class=\"panel panel-default\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelBody.tpl.html","<div class=\"panel-body\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelGroup.tpl.html","<div class=\"panel-group\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelHeading.tpl.html","<div class=\"panel-heading\"><h4 class=\"panel-title\" ng-transclude=\"\"></h4></div>");
$templateCache.put("components/progressButton/progressButton.tpl.html","<a href=\"\" class=\"btn btn-{{ type }} btn-{{ size }}\"><span class=\"mdi {{ icon }}\"></span> <span ng-transclude=\"\"></span></a>");
$templateCache.put("components/spinner/spinner.tpl.html","<div class=\"spinner\"><div class=\"spinner-container container1\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container2\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container3\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div></div>");
$templateCache.put("app/containerManagement/create/containerCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create container</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\"><div class=\"btn-group\"><label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'Docker Hub image\'\">Docker Hub image</label> <label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'DockerFile\'\">DockerFile</label></div></div><ch-image-search ng-model=\"vm.container.image\"></ch-image-search><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.containerName.$invalid && !vm.form.containerName.$pristine }\"><label for=\"containerName\">Container name</label> <input type=\"text\" class=\"form-control\" name=\"containerName\" id=\"containerName\" placeholder=\"Enter container name\" ng-model=\"vm.container.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\" required=\"\"><div ng-show=\"!vm.form.containerName.$pristine && vm.form.containerName.$error\"><p ng-show=\"vm.form.containerName.$error.pattern\">Your container name must only contain alphanumeric characters, dashes, and periods.</p></div></div><div class=\"form-group\"><label for=\"cpuShares\">CPU shares</label> <input type=\"number\" class=\"form-control\" name=\"cpuShares\" id=\"cpuShares\" placeholder=\"Enter amount of CPU shares\" ng-model=\"vm.container.cpuShares\" required=\"\"></div><div class=\"form-group\"><label for=\"memoryLimit\">Memory limit</label> <input type=\"number\" class=\"form-control\" name=\"memoryLimit\" id=\"memoryLimit\" placeholder=\"Enter memory limit\" ng-model=\"vm.container.memory\" required=\"\"></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"autorestartEnabled\"><input type=\"checkbox\" name=\"autorestartEnabled\" id=\"autorestartEnabled\" ng-model=\"vm.container.autorestart\"> Enable autorestart</label></div></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"ipv6Enabled\"><input type=\"checkbox\" name=\"autodestroyEnabled\" id=\"ipv6Enabled\" ng-model=\"vm.form.autodestroy\"> Enable autodestroy</label></div></div></form></div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\" ng-click=\"vm.submit(vm.form)\">Create container</button> <button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/containerManagement/list/containerList.tpl.html","<div class=\"list-group\"><a ng-repeat=\"container in containers\" href=\"\" class=\"list-group-item\"><span ng-bind=\"::container.Id\" class=\"item-name\"></span> <span class=\"item-status\">Running</span> <span class=\"item-created\"><i class=\"mdi mdi-clock\"></i> 5 hours ago</span></a></div>");
$templateCache.put("app/content/header/header.tpl.html","<div class=\"content-header\"><div class=\"container\"><h4 class=\"pull-left\" ng-bind=\"title\"></h4></div></div>");
$templateCache.put("app/imageManagement/create/imageCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create or add image</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\"><div class=\"btn-group\"><label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'Docker Hub image\'\">Docker Hub image</label> <label class=\"btn btn-primary\" ng-model=\"radioModel\" btn-radio=\"\'DockerFile\'\">DockerFile</label></div></div><ch-image-search ng-model=\"vm.image.image\"></ch-image-search></form></div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\" ng-click=\"vm.submit(vm.form)\">Create image</button> <button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/imageManagement/list/imageList.tpl.html","<div class=\"list-group\"><a ng-repeat=\"image in server.images\" href=\"\" class=\"list-group-item\"><span ng-bind=\"::image.RepoTags[0]\"></span></a></div>");
$templateCache.put("app/serverManagement/create/serverCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create a new server</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.serverName.$invalid && !vm.form.serverName.$pristine }\"><label for=\"serverName\">Server name</label> <input type=\"text\" class=\"form-control\" name=\"serverName\" id=\"serverName\" placeholder=\"Enter server name\" ng-model=\"vm.server.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\" required=\"\"><div ng-show=\"!vm.form.serverName.$pristine && vm.form.serverName.$error\"><p ng-show=\"vm.form.serverName.$error.pattern\">Your server name must only contain alphanumeric characters, dashes, and periods.</p></div></div><div class=\"form-group\"><label for=\"serverRegion\">Server region</label><select name=\"serverRegion\" class=\"form-control\" id=\"serverRegion\" ng-model=\"vm.server.region\" ng-disabled=\"!vm.serverOptions || !vm.serverOptions.regions.length\" ng-options=\"value.name group by value.group disable when !value.available for (label, value) in vm.serverOptions.regions\" required=\"\"></select></div><div class=\"form-group\"><label for=\"serverSize\">Server size</label><select name=\"serverSize\" class=\"form-control\" id=\"serverSize\" ng-model=\"vm.server.size\" ng-disabled=\"!vm.serverOptions || !vm.server.region || !vm.server.region.sizes.length\" ng-options=\"option for option in vm.server.region.sizes | sortBySize:option\" required=\"\"></select></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.discoveryToken.$invalid && !vm.form.discoveryToken.$pristine }\"><label for=\"discoveryToken\">Discovery token</label><div class=\"input-group\"><input type=\"url\" class=\"form-control\" name=\"discoveryToken\" id=\"discoveryToken\" placeholder=\"Enter a discovery token\" ng-model=\"vm.server.discovery_token\"> <span class=\"input-group-btn\"><button class=\"btn btn-default\" type=\"button\" ng-click=\"vm.generateToken()\"><span class=\"mdi mdi-refresh\"></span></button></span></div><p class=\"help-block\">Use an existing token to .... A new token can be generated on <a href=\"https://discovery.etcd.io/new\" target=\"_blank\">https://discovery.etcd.io/new</a>. Leaving this field empty will result in a newly generated token.</p></div><div class=\"form-group\"><label for=\"sshKeyId\">SSH key</label><select class=\"form-control\" name=\"sshKeyId\" id=\"sshKeyId\" ng-model=\"vm.server.ssh_keys\" ng-options=\"item.name for item in vm.sshKeys\" required=\"\"></select><p class=\"help-block\">Select a SSH key to ...</p></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"backupsEnabled\"><input type=\"checkbox\" name=\"backupsEnabled\" id=\"backupsEnabled\" ng-model=\"vm.server.backups\"> Enable backups</label></div></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"ipv6Enabled\"><input type=\"checkbox\" name=\"ipv6Enabled\" id=\"ipv6Enabled\" ng-model=\"vm.server.ipv6\"> Enable IPv6</label></div></div></form></div><div class=\"modal-footer\"><ch-progress-button action=\"vm.submit(vm.form)\" ng-disabled=\"vm.form.$invalid\">Create server</ch-progress-button><button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/serverManagement/show/serverShow.tpl.html","<div class=\"alert alert-warning\" role=\"alert\" ng-show=\"vm.server.locked\">This server is currently being provisioned, and thus locked from any interaction. Please wait a moment.</div><div class=\"row\"><div class=\"col-sm-4 col-md-3\"><div class=\"server-actions\" style=\"margin-bottom: 20px;\"><ch-progress-button ng-if=\"vm.server.status === \'off\'\" action=\"vm.startServer()\" size=\"sm\" icon=\"mdi-play\">Start server</ch-progress-button><ch-progress-button ng-if=\"vm.server.status !== \'off\'\" action=\"vm.stopServer()\" size=\"sm\" icon=\"mdi-stop\">Stop server</ch-progress-button><ch-progress-button action=\"\" size=\"sm\" icon=\"mdi-refresh\">Reboot server</ch-progress-button><ch-progress-button action=\"vm.deleteServer()\" size=\"sm\" type=\"danger\" icon=\"mdi-delete\">Delete server</ch-progress-button></div><div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\">Server details</h4></div><ul class=\"list-group\"><li class=\"list-group-item\"><span class=\"mdi mdi-cloud\"></span> Digital Ocean</li><li class=\"list-group-item\"><span class=\"mdi mdi-play-circle\"></span> Server <span ng-bind=\"vm.server.status\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-clock\"></span> Created 5 hours ago</li><li class=\"list-group-item\"><span class=\"mdi mdi-layers\"></span> <span ng-bind=\"::vm.server.image.distribution\"></span> <span ng-bind=\"::vm.server.image.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-map-marker\"></span> <span ng-bind=\"::vm.server.region.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-server-network\"></span> <span ng-bind=\"::vm.server.getIp()\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-cube-outline\"></span> <span ng-bind=\"vm.server.containers.length || 0\"></span> containers</li></ul></div></div><div class=\"col-sm-8 col-md-9\"><tabset><tab heading=\"Containers\"><div class=\"\" style=\"margin: 20px 0;\"><a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-plus\"></span> Create new container</a> <a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-refresh\"></span> Refresh containers</a></div><div ng-if=\"!vm.server.containers.length || vm.server.containers.$error\"><p class=\"no-margin\" ng-show=\"!vm.server.containers.length && !vm.server.containers.$error\">No containers available, <a href=\"\" ng-click=\"vm.createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"vm.server.containers.$error\"><span ng-bind=\"vm.server.containers.$error\"></span>, <a href=\"\" ng-click=\"vm.getContainers(server)\">try again</a>.</p></div><container-list server=\"vm.server\"></container-list></tab><tab heading=\"Images\"><div class=\"\" style=\"margin: 20px 0;\"><a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-plus\"></span> Create or add new image</a> <a href=\"\" class=\"btn btn-sm btn-primary\"><span class=\"mdi mdi-refresh\"></span> Refresh images</a></div><div ng-if=\"!vm.server.images.length || vm.server.images.$error\"><p class=\"no-margin\" ng-show=\"!vm.server.images.length && !vm.server.images.$error\">No images available, <a href=\"\" ng-click=\"vm.createImage(server)\">create or add a new image</a>.</p><p class=\"no-margin\" ng-show=\"vm.server.images.$error\"><span ng-bind=\"vm.server.images.$error\"></span>, <a href=\"\" ng-click=\"vm.getImages(server)\">try again</a>.</p></div><image-list server=\"vm.server\"></image-list></tab></tabset></div></div>");
$templateCache.put("app/user/apikeys/apikeys.tpl.html","<div class=\"form-group\"><label for=\"digitalOcean\">Digital Ocean API key</label> <input type=\"text\" class=\"form-control\" id=\"digitalOcean\" ng-model=\"vm.apiKey\" placeholder=\"API key for Digital Ocean\"></div><ch-progress-button action=\"vm.updateApiKey()\" ng-disabled=\"\">Update API key</ch-progress-button>");
$templateCache.put("app/user/profile/profile.tpl.html","<p>Profile</p>");}]);