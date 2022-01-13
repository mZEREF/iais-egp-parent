<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts" %>
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
                                    <c:if test="${iais_Login_User_Info_Attr.nricNum == inter_user_attr.idNumber}">
                                        <%@include file="/WEB-INF/jsp/iais/common/myinfoInstructionsLinks.jsp"%>
                                    </c:if>
                                    <%@include file="/WEB-INF/jsp/iais/common/userForm.jsp"%>
                                    <c:choose>
                                        <c:when test="${isAdmin.equals('1')}">
                                            <iais:row  style="margin-bottom:0px">
                                                <iais:field value="Is Administrator" id="userRole" width="12"/>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="${RoleConsts.USER_ROLE_ORG_ADMIN}" name="role" <c:if test="${inter_user_attr.userRole== RoleConsts.USER_ROLE_ORG_ADMIN}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >Yes</label>
                                                </div>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role" <c:if test="${inter_user_attr.userRole!='ORG_ADMIN'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >No</label>
                                                </div>
                                                <br>
                                                <div class="col-xs-12 col-md-4 control-label">&nbsp;</div>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <span style="padding-left: 15px;" class="error-msg" name="errorMsg" id="error_userRole"></span>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Roles" width="5" required="true" />
                                                <iais:value width="7" cssClass="col-md-7">
                                                 <c:forEach var="role" items="${SESSION_NAME_ROLES}">
                                                         <c:set var="value" value="${role.value}"/>
                                                         <c:set var="roles" value="${inter_user_attr.roles}"/>
                                                         <div class="form-check col-xs-7">
                                                             <input class="form-check-input" type="checkbox"
                                                                    name="roles"
                                                                    value="${value}"
                                                                    id="role${value}"
                                                                    <c:if test="${StringUtil.stringContain(roles,value)}">checked</c:if>
                                                                    aria-invalid="false" >
                                                             <label class="form-check-label"
                                                                    for="role${value}"><span
                                                                     class="check-square"></span>
                                                                 <c:out value="${role.text}"/></label>
                                                         </div>
                                                 </c:forEach>
                                                </iais:value>
                                                <iais:value width="4" cssClass="col-md-4"/>
                                                <iais:value width="3" cssClass="col-md-3">
                                                    <span id="error_roles" name="iaisErrorMsg" class="error-msg"></span>
                                                </iais:value>
                                            </iais:row>
                                            <p></p>
                                            <iais:row>
                                                <iais:field value="Is Active" width="12"/>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="active" name="active" <c:if test="${inter_user_attr.status == 'CMSTAT001'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >Yes</label>
                                                </div>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="inactive" name="active" <c:if test="${inter_user_attr.status != 'CMSTAT001'}">checked</c:if>></div>
                                                    <label class="col-md-2 control-label" >No</label>
                                                </div>
                                                <div class="col-xs-12 col-md-4 control-label">&nbsp;</div>
                                                <div class="col-md-3" style="padding-left: 0px;">
                                                    <span style="padding-left: 15px;" class="error-msg" name="errorMsg" id="error_active"></span>
                                                </div>
                                            </iais:row>
                                        </c:when>
                                    </c:choose>
                                    <iais:row>
                                        <div class="col-xs-12 col-md-4 control-label">
                                            <a align="left" class="back" href="#" onclick="cancel()"><em class="fa fa-angle-left"></em> Back</a></div>
                                        <div align="right" class="col-sm-7 col-md-6 col-xs-10">
                                            <button type="button" class="btn btn-secondary" onclick="javascript:doClearInfo()">Clear</button>
                                            <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">Save</button>
                                        </div>
                                    </iais:row>
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
    $(document).ready(function() {
        <c:if test="${fromMyinfo == 'Y'}" >
            $('#name').prop('readonly', true);
            $('#mobileNo').prop('readonly', true);
            $('#email').prop('readonly', true);
        </c:if>
    });

    function save() {
        showWaiting();
        $("#action").val("save");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }
    function cancel() {
        showWaiting();
        $("#action").val("cancel");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }

    function reLoadMyInfoTodo() {
        if(verifyTaken()){
        $("#action").val("getMyInfo");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
        }else {
            // To obtain authorization
            showWaiting();
            callAuthoriseApi();
        }
    }

    function doClearInfo(){
        $("#action").val("clearInfo");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }
</script>