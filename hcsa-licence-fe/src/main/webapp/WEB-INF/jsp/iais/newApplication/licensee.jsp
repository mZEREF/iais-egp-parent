<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<c:set var="dto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:set var="isNewApp" value="${AppSubmissionDto.appType== 'APTY002'}" scope="request"/>
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
                        <%@ include file="/WEB-INF/jsp/iais/newApplication/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <%@ include file="/WEB-INF/jsp/iais/common/licenseeDetail.jsp" %>
                                <div class="application-tab-footer">
                                    <c:choose>
                                        <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                                            <%@include file="../common/rfcFooter.jsp"%>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="row">
                                                <div class="col-xs-12 col-md-4">
                                                    <c:choose>
                                                        <c:when test="${DraftConfig != null || requestInformationConfig != null}">
                                                            <a class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:when>
                                                        <c:when test="${AssessMentConfig != null}">
                                                            <a class="back" href="/main-web/eservice/INTERNET/MohAccessmentGuide/jumpInstructionPage"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="col-xs-12 col-sm-8">
                                                    <div class="button-group">
                                                        <c:if test="${requestInformationConfig==null}">
                                                            <input type="hidden" id="selectDraftNo" value="${selectDraftNo}">
                                                            <input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
                                                            <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
                                                        </c:if>
                                                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
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