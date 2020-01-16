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
                        <h1>Create HCSA Internet User Account</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="UEN:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="uenNo" id="uenNo" value="${uenNo}" needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="UIN:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="idNo" id="idNo"
                                                        value="${idNo}"></iais:input>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_NRICFIN"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Salutation:" width="11"/>
                                        <iais:value width="11">
                                            <iais:select name="salutation" id="salutation"
                                                         value=""
                                                         codeCategory="CATE_ID_SALUTATION" firstOption="Please Select"/>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_salutation"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="First Name:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="firstName" id="firstName"
                                                        value="${firstName}"></iais:input>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_fristName"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Last Name:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="lastName" id="lastName"
                                                        value="${lastName}"></iais:input>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_lastName"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email:" width="11"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="email" id="email" value="${email}"></iais:input>
                                            <div class="col-xs-12">
                                                <span class="error-msg" name="errorMsg" id="error_emailAddr"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Is Administrator:" width="11"/>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="admin" name="role" checked></div>
                                            <label class="col-md-2 control-label" >Yes</label>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role"></div>
                                            <label class="col-md-2 control-label" >No</label>
                                        </div>
                                    </iais:row>

                                    <iais:action>
                                        <button type="button" class="search btn" onclick="create()">Create</button>
                                    </iais:action>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function create() {
        SOP.Crud.cfxSubmit("mainForm");
    }

</script>