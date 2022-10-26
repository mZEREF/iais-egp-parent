<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-management.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%--@elvariable id="newReqInfo" type="sg.gov.moh.iais.egp.bsb.dto.adhocrfi.NewAdhocRfiDto"--%>
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
                            <h2>Request For Information List</h2>
                        </div>

                        <iais:row>
                            <iais:field value="Request No." width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="${newReqInfo.requestNo}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility No." width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="${newReqInfo.facilityNo}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Name" width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="${newReqInfo.facilityName}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Title" width="15"  mandatory="true"/>
                            <iais:value width="10">
                                <input type="text" id="rfiTitle" name="rfiTitle" maxlength="250" value="${newReqInfo.title}">
                                <div>
                                    <span data-err-ind="title" style="font-weight:normal;" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Comments for Applicant" width="15"  mandatory="false"/>
                            <iais:value width="10">
                                <textarea id="rfiComments"
                                          class="textarea"
                                          style=" font-weight:normal;"
                                          maxlength="1000"
                                          rows="8"
                                          cols="64"
                                          name="rfiComments" >${newReqInfo.commentsForApplicant}</textarea><br>
                                <div>
                                    <span data-err-ind="title" style="font-weight:normal;" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Email" width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="${newReqInfo.email}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Due Date" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:datePicker value="${newReqInfo.dueDateShow}" name="dueDate"/>
                                <span data-err-ind="dueDate" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Status" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:select cssClass="statusDropdown" id="rfiStatus" value="${newReqInfo.status}" name="status" options="statusList"/>
                            </iais:value>
                        </iais:row>
                        <div class="row" >
                            <label class="col-xs-9 col-md-3 control-label">
                                Please indicate the type of response required from the facility:
                            </label>
                        </div>
                        <div class="row" >
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox"
                                       <c:if test="${newReqInfo.informationRequired==true}">checked</c:if>
                                       onchange="checkTitleInfo()"
                                       value="information"
                                       name = "info" />&nbsp;Information
                            </label>
                        </div>
                        <div class="row">
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox"
                                       <c:if test="${newReqInfo.supportingDocRequired==true}">checked</c:if>
                                       onchange="checkTitleDoc()"
                                       value="documents"
                                       name ="doc" />&nbsp;Supporting Documents
                            </label>
                        </div>
                        <br/>
                        <br/>
                        <br/>
                        <br/>
                        <div id="information" <c:if test="${newReqInfo.informationRequired ==null||newReqInfo.informationRequired ==false}">style="display: none"</c:if>>
                            <div class="row" >
                                <div class="col-sm-12 col-md-10 col-xs-12" style="margin-left: 15px;border: 1px solid #6c6c6c; padding-left: 30px; padding-top: 15px; border-radius: 10px;">
                                    <iais:row>
                                        <label class="col-xs-9 col-md-6 control-label" >
                                            <div class="infoTitIndex">
                                                1. Details of Information Required
                                                <strong style="color:#ff0000;">&nbsp;*</strong>
                                            </div>
                                        </label>
                                    </iais:row>
                                    <iais:row>
                                        <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                            <textarea  name="informationTitle" rows="8" style=" font-weight:normal;" maxlength="500" cols="120">${newReqInfo.titleOfInformationRequired}</textarea>
                                            <div>
                                                <span data-err-ind="titleOfInformationRequired" style="font-weight:normal;" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <br/>
                        <div id="stDoc"  <c:if test="${newReqInfo.supportingDocRequired ==null||newReqInfo.supportingDocRequired ==false}">style="display: none"</c:if>>
                            <div class="row" >
                                <div class="col-sm-12 col-md-10 col-xs-12" style="margin-left: 15px;border: 1px solid #6c6c6c; padding-left: 30px; padding-top: 15px; border-radius: 10px;">
                                    <iais:row>
                                        <label class="col-xs-9 col-md-6 control-label" >
                                            <div class="documents">
                                                2. Details of Supporting Documents Required
                                                <strong style="color:#ff0000;">&nbsp;*</strong>
                                            </div>
                                        </label>
                                    </iais:row>
                                    <iais:row>
                                        <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                            <textarea  name="documentsTitle" rows="8" style=" font-weight:normal;" maxlength="500" cols="120">${newReqInfo.titleOfSupportingDocRequired}</textarea>
                                            <div>
                                                <span data-err-ind="titleOfSupportingDocRequired" style="font-weight:normal;" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <iais:row>
                            <iais:action style="text-align:right;">
                                <button class="btn btn-secondary" type="button"  onclick="javascript:doBack()">Cancel</button>
                                <button class="btn btn-primary" type="button" style="margin-left: 50px"  onclick="javascript:doSubmit()  ">Submit</button>
                            </iais:action>
                        </iais:row>
                    </div>
                </div>
            </div>
            </div>
        </div>
    </form>
</div>