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
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <div class="bg-title"><h2>Manual Audit List Creation Search</h2></div>
        <iais:section title="" id="potentialAuditableHCIs">
        <div hidden="hidden">
            <iais:row>
                <iais:field value="Role"/>
                <iais:value width="8">
                    <iais:select name="createForAudit" options="createIdsForAudit"  value="${createIdsForAuditSelect}"/>
                </iais:value>
            </iais:row>
        </div>
            <div id="clearFiterForSearch" name="clearFiterForSearch">
            <iais:row>
                <iais:field value="Facility Name"/>
                <iais:value width="8">
                    <select  name="facilityName" options="facilityNameOp" firstOption="Please Select" value="${auditCreationQueryDto.facilityName}">
                        <option>Please Select</option>
                        <option>Facility Name1</option>
                        <option>Facility Name2</option>
                        <option>Facility Name3</option>
                    </select>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Facility Classification"/>
                <iais:value width="8">
                    <select  name="facilityClassification" options="facilityClassificationOp" firstOption="Please Select" value="${auditCreationQueryDto.facilityClassification}">
                        <option>Please Select</option>
                        <option>Certified Facility</option>
                        <option>Uncertified Facility</option>
                    </select>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Facility Type"/>
                <iais:value width="8">
                    <select name="facilityType" options="facilityTypeOp" firstOption="Please Select" value="${auditCreationQueryDto.typeOfRisk}">
                        <option>Please Select</option>
                        <option>Maximum Containment Facility (MCF)</option>
                        <option>High Containment Facility (HCF)</option>
                        <option>PV Essential Facility (PEF)</option>
                        <option>First Schedule Facility (FSF)</option>
                        <option>Fifth Schedule Facility or Toxin Facility (TF)</option>
                        <option>Large Scale Production Facility (LSPF)</option>
                    </select>
                    <span id="error_riskType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Audit Type" required="true"/>
                <iais:value width="8">
                    <select id="at" name="auditType" options="auditTypeOp" firstOption="Please Select" value="${auditCreationQueryDto.premisesType}">
                        <option>Please Select</option>
                        <option>Audit by MOH</option>
                        <option>Facility self-audit</option>
                        <option>Not required</option>
                    </select>
                    <span id="error_premType" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            </div>
        </iais:section>

        <iais:action style="text-align:right;">
            <button class="btn btn-secondary" id="clearbtn" type="button"
                   onclick="javascript:doClear();">
            Clear
            </button>
            <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Search
            </button>
            <br/>
            <br/>
            <br/>
            <br/>
        </iais:action>

        <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
    </form>
</div>
<script type="text/javascript">
    function doNext() {
        if(document.getElementById("at").selectedIndex == 0){
            $("#error_premType").text("Please Select One!");
            return false;
        }else {
            SOP.Crud.cfxSubmit("mainForm", "doSearch");
        }
    }

    function doClear() {
        clearSelectFiled('facilityClassification');
        clearSelectFiled('auditType');
        // clearSelectFiled('facilityType');
        $("#clearFiterForSearch .current").text("Please Select");
        $("#clearFiterForSearch input[type='checkbox']").prop('checked', false);
        $("#clearFiterForSearch .multi-select-button").html("-- Select --");
        $("#facilityName option").prop('selected',false);
        $("#facilityType option").prop('selected',false);
        $("#error_premType").html("");
    }

    function  clearSelectFiled(id) {
           var id = "#"+id;
            $(id+" option[text = 'Please Select']").val("selected", "selected");
            $(id+" option").val("");
    }
</script>
