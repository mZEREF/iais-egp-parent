<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>

<%--@elvariable id="viewReqInfo" type="sg.gov.moh.iais.egp.bsb.dto.adhocrfi.ViewAdhocRfiDto"--%>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<div class="dashboard" >
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <br><br><br>
                            <div class="col-xs-12">
                                <div class="panel-group" id="accordion" role="tablist" id="collapseOne" aria-multiselectable="true">
                                    <br><br>
                                    <div class="panel panel-default">
                                        <div class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                            <div class="panel-body">
                                                <div class="panel-main-content">
                                                    <br><br><br>
                                                    <iais:section title="" >
                                                        <iais:row>
                                                            <iais:field value="Request No." width="15" required="false"/>
                                                            <iais:value width="10">
                                                                <c:out value="${viewReqInfo.requestNo}" />
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Facility No." width="15" required="false"/>
                                                            <iais:value width="10">
                                                                <c:out value="${viewReqInfo.facilityNo}" />
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Facility Name" width="15" required="false"/>
                                                            <iais:value width="10">
                                                                <c:out value="${viewReqInfo.facilityName}" />
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Title"/>
                                                            <iais:value width="18">
                                                                <c:out value="${viewReqInfo.title}"/>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Email" width="15" required="false"/>
                                                            <iais:value width="10">
                                                                <c:out value="${viewReqInfo.email}" />
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Due Date" />
                                                            <c:choose>
                                                                <c:when test="${viewReqInfo.status=='RFIST004'}">
                                                                    <iais:value width="18">
                                                                        <iais-bsb:format-LocalDate localDate='${viewReqInfo.dueDate}'/>
                                                                        <span data-err-ind="titleOfSupportingDocRequired" class="error-msg"></span>
                                                                    </iais:value>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                                                        <iais:datePicker value="${viewReqInfo.dueDateShow}" name="dueDate"/>
                                                                        <span data-err-ind="titleOfSupportingDocRequired" class="error-msg"></span>
                                                                    </div>
                                                                </c:otherwise>

                                                            </c:choose>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Status"/>
                                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                                <iais:select cssClass="statusDropDown"  value="${viewReqInfo.status}" name="status"  options="statusLists"/>
                                                                <span data-err-ind="status" class="error-msg"></span>
                                                            </div>
                                                        </iais:row>
                                                        <div class="row" >
                                                            <label class="col-xs-9 col-md-3 control-label">
                                                                <input type="checkbox" disabled <c:if test="${viewReqInfo.informationRequired==true}">checked</c:if>/>&nbsp;Information
                                                            </label>
                                                        </div>
                                                        <div class="row">
                                                            <label class="col-xs-9 col-md-3 control-label">
                                                                <input type="checkbox" disabled <c:if test="${viewReqInfo.supportingDocRequired==true}">checked</c:if>/>&nbsp;Supporting Documents
                                                            </label>
                                                        </div>
                                                        <br/>
                                                        <br/>
                                                        <br/>
                                                        <br/>
                                                        <div class="row" >
                                                            <div class="col-sm-12 col-md-11 col-xs-12" style="margin-left: 15px;padding-left: 30px;">
                                                                <iais:row >
                                                                    <div class="field col-sm-11 control-label formtext">
                                                                        <label>More info title</label>
                                                                    </div>
                                                                    <label class="field col-sm-11 control-label formtext">
                                                                                <textarea disabled rows="5" style=" font-weight:normal;" cols="120">${viewReqInfo.suppliedInformation}</textarea>
                                                                    </label>
                                                                </iais:row>
                                                                <br/>
                                                                <iais:row>
                                                                    <c:set var="maskedAppId"><iais:mask name="file" value="${viewReqInfo.application.id}"/></c:set>
                                                                    <div class="field col-sm-11 control-label formtext">
                                                                        <label>More info Supp Doc</label>
                                                                    </div>
                                                                    <label class="field col-sm-11 control-label formtext">
                                                                        <c:forEach items="${viewReqInfo.applicationDocDtos}" var="appDoc" varStatus="ind">
                                                                            <c:set var="maskedRepoId"><iais:mask name="file" value="${appDoc.fileRepoId}"/></c:set>
                                                                            <a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}','${maskedRepoId}', '${appDoc.docName}')">${appDoc.docName}</a>
                                                                        </c:forEach>
                                                                    </label>
                                                                </iais:row>
                                                            </div>
                                                        </div>
                                                        <br/>
                                                        <br/>
                                                        <iais:row>
                                                            <iais:action style="text-align:right;">
                                                                <button class="btn btn-secondary" type="button"  onclick="javascript:doBack()">Cancel</button>
                                                                <c:if test="${viewReqInfo.status=='RFIST002'}">
                                                                    <button class="btn btn-primary" type="button" style="margin-left: 50px"  onclick="javascript:doSubmit()">Submit</button>
                                                                </c:if>
                                                            </iais:action>
                                                        </iais:row>
                                                    </iais:section>
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
    </form>
</div>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("[name='action_type']").val("cancel");
        $("#mainForm").submit();
    }
    function doSubmit(){
        showWaiting();
        $("[name='action_type']").val("update");
        $("#mainForm").submit();
    }
</script>

