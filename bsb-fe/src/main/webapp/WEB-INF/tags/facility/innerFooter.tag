<%@tag description="Inner footer of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="canSaveDraftJudge" type="java.lang.Boolean" %>

<div class="application-tab-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6 ">
            <a class="back" id="previous" href="#"><em class="fa fa-angle-left"></em> Previous</a>
        </div>
        <div class="col-xs-12 col-sm-6">
            <div class="button-group">
                <c:if test="${canSaveDraftJudge}">
                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                </c:if>
                <a class="btn btn-primary next" id="next" >Next</a>
            </div>
        </div>
    </div>
</div>