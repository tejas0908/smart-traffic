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

  <!-- <script src="lib/socket.io.js"></script>
  <script src="lib/socket.js"></script> -->
  <!-- <script src="lib/angular-timer.min.js"></script> -->
  <script src="lib/ng-stomp.standalone.min.js"></script>
  <!-- <script src="lib/ng-file-upload/ng-file-upload.min.js"></script> -->
  <!-- <script src="lib/moment/min/moment.min.js"></script>
  <script src="lib/moment/min/locales.min.js"></script>
  <script src="lib/humanize-duration/humanize-duration.js"></script> -->
  <!-- your app's js -->
  <script src="js/app.js"></script>
  <script src="js/services/CanvasService.js"></script>
  <script src="js/services/SocketService.js"></script>
  <script src="js/homeCtrl.js"></script>
  <script src="js/demoCtrl.js"></script>
  <script src="js/services/ImageService.js"></script>
  <script src="js/services/fileUploadDir.js"></script>


  </head>
  <body ng-app="traffic-mgmt-webapp">
    <ion-nav-view animation="slide-left-right"></ion-nav-view>
  </body>
</html>
