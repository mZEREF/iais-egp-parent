<div class="row">
    <div class="col-xs-12">
        <a href="#" style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Back</a>
        <c:if test="${inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
            <div style="float:right">
                <c:if test="${ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                    <button class="btn btn-primary next" type="button" onclick="javascript:doBack()">Submit</button>
                    <button class="btn btn-primary next" type="button" onclick="javascript:doSaveDraftCheckList();">Save Draft</button>
                </c:if>
            </div>
        </c:if>
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

    $(document).ready(function (){
        readOnlyAllForCheckList('${applicationViewDto.applicationDto.status}');
        var beforeFinishCheck = ${(inspectionNcCheckListDelegator_before_finish_check_list == null || inspectionNcCheckListDelegator_before_finish_check_list == "0") ? '0' : '1'};
        if( beforeFinishCheck == '1'){
            readOnlyAllForCheckListOnly();
        }
    });

    function readOnlyAllForCheckList(status) {
        if (status == 'APST032') {
            readOnlyAllForCheckListOnly();
        }
    }
    function readOnlyAllForCheckListOnly() {
        $("#checkLsitItemArea textarea").attr('readonly','readonly');
        $("#checkLsitItemArea textarea").attr('Enabled',false);
        $("#checkLsitItemArea input[type='checkbox']").attr("disabled",true);
        $("#checkLsitItemArea  input[type='radio']").attr("disabled",true);
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