<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/applyNewLicence/applyNewLicenceContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {
        $(".assessment-level-2").attr("hidden","true")
    });
</script>