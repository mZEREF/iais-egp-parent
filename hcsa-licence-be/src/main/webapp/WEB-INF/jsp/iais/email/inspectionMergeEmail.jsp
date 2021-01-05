<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
           String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
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
                            <div class="container">
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
                                            <li class="active" role="presentation"><a href="#tabProcessing"
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
                                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp" %>
                                            </div>

                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane active" id="tabProcessing" role="tabpanel">
                                                <iais:section title="" id = "process_NcEmail">
                                                    <iais:row>
                                                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
                                                        <div class="col-sm-9">
                                                            <p><input name="subject" type="text" id="subject"
                                                                      title="subject" readonly
                                                                      value="${insEmailDto.subject}"></p>
                                                        </div>
                                                    </iais:row>
                                                    <iais:row>
                                                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
                                                        <div class="col-sm-9">
                                                                <textarea name="messageContent" cols="108" rows="50"
                                                                          id="htmlEditroArea"
                                                                          title="content">${insEmailDto.messageContent}</textarea>
                                                        </div>
                                                    </iais:row>
                                                    <iais:row>
                                                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Internal Remarks</label>
                                                        <iais:value width="4000">
                                                            <textarea name="remarks" cols="60" rows="7"
                                                                      maxlength="300"
                                                            >${insEmailDto.remarks}</textarea>
                                                        </iais:value>
                                                    </iais:row>
                                                    <iais:row>
                                                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Processing Decision<span class="mandatory">*</span></label>
                                                        <iais:value width="6">
                                                            <select id="decision_merge_email" name="decision"
                                                                    onchange="thisTime()">
                                                                <option value="Select" selected>Please Select
                                                                </option>
                                                                <c:forEach items="${appTypeOption}" var="decision">
                                                                    <option value="${decision.value}">${decision.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span style="font-size: 1.6rem; color: #D22727; display: none" id="selectDecisionMsg" >
                                                                This is mandatory
                                                            </span>
                                                        </iais:value>
                                                    </iais:row>
                                                    <iais:row style="display: none" id="selectReviseNc">
                                                        <label class="col-xs-0 col-md-2  col-sm-2">Need Revise<span class="mandatory">*</span></label>
                                                        <iais:value width="6" >
                                                            <c:forEach items="${svcNames}" var="revise"
                                                                       varStatus="index"><input type="checkbox" name="revise${index.index+1}"
                                                                       value="${revise}"/>&nbsp;${revise}&nbsp;
                                                            </c:forEach>
                                                            <div class="row">&nbsp;</div>
                                                            <span id="selectDecisionMsgRevise"  style="display: none; font-size: 1.6rem; color: #D22727;">This is mandatory</span>
                                                        </iais:value>
                                                    </iais:row>
                                                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION ==applicationViewDto.applicationDto.applicationType}">
                                                        <iais:row >
                                                            <label class="col-xs-0 col-md-2 control-label col-sm-2">Licence Start Date</label>
                                                            <div class="col-sm-9 " >
                                                                <c:if test="${not empty applicationViewDto.recomLiceStartDate}">
                                                                    <p><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></p>
                                                                </c:if>
                                                                <c:if test="${empty applicationViewDto.recomLiceStartDate}">
                                                                    <p>-</p>
                                                                </c:if>
                                                            </div>
                                                        </iais:row>
                                                    </c:if>
                                                </iais:section>
                                                <p class="text-right text-center-mobile">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohBackendInbox?fromOther=1"><em class="fa fa-angle-left"></em> Back</a>

                                                    <iais:action style="text-align:right;">
                                                        <button type="button" class="btn btn-secondary"
                                                                onclick="javascript:doPreview();">Preview
                                                        </button>
                                                        <button type="button" class="btn btn-primary"
                                                                onclick="javascript:doSend();">Submit
                                                        </button>
                                                        &nbsp;
                                                    </iais:action>
                                                </p>
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
    function doPreview() {
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend() {
        if ($('#decision_merge_email option:selected').val() == "Select") {
            $("#selectDecisionMsg").show();
            $("#selectDecisionMsgRevise").hide();
        } else {
            $("#selectDecisionMsg").hide();
            if ($('#decision_merge_email option:selected').val() == "REDECI005") {
                var checkOne = false;
                var checkBox = $('input[type = checkbox]');
                for (var i = 0; i < checkBox.length; i++) {
                    if (checkBox[i].checked) {
                        checkOne = true;
                    }
                    ;
                }
                ;

                if (checkOne) {
                    showWaiting();
                    SOP.Crud.cfxSubmit("mainForm", "send");
                } else {
                    $("#selectDecisionMsgRevise").show();
                }
                ;

            } else {
                showWaiting();
                SOP.Crud.cfxSubmit("mainForm", "send");
            }
        }

    }

    function thisTime() {

        if ($('#decision_merge_email option:selected').val() == "REDECI005") {
            $("#selectReviseNc").show();
        } else {
            $("#selectReviseNc").hide();
        }
    }


</script>



