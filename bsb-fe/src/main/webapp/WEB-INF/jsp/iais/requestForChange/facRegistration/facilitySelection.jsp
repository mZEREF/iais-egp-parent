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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-afc-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8" style="margin-top: -45px">
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <h3>
                        Select the Facility & Activity Type(s) for which you wish to make this application
                    </h3>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <span data-err-ind="facClassification" class="error-msg"></span>
                    <br/>
                    <span data-err-ind="activityTypes" class="error-msg"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <%--@elvariable id="serviceSelection" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto"--%>
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                        <div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                        <p class="assessment-title">Facility Classification</p>
                        <div class="form-check-gp">
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="facClassification" id="bsl3Radio" <c:if test="${serviceSelection.facClassification eq 'FACCLA001'}">checked="checked"</c:if> value="FACCLA001"/>
                                <label for="bsl3Radio" class="form-check-label"><span class="check-square"></span>Certified High Containment (BSL-3) Facility</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="facClassification" id="bsl4Radio" <c:if test="${serviceSelection.facClassification eq 'FACCLA002'}">checked="checked"</c:if> value="FACCLA002"/>
                                <label for="bsl4Radio" class="form-check-label"><span class="check-square"></span>Certified Maximum Containment (BSL-4) Facility</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="facClassification" id="ufRadio" <c:if test="${serviceSelection.facClassification eq 'FACCLA003'}">checked="checked"</c:if> value="FACCLA003"/>
                                <label for="ufRadio" class="form-check-label"><span class="check-square"></span>Uncertified Facility</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="facClassification" id="lspfRadio" <c:if test="${serviceSelection.facClassification eq 'FACCLA004'}">checked="checked"</c:if> value="FACCLA004"/>
                                <label for="lspfRadio" class="form-check-label"><span class="check-square"></span>Biomanufacturing (Large-Scale Production) Facility</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="facClassification" id="rfRadio" <c:if test="${serviceSelection.facClassification eq 'FACCLA005'}">checked="checked"</c:if> value="FACCLA005"/>
                                <label for="rfRadio" class="form-check-label"><span class="check-square"></span>Registered Facility</label>
                            </div>
                        </div>
                        <p class="assessment-title">Activity Type</p>
                        <div id="bsl3Types" <c:if test="${serviceSelection.facClassification ne 'FACCLA001'}">style="display: none"</c:if>>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3fssba" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY001')}">checked="checked"</c:if> value="ACTVITY001"/>
                                <label for="bsl3fssba" class="form-check-label"><span class="check-square"></span>First and/or Second Schedule Biological Agent</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3lspfsba" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY005')}">checked="checked"</c:if> value="ACTVITY005"/>
                                <label for="bsl3lspfsba" class="form-check-label"><span class="check-square"></span>Large-Scale Production of First Schedule Biological Agent</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3hfst" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY007')}">checked="checked"</c:if> value="ACTVITY007"/>
                                <label for="bsl3hfst" class="form-check-label"><span class="check-square"></span>Handling of Fifth Schedule Toxin</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="bsl3ActviTypes" id="bsl3hpvim" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY009')}">checked="checked"</c:if> value="ACTVITY009"/>
                                <label for="bsl3hpvim" class="form-check-label"><span class="check-square"></span>Handling of Poliovirus Infectious Materials</label>
                            </div>
                        </div>
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
                        <div id="rfTypes" <c:if test="${serviceSelection.facClassification ne 'FACCLA005'}">style="display: none"</c:if>>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="rfActviTypes" id="rfhfste" <c:if test="${serviceSelection.facClassification eq 'FACCLA005' and serviceSelection.activityTypes.contains('ACTVITY008')}">checked="checked"</c:if> value="ACTVITY008"/>
                                <label for="rfhfste" class="form-check-label"><span class="check-square"></span>Handling of Fifth Schedule Toxin for Exempted Purposes</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="rfActviTypes" id="rfhpvpim" <c:if test="${serviceSelection.facClassification eq 'FACCLA005' and serviceSelection.activityTypes.contains('ACTVITY010')}">checked="checked"</c:if> value="ACTVITY010"/>
                                <label for="rfhpvpim" class="form-check-label"><span class="check-square"></span>Handling of Poliovirus Potentially Infectious Materials</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="../common/InnerFooter.jsp" %>
        </div>
    </div>
</form>