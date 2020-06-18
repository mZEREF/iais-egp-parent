<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="withdrawApplication">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/withdrawApplication/withdrawAppContent.jsp" %>
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

    $("#ceaseLicence").click(function(){
        guideSubmit("cease","main");
    });

    $("#resumeDraftApplication").click(function () {
        guideSubmit("resume","main");
    });

    $("#").click(function () {
        guideSubmit("renew","main");
    });

    $("#withdrawApplication").attr('checked', 'true');

</script>

