<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="ceaseLicence">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/ceaseLicence/ceaseLicenceContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {
        $(function () {
            $(".assessment-level-2").attr("hidden","true")
        });

        $("#ceaseLicence").attr('checked', 'true');

        $("#withdrawApplication").click(function(){
            guideSubmit("withdraw","main");
        });

        $("#resumeDraftApplication").click(function () {
            guideSubmit("resume","main");
        });
    })
</script>

