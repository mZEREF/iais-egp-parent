<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<div class="main-content">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="crud_action_value" value="">
        <div class="container">
            <div class="row">
                <div class="center-content">
                    <label class="col-md-2 col-md-offset-1">Reason:</label>
                    <div class="col-md-6">
                        <iais:select name="withdrawnReason" id="withdrawnReason" options="withdrawalReason"
                                     firstOption="Please select a reason" onchange="withdrawalReasons(this.value);"></iais:select>
                        <span id="error_withdrawnReason" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
            <div class="row"  id="reason" hidden>
                <div class="center-content">
                    <label class="col-md-2 col-md-offset-1">Remarks:</label>
                    <div class="col-md-6">
                        <textarea name="withdrawnRemarks" cols="58" rows="15" id="htmlEditroArea"
                                  title="content"></textarea>
                        <span id="error_withdrawnRemarks" name="iaisErrorMsg"
                              class="error-msg"></span>
                    </div>
                </div>
            </div>
            <div class="row" style="padding-top: 120px">
                <div class="col-md-2 col-md-offset-7">
                    <div class="components">
                        <a class="btn btn-primary" onclick="doSubmit()">Submit</a>
                    </div>
                </div>
                <div class="col-md-2">
                    <div class="components">
                        <a class="btn btn-primary">Cancel</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function withdrawalReasons(obj) {
        console.log(obj)
        if (obj == "Others") {
            $("#reason").show();
        } else {
            $("#reason").css("display","none")
        }
    }

    function submit(action) {
        $("[name='app_action_type']").val(action);
        $("#mainForm").submit();
    }

    function doSubmit(){
        $("viewchk").val("");
        showWaiting();
        submit("withdrawalStep");
    }
</script>



