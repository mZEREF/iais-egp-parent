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
                                    <iais:row>
                                        <iais:field value="Name:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="name" id="name" value="${user.name}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_name"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Salutation:" width="11"/>
                                        <iais:value width="11">
                                            <iais:select name="salutation" id="salutation" value=""
                                                         codeCategory="CATE_ID_SALUTATION" firstOption="${salutationDesc}"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID Type:" width="11"/>
                                        <iais:value width="11">
                                            <iais:select name="idType" id="idType" options="mcStatusSelectList"
                                                         firstOption="${idTypeSelect}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_NRICFIN"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="idNo" id="idNo" value="${user.idNo}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_idNo"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="designation" id="designation" value="${user.designation}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_designation"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="mobileNo" id="mobileNo" value="${user.mobileNo}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_mobileNo"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office/Telephone No:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="officeNo" id="officeNo" value="${user.officeNo}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_officeTelNo"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="email" id="email" value="${user.emailAddr}"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_email"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:action>
                                        <button type="button" class="search btn" onclick="save();">Save</button>
                                    </iais:action>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input hidden name="firstsolutationvalue" value="${sulationSelect}">
    <input hidden name="firstidType" value="${idTypeSelect}">
    <input hidden name="id" value="${user.id}">
    <input hidden name="orgId" value="${user.orgId}">
    <input hidden name="userId" value="${user.userId}">
    <input hidden name="firstName" value="${user.firstName}">
    <input hidden name="lastName" value="${user.lastName}">
</form>
<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function save() {
        SOP.Crud.cfxSubmit("mainForm");
    }

</script>