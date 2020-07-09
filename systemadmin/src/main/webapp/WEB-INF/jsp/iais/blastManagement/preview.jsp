<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style type="text/css">
    .flowDisplay{
        display: flex;
    }
    .flowHidden{
        display: none;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" id="crud_action_type_value" name="crud_action_type_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Preview</h2>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message ID"/>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <label >${edit.messageId}</label>
                                </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message Name" required="true"/>
                                    <label  class="col-xs-0 col-md-4 control-label">${edit.getMsgName()}</label>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" required="true"/>
                                    <label  class="col-xs-0 col-md-4 control-label">${edit.mode}</label>
                        </div>

                        <div class="form-group">
                            <iais:field value="Send date and time" required="true"/>
                                    <label  class="col-xs-0 col-md-4 control-label"><fmt:formatDate value="${edit.schedule}"
                                                           pattern="dd/MM/yyyy HH:mm:ss"/></label>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                                    <label class="col-xs-0 col-md-4 control-label"><iais:code code="${edit.status}"></iais:code> </label>
                        </div>

                        <c:choose>
                            <c:when test='${"Email".equals(edit.mode)}'>

                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>


<script type="text/javascript">
    $('#saveDis').click(function () {
        $("#crud_action_type_value").val("next")
        SOP.Crud.cfxSubmit("mainForm");
    });

    $("#back").click(function () {
        $("#crud_action_type_value").val("back")
        SOP.Crud.cfxSubmit("mainForm");
    })

    function changeFlow(mode) {
        if(mode == "SMS"){
            $("#smsFlow").addClass("flowDisplay");
            $("#smsFlow").removeClass("flowHidden");
            $("#emailFlow").addClass("flowHidden");
            $("#emailFlow").removeClass("flowDisplay");
        }else{
            $("#smsFlow").addClass("flowHidden");
            $("#smsFlow").removeClass("flowDisplay");
            $("#emailFlow").addClass("flowDisplay");
            $("#emailFlow").removeClass("flowHidden");
        }
    }

</script>