<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div class="form-group">
    <label class="col-xs-12 col-md-4 control-label">MOH: BSB Assessment and Recommendation</label>
    <div class="col-sm-7 col-md-5 col-xs-10">
        <c:forEach var="file" items="${assessmentInternalDocList}" varStatus="status">
            <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
            <p><a href="javascript:void(0)" onclick="downloadInternalDocAssessmentAndRecommendation('${maskedRepoId}')">${file.filename}</a></p>
        </c:forEach>
    </div>
    <div class="clear"></div>
</div>
