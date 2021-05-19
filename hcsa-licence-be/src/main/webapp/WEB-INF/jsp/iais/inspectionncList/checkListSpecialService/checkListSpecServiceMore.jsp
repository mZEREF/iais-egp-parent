<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
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
    <form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input id="nowTabIn" name="nowTabIn" value="${nowTabIn == null ? 'Combined' : nowTabIn}" type="hidden">
        <input id="nowComTabIn" name="nowComTabIn" value="${nowComTabIn == null ? 'General' : nowComTabIn}" type="hidden">
        <input type="hidden" name="doSubmitAction" id="doSubmitAction" value="">
        <div class="main-content">
            <div class="">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="tab-gp dashboard-tab">
                            <div class="tab-content">
                            </div>
                            <div class="tab-pane active" id="tabPayment" role="tabpanel">
                                <%@include file="tabSpecMore.jsp"%>
                                <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                <div class="tab-content" id="checkLsitItemArea">
                                    <div class="tab-content">
                                        <div class="tab-pane ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" id="General" role="tabpanel">
                                            <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
                                            <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/><br>
                                            <span><strong>General</strong></span>
                                            <%@ include file="tabsSpecMoreIns.jsp" %>
                                            <%@ include file="combinedSpec.jsp" %>
                                            <%@ include file="inspChecklistSpec.jsp" %>
                                        </div>
                                  <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
                                   <c:set value = "ServiceInfo${service.identify}" var = "errorTabName"/>
                                     <div class="tab-pane ${errorSpecTab == errorTabName ? 'active' : ''}" id="ServiceInfo${service.identify}" role="tabpanel">
                                            <c:if test="${not empty service.fdtoList ||  service.adchklDto.adItemList != null}">
                                                <c:if test="${not empty service.fdtoList}">
                                                    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${service.serviceDo}"/>/<c:out value="${service.serviceTotal}"/><br>
                                                    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${service.serviceNc}"/>
                                                </c:if>
                                                <%@ include file="tabsSpecMoreIns.jsp" %>
                                                <%@ include file="combinedServiceSpec.jsp" %>
                                                <%@ include file="inspChecklistServiceSpec.jsp" %>
                                            </c:if>
                                    </div>
                                    </c:forEach>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <a style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Back</a>
                                        <c:if test="${inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
                                            <div style="float:right">
                                                <c:if test="${ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                                    <button class="btn btn-primary next" type="button" onclick="javascript:doBack()">Submit</button>
                                                    <button class="btn btn-primary next" type="button" onclick="javascript:doSaveDraftCheckList();">Save Draft</button>
                                                </c:if>
                                            </div>
                                        </c:if>
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
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("#doSubmitAction").val("next");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doBackToMain(){
        showWaiting();
        $("#doSubmitAction").val("");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doSaveDraftCheckList(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "saveDraft");
    }

    $(document).ready(function (){
        readOnlyAllForCheckList('${applicationViewDto.applicationDto.status}');
        var beforeFinishCheck = ${(inspectionNcCheckListDelegator_before_finish_check_list == null || inspectionNcCheckListDelegator_before_finish_check_list == "0") ? '0' : '1'};
        if( beforeFinishCheck == '1'){
            readOnlyAllForCheckListOnly();
        }
    });

    function readOnlyAllForCheckList(status) {
        if (status == 'APST032') {
            readOnlyAllForCheckListOnly();
        }
    }
    function readOnlyAllForCheckListOnly() {
        $("#checkLsitItemArea textarea").attr('readonly','readonly');
        $("#checkLsitItemArea textarea").attr('Enabled',false);
        $("#checkLsitItemArea input[type='checkbox']").attr("disabled",true);
        $("#checkLsitItemArea  input[type='radio']").attr("disabled",true);
    }


    function showCheckBox(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var divId = str+'ck'+ indexIns;
        var comId = str+'comck'+indexIns;
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str,indexIns){
        if(indexIns != null &&indexIns != "" ){
            indexIns = "Ins" + indexIns;
        }else {
            indexIns = "";
        }
        var divId = str+'ck'+ indexIns;;
        var comdivId = str+'comck'+indexIns;;
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        $("#"+divId).hide();
        $("#"+comdivId).hide();
    }

    function doChangeDeconflict(index,divId,size){
        if(index == 1){
            divId += "comDiv"
        }else if (index == 2){
            divId += "serDiv";
        }else {
            divId += "adhocDiv";
            size=size-1;
        }
        for(var i = 0; i<= size; i++){
            var id = "#"+divId+i;
            $(id).css("background-color", "lightgreen");
        }
    }
</script>
