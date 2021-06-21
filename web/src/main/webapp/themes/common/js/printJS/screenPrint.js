function printPDF(){
    var scrollWidth = document.documentElement.scrollWidth || document.body.scrollWidth
    var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
    clipScreenshots("bg_canvas",scrollHeight,scrollWidth);
 }


var canvasExt = {

    drawRect: function (canvasId,pageHigth,pageWidth) {
        var x = 0;
        var y = 0;
        
        html2canvas(document.body, {
            scale: 1,
            // allowTaint: true,
            useCORS: true
        }).then(canvas => {
            printClip(canvas, x, y, Math.abs(pageWidth), Math.abs(pageHigth))
        });
    }
};
 
function clipScreenshots(canvasId,pageHigth,pageWidth){
    canvasExt.drawRect(canvasId,pageHigth,pageWidth);
}
 

function printClip(canvas, capture_x, capture_y, capture_width, capture_height) {
    var clipCanvas = document.createElement('canvas')
    clipCanvas.width = capture_width
    clipCanvas.height = capture_height
    clipCanvas.getContext('2d').drawImage(canvas, capture_x, capture_y, capture_width, capture_height, 0, 0, capture_width, capture_height)
    var clipImgBase64 = clipCanvas.toDataURL()
    var clipImg = new Image()
    clipImg.src = clipImgBase64
    $(clipImg).print()
    
}