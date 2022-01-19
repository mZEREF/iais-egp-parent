<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<%@include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="col-xs-12">
                            <div class="tab-gp dashboard-tab">
                                <%@include file="common/InnerNavTab.jsp" %>
                                <div class="tab-content">
                                    <div class="tab-pane fade in active" id="incidentInvest" role="tabpanel"
                                         style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                        <div class="panel panel-default">
                                            <div class="form-horizontal">
                                                <div class="container">
                                                    <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Incident
                                                        Investigation</h3></div>
                                                    <div class="col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                        <div class="row">
                                                            <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="backgroundInfo">Background
                                                                            information leading up to the
                                                                            incident</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="backgroundInfo"
                                                                               id="backgroundInfo" maxlength="500"
                                                                               value="${incidentInvest.backgroundInfo}">
                                                                        <span data-err-ind="backgroundInfo"
                                                                              class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentDesc"> Description of
                                                                            incident</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="incidentDesc"
                                                                               id="incidentDesc" maxlength="500"
                                                                               value="${incidentInvest.incidentDesc}">
                                                                        <span data-err-ind="incidentDesc"
                                                                              class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Cause(s) of the incident(including probable cause)</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding:7px 0;margin: 0;">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="policyCause" value="INCAUSE001"
                                                                                    <c:if test="${causeSet.contains('INCAUSE001')}"> checked="checked" </c:if>/>
                                                                            <label for="policyCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Deficiencies
                                                                                in policy and procedures</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="policyExp" <c:if test="${!causeSet.contains('INCAUSE001')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE001')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE001">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE001"
                                                                                   id="explainCause--v--INCAUSE001" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE001"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE001">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE001" id="measure--v--INCAUSE001"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE001"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE001">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE001" id="implementDate--v--INCAUSE001"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE001"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding:7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses" id="trainCause"
                                                                                   value="INCAUSE002" <c:if
                                                                                    test="${causeSet.contains('INCAUSE002')}"> checked="checked" </c:if>/>
                                                                            <label for="trainCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Deficiencies
                                                                                in training</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="trainExp" <c:if test="${!causeSet.contains('INCAUSE002')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE002')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE002">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE002"
                                                                                   id="explainCause--v--INCAUSE002" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE002"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE002">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE002" id="measure--v--INCAUSE002"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE002"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE002">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE002" id="implementDate--v--INCAUSE002"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE002"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses" id="equipCause"
                                                                                   value="INCAUSE003" <c:if
                                                                                    test="${causeSet.contains('INCAUSE003')}"> checked="checked" </c:if>/>
                                                                            <label for="equipCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Equipment
                                                                                fault/failure</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="equipExp" <c:if test="${!causeSet.contains('INCAUSE003')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE003')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE003">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE003"
                                                                                   id="explainCause--v--INCAUSE003" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE003"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE003">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE003" id="measure--v--INCAUSE003"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE003"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE003">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE003" id="implementDate--v--INCAUSE003"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE003"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="facilityCause" value="INCAUSE004"
                                                                                    <c:if test="${causeSet.contains('INCAUSE004')}"> checked="checked" </c:if>/>
                                                                            <label for="facilityCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Facility
                                                                                engineering fault/failure</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="facExp" <c:if test="${!causeSet.contains('INCAUSE004')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE004')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE004">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE004"
                                                                                   id="explainCause--v--INCAUSE004" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE004"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE004">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE004" id="measure--v--INCAUSE004"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE004"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE004">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE004" id="implementDate--v--INCAUSE004"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE004"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="personalCause" value="INCAUSE005"
                                                                                    <c:if test="${causeSet.contains('INCAUSE005')}"> checked="checked" </c:if>/>
                                                                            <label for="personalCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Issues
                                                                                with Personal Protective
                                                                                Equipment</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div  id="perExp" <c:if test="${!causeSet.contains('INCAUSE005')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE005')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE005">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE005"
                                                                                   id="explainCause--v--INCAUSE005" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE005"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE005">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE005" id="measure--v--INCAUSE005"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE005"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE005">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE005" id="implementDate--v--INCAUSE005"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE005"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="inadequateCause"
                                                                                   value="INCAUSE006" <c:if
                                                                                    test="${causeSet.contains('INCAUSE006')}"> checked="checked" </c:if>/>
                                                                            <label for="inadequateCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Inadequate
                                                                                equipment</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="inadequateExp" <c:if test="${!causeSet.contains('INCAUSE006')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE006')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE006">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE006"
                                                                                   id="explainCause--v--INCAUSE006" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE006"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE006">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE006" id="measure--v--INCAUSE006"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE006"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE006">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE006" id="implementDate--v--INCAUSE006"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE006"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="environmentCause"
                                                                                   value="INCAUSE007" <c:if
                                                                                    test="${causeSet.contains('INCAUSE007')}"> checked="checked" </c:if>/>
                                                                            <label for="environmentCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Environmental
                                                                                Factors</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="environmentExp" <c:if test="${!causeSet.contains('INCAUSE007')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE007')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE007">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE007"
                                                                                   id="explainCause--v--INCAUSE007" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE007"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE007">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE007" id="measure--v--INCAUSE007"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE007"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE007">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE007" id="implementDate--v--INCAUSE007"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE007"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="humanBeCause" value="INCAUSE008"
                                                                                    <c:if test="${causeSet.contains('INCAUSE008')}"> checked="checked" </c:if>/>
                                                                            <label for="humanBeCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Human
                                                                                Behavioural Factor(s)</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div id="humanBeExp" <c:if test="${!causeSet.contains('INCAUSE008')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE008')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE008">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE008"
                                                                                   id="explainCause--v--INCAUSE008" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE008"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE008">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE008" id="measure--v--INCAUSE008"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE008"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE008">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE008" id="implementDate--v--INCAUSE008"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE008"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-sm-5 control-label"></div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                            <input type="checkbox"
                                                                                   class="form-check-input"
                                                                                   name="incidentCauses"
                                                                                   id="performCause" value="INCAUSE009"
                                                                                    <c:if test="${causeSet.contains('INCAUSE009')}"> checked="checked" </c:if>/>
                                                                            <label for="performCause"
                                                                                   class="form-check-label"><span
                                                                                    class="check-square"></span>Human
                                                                                Performance Issues</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div  id="performExp" <c:if test="${!causeSet.contains('INCAUSE009')}">style="display: none"</c:if>>
                                                                    <c:set var="cause" value="${causeMap.get('INCAUSE009')}"/>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="explainCause--v--INCAUSE009">Explain the
                                                                                cause</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="explainCause--v--INCAUSE009"
                                                                                   id="explainCause--v--INCAUSE009" maxlength="50"
                                                                                   value="${cause.explainCause}">
                                                                            <span data-err-ind="explainCause--v--INCAUSE009"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="measure--v--INCAUSE009">Corrective measures and/or
                                                                                preventive measures to address the
                                                                                probable</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="measure--v--INCAUSE009" id="measure--v--INCAUSE009"
                                                                                   maxlength="400"
                                                                                   value="${cause.measure}">
                                                                            <span data-err-ind="measure--v--INCAUSE009"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="implementDate--v--INCAUSE009">Due date for
                                                                                implementation of corrective and/or
                                                                                preventive measures</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off"
                                                                                   name="implementDate--v--INCAUSE009" id="implementDate--v--INCAUSE009"
                                                                                   data-date-start-date="01/01/1900"
                                                                                   placeholder="dd/mm/yyyy" maxlength="10"
                                                                                   value="${cause.implementDate}"
                                                                                   class="date_picker form-control"/>
                                                                            <span data-err-ind="implementDate--v--INCAUSE009"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div style="margin-bottom: 15px">
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label"></div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <div class="form-check" style="padding: 7px 0 0 0;margin: 0">
                                                                                <input type="checkbox"
                                                                                       class="form-check-input"
                                                                                       name="incidentCauses"
                                                                                       id="othersCause" value="INCAUSE010"
                                                                                        <c:if test="${causeSet.contains('INCAUSE010')}"> checked="checked" </c:if>/>
                                                                                <label for="othersCause"
                                                                                       class="form-check-label"><span
                                                                                        class="check-square"></span>Others,
                                                                                    please specify</label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div id="otherExp" <c:if test="${!causeSet.contains('INCAUSE010')}">style="display: none"</c:if>>
                                                                        <c:set var="cause" value="${causeMap.get('INCAUSE010')}"/>
                                                                        <div class="row">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="otherCause">Cause(s) of the
                                                                                    Incident, Others</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-7">
                                                                                <input type="text" name="otherCause"
                                                                                       id="otherCause" maxlength="50"
                                                                                       value="${cause.otherCause}">
                                                                                <span data-err-ind="otherCause"
                                                                                      class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="explainCause--v--INCAUSE010">Explain the
                                                                                    cause</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-7">
                                                                                <input type="text" name="explainCause--v--INCAUSE010"
                                                                                       id="explainCause--v--INCAUSE010" maxlength="50"
                                                                                       value="${cause.explainCause}">
                                                                                <span data-err-ind="explainCause--v--INCAUSE010"
                                                                                      class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="measure--v--INCAUSE010">Corrective measures and/or
                                                                                    preventive measures to address the
                                                                                    probable</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-7">
                                                                                <input type="text" name="measure--v--INCAUSE010" id="measure--v--INCAUSE010"
                                                                                       maxlength="400"
                                                                                       value="${cause.measure}">
                                                                                <span data-err-ind="measure--v--INCAUSE010"
                                                                                      class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="implementDate--v--INCAUSE010">Due date for
                                                                                    implementation of corrective and/or
                                                                                    preventive measures</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-7">
                                                                                <input type="text" autocomplete="off"
                                                                                       name="implementDate--v--INCAUSE010" id="implementDate--v--INCAUSE010"
                                                                                       data-date-start-date="01/01/1900"
                                                                                       placeholder="dd/mm/yyyy" maxlength="10"
                                                                                       value="${cause.implementDate}"
                                                                                       class="date_picker form-control"/>
                                                                                <span data-err-ind="implementDate--v--INCAUSE010"
                                                                                      class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div style="margin-bottom: 15px">
                                                                    <div class="row">
                                                                        <div class="col-sm-5 control-label"></div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <span data-err-ind="incidentCause" class="error-msg"></span>
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
                                    <%@include file="common/InnerFooter.jsp" %>

                                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


