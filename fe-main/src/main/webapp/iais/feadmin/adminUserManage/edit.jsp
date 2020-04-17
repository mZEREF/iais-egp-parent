<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Edit HCSA Internet User Account</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <%@include file="../../common/userForm.jsp"%>
                                    <iais:row>
                                        <iais:field value="Is Administrator" width="11"/>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="admin" name="role" <c:if test="${user.userRole=='ORG_ADMIN'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >Yes</label>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role" <c:if test="${user.userRole!='ORG_ADMIN'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >No</label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Is Active" width="11"/>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="active" name="active" <c:if test="${user.available}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >Yes</label>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="inactive" name="active" <c:if test="${!user.available}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >No</label>
                                        </div>
                                    </iais:row>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-11 col-md-11">
                                                <div class="text-right col-xs-1 col-md-1">
                                                    <a align="left" class="back" href="#" onclick="cancel()"><em class="fa fa-angle-left"></em> Back</a></div>
                                                <div class="text-right col-xs-10 col-md-10">
                                                    <button class="btn btn-primary" id="savebtn" onclick="javascript:save()">Save</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input hidden name="id" value="${user.id}">
    <input hidden name="action" id="action" value="">
</form>
<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function save() {
        $("#action").val("save");
        SOP.Crud.cfxSubmit("mainForm");
    }
    function cancel() {
        $("#action").val("cancel");
        SOP.Crud.cfxSubmit("mainForm");
    }
</script>