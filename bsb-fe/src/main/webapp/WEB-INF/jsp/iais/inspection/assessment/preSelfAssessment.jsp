<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.RfiType" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-assessment.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="dashboard.jsp"%>
<%--@elvariable id="rfiType" type="sg.gov.moh.iais.egp.bsb.constant.RfiType"--%>
<%--@elvariable id="dataDto" type="sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto"--%>
<%--@elvariable id="actions" type="java.util.List<java.lang.String>"--%>
<div class="main-content">
    <div class="container">
        <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="action_type" value="">
            <input type="hidden" name="action_value" value="">
            <input type="hidden" name="action_additional" value="">
            <c:if test="${RfiType.PRE_INSPECTION_CHECKLIST_APPOINTMENT eq rfiType}">
                <p><strong>Checklist</strong></p>
            </c:if>
            <div>
                <table aria-describedby="" class="table" border="1">
                    <thead>
                        <tr>
                            <th scope="col" >SN</th>
                            <th scope="col" >Facility Name</th>
                            <th scope="col" >Facility Classification</th>
                            <th scope="col" >Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td><c:out value="${dataDto.facName}"/></td>
                            <td><iais:code code="${dataDto.classification}"/></td>
                            <td>
                                <c:forEach var="action" items="${actions}" varStatus="status">
                                    <c:set var="maskApp"><iais:mask name="selfAssessAppId" value="${dataDto.appId}"/></c:set>
                                    <%-- TODO use different mask param for different action, so when an action does not exist, user can not access it --%>
                                    <c:choose>
                                        <c:when test="${action eq 'Print'}">
                                            <button type="button" id="printSelfAssessment${status.index}" data-custom-ind="printSelfAssessment" value="${maskApp}" class="btn btn-default btn-md" >Print</button>
                                        </c:when>
                                        <c:when test="${action eq 'Download'}">
                                            <button type="button" id="selfAssessment${status.index}" data-custom-ind="downloadSelfAssessment" data-custom-app="${maskApp}" value="${action}" class="btn btn-default btn-md" >${action}</button>
                                        </c:when>
                                        <c:when test="${action eq 'Upload'}">
                                            <button type="button" id="selfAssessment${status.index}" data-custom-ind="uploadSelfAssessment" data-custom-app="${maskApp}" value="${action}" class="btn btn-default btn-md" >${action}</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" id="selfAssessment${status.index}" data-custom-ind="processSelfAssessment" data-custom-app="${maskApp}" value="${action}" class="btn btn-default btn-md" >${action}</button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <span data-err-ind="checklist" class="error-msg"></span>
                <c:if test="${RfiType.PRE_INSPECTION_CHECKLIST_APPOINTMENT eq rfiType}">
                    <br/>
                    <p><strong>Appointment Scheduling</strong></p>
                    <%--@elvariable id="inspectionDateDto" type="sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto"--%>
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label for="specifyStartDt" class="col-xs-12 col-md-4 control-label">Preferred Date Range for Inspection (Start) <span style="color: red">*</span></label>
                            <div class="col-sm-7 col-md-5 col-xs-10">
                                <iais:datePicker id="specifyStartDt" name="specifyStartDt" value="${inspectionDateDto.specifyStartDt}"/>
                                <span data-err-ind="specifyStartDt" class="error-msg" ></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="specifyEndDt" class="col-xs-12 col-md-4 control-label">Preferred Date Range for Inspection (Start) <span style="color: red">*</span></label>
                            <div class="col-sm-7 col-md-5 col-xs-10">
                                <iais:datePicker id="specifyEndDt" name="specifyEndDt" value="${inspectionDateDto.specifyEndDt}"/>
                                <span data-err-ind="specifyEndDt" class="error-msg" ></span>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div>
                    <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                    <div class="text-left">
                        <c:choose>
                            <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                                <a class="back" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                            </c:when>
                            <c:otherwise>
                                <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="text-right">
                        <a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" class="btn btn-secondary">CANCEL</a>
                        <a class="btn btn-primary" id="submit" href="javascript:void(0);">Submit</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>