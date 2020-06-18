<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1.jsp" %>
<div class="self-assessment-item assessment-level-2">
    <div class="amendLicence1">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/assessmentGuide/renewLicence/remewLicenceContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {

        $("#renewLicence").attr('checked', 'true');
        $("#renewLicence1").attr('checked', 'true');

        $("#withdrawApplication").click(function(){
            guideSubmit("withdraw","main");
        });

        $("#resumeDraftApplication").click(function () {
            guideSubmit("resume","main");
        });
    })
</script>

