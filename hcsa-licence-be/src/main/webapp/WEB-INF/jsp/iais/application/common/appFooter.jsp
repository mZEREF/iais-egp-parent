<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="alignctr border-top-solid" style="border-top: 1px solid #BABABA;">
    <div class="row">
        <div class="col-xs-12 col-md-4">
            <a class="back" id="Back" href="javascript:;"><em class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="col-xs-12 col-sm-8">
            <div class="button-group text-right">
                <%--<c:if test="${requestInformationConfig==null}">
                    <input type="hidden" id="selectDraftNo" value="${selectDraftNo}">
                    <input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
                    <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
                </c:if>--%>
                <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
        </div>
    </div>
</div>
