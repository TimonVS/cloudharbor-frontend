angular.module("app").run(["$templateCache", function($templateCache) {$templateCache.put("app/containerManagement/overview.tpl.html","<div class=\"management-overview\"><ch-panel-group><container-list server=\"server\" panel=\"true\" ng-repeat=\"server in containerManagement.servers | limitTo:containerManagement.pagination.limit:containerManagement.pagination.from\"></container-list></ch-panel-group><ch-pagination total-items=\"{{ containerManagement.servers.length }}\" items-per-page=\"{{ containerManagement.pagination.limit }}\" on-page-change=\"containerManagements.changePage(pageNumber)\"></ch-pagination></div>");
$templateCache.put("app/content/content.tpl.html","<div ui-view=\"header\"></div><div class=\"container\"><div ui-view=\"content\"></div></div>");
$templateCache.put("app/dashboard/dashboard.tpl.html","");
$templateCache.put("app/imageManagement/overview.tpl.html","<div class=\"management-overview\"><ch-panel-group><image-list server=\"server\" panel=\"true\" ng-repeat=\"server in vm.servers\"></image-list></ch-panel-group></div>");
$templateCache.put("app/notification/notificationList.tpl.html","<li class=\"navbar-icon navbar-notifications dropdown\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-expanded=\"false\"><span class=\"mdi mdi-bell-outline\"></span> <span class=\"notification-ball\"></span></a><ul class=\"dropdown-menu notifications-list\" role=\"menu\"><li class=\"notification-list-item\" ng-repeat=\"notification in notifications track by $index\"><a href=\"#\"><span class=\"notification-description\" ng-bind=\"notification.message.body\"></span></a></li></ul></li>");
$templateCache.put("app/serverManagement/overview.tpl.html","<div class=\"row\"><div class=\"col-sm-8\"><div class=\"server-overview-actions\"><a href=\"\" class=\"btn btn-md btn-primary\" ng-click=\"serverManagement.createServer()\"><span class=\"mdi mdi-plus\"></span> Create server</a> <a href=\"\" class=\"btn btn-md btn-primary\" ng-click=\"serverManagement.getServers()\"><span class=\"mdi mdi-refresh\"></span> Refresh servers</a></div></div><div class=\"col-sm-4\"><div class=\"server-overview-search input-group\"><div class=\"input-group-addon\"><span class=\"mdi mdi-magnify\"></span></div><input type=\"text\" class=\"form-control\" placeholder=\"Search servers\" ng-model=\"serverManagement.searchQuery\"></div></div></div><div class=\"table-responsive\"><table class=\"table table-hover servers-table\"><tr class=\"header-row\"><th ng-click=\"serverManagement.orderBy(\'name\')\">Name <span ng-show=\"serverManagement.sortType == \'name\'\" class=\"mdi\" ng-class=\"serverManagement.sortReverse ? \'mdi-chevron-up\' : \'mdi-chevron-down\'\"></span></th><th ng-click=\"serverManagement.orderBy(\'status\')\">Status <span ng-show=\"serverManagement.sortType == \'status\'\" class=\"mdi\" ng-class=\"serverManagement.sortReverse ? \'mdi-chevron-up\' : \'mdi-chevron-down\'\"></span></th><th>Operating System</th><th>IP address</th><th ng-click=\"serverManagement.orderBy(\'region\')\">Region <span ng-show=\"serverManagement.sortType == \'region\'\" class=\"mdi\" ng-class=\"serverManagement.sortReverse ? \'mdi-chevron-up\' : \'mdi-chevron-down\'\"></span></th></tr><tr ng-class=\"{ disabled: server.locked }\" ng-repeat=\"server in serverManagement.servers | limitTo:serverManagement.pagination.limit:serverManagement.pagination.from | orderBy:serverManagement.sortType:serverManagement.sortReverse | filter:serverManagement.searchQuery\" class=\"servers-table-item\" ng-click=\"serverManagement.goTo(server)\"><td><span ng-if=\"server.locked\" class=\"mdi mdi-lock\"></span> <span ng-bind=\"::server.name\"></span></td><td>{{ server.status | capitalize }}</td><td><span ng-bind=\"::server.image.distribution\"></span> <span ng-bind=\"::server.image.name\"></span></td><td ng-bind=\"::server.getIp()\"></td><td ng-bind=\"::server.region.name\"></td></tr></table><ch-spinner ng-show=\"serverManagement.busy\"></ch-spinner></div><div class=\"row\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ serverManagement.servers.length }}\" items-per-page=\"{{ serverManagement.pagination.limit }}\" on-page-change=\"serverManagement.changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"server-overview-pager form-control pull-right\" ng-options=\"range for range in serverManagement.pagination.ranges\" ng-model=\"serverManagement.pagination.limit\"></select></div></div>");
$templateCache.put("app/user/user.tpl.html","<div class=\"row\"><div class=\"col-sm-3\"><ul class=\"nav nav-pills nav-stacked\"><li ng-class=\"{ active: $state.includes(\'user.profile\') }\"><a href=\"\" ui-sref=\"user.profile\"><span class=\"mdi mdi-account-circle\"></span> Profile settings</a></li><li ng-class=\"{ active: $state.includes(\'user.apikeys\') }\"><a href=\"\" ui-sref=\"user.apikeys\"><span class=\"mdi mdi-key\"></span> API keys</a></li></ul></div><div ui-view=\"content\" class=\"col-sm-9\"></div></div>");
$templateCache.put("components/dialog/dialog.tpl.html","<div class=\"modal-body\"><p ng-bind=\"message\"></p></div><div class=\"modal-footer\"><button type=\"button\" class=\"btn btn-link\" ng-click=\"$dismiss()\">{{ dismiss || \'Cancel\' }}</button> <button type=\"submit\" class=\"btn btn-primary\" ng-click=\"$close()\">{{ action }}</button></div>");
$templateCache.put("components/imageSearch/imageSearch.tpl.html","<div class=\"docker-image-search\"><form name=\"imageSearch\" novalidate=\"\"><div class=\"form-group\"><label for=\"search\">Image name</label> <input ng-model=\"search.input\" ng-model-options=\"{ debounce: 500 }\" type=\"text\" class=\"form-control\" id=\"search\" name=\"searchInput\" placeholder=\"Search by image name\"></div></form><div class=\"search-results\"><div class=\"search-results-box\"><ch-spinner ng-show=\"busy\"></ch-spinner><div ng-show=\"search.error && !results && !busy\"><p>An error has occurred. <a ng-click=\"search(search.error.query, search.error.params)\" href=\"\">Try again?</a></p></div><div ng-hide=\"busy\" class=\"list-group search-results-box\"><a href=\"\" class=\"list-group-item\" ng-click=\"selectImage(result)\" ng-class=\"{ \'active\': result === selected }\" ng-repeat=\"result in results.results\"><span ng-bind=\"result.name\"></span><div ng-if=\"result.star_count > 0\" class=\"pull-right\"><span class=\"mdi mdi-star\"></span> <span ng-bind=\"result.star_count\"></span></div></a></div></div><ch-pagination ng-if=\"results.num_pages > 1\" total-items=\"{{ results.num_results }}\" items-per-page=\"10\" pagination-range=\"9\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div></div>");
$templateCache.put("components/navMenu/navMenu.tpl.html","<nav class=\"navmenu navmenu-fixed-left offcanvas-sm\" role=\"navigation\"><a href=\"\" ui-sref=\"dashboard\" class=\"navmenu-brand\"><img src=\"/images/logo-small.png\" alt=\"Cloudharbor logo\"></a><ul class=\"nav navmenu-nav\"><li ng-class=\"{ active: $state.includes(\'dashboard\') }\"><a href=\"\" ui-sref=\"dashboard\"><span class=\"mdi mdi-speedometer\"></span> Dashboard</a></li><li ng-class=\"{ active: $state.includes(\'servers\') }\"><a href=\"\" ui-sref=\"servers.overview\"><span class=\"mdi mdi-dns\"></span> Servers</a></li><li ng-class=\"{ active: $state.includes(\'images\') }\"><a href=\"\" ui-sref=\"images.overview\"><span class=\"mdi mdi-layers\"></span> Images</a></li><li ng-class=\"{ active: $state.includes(\'containers\') }\"><a href=\"\" ui-sref=\"containers.overview\"><span class=\"mdi mdi-cube-outline\"></span> Containers</a></li><li ng-class=\"{ active: $state.includes(\'statistics\') }\"><a href=\"\" ui-sref=\"statistics\"><span class=\"mdi mdi-information\"></span> Statistics</a></li><li ng-class=\"{ active: $state.includes(\'settings\') }\"><a href=\"\" ui-sref=\"settings\"><span class=\"mdi mdi-settings\"></span> Settings</a></li><li ng-class=\"{ active: $state.includes(\'help\') }\"><a href=\"\" ui-sref=\"help\"><span class=\"mdi mdi-help-circle\"></span> Help</a></li></ul></nav>");
$templateCache.put("components/pagination/pagination.tpl.html","<nav><ul class=\"pagination\"><li><a ng-click=\"goToPage(1)\" href=\"\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li><li ng-repeat=\"page in pages\" ng-class=\"{active: page === currentPage}\"><a ng-bind=\"page\" ng-click=\"goToPage(page)\" href=\"\"></a></li><li><a ng-click=\"goToPage(numPages)\" href=\"\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li></ul></nav>");
$templateCache.put("components/panel/panel.tpl.html","<div class=\"panel panel-default\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelBody.tpl.html","<div class=\"panel-body\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelGroup.tpl.html","<div class=\"panel-group\" ng-transclude=\"\"></div>");
$templateCache.put("components/panel/panelHeading.tpl.html","<div class=\"panel-heading\" ng-transclude=\"\"></div>");
$templateCache.put("components/progressButton/progressButton.tpl.html","<a href=\"\" class=\"btn btn-{{ type }} btn-{{ size }}\"><span class=\"mdi {{ icon }}\"></span> <span ng-transclude=\"\"></span></a>");
$templateCache.put("components/spinner/spinner.tpl.html","<div class=\"spinner\"><div class=\"spinner-container container1\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container2\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div><div class=\"spinner-container container3\"><div class=\"circle1\"></div><div class=\"circle2\"></div><div class=\"circle3\"></div><div class=\"circle4\"></div></div></div>");
$templateCache.put("app/containerManagement/create/containerCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create container</h3></div><div class=\"modal-body\"><alert ng-if=\"vm.error\" type=\"danger\">{{ vm.error }}</alert><form name=\"vm.form\" novalidate=\"\"><tabset><tab heading=\"Existing images\"><image-select ng-model=\"vm.container.image\" server=\"vm.server\"></image-select></tab><tab heading=\"Docker Hub\"><ch-image-search ng-model=\"vm.container.dockerhubimage\"></ch-image-search></tab><tab heading=\"DockerFile\"></tab></tabset><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.containerName.$invalid && !vm.form.containerName.$pristine }\"><label for=\"containerName\">Container name</label> <input type=\"text\" class=\"form-control\" name=\"containerName\" id=\"containerName\" placeholder=\"Enter container name\" ng-model=\"vm.container.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\"><div ng-show=\"!vm.form.containerName.$pristine && vm.form.containerName.$error\"><p ng-show=\"vm.form.containerName.$error.pattern\">Your container name must only contain alphanumeric characters, dashes, and periods.</p></div><p class=\"help-block\">Enter a name for your container, leaving this empty will result in a randomly generated name.</p></div><div class=\"form-group\"><label for=\"cpuShares\">Command</label> <input type=\"text\" class=\"form-control\" name=\"command\" id=\"command\" placeholder=\"Enter a command\" ng-model=\"vm.container.command\"></div><div class=\"form-group\"><label for=\"cpuShares\">CPU shares</label> <input type=\"number\" class=\"form-control\" name=\"cpuShares\" id=\"cpuShares\" placeholder=\"Enter amount of CPU shares\" ng-model=\"vm.container.cpuShares\" min=\"0\"></div><div class=\"form-group\"><label for=\"memoryLimit\">Memory limit</label> <input type=\"number\" class=\"form-control\" name=\"memoryLimit\" id=\"memoryLimit\" placeholder=\"Enter memory limit\" ng-model=\"vm.container.memory\" min=\"0\"></div></form></div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\" ng-click=\"vm.submit(vm.form)\">Create container</button> <button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/containerManagement/detail/containerDetail.tpl.html","<div class=\"row\"><div class=\"col-sm-4 col-md-3\"><div class=\"server-actions\" style=\"margin-bottom: 20px;\"><ch-progress-button ng-if=\"!vm.container.State.Running\" action=\"vm.startContainer()\" size=\"sm\" icon=\"mdi-play\">Start container</ch-progress-button><ch-progress-button ng-if=\"vm.container.State.Running\" action=\"vm.stopContainer()\" size=\"sm\" icon=\"mdi-stop\">Stop container</ch-progress-button><ch-progress-button action=\"vm.rebootContainer()\" size=\"sm\" icon=\"mdi-refresh\">Reboot container</ch-progress-button><ch-progress-button action=\"vm.deleteContainer()\" size=\"sm\" type=\"danger\" icon=\"mdi-delete\">Delete container</ch-progress-button></div><div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\">Container details</h4></div><ul class=\"list-group\"><li class=\"list-group-item\"><span class=\"mdi mdi-layers\"></span> {{ vm.container.Config.Image }}</li><li class=\"list-group-item\"><span class=\"mdi mdi-xml\"></span> <span ng-repeat=\"cmd in vm.container.Config.Cmd\" ng-bind=\"cmd\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-clock\"></span> <span>{{ vm.container.Created | timeAgo }}</span></li></ul></div></div><div class=\"col-sm-8 col-md-9\"><ch-panel><ch-panel-heading><h4 class=\"panel-title\">Processes</h4></ch-panel-heading><ch-panel-body><div class=\"table-responsive\"><table class=\"table table-hover servers-table\"><tr class=\"header-row\"><th ng-repeat=\"title in vm.processes.Titles\" ng-bind=\"title\"></th></tr><tr ng-repeat=\"process in vm.processes.Processes\"><td ng-repeat=\"item in process track by $index\" ng-bind=\"item\"></td></tr></table></div></ch-panel-body></ch-panel></div></div>");
$templateCache.put("app/containerManagement/list/containerList.tpl.html","<div><div ng-if=\"panel\"><ch-panel><ch-panel-heading><h4 class=\"panel-title\"><a href=\"\" ui-sref=\"servers.show({ id: server.id })\" ng-bind=\"server.name\"></a></h4></ch-panel-heading><ch-panel-body ng-if=\"!server.containers.length || server.containers.$error\"><div><p class=\"no-margin\" ng-show=\"!server.containers.length && !server.containers.$error\">No containers available, <a href=\"\" ng-click=\"createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"server.containers.$error\"><span ng-bind=\"server.containers.$error\"></span>, <a href=\"\" ng-click=\"getContainers(server)\">try again</a>.</p></div></ch-panel-body><div class=\"list-group\" ng-if=\"server.containers.length\"><a ng-repeat=\"container in server.containers | limitTo:pagination.limit:pagination.from\" href=\"\" class=\"list-group-item containers-list-item\" ui-sref=\"containers.detail({ id: container.Id, serverUrl: server.getIp() })\"><span ng-bind=\"::container.Id\" class=\"item-name\"></span> <span class=\"item-created\"><i class=\"mdi mdi-clock\"></i> {{ container.Created | convertTimestamp | timeAgo }}</span></a></div><div class=\"panel-body\" ng-if=\"server.containers.length > pagination.limit\"><div class=\"row panel-pagination-controls\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ server.containers.length }}\" items-per-page=\"{{ pagination.limit }}\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"pager form-control pull-right\" ng-options=\"range for range in pagination.ranges\" ng-model=\"pagination.limit\"></select></div></div></div></ch-panel></div><div ng-if=\"!panel\"><div class=\"\" style=\"margin-bottom: 20px;\"><a href=\"\" class=\"btn btn-sm btn-primary\" ng-click=\"createContainer(server)\"><span class=\"mdi mdi-plus\"></span> Create new container</a> <a href=\"\" class=\"btn btn-sm btn-primary\" ng-click=\"getContainers(server)\"><span class=\"mdi mdi-refresh\"></span> Refresh containers</a></div><div ng-if=\"!server.containers.length || server.containers.$error\"><p class=\"no-margin\" ng-show=\"!server.containers.length && !server.containers.$error\">No containers available, <a href=\"\" ng-click=\"createContainer(server)\">create a new container</a>.</p><p class=\"no-margin\" ng-show=\"server.containers.$error\"><span ng-bind=\"server.containers.$error\"></span>, <a href=\"\" ng-click=\"getContainers(server)\">try again</a>.</p></div><div class=\"list-group\" ng-if=\"server.containers.length\"><a ng-repeat=\"container in server.containers | limitTo:pagination.limit:pagination.from\" href=\"\" class=\"list-group-item containers-list-item\" ui-sref=\"containers.detail({ id: container.Id, serverUrl: server.getIp() })\"><span ng-bind=\"::container.Id\" class=\"item-name\"></span> <span class=\"item-created\"><i class=\"mdi mdi-clock\"></i> {{ container.Created | convertTimestamp | timeAgo }}</span></a></div><div class=\"row panel-pagination-controls\" ng-if=\"server.containers.length > pagination.limit\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ server.containers.length }}\" items-per-page=\"{{ pagination.limit }}\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"pager form-control pull-right\" ng-options=\"range for range in pagination.ranges\" ng-model=\"pagination.limit\"></select></div></div></div></div>");
$templateCache.put("app/content/header/header.tpl.html","<div class=\"content-header\"><div class=\"container\"><h4 class=\"pull-left\" ng-bind=\"title\"></h4></div></div>");
$templateCache.put("app/imageManagement/create/imageCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create or add image</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><ch-image-search ng-model=\"vm.image.image\"></ch-image-search></form></div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-disabled=\"vm.form.$invalid\" ng-click=\"vm.submit(vm.form)\">Create image</button> <button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/imageManagement/detail/imageDetail.tpl.html","");
$templateCache.put("app/imageManagement/list/imageList.tpl.html","<div><div ng-if=\"panel\"><ch-panel><ch-panel-heading><h4 class=\"panel-title\"><a href=\"\" ui-sref=\"servers.show({ id: server.id })\" ng-bind=\"server.name\"></a></h4></ch-panel-heading><ch-panel-body ng-if=\"!server.images.length || server.images.$error\"><div><p class=\"no-margin\" ng-show=\"!server.images.length && !server.images.$error\">No images available, <a href=\"\" ng-click=\"createImage(server)\">add or create a new image</a>.</p><p class=\"no-margin\" ng-show=\"server.images.$error\"><span ng-bind=\"server.images.$error\"></span>, <a href=\"\" ng-click=\"getImages(server)\">try again</a>.</p></div></ch-panel-body><div class=\"list-group\" ng-if=\"server.images.length\"><a ng-repeat=\"image in server.images | limitTo:pagination.limit:pagination.from\" href=\"\" class=\"list-group-item\"><span ng-bind=\"::image.RepoTags[0]\"></span></a></div><div class=\"panel-body\" ng-if=\"server.images.length > pagination.limit\"><div class=\"row panel-pagination-controls\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ server.images.length }}\" items-per-page=\"{{ pagination.limit }}\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"pager form-control pull-right\" ng-options=\"range for range in pagination.ranges\" ng-model=\"pagination.limit\"></select></div></div></div></ch-panel></div><div ng-if=\"!panel\"><div class=\"\" style=\"margin-bottom: 20px;\"><a href=\"\" class=\"btn btn-sm btn-primary\" ng-click=\"createImage(server)\"><span class=\"mdi mdi-plus\"></span> Add or create new image</a> <a href=\"\" class=\"btn btn-sm btn-primary\" ng-click=\"getImages(server)\"><span class=\"mdi mdi-refresh\"></span> Refresh images</a></div><div ng-if=\"!server.images.length || server.images.$error\"><p class=\"no-margin\" ng-show=\"!server.images.length && !server.images.$error\">No images available, <a href=\"\" ng-click=\"createImage(server)\">add or create a new image</a>.</p><p class=\"no-margin\" ng-show=\"server.images.$error\"><span ng-bind=\"server.images.$error\"></span>, <a href=\"\" ng-click=\"getImages(server)\">try again</a>.</p></div><div class=\"list-group\" ng-if=\"server.images.length\"><a ng-repeat=\"image in server.images | limitTo:pagination.limit:pagination.from\" href=\"\" class=\"list-group-item\"><span ng-bind=\"::image.RepoTags[0]\"></span></a></div><div class=\"row panel-pagination-controls\" ng-if=\"server.images.length > pagination.limit\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ server.images.length }}\" items-per-page=\"{{ pagination.limit }}\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"pager form-control pull-right\" ng-options=\"range for range in pagination.ranges\" ng-model=\"pagination.limit\"></select></div></div></div></div>");
$templateCache.put("app/imageManagement/select/imageSelect.tpl.html","<div style=\"margin: 20px 0;\"><div class=\"list-group\"><a ng-repeat=\"image in server.images | limitTo:pagination.limit:pagination.from\" href=\"\" class=\"list-group-item\" ng-click=\"selectImage(image)\" ng-class=\"{ \'active\': image === selected }\"><span ng-bind=\"::image.RepoTags[0]\"></span></a></div><div class=\"row panel-pagination-controls\" ng-if=\"server.images.length > pagination.limit\"><div class=\"col-xs-8 col-sm-10\"><ch-pagination total-items=\"{{ server.images.length }}\" items-per-page=\"{{ pagination.limit }}\" on-page-change=\"changePage(pageNumber)\"></ch-pagination></div><div class=\"col-xs-4 col-sm-2\"><select class=\"pager form-control pull-right\" ng-options=\"range for range in pagination.ranges\" ng-model=\"pagination.limit\"></select></div></div></div>");
$templateCache.put("app/serverManagement/create/serverCreateForm.tpl.html","<div><div class=\"modal-header\"><h3 class=\"modal-title\">Create a new server</h3></div><div class=\"modal-body\"><form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.serverName.$invalid && !vm.form.serverName.$pristine }\"><label for=\"serverName\">Server name</label> <input type=\"text\" class=\"form-control\" name=\"serverName\" id=\"serverName\" placeholder=\"Enter server name\" ng-model=\"vm.server.name\" ng-pattern=\"/[a-zA-Z\\.\\-0-9]+/g\" required=\"\"><div ng-show=\"!vm.form.serverName.$pristine && vm.form.serverName.$error\"><p ng-show=\"vm.form.serverName.$error.pattern\">Your server name must only contain alphanumeric characters, dashes, and periods.</p></div></div><div class=\"form-group\"><label for=\"serverRegion\">Server region</label><select name=\"serverRegion\" class=\"form-control\" id=\"serverRegion\" ng-model=\"vm.server.region\" ng-disabled=\"!vm.serverOptions || !vm.serverOptions.regions.length\" ng-options=\"value.name group by value.group disable when !value.available for (label, value) in vm.serverOptions.regions\" required=\"\"></select></div><div class=\"form-group\"><label for=\"serverSize\">Server size</label><select name=\"serverSize\" class=\"form-control\" id=\"serverSize\" ng-model=\"vm.server.size\" ng-disabled=\"!vm.serverOptions || !vm.server.region || !vm.server.region.sizes.length\" ng-options=\"option for option in vm.server.region.sizes | sortBySize:option\" required=\"\"></select></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.discoveryToken.$invalid && !vm.form.discoveryToken.$pristine }\"><label for=\"discoveryToken\">Discovery token</label><div class=\"input-group\"><input type=\"url\" class=\"form-control\" name=\"discoveryToken\" id=\"discoveryToken\" placeholder=\"Enter a discovery token\" ng-model=\"vm.server.discovery_token\" required=\"\"> <span class=\"input-group-btn\"><button class=\"btn btn-default\" type=\"button\" ng-click=\"vm.generateToken()\"><span class=\"mdi mdi-refresh\"></span></button></span></div><p class=\"help-block\">Use an existing token to add the server to an existing Etcd cluster. A new token can be generated by clicking the button on the right.</p></div><div class=\"form-group\"><label for=\"sshKeyId\">SSH key</label><select class=\"form-control\" name=\"sshKeyId\" id=\"sshKeyId\" ng-model=\"vm.server.ssh_keys\" ng-options=\"item.name for item in vm.sshKeys\" required=\"\"></select><p class=\"help-block\">Select one or more SSH keys.</p></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"backupsEnabled\"><input type=\"checkbox\" name=\"backupsEnabled\" id=\"backupsEnabled\" ng-model=\"vm.server.backups\"> Enable backups</label></div></div><div class=\"form-group\"><div class=\"checkbox\"><label for=\"ipv6Enabled\"><input type=\"checkbox\" name=\"ipv6Enabled\" id=\"ipv6Enabled\" ng-model=\"vm.server.ipv6\"> Enable IPv6</label></div></div></form></div><div class=\"modal-footer\"><ch-progress-button action=\"vm.submit(vm.form)\" ng-disabled=\"vm.form.$invalid\">Create server</ch-progress-button><button class=\"btn btn-danger\" ng-click=\"vm.cancel()\">Cancel</button></div></div>");
$templateCache.put("app/serverManagement/show/serverContainers.tpl.html","<div class=\"tab-pane fade in active\" id=\"containers\"><container-list server=\"vm.server\"></container-list></div>");
$templateCache.put("app/serverManagement/show/serverShow.tpl.html","<div class=\"alert alert-warning\" role=\"alert\" ng-show=\"vm.server.locked\">This server is currently being provisioned, and thus locked from any interaction. Please wait a moment.</div><div class=\"row\"><div class=\"col-sm-4 col-md-3\"><div class=\"server-actions\" style=\"margin-bottom: 20px;\"><ch-progress-button ng-if=\"vm.server.status === \'off\'\" action=\"vm.startServer()\" size=\"sm\" icon=\"mdi-play\">Start server</ch-progress-button><ch-progress-button ng-if=\"vm.server.status !== \'off\'\" action=\"vm.stopServer()\" size=\"sm\" icon=\"mdi-stop\">Stop server</ch-progress-button><ch-progress-button action=\"vm.rebootServer()\" size=\"sm\" icon=\"mdi-refresh\">Reboot server</ch-progress-button><ch-progress-button action=\"vm.deleteServer()\" size=\"sm\" type=\"danger\" icon=\"mdi-delete\">Delete server</ch-progress-button></div><div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\">Server details</h4></div><ul class=\"list-group\"><li class=\"list-group-item\"><span class=\"mdi mdi-cloud\"></span> Digital Ocean</li><li class=\"list-group-item\"><span class=\"mdi mdi-play-circle\"></span> {{ vm.server.status | capitalize }}</li><li class=\"list-group-item\"><span class=\"mdi mdi-clock\"></span> <span>{{ vm.server.created_at | timeAgo }}</span></li><li class=\"list-group-item\"><span class=\"mdi mdi-layers\"></span> <span ng-bind=\"::vm.server.image.distribution\"></span> <span ng-bind=\"::vm.server.image.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-map-marker\"></span> <span ng-bind=\"::vm.server.region.name\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-server-network\"></span> <span ng-bind=\"::vm.server.getIp()\"></span></li><li class=\"list-group-item\"><span class=\"mdi mdi-cube-outline\"></span> <span ng-bind=\"vm.server.containers.length || 0\"></span> containers</li></ul></div></div><div class=\"col-sm-8 col-md-9\"><ch-panel class=\"with-nav-tabs\"><ch-panel-heading><ul class=\"nav nav-tabs\"><li ui-sref-active=\"active\"><a href=\"\" ui-sref=\".containers\">Containers</a></li><li ui-sref-active=\"active\"><a href=\"\" ui-sref=\".images\">Images</a></li></ul></ch-panel-heading><ch-panel-body><div class=\"tab-content\"><div ui-view=\"tab-content\"></div></div></ch-panel-body></ch-panel></div></div>");
$templateCache.put("app/user/apikeys/apikeys.tpl.html","<div class=\"form-group\"><label for=\"digitalOcean\">Digital Ocean API key</label> <input type=\"text\" class=\"form-control\" id=\"digitalOcean\" ng-model=\"vm.apiKey\" placeholder=\"API key for Digital Ocean\"></div><ch-progress-button action=\"vm.updateApiKey()\" ng-disabled=\"\">Update API key</ch-progress-button>");
$templateCache.put("app/user/dropdown/userDropdown.tpl.html","<li class=\"navbar-user dropdown\"><a href=\"\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-expanded=\"false\"><span ng-bind=\"fullName\"></span></a><ul class=\"dropdown-menu\" role=\"menu\"><li><a href=\"\" ui-sref=\"user.profile\">My profile</a></li><li><a href=\"\" ng-click=\"logout()\">Log out</a></li></ul></li>");
$templateCache.put("app/user/profile/profile.tpl.html","<form name=\"vm.form\" novalidate=\"\"><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.username.$invalid && !vm.form.username.$pristine }\"><label for=\"username\">username</label> <input type=\"text\" class=\"form-control\" name=\"username\" id=\"username\" placeholder=\"Enter username\" ng-model=\"vm.user.username\" required=\"\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.password.$invalid && !vm.form.password.$pristine }\"><label for=\"password\">Password</label> <input type=\"password\" class=\"form-control\" name=\"password\" id=\"password\" placeholder=\"Enter password\" ng-model=\"vm.user.password\" required=\"\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.passwordRepeat.$invalid && !vm.form.passwordRepeat.$pristine }\"><label for=\"passwordRepeat\">Repeat password</label> <input type=\"password\" class=\"form-control\" name=\"passwordRepeat\" id=\"passwordRepeat\" placeholder=\"Repeat password\" ng-model=\"vm.user.passwordRepeat\" compare-to=\"vm.user.password\" required=\"\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.email.$invalid && !vm.form.email.$pristine }\"><label for=\"email\">Email</label> <input type=\"email\" class=\"form-control\" name=\"email\" id=\"email\" placeholder=\"Enter e-mail\" ng-model=\"vm.user.email\" required=\"\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.firstName.$invalid && !vm.form.firstName.$pristine }\"><label for=\"firstName\">First name</label> <input type=\"text\" class=\"form-control\" name=\"firstName\" id=\"firstName\" placeholder=\"Enter first name\" ng-model=\"vm.user.firstName\" required=\"\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.prefix.$invalid && !vm.form.prefix.$pristine }\"><label for=\"prefix\">Prefix</label> <input type=\"text\" class=\"form-control\" name=\"prefix\" id=\"prefix\" placeholder=\"Enter prefix\" ng-model=\"vm.user.prefix\"></div><div class=\"form-group\" ng-class=\"{ \'has-error\' : vm.form.lastName.$invalid && !vm.form.lastName.$pristine }\"><label for=\"lastName\">Last name</label> <input type=\"text\" class=\"form-control\" name=\"lastName\" id=\"lastName\" placeholder=\"Enter lastName\" ng-model=\"vm.user.lastName\" required=\"\"></div><ch-progress-button action=\"vm.submit(vm.form)\" ng-disabled=\"vm.form.$invalid\">Update profile</ch-progress-button></form>");}]);