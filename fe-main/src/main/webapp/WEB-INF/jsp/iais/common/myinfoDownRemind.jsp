<input type="hidden" id="myinfoServiceDown" name="myinfoServiceDown" value="${myinfoServiceDown}">
<input type="hidden" id="myinfoTrueOpen" name="myinfoTrueOpen" value="${myinfoTrueOpen}">
<input type="hidden" id="verifyTakenConfiguration" name="verifyTakenConfiguration" value="<c:out value="${verifyTakenConfiguration}"/>">
<input type="hidden" id="callAuthoriseApiUri" name="callAuthoriseApiUri" value="<c:out value="${callAuthoriseApiUri}"/>">
<iais:confirm msg="USER_ACK001"  needCancel="false" callBack="myinfoSupportCallbacksupport()"  yesBtnDesc="Ok" popupOrder="myinfoSupport" />
<input type="hidden" id="loadMyInfoData" name="loadMyInfoData" value="${isLoadMyInfoData}"/>
<script type="text/javascript">
    $(document).ready(function () {
        if( $("#myinfoServiceDown").val() == "Y"){
            $('#myinfoSupport').modal('show');
        }
    });
    function myinfoSupportCallbacksupport(){
        $('#myinfoSupport').modal('hide');
    }

    function verifyTaken(){
        var myinfoTrueOpen = $("#myinfoTrueOpen").val();
        if('Y' != myinfoTrueOpen ){
            return true;
        }
        var timestamp = new Date().getTime();
        var startTime = $("#verifyTakenConfiguration").val();
        if(startTime == '-1'){
            return false;
        }
        var ex = parseInt(timestamp) - parseInt(startTime) - 30*60*1000;
        return ex > 0;
    }

    function callAuthoriseApi() {
        var addrType = $("#addrType").val();
        var authoriseUrl = $("#callAuthoriseApiUri").val();
        if(addrType != null && addrType != ""){
            authoriseUrl += addrType;
        }
        window.location = authoriseUrl;
    }
</script>