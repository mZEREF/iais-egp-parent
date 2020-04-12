<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
           String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm"  enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
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
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   complete
                                                </c:when>
                                                <c:otherwise>
                                                    active
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                                <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                            data-toggle="tab">Documents</a></li>
                                <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                                data-toggle="tab">CheckList</a></li>
                                <li class="complete" role="presentation"><a href="#Processing" aria-controls="Processing" role="tab"
                                                                            data-toggle="tab">Processing</a></li>
                            </ul>
                            <div class="tab-nav-mobile visible-xs visible-sm">
                                <div class="swiper-wrapper" role="tablist">
                                    <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                    <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                    <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
                                    <div class="swiper-slide"><a href="#Processing" aria-controls="Processing" role="tab" data-toggle="tab">Processing</a></div>
                                </div>
                                <div class="swiper-button-prev"></div>
                                <div class="swiper-button-next"></div>
                            </div>
                            <div class="tab-content">
                                <div class="tab-pane  <c:if test="${serListDto.checkListTab!='chkList'}">active</c:if>" id="tabInfo" role="tabpanel">
                                    <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                                </div>
                                <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                    <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                                </div>
                                <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                                    <%@include file="/iais/inspectionncList/inspectiondetail.jsp"%>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div align="right">
                                                <input type="hidden" name="viewchk" id = "viewchk">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doViewCheckList();">
                                                    View CheckList
                                                </button>
                                                <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="Processing" role="tabpanel">
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4>Processing Status Update</h4>
                                        </strong>
                                    </div>
                                    <iais:section title="" id = "process_Rectification">
                                        <br>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <label style="font-size: 16px">Current Status</label>
                                        </div>
                                        <div class="col-md-6">
                                            <p><label style="font-size: 16px"><iais:code code="${applicationViewDto.applicationDto.status}"/></label></p>
                                        </div>
                                    </div>
                                        <br>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <label style="font-size: 16px">Licence Start Date</label>
                                        </div>
                                        <div class="col-md-6">
                                            <c:if test="${not empty applicationViewDto.recomLiceStartDate}">
                                                <label style="font-size: 16px"><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></label>
                                            </c:if>
                                            <c:if test="${empty applicationViewDto.recomLiceStartDate}">
                                                <label style="font-size: 16px">-</label>
                                            </c:if>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <label style="font-size: 16px">Fast Tracking</label>
                                        </div>
                                        <div class="col-md-6">
                                            <input disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div align="right">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doSubmit();">
                                                    Submit
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <br><br>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4>Processing History</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>Username</th>
                                                        <th>Working Group</th>
                                                        <th>Status Update</th>
                                                        <th>Remarks</th>
                                                        <th>Last Updated</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach
                                                            items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                                                            var="appPremisesRoutingHistoryDto">
                                                        <tr>
                                                            <td>
                                                                <p><c:out
                                                                        value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                                                            </td>
                                                            <td>
                                                                <p><c:out
                                                                        value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                                                            </td>
                                                            <td>
                                                                <p><c:out
                                                                        value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                                                            </td>
                                                            <td>
                                                                <p><c:out
                                                                        value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                                                            </td>
                                                            <td>
                                                                <p><c:out
                                                                        value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    </iais:section>

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
<%@include file="/iais/inspectionncList/uploadFile.jsp"%>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function doViewCheckList(){
        $("#viewchk").val("viewchk");
        SOP.Crud.cfxSubmit("mainForm", "vewChkl");
    }

    function doSubmit(){
        $("#viewchk").val("");
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "next");
        dismissWaiting();
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();

    }
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $("#licFileName").html(getFileName(file));
        alert(123);
/*        $(this).parent().children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('span:eq(0)').next().removeClass("hidden");
        $(this).parent().children('input delFlag').val('N');*/
    });

</script>
