<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>
<%@ include file="../common/dashboard.jsp" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<c:set var="dto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:if test="${AppSubmissionDto.needEditController }">
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.licenseeEdit}" scope="request"/>
</c:if>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <%@ include file="section/licenseeDetail.jsp" %>
                                <%@ include file="common/appFooter.jsp"%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<c:if test="${!(isRFC || isRenew)}">
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
        <c:if test="${('APTY002' != AppSubmissionDto.appType || requestInformationConfig != null) && not empty errormapIs}">
        if ($('#edit').length > 0) {
            $('#edit').trigger('click');
        }
        </c:if>
    });

</script>