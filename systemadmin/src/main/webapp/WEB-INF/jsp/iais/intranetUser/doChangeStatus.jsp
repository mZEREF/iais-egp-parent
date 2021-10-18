<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form class="form-horizontal" style="margin-left: 1%;margin-right:1%;width: 100%" method="post" id="ChangeStatusForm"
      action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <br><br><br><br><br><br>
        <h2>Change Status</h2>
        <iais:section title="" id="change_status">
            <iais:row>
                <iais:field  value="UserId" required="true"/>
                <iais:value width="7">
                    <input id="userId" type="text" name="statusUserId" value="${statusUserId}">
                    <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
        </iais:section>
        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-1 col-md-1"></div>
                <div class="col-xs-2 col-md-2">
                    <div class="text-left">
                        <a href="#" class="back" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-8 col-md-8">
                    <div class="text-right">
                        <a class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Deactivate</a>
                        <a class="btn btn-primary" href="#" onclick="submit('doReactivate')">Reactivate</a>
                        <a class="btn btn-primary" href="#" onclick="submit('doTerminate')">Terminate</a>
                        <a class="btn btn-primary" href="#" onclick="submit('doUnlock')">Unlock</a>
                    </div>

                </div>

            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#ChangeStatusForm").submit();
    }
</script>