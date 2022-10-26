<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-ncs.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="itemValue" value="">
    <input type="hidden" name="isAllRectify" value="${isAllRectify}">

    <div class="main-content">
        <div class="container">
            <div class="row" style="margin: 0 auto">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <h3 style="margin: 50px 0">List of Rectifications</h3>
                        <div class="row" style="font-weight: 700;text-align: center">
                            <div class="col-md-1">Checklist Item</div>
                            <div class="col-md-2">Item Description</div>
                            <div class="col-md-2">Findings/Non-Compliance</div>
                            <div class="col-md-2">Action Required</div>
                            <div class="col-md-2">MOH Remarks</div>
                            <div class="col-md-1">Actions</div>
                            <div class="col-md-1">Rectification uploaded</div>
                            <div class="col-md-1">Status</div>
                        </div>
                        <c:choose>
                            <c:when test="${empty ncsPreData.itemDtoList}">
                                <iais:message key="GENERAL_ACK018" escape="true"/>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${ncsPreData.itemDtoList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${item.checklistItem}"/></div>
                                        <div class="col-md-2"><c:out value="${item.itemDesc}"/></div>
                                        <div class="col-md-2"><c:out value="${item.finding}"/></div>
                                        <div class="col-md-2"><c:out value="${item.actionRequired}"/></div>
                                        <div class="col-md-2">
                                            <c:choose>
                                                <c:when test="${item.remark ne null && item.remark ne ''}"><c:out value="${item.remark}"/></c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-md-1"><button type="button" class="btn btn-default btn-sm" onclick="rectifyItem('<iais:mask name="itemVal" value="${item.itemValue}"/>')">Rectify</button></div>
                                        <div class="col-md-1">
                                            <c:if test="${rectifyMap.get(item.itemValue) eq null or rectifyMap.get(item.itemValue) eq 'N'}">
                                                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4>
                                            </c:if>
                                            <c:if test="${rectifyMap.get(item.itemValue) eq 'Y'}">
                                                <h4 class="text-success"><em class="fa fa-check-circle del-size-36"></em></h4>
                                            </c:if>
                                        </div>
                                        <div class="col-md-1"><c:out value="${item.status}"/></div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="row" style="border-top: 1px solid #D1D1D1;margin-top: 100px;padding: 20px 0">
                        <div class="col-xs-12 col-sm-6 ">
                            <c:choose>
                                <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                                <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                                    <a class="back" id="back" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="back" id="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="button-group" style="float: right">
                                <a class="btn btn-primary" id="submitBtn" >Submit</a>
                            </div>
                        </div>
                    </div>

                    <div class="modal fade" id="submitModal" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-12"><span style="font-size: 2rem"><iais:message key="BISERRINS002"/></span></div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>