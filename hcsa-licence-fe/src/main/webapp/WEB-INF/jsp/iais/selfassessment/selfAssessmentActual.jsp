<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6
  Time: 9:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content">
    <div class = "container">
        <form id = "mainForm" name="mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
            <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
            <input type="hidden" name="tagIndex" value="<iais:mask name="tagIndex" value="${tagIndex}"/>">
            <input type="hidden" name="prevTagIndex" value="<iais:mask name="prevTagIndex" value="${prevTagIndex}"/>">
            <input type="hidden" name="selfAssessmentCorrId" value="${param.selfAssessmentCorrId}"/>
            <input type="hidden" name="loadPopupPrint" value="${param.loadPopupPrint}" />
            <div id="printContent">
            <%@ include file="/WEB-INF/jsp/iais/common/selfassessmentQuestion.jsp" %>
            </div>
                <div class="print-hidden-flag">
                    <iais:action style="text-align:left;">
                        <a  id="backLastPageId" ><em class="fa fa-angle-left"> </em> Back</a>
                    </iais:action>

                    <td>
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-secondary <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>" id="clearButtonId" href="javascript:void(0);">Clear</a>
                            <a class="btn btn-primary next <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>" id="submitButtonId" href="javascript:void(0);">Submit</a>
                        </div>
                    </td>

                    <br>

                </div>
        </form>
    </div>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script>
    function draftAnswer(value, key){
        $("[name ="+key+ "]").val(value);
    }

    function switchNextStep(index){
        $("[name='tagIndex']").val(index);
        let val = getPrintLoadParam();
        if(val != null && 'Y' == val){
            let prevTagIndex = $("[name='prevTagIndex']").val();
            let selfAssessmentCorrId = $("[name='selfAssessmentCorrId']").val();
            window.location.href= "/hcsa-licence-web/eservice/INTERNET/MohSelfAssessmentSubmit/1/switchNextStep?tagIndex=" + index
            + "&selfAssessmentCorrId=" + selfAssessmentCorrId + "&loadPopupPrint=" + val + "&prevTagIndex=" + prevTagIndex;
        }else{
            $("[name='crud_action_type']").val("switchNextStep");
            let mainForm = document.getElementById('mainForm');
            mainForm.submit();
        }
    }

    function doSubmit(){
        SOP.Crud.cfxSubmit("mainForm", "submitSelfDesc");
    }

    clearButtonId.onclick = (function () {
        let tagIndex = $("[name='tagIndex']").val();
        /*$("input[type='radio']").removeAttr('checked');*/
        //clear answer with current tab
        SOP.Crud.cfxSubmit("mainForm", "clearAnswer", tagIndex);
    });

    submitButtonId.onclick = (function () {
        SOP.Crud.cfxSubmit("mainForm", "draftItem");
    });

    function getPrintLoadParam(){
        let val = $("input[name='loadPopupPrint']").val();
        if (val == null || val == ''){
            val = getQueryVariable('loadPopupPrint');
            $("input[name='loadPopupPrint']").val(val);
        }
        return val;
    }

    $(document).ready(function () {
        let val = getPrintLoadParam();
        if(val != null && 'Y' == val){
            $('.print-hidden-flag').addClass("hidden");
            let aTagList = $("a");
            for(let i = 0; i < aTagList.length; i++){
                aTagList[i].href= '#';
            }
            printpage('printContent');
        }
    });


</script>