<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
         String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body>
                        <div class="">
                            <div class="col-xs-12">
                                <div class="tab-gp dashboard-tab">

                                    <%@ include file="./navTabs.jsp" %>
                                    <div class="tab-content">
                                        <div class="tab-pane" id="tabInfo" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp" %>
                                        </div>
                                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp" %>
                                        </div>
                                        <div class="tab-pane " id="tabLetter" role="tabpanel">
                                            <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                            <%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
                                            <table aria-describedby="" class="table">
                                                <thead style="display: none">
                                                <tr>
                                                    <th scope="col"></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr height="1">
                                                    <td class="col-xs-2">
                                                        <strong>
                                                            Subject
                                                        </strong>
                                                    </td>
                                                    <td>
                                                        <div class="col-sm-9">
                                                            <p><input name="subject" type="text" id="subject"
                                                                      title="subject" readonly
                                                                      value="<c:out value="${insEmailDto.subject}"/>"></p>
                                                        </div>
                                                    </td>

                                                </tr>
                                                <tr height="1">
                                                    <td class="col-xs-2">
                                                        <strong>
                                                            Content
                                                        </strong>
                                                    </td>
                                                    <td>
                                                        <div class="col-sm-9">
                                                            <p><textarea name="messageContent" cols="108" rows="50"
                                                                         id="htmlEditroArea"
                                                                         title="content">${insEmailDto.messageContent}</textarea>
                                                            </p>
                                                        </div>
                                                    </td>
                                                </tr>

                                                </tbody>
                                            </table>
                                            <p class="text-right text-center-mobile">
                                                <iais:action style="text-align:right;">
                                                    <button type="button" class="btn btn-primary"
                                                            onclick="javascript:doPreview();">Preview
                                                    </button>
                                                    &nbsp;
                                                    <button type="button" class="btn btn-primary"
                                                            onclick="javascript:doReload();">Reload
                                                    </button>
                                                </iais:action>
                                            </p>
                                            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                        </div>
                                        <div class="tab-pane active" id="tabProcessing" role="tabpanel">

                                            <div class="alert alert-info" role="alert">
                                                <strong>
                                                    <h4>Processing Status Update</h4>
                                                </strong>
                                            </div>
                                            <iais:section title="" id = "process_NcProcess">
                                                <iais:row>
                                                    <iais:field value="Current Status"/>
                                                    <iais:value width="7">
                                                        <p><span style="font-size: 16px"><iais:code code="${insEmailDto.appStatus}"/></span></p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <label class="col-xs-4 col-md-4 control-label">Internal Remarks <span style="color: red" id="internalRemarkStar"> *</span></label>
                                                    <iais:value width="4000">
                                                            <textarea style="width: 100%;overflow: auto;word-break: break-all;" id="Remarks" name="Remarks" cols="60" rows="7"
                                                                      maxlength="300" class="internalRemarks"
                                                                      >${insEmailDto.remarks}</textarea>
                                                        <br/><span style="font-size: 1.6rem; color: #D22727; display: none" id="remarksMsg" >Remarks should not be more than 300 characters.</span>
                                                        <span id="error_internalRemarks1" class="error-msg" style="display: none;"><iais:message key="GENERAL_ERR0006"/></span>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Processing Decision" required="true"/>
                                                    <iais:value width="7">
                                                        <iais:select id="decision-revise-email" name="decision"  cssClass="nice-select nextStage" options="appTypeOption" firstOption="Please select"  />
                                                        <span style="font-size: 1.6rem; color: #D22727; display: none" id="selectDecisionMsg" ><iais:message key="GENERAL_ERR0006"/></span>
                                                    </iais:value>
                                                </iais:row>
                                                <div id="laterallySelectRow">
                                                    <c:set var="roleId" value="${taskDto.roleId}"/>
                                                    <%@include file="../hcsaLicence/laterallySelect.jsp" %>
                                                </div>
                                                <jsp:include page="/WEB-INF/jsp/iais/inspectionPreTask/rollBackPart.jsp"/>
                                                <iais:row id="ao1SelectRow">
                                                    <iais:field value="Select Approving Officer" required="false"/>
                                                    <iais:value width="7" id = "showAoDiv">
                                                        <iais:select name="aoSelect" firstOption="By System"/>
                                                    </iais:value>
                                                </iais:row>
                                                <c:if test="${ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION ==applicationViewDto.applicationDto.applicationType}">
                                                    <iais:row>
                                                        <iais:field value="Licence Start Date" />
                                                        <iais:value width="7">
                                                            <c:if test="${not empty applicationViewDto.recomLiceStartDate}">
                                                                <p><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></p>
                                                            </c:if>
                                                            <c:if test="${empty applicationViewDto.recomLiceStartDate}">
                                                                <p>-</p>
                                                            </c:if>
                                                        </iais:value>
                                                    </iais:row>
                                                </c:if>
                                                <iais:row>
                                                    <iais:field value="Fast Tracking?" />

                                                    <iais:value width="7" cssClass="control-label">
                                                        <input disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                                                    </iais:value >
                                                </iais:row>
                                                <iais:action>
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>

                                                    <button name="submitBtn" class="btn btn-primary" style="float:right" type="button" onclick="javascript:doSend()">Submit</button>
                                                </iais:action>
                                                <br><br><br>
                                            </iais:section>
                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </iais:body>
        </div>
    </div>
