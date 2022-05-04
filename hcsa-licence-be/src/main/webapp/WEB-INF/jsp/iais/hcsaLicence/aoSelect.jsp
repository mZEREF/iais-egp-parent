<iais:row id = "showAoRow">
  <iais:field value="Officer"/>
  <iais:value width="10" id = "showAoDiv" cssClass="other-charges-type-div">
    <iais:select name="aoSelect" firstOption="Please Select"
                 value="Assign To"/>
  </iais:value>
</iais:row>
<script type="text/javascript">
    $(document).ready(function () {
        $("#showAoRow").hide();
        checkAo();
        $("#verified").change(function () {
            checkAo();
        })
    });


    function checkAo(){
        var verified = $("#verified").val();
        if(verified != ""){
            var data = {
                'verified':verified
            };
            showWaiting();
            $.ajax({
                'url':'${pageContext.request.contextPath}/check-ao',
                'dataType':'json',
                'data':data,
                'type':'POST',
                'success':function (data) {
                    if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                        $("#error_aoSelect").html('');
                        $("#showAoDiv").html(data.resultJson + '');
                        $("#aoSelect").niceSelect();
                        $("#showAoRow").show();
                    }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){
                        $("#error_aoSelect").html(data.resultJson + '');
                        $("#showAoRow").hide();
                    }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){
                        $("#error_aoSelect").html('');
                        $("#showAoRow").hide();
                    }
                    // setValue();
                },
                'error':function () {

                }
            });
            dismissWaiting();
        }
    }
</script>
