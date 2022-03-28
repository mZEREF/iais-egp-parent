<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<p class="assessment-title">Facility Classification</p>
<div class="form-check-gp">
    <div class="form-check">
        <input type="radio" class="form-check-input" name="facClassification" id="bsl3Radio" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3}">checked="checked"</c:if> value="${MasterCodeConstants.FAC_CLASSIFICATION_BSL3}"/>
        <label for="bsl3Radio" class="form-check-label"><span class="check-circle"></span>Certified High Containment (BSL-3) Facility</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="facClassification" id="bsl4Radio" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4}">checked="checked"</c:if> value="${MasterCodeConstants.FAC_CLASSIFICATION_BSL4}"/>
        <label for="bsl4Radio" class="form-check-label"><span class="check-circle"></span>Certified Maximum Containment (BSL-4) Facility</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="facClassification" id="ufRadio" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_UF}">checked="checked"</c:if> value="${MasterCodeConstants.FAC_CLASSIFICATION_UF}"/>
        <label for="ufRadio" class="form-check-label"><span class="check-circle"></span>Uncertified Facility</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="facClassification" id="lspfRadio" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_LSPF}">checked="checked"</c:if> value="${MasterCodeConstants.FAC_CLASSIFICATION_LSPF}"/>
        <label for="lspfRadio" class="form-check-label"><span class="check-circle"></span>Biomanufacturing (Large-Scale Production) Facility</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="facClassification" id="rfRadio" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_RF}">checked="checked"</c:if> value="${MasterCodeConstants.FAC_CLASSIFICATION_RF}"/>
        <label for="rfRadio" class="form-check-label"><span class="check-circle"></span>Registered Facility</label>
    </div>
</div>

<p id="activityTypeP" class="assessment-title" <c:if test="${!MasterCodeConstants.VALID_FAC_CLASSIFICATION.contains(serviceSelection.facClassification)}">style="display: none"</c:if>>Activity Type</p>
<div id="bsl3Types" <c:if test="${serviceSelection.facClassification ne MasterCodeConstants.FAC_CLASSIFICATION_BSL3}">style="display: none"</c:if>>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3fssba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}"/>
        <label for="bsl3fssba" class="form-check-label"><span class="check-square"></span>Possession of First and/or Second Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3lspfsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}"/>
        <label for="bsl3lspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3lsptsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}"/>
        <label for="bsl3lsptsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3pfst" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}"/>
        <label for="bsl3pfst" class="form-check-label"><span class="check-square"></span>Possession of Fifth Schedule Toxin</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3hnfspim" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL3 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}"/>
        <label for="bsl3hnfspim" class="form-check-label"><span class="check-square"></span>Handling of non-First Schedule Poliovirus Infectious Materials</label>
    </div>
</div>
<div id="bsl4Types" <c:if test="${serviceSelection.facClassification ne MasterCodeConstants.FAC_CLASSIFICATION_BSL4}">style="display: none"</c:if>>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4fssba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}"/>
        <label for="bsl4fssba" class="form-check-label"><span class="check-square"></span>Possession of First and/or Second Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4lspfsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}"/>
        <label for="bsl4lspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4lsptsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}"/>
        <label for="bsl4lsptsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4pfst" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}"/>
        <label for="bsl4pfst" class="form-check-label"><span class="check-square"></span>Possession of Fifth Schedule Toxin</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="bsl4ActviTypes" id="bsl4hnfspim" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_BSL4 and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}"/>
        <label for="bsl4hnfspim" class="form-check-label"><span class="check-square"></span>Handling of non-First Schedule Poliovirus Infectious Materials</label>
    </div>
</div>
<div id="ufTypes" <c:if test="${serviceSelection.facClassification ne MasterCodeConstants.FAC_CLASSIFICATION_UF}">style="display: none"</c:if>>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uffsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_UF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}"/>
        <label for="uffsba" class="form-check-label"><span class="check-square"></span>Possession of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uflspfsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_UF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}"/>
        <label for="uflspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uflsptsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_UF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}"/>
        <label for="uflsptsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="ufActviTypes" id="uffst" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_UF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}"/>
        <label for="uffst" class="form-check-label"><span class="check-square"></span>Possession of Fifth Schedule Toxin</label>
    </div>
</div>
<div id="lspfTypes" <c:if test="${serviceSelection.facClassification ne MasterCodeConstants.FAC_CLASSIFICATION_LSPF}">style="display: none"</c:if>>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspffsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_LSPF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}"/>
        <label for="lspffsba" class="form-check-label"><span class="check-square"></span>Possession of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspflspfsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_LSPF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}"/>
        <label for="lspflspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspflsptsba" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_LSPF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}"/>
        <label for="lspflsptsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of Third Schedule Biological Agent</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" name="lspfActviTypes" id="lspffst" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_LSPF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}"/>
        <label for="lspffst" class="form-check-label"><span class="check-square"></span>Possession of Fifth Schedule Toxin</label>
    </div>
</div>
<div id="rfTypes" <c:if test="${serviceSelection.facClassification ne MasterCodeConstants.FAC_CLASSIFICATION_RF}">style="display: none"</c:if>>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="rfActviTypes" id="rfhppim" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_RF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}"/>
        <label for="rfhppim" class="form-check-label"><span class="check-circle"></span>Handling of Poliovirus Potentially Infectious Materials</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" name="rfActviTypes" id="rfhfstep" <c:if test="${serviceSelection.facClassification eq MasterCodeConstants.FAC_CLASSIFICATION_RF and serviceSelection.activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)}">checked="checked"</c:if> value="${MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}"/>
        <label for="rfhfstep" class="form-check-label"><span class="check-circle"></span>Handling of Fifth Schedule Toxin for Exempted Purposes</label>
    </div>
</div>