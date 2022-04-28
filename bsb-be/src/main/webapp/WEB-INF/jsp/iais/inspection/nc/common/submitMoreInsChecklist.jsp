<div class="row">
    <div class="col-xs-12">
        <a href="#" style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Back</a>
        <div style="float:right">
            <button class="btn btn-primary next" type="button" onclick="javascript:doBack()">Submit</button>
        </div>
    </div>
</div>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("#doSubmitAction").val("next");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doBackToMain(){
        showWaiting();
        $("#doSubmitAction").val("");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doSaveDraftCheckList(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "saveDraft");
    }




    function showCheckBox(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var divId = str+'ck'+ indexIns;
        var comId = str+'comck'+indexIns;
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var divId = str+'ck'+ indexIns;;
        var comdivId = str+'comck'+indexIns;;
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        $("#"+divId).hide();
        $("#"+comdivId).hide();
    }

    function doChangeDeconflict(index,divId,size){
        if(index == 1){
            divId += "comDiv"
        }else if (index == 2){
            divId += "serDiv";
        }else {
            divId += "adhocDiv";
            size=size-1;
        }
        for(var i = 0; i<= size; i++){
            var id = "#"+divId+i;
            $(id).css("background-color", "lightgreen");
        }
    }
</script>