<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
            String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<%@ page
        import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body>
                            <div class="">
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <br><br><br>
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="complete" role="presentation"><a href="#tabInfo"
                                                                                        aria-controls="tabInfo"
                                                                                        role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li class="complete" role="presentation"><a href="#tabDocuments"
                                                                                        aria-controls="tabDocuments"
                                                                                        role="tab"
                                                                                        data-toggle="tab">Documents</a>
                                            </li>
                                            <li class="active" role="presentation"><a href="#tabLetter"
                                                                                      aria-controls="tabLetter"
                                                                                      role="tab"
                                                                                      data-toggle="tab">Email</a></li>
                                            <li class="complete" role="presentation"><a href="#tabProcessing"
                                                                                        aria-controls="tabProcessing"
                                                                                        role="tab"
                                                                                        data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                             role="tab" data-toggle="tab">Info</a></div>
                                                <div class="swiper-slide"><a href="#tabDocuments"
                                                                             aria-controls="tabDocuments" role="tab"
                                                                             data-toggle="tab">Documents</a></div>
                                                <div class="swiper-slide"><a href="#tabProcessing"
                                                                             aria-controls="tabProcessing" role="tab"
                                                                             data-toggle="tab">Processing</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content">
                                            <div class="tab-pane " id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane active" id="tabLetter" role="tabpanel">
                                                <%@ include file="email.jsp" %>
                                            </div>
                                            <div class="tab-pane " id="tabProcessing" role="tabpanel">
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
                                                        <iais:field value="Internal Remarks"/>
                                                        <iais:value width="4000">
                                                            <textarea id="Remarks" name="Remarks" cols="60" rows="7"
                                                                      maxlength="300"
                                                                      >${insEmailDto.remarks}</textarea>
                                                            <span style="font-size: 1.6rem; color: #D22727; display: none" id="remarksMsg" >Remarks should not be more than 300 characters.</span>
                                                        </iais:value>
                                                    </iais:row>
                                                    <iais:row>
                                                        <iais:field value="Processing Decision" required="true"/>
                                                        <iais:value width="7">
                                                            <iais:select id="decision_email" name="decision" cssClass="nice-select nextStage" options="appTypeOption" firstOption="Please select"  />
                                                            <span style="font-size: 1.6rem; color: #D22727; display: none" id="selectDecisionMsg" >This field is mandatory</span>
                                                        </iais:value>
                                                    </iais:row>
                                                    <iais:row id="ao1SelectRow">
                                                        <iais:field value="Officer" required="true"/>
                                                        <iais:value width="7" id = "showAoDiv">
                                                            <iais:select name="aoSelect" firstOption="Please Select"/>
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
                        </iais:body>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</div>
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>


<script type="text/javascript">
    $(document).ready(function () {
        $("#ao1SelectRow").hide();
        $("#decision_email").change(function () {
            var fv = $('#decision_email option:selected').val();
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
            } else {
                $("#ao1SelectRow").hide();
            }
        })
    });

    function doPreview() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend() {
        var f = $('#decision_email option:selected').val();
        var remark = $('#Remarks').val();

        if (f == null || f == ""  ) {
            $("#selectDecisionMsg").show();
        }

        if(remark.length>300){
            $("#remarksMsg").show();
        }
        if(f != null && f != ""  &&remark.length<=300){
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "send");
        }
    }


</script>



