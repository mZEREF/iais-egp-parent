<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>


<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-assessment.js"></script>


<%@include file="dashboard.jsp"%>


<%--@elvariable id="dataDto" type="sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto"--%>
<%--@elvariable id="actions" type="java.util.List<java.lang.String>"--%>
<div class="main-content">
    <div class="container">
        <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="action_type" value="">
            <input type="hidden" name="action_value" value="">
            <input type="hidden" name="action_additional" value="">
            <div>
                <span data-err-ind="iaisErrorMsg" class="error-msg"></span>

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

                <div class="alignctr">
                    <c:choose>
                        <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                        <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                            <a class="back" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='${RfiConstants.KEY_RFI_APP_ID}' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                        </c:when>
                        <c:otherwise>
                            <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form>
    </div>
</div>