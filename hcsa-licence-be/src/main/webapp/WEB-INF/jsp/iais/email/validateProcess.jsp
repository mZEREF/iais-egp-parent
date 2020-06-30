<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
       String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body>
                        <div class="container">
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
                                            <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
                                            <table class="table">
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
                                                                      value="${insEmailDto.subject}"></p>
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
                                                                         class="wenbenkuang" id="htmlEditroArea"
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
                                        </div>
                                        <div class="tab-pane active" id="tabProcessing" role="tabpanel">
                                            <div class="alert alert-info" role="alert">
                                                <strong>
                                                    <h4>Processing Status Update</h4>
                                                </strong>
                                            </div>
                                            <iais:section title="" id = "process_NcEmail">
                                                <iais:row>
                                                    <iais:field value="Current Status"/>
                                                    <iais:value width="7">
                                                        <p><span style="font-size: 16px"><iais:code code="${insEmailDto.appStatus}"/></span></p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Internal Remarks"/>
                                                    <iais:value width="4000">
                                                            <textarea name="remarks" cols="60" rows="7"
                                                                      maxlength="300"
                                                                      >${insEmailDto.remarks}</textarea>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Processing Decision" required="true"/>
                                                    <iais:value width="7">
                                                        <select id="decision-validate-email" name="decision">
                                                            <option value="Select" selected>Please Select
                                                            </option>
                                                            <c:forEach items="${appTypeOption}" var="decision">
                                                                <option value="${decision.value}">${decision.text}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </iais:value>
                                                </iais:row>
                                                <div style="color:#ff0000; display: none" id="selectDecisionMsg" >
                                                    <iais:row >
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            This field is mandatory.
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
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
                                                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doSend()">Submit</button>
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

    function doSend() {
        showWaiting();
        var f = $('#decision-validate-email option:selected').val();

        if (f != "Select") {
            SOP.Crud.cfxSubmit("mainForm", "send");
        } else {
            $("#selectDecisionMsg").show();
            dismissWaiting();
        }
    }


</script>





