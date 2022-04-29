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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
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
                            <iais:field value="Approval No." width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="${newReqInfo.approvalNo}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Submission Type" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:code code="${newReqInfo.submissionType}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Title" width="15"  mandatory="true"/>
                            <iais:value width="10">
                                <textarea id="rfiTitle"
                                          class="textarea"
                                          style=" font-weight:normal;"
                                          maxlength="500"
                                          rows="8"
                                          cols="64"
                                          name="rfiTitle" >${newReqInfo.title}</textarea>
                                <span data-err-ind="title" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Due Date" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:datePicker value="${newReqInfo.dueDate}" name="dueDate"></iais:datePicker>
                                <span data-err-ind="dueDate" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Status" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:select cssClass="statusDropdown" id="rfiStatus" value="${newReqInfo.status}" name="status" options="statusList"></iais:select>
                            </iais:value>
                        </iais:row>
                        <div class="row" >
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox"
                                       <c:if test="${newReqInfo.informationRequired==true}">checked</c:if>
                                       onchange="checkTitleInfo()"
                                       value = "0"
                                       name = "info" />&nbsp;Information
                            </label>
                        </div>
                        <div class="row">
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox"
                                       <c:if test="${newReqInfo.supportingDocRequired==true}">checked</c:if>
                                       onchange="checkTitleDoc()"
                                       value = "0"
                                       name ="doc" />&nbsp;Supporting Documents
                            </label>
                        </div>
                        <br/>
                        <br/>
                        <br/>
                        <br/>
                        <div id="information" <c:if test="${newReqInfo.informationRequired ==null||newReqInfo.informationRequired ==false}">style="display: none"</c:if>>
                            <div class="row" >
                                <div class="col-sm-12 col-md-9 col-xs-12" style="margin-left: 15px;border: 1px solid #6c6c6c; padding-left: 30px; padding-top: 15px; border-radius: 10px;">
                                    <iais:row>
                                        <label class="col-xs-9 col-md-6 control-label" >
                                            <div class="infoTitIndex">
                                                1. Title of Information Required
                                                <strong style="color:#ff0000;">&nbsp;*</strong>
                                            </div>
                                        </label>
                                    </iais:row>
                                    <iais:row>
                                        <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                            <textarea  name="information" rows="8" style=" font-weight:normal;" maxlength="500" cols="120">${newReqInfo.titleOfInformationRequired}</textarea>
                                            <span data-err-ind="titleOfInformationRequired" class="error-msg"></span>
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
                                <div class="col-sm-12 col-md-9 col-xs-12" style="margin-left: 15px;border: 1px solid #6c6c6c; padding-left: 30px; padding-top: 15px; border-radius: 10px;">
                                    <iais:row>
                                        <label class="col-xs-9 col-md-6 control-label" >
                                            <div class="documents">
                                                2. Title of Supporting Documents
                                                <strong style="color:#ff0000;">&nbsp;*</strong>
                                            </div>
                                        </label>
                                    </iais:row>
                                    <iais:row>
                                        <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                            <textarea  name="documentsTitle" rows="8" style=" font-weight:normal;" maxlength="500" cols="120">${newReqInfo.titleOfSupportingDocRequired}</textarea>
                                            <span data-err-ind="titleOfSupportingDocRequired" class="error-msg"></span>
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
<script>
    function doBack(){
        showWaiting();
        $("[name='action_type']").val("cancel");
        $("#mainForm").submit();
    }
    function doSubmit(){
        showWaiting();
        $("[name='action_type']").val("validate");
        $("#mainForm").submit();
    }
    function checkTitleInfo(){
        if($('input[type = checkbox][name="info"]').prop('checked')){
            $('#information').attr("style","display: block");
            $('input[type = checkbox][name="info"]').attr("value","1");
        }else {
            $("#information").attr("style","display: none");
            $('input[type = checkbox][name="info"]').attr("value","0");
        }
    }
    function checkTitleDoc(){
        if($('input[type = checkbox][name="doc"]').prop('checked')){
            $('#stDoc').attr("style","display: block");
            $('input[type = checkbox][name="doc"]').attr("value","1");
        }else {
            $('#stDoc').attr("style","display: none");
            $('input[type = checkbox][name="doc"]').attr("value","0");
        }
    }
</script>