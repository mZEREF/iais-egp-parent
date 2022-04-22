<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-rfi-file.js"></script>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
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
                                    <iais:section title="" id="rfiDetail">
                                        <iais:row>
                                            <iais:field value="Reference No. "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${adhocReqForInfoDto.adhocRfiDto.facilityNo}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Submission Type "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <iais:code code="${adhocReqForInfoDto.adhocRfiDto.submissionType}"/>
                                            </iais:value>

                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Title "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${adhocReqForInfoDto.adhocRfiDto.title}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <iais-bsb:format-LocalDate localDate='${adhocReqForInfoDto.adhocRfiDto.dueDate}'/>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${ adhocReqForInfoDto.adhocRfiDto.supportingDocRequired}">
                                            <iais:row>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="checkbox" disabled name="reqType"
                                                               <c:if test="${adhocReqForInfoDto.adhocRfiDto.informationRequired}">checked</c:if> />&nbsp;Information
                                                    </label>
                                                    <label>
                                                        <input type="checkbox" disabled name="reqType"
                                                               <c:if test="${adhocReqForInfoDto.adhocRfiDto.supportingDocRequired}">checked</c:if> />&nbsp;Supporting
                                                        Documents
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>
                                        <H3></H3>
                                        <c:if test="${adhocReqForInfoDto.adhocRfiDto.informationRequired}">
                                            <iais:row >
                                                <div class="col-sm-7 col-md-10 col-xs-10">
                                                    <strong>${adhocReqForInfoDto.adhocRfiDto.titleOfInformationRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row >
                                                <iais:value width="18">
                                                    <label>
                                                        <textarea  maxlength="1000" name="userReply"
                                                                   rows="8" style=" font-weight:normal;"
                                                                   cols="130">${adhocReqForInfoDto.adhocRfiDto.suppliedInformation}</textarea>
                                                    </label>
                                                    <span id="error_userReply" name="iaisErrorMsg"
                                                          class="error-msg"></span>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>

                                        <c:if test="${adhocReqForInfoDto.adhocRfiDto.supportingDocRequired}">

                                            <c:set var="fileList" value="${rfiMultiFile.value}"/>
                                            <c:set var="configIndex" value="${rfiMultiFile.key}"/>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-11 col-xs-10">
                                                    <strong>${adhocReqForInfoDto.adhocRfiDto.titleOfSupportingDocRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-12 col-xs-10">
                                                    <%@ include file="document.jsp" %>
                                                </div>
                                            </iais:row>

                                        </c:if>
                                        <iais:action>
                                            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a>
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



