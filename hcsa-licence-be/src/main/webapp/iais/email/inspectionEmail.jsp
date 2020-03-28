<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <br><br><br>
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="complete" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="active" role="presentation"><a href="#tabLetter" aria-controls="tabLetter" role="tab"
                                                                      data-toggle="tab">Email</a></li>
                            <li class="complete" role="presentation"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab"
                                                                        data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>

                        <div class="tab-content">
                            <div class="tab-pane " id="tabInfo" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <div class="tab-pane active" id="tabLetter" role="tabpanel">
                                <%@ include file="email.jsp" %>
                            </div>
                            <div class="tab-pane " id="tabProcessing" role="tabpanel">
                                <%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
                                <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
                                <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
                                <table class="table">
                                    <tbody>
                                    <div class="alert alert-info" role="alert">
                                        <p><span><strong>Processing Status Update</strong></span></p>
                                    </div>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Current Status:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p>${insEmailDto.appStatus}</p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Internal Remarks:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><textarea name="remarks" cols="90" rows="6" maxlength="300" title="content"  >${insEmailDto.remarks}</textarea></p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Processing Decision:<strong style="color:#ff0000;"> *</strong>
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <select id="decision_email" name="decision">
                                                    <option value="Select" selected>Please Select</option>
                                                    <c:forEach items="${appTypeOption}" var="decision">
                                                        <option  value="${decision.value}">${decision.text}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1"   style="display: none" id="selectDecisionMsg">
                                        <td class="col-xs-2" >
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p style="color:#ff0000;">
                                                    This field is mandatory
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">

                                    <iais:action style="text-align:center;">
                                        <button type="button" class="btn btn-primary" onclick="javascript:doSend();">Submit</button>
                                    </iais:action>
                                </p>
                                <br>
                                <div class="alert alert-info" role="alert">
                                    <p><span><strong>Processing History</strong></span></p>
                                </div>
                                <table class="table">
                                    <thead>
                                    <tr align="center">
                                        <iais:sortableHeader needSort="false" field="USERNAME" value="Username"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="WORKING GROUP" value="Working Group"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="APP_STATUS" value="Status Update"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="REMARKS" value="Remarks"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false" field="UPDATED_DT" value="Last Updated"></iais:sortableHeader>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty appPremisesRoutingHistoryDtos}">
                                            <tr>
                                                <td colspan="7">
                                                    <iais:message key="ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="pool" items="${appPremisesRoutingHistoryDtos}" varStatus="status">
                                                <tr>
                                                    <td><c:out value="${pool.actionby}"/></td>
                                                    <td><c:out value="${pool.wrkGrpId}"/></td>
                                                    <td><c:out value="${pool.appStatus}"/></td>
                                                    <td><c:out value="${pool.internalRemarks}"/></td>
                                                    <td><c:out value="${pool.updatedDt}" /></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>

                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script type="text/javascript">
    function doPreview(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend(){
        showWaiting();
        if($('#decision_email option:selected').val()=="Select"){
            $("#selectDecisionMsg").show();
            dismissWaiting();
        } else {
            SOP.Crud.cfxSubmit("mainForm", "send");
        }
    }


</script>



