<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<webui:setLayout name="iais-intranet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<c:set var="dto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:set var="isNewApp" value="${AppSubmissionDto.appType== 'APTY002'}" scope="request"/>
<c:if test="${AppSubmissionDto.needEditController }">
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.licenseeEdit}" scope="request"/>
</c:if>

<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <div class="main-content">
        <div class="row">
            <div class="center-content">
                <div class="col-xs-12 intranet-content">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <%@ include file="section/licenseeDetail.jsp" %>
                                <%@ include file="/WEB-INF/jsp/iais/application/common/appFooter.jsp"%>
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
<c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
    <iais:confirm msg="This application has been saved successfully" callBack="$('#saveDraft').modal('hide');" popupOrder="saveDraft"
                  yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelFunc="submit('licensee','saveDraft','jumpPage');" />
</c:if>
<script type="text/javascript">
    $(document).ready(function() {
        //assignSelectBindEvent();
        $('#Back').on('click',function(){
            showWaiting();
            submit(null,'back',null);
        });
        $('#Next').on('click',function(){
            showWaiting();
            $('#mainForm').find(':input').prop('disabled',false);
            submit('premises',null,null);
        });
        $('#SaveDraft').on('click',function(){
            showWaiting();
            $('#mainForm').find(':input').prop('disabled',false);
            submit('licensee','saveDraft',$('#selectDraftNo').val());
        });

        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }

        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}">
            disableContent('div.licenseeContent');
        </c:if>
    });

</script>