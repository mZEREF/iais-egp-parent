$(document).ready(function () {
    $(':input').prop('disabled', true);
    $('#accordion').find('.collapse').collapse('show');
    // textarea
    $('textarea').each(function(index, ele){
        $(ele).parent().append('<div style="border-radius:8px;border: 1px solid #000;padding: 5px;">'
            + $(ele).val() + '</div>');
        $(ele).remove();
    });

    var userAgent = navigator.userAgent;
    var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;

    // disabled <a>
    $('a').prop('disabled',true);
    if(isChrome){
        addPrintListener();
        window.print();
    }else{
        window.print();
        window.close();
    }
});


var addPrintListener = function () {
    if (window.matchMedia) {
        var mediaQueryList = window.matchMedia('print');
        mediaQueryList.addListener(function(mql) {
            if (mql.matches) {

            } else {
                window.close();
            }
        });
    }
}