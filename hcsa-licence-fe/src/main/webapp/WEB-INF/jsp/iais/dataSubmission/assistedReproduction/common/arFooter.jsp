<div class="application-tab-footer">
    <div class="col-xs-12 col-sm-4 col-md-2 text-left">
        <a style="padding-left: 5px;" class="back" id="backBtn">
            <em class="fa fa-angle-left">&nbsp;</em> Back
        </a>
    </div>
    <div class="col-xs-12 col-sm-8 col-md-10">
        <div class="button-group">
            <c:if test="${arSuperDataSubmissionDto.appType ne 'DSTY_005'}">
                <a class="btn btn-secondary premiseSaveDraft" id="saveDraftBtn" >Save as Draft</a>
            </c:if>
            <a class="btn btn-primary next premiseId" id="nextBtn" >Preview</a></div>
        </div>
    </div>
</div>
<input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
<iais:confirm msg="This application has been saved successfully" callBack="cancelDraft();" popupOrder="saveDraft"
              yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary"
              yesBtnCls="btn btn-secondary" cancelFunc="jumpToInbox()" needFungDuoJi="false"/>
<input type="hidden" value="${RFC_NO_CHANGE_ERROR}" id="rfcNoChangeShow">
<iais:confirm msg="DS_ERR021" needCancel="false" popupOrder="rfcNoChangeModal" yesBtnDesc="ok" needFungDuoJi="false"
              yesBtnCls="btn btn-primary" callBack="$('#rfcNoChangeModal').modal('hide');" />
<%-- validation --%>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@ include file="../../common/formHidden.jsp" %>
