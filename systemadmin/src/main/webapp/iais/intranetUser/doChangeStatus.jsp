<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="ChangeStatusForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Change Status</h2>
                        </div>
                        <div class="form-group">
                            <span style="color:red">*</span>
                            <label class="col-xs-12 col-md-4 control-label" for="userId">UserId.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="userId" type="text" name="userId" value="">
                                    <span id="error_displayName" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <span style="color:red">*</span>
                            <label class="col-xs-12 col-md-4 control-label" for="password">Password.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="password" type="text" name="password" value="">
                                    <span id="error_password" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                </div>
        </div>
</div>
        <div>
            <div class="row">
                <div class="col-xs-2 col-sm-2">
                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('back')">BACK</a>
                    </div>
                </div>
                <div class="col-xs-2 col-sm-2">
                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('deactivate')">Deactivate</a>
                    </div>
                </div>
                <div class="col-xs-2 col-sm-2">
                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('reactivate')">Reactivate</a>
                    </div>
                </div>
                <div class="col-xs-2 col-sm-2">
                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('terminate')">Terminate</a>
                    </div>
                </div>
                <div class="col-xs-2 col-sm-2">
                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('unlock')">Unlock</a>
                    </div>
                </div>

            </div>
        </div>
</form>
<%@include file="/include/validation.jsp" %>
</div>

<script type="text/javascript">
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#ChangeStatusForm").submit();
    }
</script>