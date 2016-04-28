<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <title></title>

    <link href="lib/ionic/css/ionic.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

    <!-- IF using Sass (run gulp sass first), then uncomment below and remove the CSS includes above
    <link href="css/ionic.app.css" rel="stylesheet">
    -->

    <!-- ionic/angularjs js -->
    <script src="lib/ionic/js/ionic.bundle.js"></script>

    <!-- cordova script (this will be a 404 during development) -->
    <script src="cordova.js"></script>

   <script src="lib/socket.io.js"></script>
  <script src="lib/socket.js"></script>
  <!-- your app's js -->
  <script src="js/app.js"></script>
  <script src="js/services/CanvasService.js"></script>
  <script src="js/services/SocketService.js"></script>
    <script src="js/homeCtrl.js"></script>
  </head>
  <body ng-app="traffic-mgmt-webapp">
    <ion-nav-view animation="slide-left-right"></ion-nav-view>
  </body>
</html>
