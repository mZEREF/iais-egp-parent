<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Audit Information</h2>
                        </div>
                        <iais:row>
                            <iais:field value="Facility Name" width="15" required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${facilityList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            ${item.facilityName}
                                        </c:when>
                                        <c:otherwise>
                                            ${item.facilityName},
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Classification" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${facilityList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            <iais:code code="${item.facilityClassification}"></iais:code>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:code code="${item.facilityClassification}"></iais:code>,
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Type" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${facilityList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            <iais:code code="${item.facilityType}"></iais:code>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:code code="${item.facilityType}"></iais:code>,
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Audit Type" width="15" required="true"/>
                            <iais:value width="10">
                                <iais:select name="auditType" id="auditType"
                                             value="${auditType}"
                                             codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="Please Select"/>
                                <span id="error_auditType" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Audit Window Period" required="false"/>
                            <iais:value width="18">
                                <p><c:out value="1/1/${year} - 31/12/${year}"></c:out></p>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Remarks" width="15" required="false"/>
                            <iais:value width="10">
                                <textarea id="remark"
                                          name="remark"
                                          cols="70"
                                          rows="7"
                                          maxlength="2000"></textarea>
                            </iais:value>
                        </iais:row>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em>
                                    Back</a>
                            </div>
                            <div align="right">
                                <button name="submitBtn" id="submitAudit" type="button" class="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>