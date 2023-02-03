﻿<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
    String webrootBe=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>

<link href="<%=webrootBe%>css/rightpanelstyle.css" rel="stylesheet"  >
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="base_action_type" id="base_action_type"/>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Assisted Reproduction Enquiry</h2>
                        </div>
                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <iais:field width="4" value="SEARCH BY" />
                                <div class="col-md-8 row">
                                    <iais:value width="4" cssClass="col-md-4 row">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="radio"
                                                   name="searchBy"
                                                   value="1"
                                                   id="searchByPatient"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='1' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="searchByPatient"><span
                                                    class="check-circle"></span>Patient Information</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio"
                                                   name="searchBy" value="0" id="searchBySubmission"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.searchBy == '0'}">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="searchBySubmission"><span
                                                    class="check-circle"></span>Submission ID</label>
                                        </div>
                                    </iais:value>
                                </div>
                            </iais:row>

                            <hr>

                            <iais:row>
                                <iais:field width="4" value="AR Centre" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="arCentre" id="arCentre" firstOption="Please Select" options="arCentreSelectOption" cssClass="clearSel"
                                                 value="${assistedReproductionEnquiryFilterDto.arCentre}"  />
                                </iais:value>
                            </iais:row>
                            <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='1' }">
                                <div id="patientInformationFilter" >
                                    <iais:row>
                                        <iais:field width="4" value="Patient Name"/>
                                        <iais:value width="4" cssClass="col-md-4" >
                                            <input type="text" maxlength="66" id="patientName"  name="patientName" value="${assistedReproductionEnquiryFilterDto.patientName}" >
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field width="4" value="Patient ID Type"/>
                                        <div class="col-md-4 multi-select col-xs-4">
                                            <iais:select cssClass="clearMultiSel" name="patientIdTypeList"  multiValues="${assistedReproductionEnquiryFilterDto.patientIdTypeList}" codeCategory="CATE_ID_DS_ID_TYPE_DTV"  multiSelect="true"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field width="4" value="Patient ID No."/>
                                        <iais:value width="4" cssClass="col-md-4"  >
                                            <input type="text" maxlength="20" id="patientIdNumber"  name="patientIdNumber" value="${assistedReproductionEnquiryFilterDto.patientIdNumber}" >
                                        </iais:value>
                                    </iais:row>

                                </div>
                            </c:if>

                            <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='0'}">
                                <div id="submissionFilter" >
                                    <iais:row>
                                        <iais:field width="4" value="Submission ID"/>
                                        <iais:value width="4" cssClass="col-md-4" display="true" >
                                            <input type="text" maxlength="20" id="submissionId"  name="submissionId" value="${assistedReproductionEnquiryFilterDto.submissionId}" >
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field width="4" value="Submission Type"/>
                                        <iais:value width="4" cssClass="col-md-4">
                                            <iais:select name="submissionType" id="submissionType" firstOption="Please Select" options="submissionTypeOptions"
                                                         value="${assistedReproductionEnquiryFilterDto.submissionType}" cssClass="clearSel" />
                                        </iais:value>
                                        <iais:value width="4" cssClass="col-md-4">
                                            <div id="cycleStageDisplay" <c:if test="${assistedReproductionEnquiryFilterDto.submissionType!='AR_TP002'}">style="display: none"</c:if> >
                                                <iais:select name="cycleStage" id="cycleStage" firstOption="Please Select" options="stageTypeSelectOption" needSort="true"
                                                             value="${assistedReproductionEnquiryFilterDto.cycleStage}" cssClass="clearSel" />
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field width="4" value="Submission Date Range"/>
                                        <iais:value width="4" cssClass="col-md-4">
                                            <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateFrom}"/>
                                        </iais:value>
                                        <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                        <iais:value width="3" cssClass="col-md-3">
                                            <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateTo}"/>
                                        </iais:value>
                                    </iais:row>

                                </div>
                            </c:if>


                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doAdvancedSearch();">Advanced Search
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>

                    <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='1' }">
                        <div id="patientResultDisplay" >
                                <div class="components">

                                    <iais:pagination param="patientParam" result="patientResult"/>
                                    <div class="table-responsive">
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                                <thead>
                                                <tr >

                                                    <iais:sortableHeader needSort="true"
                                                                         field="NAME"
                                                                         value="Patient Name"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="ID_TYPE_DESC"
                                                                         value="Patient ID Type"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="ID_NUMBER"
                                                                         value="Patient ID No."/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="DATE_OF_BIRTH"
                                                                         value="Patient Date of Birth"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="NATIONALITY_DESC"
                                                                         value="Patient Nationality"/>

                                                    <iais:sortableHeader needSort="false"
                                                                         field=""
                                                                         value="Action"/>
                                                </tr>
                                                </thead>
                                                <tbody class="form-horizontal">
                                                <c:choose>
                                                    <c:when test="${empty patientResult.rows}">
                                                        <tr>
                                                            <td colspan="15">
                                                                <iais:message key="GENERAL_ACK018"
                                                                              escape="true"/>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <style>
                                                            .form-horizontal p {
                                                                line-height: 23px;
                                                            }
                                                        </style>
                                                        <c:forEach var="patient"
                                                                   items="${patientResult.rows}"
                                                                   varStatus="status">
                                                            <tr id="advfilter${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}">

                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Patient Name</p>
                                                                    <p style="white-space: nowrap;"><c:out value="${patient.patientName}"/>
                                                                        <c:if test="${not empty patient.cdPatientCode}">
                                                                            <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}" onclick="getPatientByPatientCode('${patient.patientCode}','${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}')">
                                                                            </a>
                                                                        </c:if>
                                                                    </p>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Patient ID Type</p>
                                                                    <iais:code code="${patient.patientIdType}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Patient ID No.</p>
                                                                    <c:out value="${patient.patientIdNo}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Patient Date of Birth</p>
                                                                    <fmt:formatDate
                                                                            value="${patient.patientDateBirth}"
                                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Patient Nationality</p>
                                                                    <iais:code code="${patient.patientNationality}"/>
                                                                </td>

                                                                <td >
                                                                    <p class="visible-xs visible-sm table-row-title">Action</p>
                                                                    <button type="button" onclick="quickView('${patient.patientCode}')"   data-panel="main" class=" btn btn-sm cd-btn js-cd-panel-trigger">
                                                                        Quick View
                                                                    </button>
                                                                    <br>
                                                                    <button type="button" onclick="fullDetailsView('${patient.patientCode}')" class="btn btn-default btn-sm">
                                                                        View Full Details
                                                                    </button>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                                </tbody>
                                            </table>

                                        </div>

                                    </div>


                                </div>
                        </div>
                    </c:if>

                    <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='0' }">
                        <div id="submissionResultDisplay" >
                                <div class="components">
                                    <iais:pagination param="submissionParam" result="submissionResult"/>
                                    <div class="table-responsive">
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr >
                                                    <iais:sortableHeader needSort="true"
                                                                         field="BUSINESS_NAME"
                                                                         value="AR Centre"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="SUBMISSION_NO"
                                                                         value="Submission ID"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="SUBMISSION_TYPE"
                                                                         value="Submission Type"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="CYCLE_STAGE_DESC"
                                                                         value="Submission Subtype"/>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="SUBMIT_DT"
                                                                         value="Submission Date"/>
                                                </tr>
                                                </thead>
                                                <tbody class="form-horizontal">
                                                <c:choose>
                                                    <c:when test="${empty submissionResult.rows}">
                                                        <tr>
                                                            <td colspan="15">
                                                                <iais:message key="GENERAL_ACK018"
                                                                              escape="true"/>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="submission"
                                                                   items="${submissionResult.rows}"
                                                                   varStatus="status">
                                                            <tr>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">AR Centre</p>
                                                                    <iais:optionText value="${submission.arCentre}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Submission ID</p>
                                                                    <a href="#" onclick="fullDetailsViewBySubId('${submission.submissionId}','${submission.submissionType}','${submission.submissionIdNo}')">${submission.submissionIdNo}
                                                                    </a>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Submission Type</p>
                                                                    <c:out value="${submission.submissionType}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Submission Subtype</p>
                                                                    <iais:code code="${submission.submissionSubtype}"/>
                                                                </td>
                                                                <td style="vertical-align:middle;">
                                                                    <p class="visible-xs visible-sm table-row-title">Submission Date</p>
                                                                    <fmt:formatDate
                                                                            value="${submission.submissionDate}"
                                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>

                                </div>
                        </div>
                    </c:if>

                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>

<div class="cd-panel cd-panel--from-right js-cd-panel-main">
    <div class="cd-panel__header">
        <h3>Quick View Panel</h3>
        <a  class="cd-panel__close js-cd-close">Close</a>
    </div>
    <div class="cd-panel__container">
        <div class="cd-panel__content quickBodyDiv">

        </div> <!-- cd-panel__content -->
    </div> <!-- cd-panel__container -->
</div>

<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/arPatientResults.js"></script>