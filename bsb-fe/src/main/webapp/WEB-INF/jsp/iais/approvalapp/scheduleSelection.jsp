<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../approval/common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <tr height="1">
        <td class="first last" style="">
            <div class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label class="control-label control-set-font control-font-label">Facility Name</label>
                        <span class="mandatory">*</span>
                    </div>
                    <div class="col-sm-4 col-md-7 control-font-label">
                        <input type="hidden" id="facilityName" name="facilityName" value="${approvalApplicationDto.facilityName}">
                        <iais:select name="facilityId" id="facilityId" disabled="false" options="facilityNameSelect" firstOption="Please Select" value="${approvalApplicationDto.facilityId}"></iais:select>
                        <span id="error_facilityId" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last">
            <div class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label class="control-label control-set-font control-font-label">Schedule</label>
                        <span class="mandatory">*</span>
                    </div>
                    <div class="col-sm-4 col-md-7 control-font-label">
                        <iais:select id="schedule" name="schedule" disabled="false" codeCategory="CATE_ID_BSB_SCHEDULE_TYPE" firstOption="Please Select" value="${approvalApplicationDto.schedule}"></iais:select>
                        <span id="error_schedule" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </td>
    </tr>

        <div class="row">
            <div class="col-xs-12 col-md-10">
                <div class="self-assessment-checkbox-gp gradient-light-grey">
                    <div id="bsl4Types" <c:if test="${serviceSelection.facClassification ne 'FACCLA002'}">style="display: none"</c:if>>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4fssba" <c:if test="${serviceSelection.facClassification eq 'FACCLA002' and serviceSelection.activityTypes.contains('ACTVITY001')}">checked="checked"</c:if> value="ACTVITY001"/>
                            <label for="bsl4fssba" class="form-check-label"><span class="check-square"></span>First and/or Second Schedule Biological Agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4lspfsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA002' and serviceSelection.activityTypes.contains('ACTVITY005')}">checked="checked"</c:if> value="ACTVITY005"/>
                            <label for="bsl4lspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4fst" <c:if test="${serviceSelection.facClassification eq 'FACCLA002' and serviceSelection.activityTypes.contains('ACTVITY003')}">checked="checked"</c:if> value="ACTVITY003"/>
                            <label for="bsl4fst" class="form-check-label"><span class="check-square"></span>Fifth Schedule Toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4pvim" <c:if test="${serviceSelection.facClassification eq 'FACCLA002' and serviceSelection.activityTypes.contains('ACTVITY004')}">checked="checked"</c:if> value="ACTVITY004"/>
                            <label for="bsl4pvim" class="form-check-label"><span class="check-square"></span>Poliovirus Infectious Materials</label>
                        </div>
                    </div>
                    <div id="ufTypes" <c:if test="${serviceSelection.facClassification ne 'FACCLA003'}">style="display: none"</c:if>>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uffsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA003' and serviceSelection.activityTypes.contains('ACTVITY002')}">checked="checked"</c:if> value="ACTVITY002"/>
                            <label for="uffsba" class="form-check-label"><span class="check-square"></span>First Schedule Biological Agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uflsptsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA003' and serviceSelection.activityTypes.contains('ACTVITY006')}">checked="checked"</c:if> value="ACTVITY006"/>
                            <label for="uflsptsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uffst" <c:if test="${serviceSelection.facClassification eq 'FACCLA003' and serviceSelection.activityTypes.contains('ACTVITY003')}">checked="checked"</c:if> value="ACTVITY003"/>
                            <label for="uffst" class="form-check-label"><span class="check-square"></span>Fifth Schedule Toxin</label>
                        </div>
                    </div>
                    <div id="lspfTypes" <c:if test="${serviceSelection.facClassification ne 'FACCLA004'}">style="display: none"</c:if>>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspffsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA004' and serviceSelection.activityTypes.contains('ACTVITY005')}">checked="checked"</c:if> value="ACTVITY005"/>
                            <label for="lspffsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspftsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA004' and serviceSelection.activityTypes.contains('ACTVITY006')}">checked="checked"</c:if> value="ACTVITY006"/>
                            <label for="lspftsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--<%@ include file="InnerFooter.jsp" %>--%>
        </div>
    </div>
</form>