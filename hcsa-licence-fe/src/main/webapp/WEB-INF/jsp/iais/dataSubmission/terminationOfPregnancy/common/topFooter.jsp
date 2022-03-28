<div class="application-tab-footer">
<div class="application-tab-footer">
    <div class="col-xs-12 col-sm-4 col-md-2 text-left">
        <a style="padding-left: 5px;" class="back" id="backBtn">
            <em class="fa fa-angle-left">&nbsp;</em> Back
        </a>
    </div>
    <div class="col-xs-12 col-sm-8 col-md-10">
        <div class="button-group">
            <%--<a class="btn btn-secondary premiseSaveDraft" id="saveDraftBtn" >Save as Draft</a>--%>
            <a class="btn btn-primary next premiseId" id="nextBtn" >Next</a></div>
    </div>
</div>
</div>
<input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
<iais:confirm msg="This application has been saved successfully" callBack="cancelDraft();" popupOrder="saveDraft" yesBtnDesc="continue"
              cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpToInbox()" />
<%-- validation --%>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@ include file="../../common/formHidden.jsp" %>
