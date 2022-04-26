<div class="row">
    <div class="col-xs-12">
        <c:if test="${applicationViewDto.applicationDto.status ==  ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW}">
            <a href="#" class="back" id="Back" onclick="javascript:doInspRecCheckListViewBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
        </c:if>
        <c:if test="${applicationViewDto.applicationDto.status !=  ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW}">
            <a href="#" style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Back</a>
        </c:if>
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
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();
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
        if (status == 'APST032' || status == 'APST022') {
            readOnlyAllForCheckListOnly();
        }
    }
    function readOnlyAllForCheckListOnly() {
        $("#checkLsitItemArea textarea").attr('readonly','readonly');
        $("#checkLsitItemArea textarea").attr('Enabled',false);
        $("#checkLsitItemArea input[type='checkbox']").attr("disabled",true);
        $("#checkLsitItemArea  input[type='radio']").attr("disabled",true);
    }

    function doInspRecCheckListViewBack() {
        showWaiting();
        $("#actionValue").val('view');
        inspRecCheckListViewSubmit('view');
    }

    function inspRecCheckListViewSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }
</script>
