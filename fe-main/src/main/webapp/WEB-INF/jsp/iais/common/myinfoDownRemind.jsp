<input type="hidden" id="myinfoServiceDown" name="myinfoServiceDown" value="${myinfoServiceDown}">
<input type="hidden" id="verifyTakenConfiguration" name="verifyTakenConfiguration" value="<c:out value="${verifyTakenConfiguration}"/>">
<input type="hidden" id="callAuthoriseApiUri" name="callAuthoriseApiUri" value="<c:out value="${callAuthoriseApiUri}"/>">
<iais:confirm msg="USER_ACK001"  needCancel="false" callBack="myinfoSupportCallbacksupport()"  yesBtnDesc="I know." popupOrder="myinfoSupport" />
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
        var timestamp = new Date().getTime();
        var startTime = $("#verifyTakenConfiguration").val();
        var ex = parseInt(timestamp) - parseInt(startTime) - 30*60*1000;
        return ex > 0;
    }

    function callAuthoriseApi() {
        var authoriseUrl = $("#callAuthoriseApiUri").val();
        window.location = authoriseUrl;
    }
</script>