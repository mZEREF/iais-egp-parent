<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="prelogin-title">
                <h1>Laboratory Developed Test</h1>
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
                                        <iais:field value="Name of Laboratory" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="Salutation" name="hciCode" id="hciCode"  options="personnelOptions" firstOption="Please Select" value="${laboratoryDevelopTestDto.hciCode}" disabled="disabled"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of LDT Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="ldtTestName" id="ldtTestName"  maxlength="50" value="${laboratoryDevelopTestDto.ldtTestName}" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Intended Purpose of Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <textarea id="intendedPurpose" style="width: 100%;margin-bottom: 15px;" rows="6" name="intendedPurpose"
                                                      maxlength="500" disabled>${laboratoryDevelopTestDto.intendedPurpose}</textarea>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Date LDT was made or will be made available" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:datePicker disabled ="true"  id="ldtDate" name="ldtDate" dateVal="${laboratoryDevelopTestDto.ldtDate}"></iais:datePicker>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Person responsible for the test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="responsePerson" id="responsePerson"  value="${laboratoryDevelopTestDto.responsePerson}" disabled maxlength="66"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="designation" id="designation" value="${laboratoryDevelopTestDto.designation}" maxlength="20" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Status of Test" width="11" required="true"/>
                                        <iais:value width="5">
                                            <input class="form-check-input " id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '1'}"> checked="checked"</c:if> aria-invalid="false" value="1" disabled> Active
                                        </iais:value>
                                        <iais:value width="5">
                                            <input class="form-check-input " id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '0'}"> checked="checked"</c:if> aria-invalid="false" value="0" disabled> Inactive
                                        </iais:value>
                                        <iais:value width="11" style="padding-top:12px">
                                        </iais:value>

                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6" name="remarks"
                                                      maxlength="300" disabled>${laboratoryDevelopTestDto.remarks}</textarea>
                                        </iais:value>
                                    </iais:row>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-2 col-md-2">
                                                <a   style="padding-left: 90px;" align="left" class="back" href="/main-web/eservice/INTERNET/MohAccessmentGuide"><em class="fa fa-angle-left"></em> Back</a></div>
                                            <div class="text-right col-xs-9 col-md-9">
                                                <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">Submit</button>
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
<style>
    .mandatory {
        color: rgb(255, 0, 0);
    }

    .prelogin-title{
        padding-left: 90px;
    }

</style>