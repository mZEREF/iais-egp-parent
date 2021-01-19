<input type="hidden" id="myinfoServiceDown" name="myinfoServiceDown" value="${myinfoServiceDown}">
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
</script>