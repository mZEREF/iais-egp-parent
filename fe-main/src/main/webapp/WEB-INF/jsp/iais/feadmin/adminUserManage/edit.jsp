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
                        <h1>${title} Account</h1>
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
                                    <%@include file="/WEB-INF/jsp/iais/common/userForm.jsp"%>
                                    <c:choose>
                                        <c:when test="${isAdmin.equals('1')}">
                                            <iais:row>
                                                <iais:field value="Is Administrator" width="11"/>
                                                <div class="col-md-3">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="admin" name="role" <c:if test="${inter_user_attr.userRole=='ORG_ADMIN'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >Yes</label>
                                                </div>
                                                <div class="col-md-3">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role" <c:if test="${inter_user_attr.userRole!='ORG_ADMIN'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >No</label>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Is Active" width="11"/>
                                                <div class="col-md-3">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="active" name="active" <c:if test="${inter_user_attr.status == 'CMSTAT001'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >Yes</label>
                                                </div>
                                                <div class="col-md-3">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="inactive" name="active" <c:if test="${inter_user_attr.status != 'CMSTAT001'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >No</label>
                                                </div>
                                            </iais:row>
                                        </c:when>
                                    </c:choose>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-2 col-md-2">
                                                <a   style="padding-left: 90px;" align="left" class="back" href="#" onclick="cancel()"><em class="fa fa-angle-left"></em> Back</a></div>
                                            <div class="text-right col-xs-9 col-md-9">
                                                <c:if test="${iais_Login_User_Info_Attr.nricNum == inter_user_attr.idNumber}">
                                                <button class="btn btn-primary save" id="reLoadMyInfo" onclick="javascript:reLoadMyInfoTodo()">Refresh Data</button>
                                                </c:if>
                                                <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">Save</button>
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
    <%@ include file="/WEB-INF/jsp/iais/common/myinfoDownRemind.jsp" %>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function save() {
        $("#action").val("save");
        SOP.Crud.cfxSubmit("mainForm");
    }
    function cancel() {
        $("#action").val("cancel");
        SOP.Crud.cfxSubmit("mainForm");
    }

    function reLoadMyInfoTodo() {
        $("#action").val("getMyInfo");
        SOP.Crud.cfxSubmit("mainForm");
    }
</script>