</form>
</div>
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>

<script type="text/javascript">
    $(document).ready(function () {
        $("#ao1SelectRow").hide();
        $("#laterallySelectRow").hide();
        $("#decision-revise-email").change(function () {
            $("#laterallySelectRow").hide();
            var fv = $('#decision-revise-email option:selected').val();
            if (fv == 'REDECI003') {
                showWaiting();
                var data = {
                    'verified':fv
                };
                $.ajax({
                    'url':'${pageContext.request.contextPath}/check-ao',
                    'dataType':'json',
                    'data':data,
                    'type':'POST',
                    'success':function (data) {
                        if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                            $("#error_aoSelect").html('');
                            $("#showAoDiv").html(data.resultJson + '');
                            $("#aoSelect").niceSelect();
                            $("#ao1SelectRow").show();
                        }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){
                            $("#error_aoSelect").html(data.resultJson + '');
                            $("#ao1SelectRow").hide();
                        }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){
                            $("#error_aoSelect").html('');
                            $("#ao1SelectRow").hide();
                        }
                        // setValue();
                    },
                    'error':function () {

                    }
                });
                dismissWaiting();
            } else if (fv == 'PROCRLR'){
                $("#laterallySelectRow").show();
                $("#ao1SelectRow").hide();
            } else {
                $("#ao1SelectRow").hide();
            }
            showRollBackTo();
        });
        showRollBackTo();
    });

    function doSend() {
        var f = $('#decision-revise-email option:selected').val();
        var remark = $('#Remarks').val();
        var lrSelect = $('#lrSelect').val();
        clearErrorMsg();
        $('#selectDecisionMsg').hide();
        $("#laterallyMsg").hide();
        $("#error_internalRemarks1").hide();
        if (f == null || f == ""  ) {
            $("#selectDecisionMsg").show();
        }

        if(remark.length>300){
            $("#remarksMsg").show();
        }
        if('REDECI027' === f){
            submitRollBack(rollBackSubmit);
        }else if("PROCRLR" === f){
            var remarkNull = (remark == null || remark == "");
            var lrSelectNull = (lrSelect == null || lrSelect == "");
            if(remarkNull || lrSelectNull){
                if(remarkNull){
                    $("#error_internalRemarks1").show();
                }
                if(lrSelectNull){
                    $("#laterallyMsg").show();
                }
            } else {
                showWaiting();
                SOP.Crud.cfxSubmit("mainForm", "send");
            }
        }else if(f != null && f != ""  &&remark.length<=300){
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "send");
        }
    }

    function rollBackSubmit(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "send");
    }
</script>





