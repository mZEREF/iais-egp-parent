<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="valEntity" id="valEntity"
               value="com.ecquaria.cloud.moh.iais.dto.AduitSystemGenerateValidateDto"/>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="bg-title"><h2>Search HCI and Services</h2></div>
        <iais:section title="" id="potentialAuditableHCIs">
            <iais:row>
                <iais:field value="Service Name:"/>
                <iais:value width="8">
                    <iais:select name="svcName" options="activeHCIServiceNames" firstOption="Please Select"
                                 value=""/>
                    <span id="error_svcName" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Postal Code/Region:"/>
                <iais:value width="8">
                    <input type="number" name="postcode" id="postcode"  oninput="if(value.length>6)value=value.slice(0,6)" value="${auditSystemPotentialDtoForSearch.postalCode}" />
                    <span id="error_postcode" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Last Inspection done before(Start):"/>
                <iais:value width="8">
                    <iais:datePicker id="inspectionStartDate" name="inspectionStartDate" value="${auditSystemPotentialDtoForSearch.lastInspectionStart}"/>
                    <span id="error_inspectionStartDate" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Last Inspection done before(End):"/>
                <iais:value width="8">
                    <iais:datePicker id="inspectionEndDate" name="inspectionEndDate" value="${auditSystemPotentialDtoForSearch.lastInspectionEnd}"/>
                    <span id="error_inspectionEndDate" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Results of last compliance:"/>
                <iais:value width="8">
                    <iais:select name="complianceLastResult" options="complianceLastResultOptions" firstOption="Please Select"
                                 value="${auditSystemPotentialDtoForSearch.resultLastCompliance}"/>
                    <span id="error_complianceLastResult" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>


            <iais:row>
                <iais:field value="HSCA Service Code:"/>
                <iais:value width="8">
                    <iais:select name="hclSCode" options="activeHCIServiceCodes" firstOption="Please Select" value=""/>
                    <span id="error_hclSCode" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="HCI Code:"/>
                <iais:value width="8">
                    <iais:select name="hclCode" options="hclCodeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.hclCode}"/>
                    <span id="error_hclCode" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Premises Type:"/>
                <iais:value width="8">
                    <iais:select name="premType" options="premTypeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.premisesType}"/>
                    <span id="error_premType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Type of Risk:"/>
                <iais:value width="8">
                    <iais:select name="riskType" options="riskTypeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.typeOfRisk}"/>
                    <span id="error_riskType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

        </iais:section>

        <iais:action style="text-align:right;">
            <button class="btn btn-lg" id="clearbtn" type="button"
                    style="background:#2199E8; color: white" onclick="javascript:doClear();">
            Clear
            </button>
            <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Search
            </button>
        </iais:action>

        <%@include file="/include/validation.jsp" %>
    </form>
</div>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm", "next");
    }

    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "backToMenu");
    }

    function doClear() {
        $("#svcName").val("");
        $("#postcode").val("");
        $("#inspectionStartDate").val("");
        $("#inspectionEndDate").val("");
        $("#complianceLastResult").val("");
        $("#hclSCode").val("");
        $("#hclCode").val("");
        $("#premType").val("");
        $("#riskType").val("");
    }
</script>
