<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<iais:row id = "showLrRow">
  <iais:field value="Route Laterally To" required="true" id="laterallyField"/>
  <iais:value width="10" id = "showLrDiv" >
    <iais:select name="lrSelect" firstOption="By System"
                 value="Assign To"/>
  </iais:value>
  <iais:field value=" "/>
  <iais:value width="10">
    <span id="error_lrSelectIns" name="iaisErrorMsg" class="error-msg"></span>
  <span id="laterallyMsg"
          style="display: none; font-size: 1.6rem; color: #D22727;"><iais:message
            key="GENERAL_ERR0006"/></span>
  </iais:value>
</iais:row>
<script type="text/javascript">
    $(document).ready(function () {
        $("#showLrRow").hide();
        checkLr();

    });


    function checkLr(){
        var verified = '${roleId}';
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
                        $("#error_lrSelect").html('');
                        $("#showLrDiv").html(data.resultJson + '');
                        $("#lrSelect").niceSelect();
                        $("#showLrRow").show();
                    }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){
                        $("#error_lrSelect").html(data.resultJson + '');
                        $("#showLrRow").hide();
                    }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){
                        $("#error_lrSelect").html('');
                        $("#showLrRow").hide();
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
