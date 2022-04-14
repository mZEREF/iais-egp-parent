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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp"%>
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
                                <%@include file="common/InnerNavTab.jsp"%>
                                <div class="tab-content">
                                    <div class="tab-pane fade in active" id="tabIncidentInfo" role="tabpanel" style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                        <div class="panel panel-default">
                                            <div class="form-horizontal">
                                                <div class="container">
                                                    <div class="component-gp col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                        <div class="row">
                                                            <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="referenceNo">Incident Reference No.</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="11" name="referenceNo" id="referenceNo" value="${incidentInfo.referenceNo}">
                                                                        <span data-err-ind="referenceNo" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentReporting">Reporting of Incident</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <select name="incidentReporting" class="incidentReportingDropdown" id="incidentReporting" style="overflow: hidden">
                                                                            <c:forEach var="item" items="${incidentReportOps}">
                                                                                <option value="${item.value}" <c:if test="${incidentInfo.incidentReporting eq item.value}">selected="selected"</c:if>>${item.text}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                        <span data-err-ind="incidentReporting" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label >Type of Incident(s)</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="form-check-gp">
                                                                            <div class="form-check">
                                                                                <input type="radio" class="form-check-input" name="incidentType" id="safeRadio" value="INDTYPE001" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001'}"> checked="checked" </c:if>/>
                                                                                <label for="safeRadio" class="form-check-label"><span class="check-circle"></span>Biosafety related incident or accident</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="radio" class="form-check-input" name="incidentType" id="securRadio" value="INDTYPE002" <c:if test="${incidentInfo.incidentType eq 'INDTYPE002'}"> checked="checked" </c:if>/>
                                                                                <label for="securRadio" class="form-check-label"><span class="check-circle"></span>Biosecurity related incident or accident</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="radio" class="form-check-input" name="incidentType" id="generRadio"  value="INDTYPE003" <c:if test="${incidentInfo.incidentType eq 'INDTYPE003'}"> checked="checked" </c:if>/>
                                                                                <label for="generRadio" class="form-check-label"><span class="check-circle"></span>General safety related incident or accident</label>
                                                                            </div>
                                                                            <span data-err-ind="incidentType" class="error-msg"></span>
                                                                        </div>

                                                                        <div id="safeTypes" <c:if test="${incidentInfo.incidentType ne 'INDTYPE001'}">style="display: none"</c:if>>
                                                                            <div class="form-check" >
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="suspectedInfection" value="INDTYPE004" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE004')}"> checked="checked" </c:if>/>
                                                                                <label for="suspectedInfection" class="form-check-label"><span class="check-square"></span>Suspected Infection</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="confirmedInfection" value="INDTYPE005" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE005')}"> checked="checked" </c:if>/>
                                                                                <label for="confirmedInfection" class="form-check-label"><span class="check-square"></span>Confirmed Infection</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="batSuspectedExp"  value="INDTYPE006" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE006')}"> checked="checked" </c:if>/>
                                                                                <label for="batSuspectedExp" class="form-check-label"><span class="check-square"></span>Suspected exposure to biological agent or toxin</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="batConfirmedExp"  value="INDTYPE007" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE007')}"> checked="checked" </c:if>/>
                                                                                <label for="batConfirmedExp" class="form-check-label"><span class="check-square"></span>Confirmed exposure to biological agent or toxin</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="batSpillage"  value="INDTYPE008" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE008')}"> checked="checked" </c:if>/>
                                                                                <label for="batSpillage" class="form-check-label"><span class="check-square"></span>Spillage involving biological agent or toxin that could have exposed personnel to infection/hazard</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="batRelease"  value="INDTYPE009" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE009')}"> checked="checked" </c:if>/>
                                                                                <label for="batRelease" class="form-check-label"><span class="check-square"></span>Release of biological agent or toxin into the environment</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSafeTypes" id="others"  value="INDTYPE010" <c:if test="${incidentInfo.incidentType eq 'INDTYPE001' and incidentInfo.childTypes.contains('INDTYPE010')}"> checked="checked" </c:if>/>
                                                                                <label for="others" class="form-check-label"><span class="check-square"></span>Others (please specify)</label>
                                                                            </div>
                                                                        </div>

                                                                        <div id="securTypes" <c:if test="${incidentInfo.incidentType ne 'INDTYPE002'}">style="display: none"</c:if>>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSecurTypes" id="batLoss"  value="INDTYPE011" <c:if test="${incidentInfo.incidentType eq 'INDTYPE002' and incidentInfo.childTypes.contains('INDTYPE011')}"> checked="checked" </c:if>/>
                                                                                <label for="batLoss" class="form-check-label"><span class="check-square"></span>Theft/Loss of biological agent or toxin</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSecurTypes" id="unaccountable"  value="INDTYPE012" <c:if test="${incidentInfo.incidentType eq 'INDTYPE002' and incidentInfo.childTypes.contains('INDTYPE012')}"> checked="checked" </c:if>/>
                                                                                <label for="unaccountable" class="form-check-label"><span class="check-square"></span>Unaccountable inventory</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSecurTypes" id="intrusion"  value="INDTYPE013" <c:if test="${incidentInfo.incidentType eq 'INDTYPE002' and incidentInfo.childTypes.contains('INDTYPE013')}"> checked="checked" </c:if>/>
                                                                                <label for="intrusion" class="form-check-label"><span class="check-square"></span>Intrusion/Security breach</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childSecurTypes" id="others1"  value="INDTYPE010" <c:if test="${incidentInfo.incidentType eq 'INDTYPE002' and incidentInfo.childTypes.contains('INDTYPE010')}"> checked="checked" </c:if>/>
                                                                                <label for="others1" class="form-check-label"><span class="check-square"></span>Others (please specify)</label>
                                                                            </div>
                                                                        </div>

                                                                        <div id="generTypes" <c:if test="${incidentInfo.incidentType ne 'INDTYPE003'}">style="display: none"</c:if>>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childGenerTypes" id="perInjury"  value="INDTYPE014" <c:if test="${incidentInfo.incidentType eq 'INDTYPE003' and incidentInfo.childTypes.contains('INDTYPE014')}"> checked="checked" </c:if>/>
                                                                                <label for="perInjury" class="form-check-label"><span class="check-square"></span>Personnel injury (with no potential for exposure to biological agent or toxin)</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childGenerTypes" id="fire"  value="INDTYPE015" <c:if test="${incidentInfo.incidentType eq 'INDTYPE003' and incidentInfo.childTypes.contains('INDTYPE015')}"> checked="checked" </c:if>/>
                                                                                <label for="fire" class="form-check-label"><span class="check-square"></span>Fire that resulted in personnel injury or property damage/label</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childGenerTypes" id="spill"  value="INDTYPE016" <c:if test="${incidentInfo.incidentType eq 'INDTYPE003' and incidentInfo.childTypes.contains('INDTYPE016')}"> checked="checked" </c:if>/>
                                                                                <label for="spill" class="form-check-label"><span class="check-square"></span>Spill of chemical or other hazardous (other than biological agents and toxins) that resulted in personnel injury or release of chemical to the environment</label>
                                                                            </div>
                                                                            <div class="form-check">
                                                                                <input type="checkbox" class="form-check-input" name="childGenerTypes" id="others2"  value="INDTYPE010" <c:if test="${incidentInfo.incidentType eq 'INDTYPE003' and incidentInfo.childTypes.contains('INDTYPE010')}"> checked="checked" </c:if>/>
                                                                                <label for="others2" class="form-check-label"><span class="check-square"></span>Others (please specify)</label>
                                                                            </div>
                                                                        </div>
                                                                        <span data-err-ind="childTypes" class="error-msg"></span>
                                                                    </div>

                                                                    <div id="otherDel" <c:if test="${!incidentInfo.childTypes.contains('INDTYPE010')}">style="display: none"</c:if>>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="typeOtherDetail">Details related to Other incident type</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-7">
                                                                                <input type="text" maxlength="400" name="typeOtherDetail" id="typeOtherDetail" value="${incidentInfo.typeOtherDetail}">
                                                                                <span data-err-ind="typeOtherDetail" class="error-msg"></span>
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
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="homeBack" href="/bsb-fe/eservice/INTERNET/MohBsbReportableEvents"><em class="fa fa-angle-left"></em> Back</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group" style="float: right">
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary next" id="next" >Next</a>
                                            </div>
                                        </div>
                                    </div>

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

