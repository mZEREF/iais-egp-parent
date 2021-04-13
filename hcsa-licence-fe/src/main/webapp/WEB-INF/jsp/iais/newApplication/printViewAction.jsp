<input type="hidden" name="loadFirstPrint" id="loadFirstPrint" value="Y">
<input type="hidden" name="loadFirstPrintNeed" id="loadFirstPrintNeed" value="N">
<script type="text/javascript">
    function previewPrintForInter(){
        showWaiting();
        var loadFirstPrintNeed = $("#loadFirstPrintNeed").val();
        var loadFirstPrint = $("#loadFirstPrint").val();
        let time = 1000;
        var as = $("a[name='printControlNameForApp']");
        var list = new Array();
        let cardinalNumber = 500;
        if(loadFirstPrintNeed == "Y"&& loadFirstPrint == "Y"){
            cardinalNumber = 4000;
        }
        let size = as.length;
        if (size >=3){
            time += (size-2) * cardinalNumber;
        }
        for(let i = 0; i<as.length;i++){
            if($(as[i]).hasClass("collapsed")){
                $(as[i]).click();
                list.push(i);
            }
        }
        dismissWaiting();
        setTimeout(function(){
            window.print();
        },time);
        setTimeout(function(){
            for(let i = 0; i< list.length;i++){
                var index = list[i];
                $(as[index]).click();
            }
            $("#loadFirstPrint").val("N");
        },time+1000);
    }
</script>