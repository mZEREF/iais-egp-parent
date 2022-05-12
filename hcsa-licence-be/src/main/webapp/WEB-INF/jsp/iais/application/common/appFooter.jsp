<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="application-tab-footer">
    <c:choose>
        <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
            <%@include file="rfcFooter.jsp"%>
        </c:when>
        <c:otherwise>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                    <c:choose>
                        <c:when test="${DraftConfig != null || requestInformationConfig != null}">
                            <a class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em class="fa fa-angle-left"></em> Back</a>
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