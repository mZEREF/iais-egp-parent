<div class="row">
    <div class="col-xs-12">
        <a href="#" style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Previous</a>
        <div style="float:right">
            <c:if test="${nowTabIn == 'Combined'}">
                <button class="btn btn-primary next" type="button" onclick="javascript:doBack()">Submit</button>
            </c:if>
            <c:if test="${nowTabIn != 'Combined'}">
                <button class="btn btn-primary next" type="button" onclick="javascript:doSaveDraftCheckList();">Save Draft</button>
            </c:if>
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
        var divId = str+'ckIns'+ indexIns;
        divId  = divId.replace(/\s*/g,"");
        $("#"+divId).show();
    }
    function hideCheckBox(str,indexIns){
        var divId = str+'ckIns'+ indexIns;
        divId  = divId.replace(/\s*/g,"");
        $("#"+divId).hide();
    }

    function showFollUp(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var comIdobserveFoll = str+'observeFoll'+indexIns;
        comIdobserveFoll = comIdobserveFoll.replace(/\s*/g,"");
        $("#"+comIdobserveFoll).show();
        var comIdfollAction = str+'follAction'+indexIns;
        comIdfollAction = comIdfollAction.replace(/\s*/g,"");
        $("#"+comIdfollAction).show();
        var comIdDueDate = str+'DueDate'+indexIns;
        comIdDueDate = comIdDueDate.replace(/\s*/g,"");
        $("#"+comIdDueDate).show();
    }
    function hideFollUp(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var comIdobserveFoll = str+'observeFoll'+ indexIns;
        comIdobserveFoll  = comIdobserveFoll.replace(/\s*/g,"");
        $("#"+comIdobserveFoll).hide();
        var comIdfollAction = str+'follAction'+ indexIns;
        comIdfollAction  = comIdfollAction.replace(/\s*/g,"");
        $("#"+comIdfollAction).hide();
        var comIdDueDate = str+'DueDate'+ indexIns;
        comIdDueDate  = comIdDueDate.replace(/\s*/g,"");
        $("#"+comIdDueDate).hide();
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