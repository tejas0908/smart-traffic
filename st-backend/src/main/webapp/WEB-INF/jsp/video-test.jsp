<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
function init() {
    "use strict";

    var video, $output;
    var scale = 0.5;

    function captureImage() {
        console.log("capturing image");
        var canvas = document.createElement("canvas");
        canvas.width = video.videoWidth * scale;
        canvas.height = video.videoHeight * scale;
        canvas.getContext('2d')
              .drawImage(video, 0, 0, canvas.width, canvas.height);

        var img = document.createElement("img");
        img.src = canvas.toDataURL();
        $output.appendChild(img);
    }

    function initialize() {
        $output = document.getElementById("output");
        video = document.getElementById("video");
        document.getElementById("capture").addEventListener ("click", captureImage, false);
        console.log("initialized");
    }

    initialize();
}
</script>
</head>
<body onload="init()">

<video id="video" width="640" height="480" autoplay muted>
  <source src="video/traffic1.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>
<br/>
<button id="capture">Capture</button>
<br/>
<div id="output"></div>
</body>
</html>
