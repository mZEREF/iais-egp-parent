<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
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
                            <th scope="col" >No.</th>
                            <th scope="col" >Facility Name</th>
                            <th scope="col" >Address</th>
                            <th scope="col" >Classification</th>
                            <th scope="col" >Activity</th>
                            <th scope="col" >Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>${dataDto.facName}</td>
                            <td>${TableDisplayUtil.getOneLineAddress(dataDto.blk, dataDto.street, dataDto.floor, dataDto.unit, dataDto.postalCode)}</td>
                            <td>${dataDto.classification}</td>
                            <td>${dataDto.activity}</td>
                            <td>
                                <c:forEach var="action" items="${actions}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${action ne 'Print'}">
                                            <button type="button" id="selfAssessment${status.index}" data-custom-ind="processSelfAssessment" data-custom-app="<iais:mask name="selfAssessAppId" value="${dataDto.appId}"/>" value="${action}" class="btn btn-default btn-md" >${action}</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" id="printSelfAssessment${status.index}" data-custom-ind="printSelfAssessment" value="<iais:mask name="selfAssessAppId" value="${dataDto.appId}"/>" class="btn btn-default btn-md" >Print</button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="alignctr">
                    <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </form>
    </div>
</div>