<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
           String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm"  enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                <div class="">
                    <div class="col-xs-12">
                        <div class="tab-gp dashboard-tab">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li id="infoli" class="<c:choose>
                                            <c:when test="${serListDto.checkListTab!='chkList' && serListDto.checkListTab!='process'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                                <li id="documentsli" class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                            data-toggle="tab">Documents</a></li>
                                <li id="checkListli" class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                                data-toggle="tab">Checklist</a></li>
                                <li id="processingli" class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='process'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#Processing" aria-controls="Processing" role="tab"
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
                                <div class="tab-pane  <c:if test="${serListDto.checkListTab!='chkList' && serListDto.checkListTab!='process'}">active</c:if>" id="tabInfo" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp"%>
                                </div>
                                <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp"%>
                                </div>
                                <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/inspectiondetail.jsp"%>
                                    <div class="row" align="right">
                                        <input type="hidden" name="viewchk" id = "viewchk">
                                        <c:if test="${inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
                                            <button type="button" class="btn btn-primary" onclick="javascript: doListAhoc();">List Adhoc</button>
                                        </c:if>
                                        <button type="button" class="btn btn-primary" onclick="javascript: doViewCheckList();">
                                            View CheckList
                                        </button>
                                        <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                    </div>
                                    <div align="right">
                                    <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                    </div>
                                </div>
                                <div class="tab-pane <c:if test="${serListDto.checkListTab=='process'}">active</c:if>" id="Processing" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/inspecProcessing.jsp"%>
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
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp"%>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

<script type="text/javascript">
    $(document).ready(function () {
        showRollBackTo();
    });

    function doViewCheckList(){
        $("#viewchk").val("viewchk");
        SOP.Crud.cfxSubmit("mainForm", "vewChkl");
    }
    function doListAhoc(){
        $("#viewchk").val("");
        SOP.Crud.cfxSubmit("mainForm", "listAhoc");
    }
    function doSubmit(){
        clearErrorMsg();
        $("#error_rollBackTo1").hide();
        let processDecValue = $('#processDec').val();
        $("#viewchk").val("");
        if('REDECI027' === processDecValue){
            submitRollBack(()=>{
                showWaiting();
                SOP.Crud.cfxSubmit('mainForm', 'next');
            });
        }else {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "next");
        }
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        showTag($("#"+divId));
        showTag($("#"+comId));
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
