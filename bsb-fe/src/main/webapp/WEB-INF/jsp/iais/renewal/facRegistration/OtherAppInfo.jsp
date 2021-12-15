<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
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

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="otherInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="otherInfoPanel" role="tabpanel">
                                    <div class="panel panel-default">
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <div class="panel-heading" role="tab">
                                            <h4 class="panel-title">Declaration</h4>
                                        </div>
                                        <div class="panel-collapse">
                                            <div class="panel-body">
                                                <h4>I, hereby declare the following:</h4>
                                                <div>
                                                    <div class="col-xs-12 form-group">
                                                        <span>The Facility Operator and the Facility Operator Designee are informed of their responsibilities as Facility Operator under the Biological Agents and Toxins Act.</span>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="facOpDeInformedResponsibilities" id="facOpDeInformedResponsibilitiesYes" value="Y" <c:if test="${otherInfo.facOpDeInformedResponsibilities eq 'Y'}">checked="checked"</c:if>>
                                                        <label for="facOpDeInformedResponsibilitiesYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="facOpDeInformedResponsibilities" id="facOpDeInformedResponsibilitiesNo" value="N" <c:if test="${otherInfo.facOpDeInformedResponsibilities eq 'N'}">checked="checked"</c:if>>
                                                        <label for="facOpDeInformedResponsibilitiesNo" class="form-check-label"><span class="check-circle"></span>No</label>
                                                    </div>
                                                    <span data-err-ind="facOpDeInformedResponsibilities" class="error-msg col-xs-12 form-group"></span>
                                                </div>
                                                <div>
                                                    <div class="col-xs-12 form-group">
                                                        <span>The Biosafety Committee is informed of its responsibilities stipulated under the Biological Agents and Toxins Act.</span>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="facCommitteeInformedResponsibilities" id="facCommitteeInformedResponsibilitiesYes" value="Y" <c:if test="${otherInfo.facCommitteeInformedResponsibilities eq 'Y'}">checked="checked"</c:if>>
                                                        <label for="facCommitteeInformedResponsibilitiesYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="facCommitteeInformedResponsibilities" id="facCommitteeInformedResponsibilitiesNo" value="N" <c:if test="${otherInfo.facCommitteeInformedResponsibilities eq 'N'}">checked="checked"</c:if>>
                                                        <label for="facCommitteeInformedResponsibilitiesNo" class="form-check-label"><span class="check-circle"></span>No</label>
                                                    </div>
                                                    <span data-err-ind="facCommitteeInformedResponsibilities" class="error-msg col-xs-12 form-group"></span>
                                                </div>
                                                <div>
                                                    <div class="col-xs-12 form-group">
                                                        <span>The facility will develop/has a biorisk management system in place for managing biosafety, biosecurity and dual-use risks, that minimally encompasses the following:</span>
                                                        <br/><span>- Institutional governance and oversight</span>
                                                        <br/><span>- Risk assessment and risk management programme</span>
                                                        <br/><span>- Hazardous waste management for solid and liquid waste</span>
                                                        <br/><span>- Emergency response plan, preparedness and readiness</span>
                                                        <br/><span>- Material management and accountability (including storage, inventory management and inventory audits)</span>
                                                        <br/><span>- Incident investigation and reporting</span>
                                                        <br/><span>- Occupational health and medical surveillance programme</span>
                                                        <br/><span>- Personal protective equipment</span>
                                                        <br/><span>- Personnel management including recruitment, authorization of access to the facility and materials, training (including refresher training) and competency assessments</span>
                                                        <br/><span>- Policy on sharps use, if applicable</span>
                                                        <br/><span>- Policy and procedures for transfer and/or transportation of biological agent/toxin</span>
                                                        <br/><span>- Compliance to applicable legislations and regulations (please indicate where applicable)</span>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="bioRiskManagementDeclare" id="bioRiskManagementDeclareYes" value="Y" <c:if test="${otherInfo.bioRiskManagementDeclare eq 'Y'}">checked="checked"</c:if>>
                                                        <label for="bioRiskManagementDeclareYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="bioRiskManagementDeclare" id="bioRiskManagementDeclareNo" value="N" <c:if test="${otherInfo.bioRiskManagementDeclare eq 'N'}">checked="checked"</c:if>>
                                                        <label for="bioRiskManagementDeclareNo" class="form-check-label"><span class="check-circle"></span>No</label>
                                                    </div>
                                                    <span data-err-ind="bioRiskManagementDeclare" class="error-msg col-xs-12 form-group"></span>
                                                </div>
                                                <div>
                                                    <div class="col-xs-12 form-group">
                                                        <span>All information provided by me in this application is true and accurate.</span>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="infoAuthenticatedDeclare" id="infoAuthenticatedDeclareYes" value="Y" <c:if test="${otherInfo.infoAuthenticatedDeclare eq 'Y'}">checked="checked"</c:if>>
                                                        <label for="infoAuthenticatedDeclareYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                    </div>
                                                    <div class="form-check col-xs-3">
                                                        <input class="form-check-input" type="radio" name="infoAuthenticatedDeclare" id="infoAuthenticatedDeclareNo" value="N" <c:if test="${otherInfo.infoAuthenticatedDeclare eq 'N'}">checked="checked"</c:if>>
                                                        <label for="infoAuthenticatedDeclareNo" class="form-check-label"><span class="check-circle"></span>No</label>
                                                    </div>
                                                    <span data-err-ind="infoAuthenticatedDeclare" class="error-msg col-xs-12 form-group"></span>
                                                </div>
                                                <div>
                                                    <div class="col-xs-12 form-group">
                                                        <p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. Some of these may not be available at point of application submission but must be provided subsequently, when available. Please note that incomplete submissions may result in delays to processing or rejection of the application.</p>
                                                        <span>Supporting Documents</span>
                                                        <br/><span>1. Facility Administrative Oversight Plan.
                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Information regarding the Facility Administrative Oversight Plan can be found on the <a href='https://www.moh.gov.sg/biosafety' style='text-decoration: none'>MOH Biosafety website</a></p>">i</a>
                                                        </span>
                                                        <br/><span>2. Documentation of successful facility certification.</span>
                                                        <br/><span>3. Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</span>
                                                        <br/><span>4. Documentation of approval from relevant ministry or statutory board, where applicable.</span>
                                                        <br/><span>5. Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>