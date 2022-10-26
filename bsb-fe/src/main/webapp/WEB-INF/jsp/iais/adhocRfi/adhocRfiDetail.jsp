<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-rfi.js"></script>

<%--@elvariable id="adhocReqForInfoDto" type="sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiViewDto"--%>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>" onsubmit="return validateOtherDocType();">
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">

    <div class="main-content">
        <br><br><br>
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <c:set var="detail" value="${adhocReqForInfoDto.detailDto}"/>
                                    <iais:section title="" id="rfiDetail">
                                        <iais:row>
                                            <iais:field value="Title"/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${detail.title}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Request No."/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${detail.facilityNo}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Facility Name"/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${detail.facilityName}"/>
                                            </iais:value>
                                        </iais:row>

                                        <iais:row>
                                            <iais:field value="Facility No"/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${detail.facilityNo}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date"/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <iais-bsb:format-LocalDate localDate='${detail.dueDate}'/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Comments by MOH Officer"/>
                                        </iais:row>
                                        <iais:row >
                                            <iais:value width="18">
                                                <label>
                                                        <textarea  maxlength="1000"
                                                                   name="comments"
                                                                   rows="8" style=" font-weight:normal;"
                                                                   cols="130" readonly>${detail.commentsForApplicant}</textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <div class="col-sm-7 col-md-11 col-xs-10">
                                                <strong>Information Required</strong>
                                            </div>
                                        </iais:row>
                                        <c:if test="${detail.supportingDocRequired or detail.informationRequired}">
                                            <iais:row>
                                                <iais:value width="18">
                                                    <c:if test="${detail.informationRequired}">
                                                        <label>
                                                            <input type="checkbox" disabled name="reqType" <c:if test="${detail.informationRequired}">checked</c:if> />&nbsp;Information
                                                        </label>
                                                    </c:if>
                                                    <c:if test="${detail.supportingDocRequired}">
                                                        <label>
                                                            <input type="checkbox" disabled name="reqType" <c:if test="${detail.supportingDocRequired}">checked</c:if> />&nbsp;Supporting Documents
                                                        </label>
                                                    </c:if>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>

                                        <c:if test="${detail.informationRequired}">
                                            <iais:row >
                                                <div class="col-sm-7 col-md-10 col-xs-10">
                                                    <strong>${detail.titleOfInformationRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row >
                                                <iais:value width="18">
                                                    <label>
                                                        <textarea  maxlength="1000"
                                                                   name="userReply"
                                                                   rows="8" style=" font-weight:normal;"
                                                                   cols="130">${detail.suppliedInformation}</textarea>
                                                    </label>
                                                    <span id="error_userReply" name="iaisErrorMsg" class="error-msg"></span>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>

                                        <c:if test="${detail.supportingDocRequired}">
                                            <c:set var="fileList" value="${rfiMultiFile.value}"/>
                                            <c:set var="configIndex" value="${rfiMultiFile.key}"/>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-11 col-xs-10">
                                                    <strong>${detail.titleOfSupportingDocRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-12 col-xs-10">
                                                    <%@ include file="supportingDocuments.jsp" %>
                                                </div>
                                            </iais:row>

                                        </c:if>
                                        <iais:action>
                                            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Previous</a>
                                            <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doSubmit()">Submit</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>



