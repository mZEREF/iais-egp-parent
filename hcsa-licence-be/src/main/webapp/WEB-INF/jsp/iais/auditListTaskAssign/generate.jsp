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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="valEntity" id="valEntity"
               value="com.ecquaria.cloud.moh.iais.dto.AduitSystemGenerateValidateDto"/>
        <div class="bg-title"><h2>Risk Score Module</h2></div>
        <iais:section title="" id="potentialAuditableHCIs">
            <div hidden="hidden">
            <iais:row>
                <iais:field value="Role"/>
                <iais:value width="8">
                    <iais:select name="roleIdsForAuditSelect" options="roleIdsForAudit"  value="${roleIdsForAuditSelect}"/>
                </iais:value>
            </iais:row>
            </div>
        <div id="clearFiterForSearch" name="clearFiterForSearch">
            <iais:row>
            <iais:field value="Service Name"/>
                <iais:value width="8">
                    <iais:select name="svcName" options="activeHCIServiceNames" multiSelect="true" multiValues="${auditSystemPotentialDtoForSearch.svcNameSelectList}" needSort="true"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Postal Code"/>
                <iais:value width="8">
                    <input type="text" name="postcode" id="postcode"  oninput="if(value.length>6)value=value.slice(0,6)" value="${auditSystemPotentialDtoForSearch.postalCode}" />
                    <span id="error_postcode" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Last Inspection done before(Start)"/>
                <iais:value width="8">
                    <iais:datePicker id="inspectionStartDate" name="inspectionStartDate" value="${auditSystemPotentialDtoForSearch.lastInspectionStart}" needErrorSpan="false"/>
                    <span id="error_inspectionStartDate" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Last Inspection done before(End)"/>
                <iais:value width="8">
                    <iais:datePicker id="inspectionEndDate" name="inspectionEndDate" value="${auditSystemPotentialDtoForSearch.lastInspectionEnd}"/>
                    <span id="error_inspectionEndDate" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Results of last compliance"/>
                <iais:value width="8">
                    <iais:select name="complianceLastResult" options="complianceLastResultOptions" firstOption="Please Select"
                                 value="${auditSystemPotentialDtoForSearch.resultLastCompliance}"/>
                    <span id="error_complianceLastResult" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>


            <iais:row>
                <iais:field value="HSCA Service Code"/>
                <iais:value width="8">
                    <iais:select name="hclSCode" options="activeHCIServiceCodes" multiSelect="true" multiValues="${auditSystemPotentialDtoForSearch.svcNameCodeSelectList}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="HCI Code"/>
                <iais:value width="8">
                    <iais:select name="hclCode" options="hclCodeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.hclCode}"/>
                    <span id="error_hclCode" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Type of Risk"/>
                <iais:value width="8">
                    <iais:select name="riskType" options="riskTypeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.typeOfRisk}"/>
                    <span id="error_riskType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Mode of Service Delivery"/>
                <iais:value width="8">
                    <iais:select name="premType" options="premTypeOp" firstOption="Please Select" value="${auditSystemPotentialDtoForSearch.premisesType}"/>
                    <span id="error_premType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="How many candidates to generate"/>
                <iais:value width="8">
                    <input type="text" name="genNum" id="genNum"  oninput="if(value.length>3)value=value.slice(0,3)" value="${auditSystemPotentialDtoForSearch.generateNumString}"/>
                    <span id="error_genNum" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

        </div>
        </iais:section>
        <iais:action style="text-align:right;">
            <button class="btn btn-secondary" id="clearbtn" type="button"
                    onclick="javascript:doClear();">
            Clear
            </button>
            <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Generate Audit
                List
            </button>
        </iais:action>

        <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
    </form>
</div>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "next");
    }

    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "backToMenu");
    }

    function doClear() {
        $("#svcNameClear label input[type='checkbox']").prop('checked',false);
        $("#postcode").val("");
        $("#inspectionStartDate").val("");
        $("#inspectionEndDate").val("");
        clearSelectFiled('complianceLastResult');
        $("#hclSCodeClear label input[type='checkbox']").prop('checked',false);
        clearSelectFiled('hclCode');
        clearSelectFiled('premType');
        clearSelectFiled('riskType');
        $("#clearFiterForSearch .current").text("Please Select");
        $("#genNum").val("");
        $("#error_inspectionStartDate").html("");
        $("#clearFiterForSearch input[type='checkbox']").prop('checked', false);
        $("#clearFiterForSearch .multi-select-button").html("-- Select --");
        $("#svcName option").prop('selected',false);
        $("#hclSCode option").prop('selected',false);
    }
    function  clearSelectFiled(id) {
        var id = "#"+id;
        $(id+" option[text = 'Please Select']").val("selected", "selected");
        $(id+" option").val("");
    }
</script>
