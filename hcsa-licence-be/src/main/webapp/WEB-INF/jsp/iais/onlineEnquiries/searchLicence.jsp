<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="center-content">
            <div class="intranet-content">
                <div class="bg-title col-xs-12 col-md-12">
                    <h2>Advanced Search Criteria</h2>
                </div>
                <div class="row">&nbsp;</div>
                <div class="panel-group " id="accordion" role="tablist" aria-multiselectable="true">
                    <%@ include file="advancedFilter.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function doLicSearch() {
        showWaiting();

        $('input[name="pageJumpNoTextchangePage"]').val(1);
        $("[name='crud_action_type']").val('search');
        $('#mainForm').submit();

    }

    function doLicBack() {
        showWaiting();
        $("[name='crud_action_type']").val('back');
        $('#mainForm').submit();
    }

    function doLicClear() {
        $('input[type="text"]').val("");
        $("#service_licence_type option:first").prop("selected", 'selected');
        $("#service_sub_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").prop("selected", 'selected');
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#personnelRoleOption option:first").prop("selected", 'selected');
        $(".current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
        $('input[name="start_date"]').val("");
        $('input[name="start_to_date"]').val("");
        $('input[name="expiry_start_date"]').val("");
        $('input[name="expiry_date"]').val("");
        $("#error_to_date").hide();
        $("#error_start_to_date").hide();
        $("#error_expiry_date").hide();

    }

    function keyPress() {
        var keyCode = event.keyCode;
        if (keyCode >= 48 && keyCode <= 57) {
            event.returnValue = true;
        } else {
            event.returnValue = false;
        }
    }
</script>