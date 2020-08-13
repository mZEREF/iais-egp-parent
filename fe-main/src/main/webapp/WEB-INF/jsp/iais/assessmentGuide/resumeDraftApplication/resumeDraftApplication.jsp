<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="resumeDraftApplication">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/resumeDraftApplication/resumDraftAppContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script type="application/javascript">
    $(function () {
        $(".assessment-level-2").attr("hidden","true");
    });

    $("#ceaseLicence").click(function(){
        guideSubmit("cease","main");
    });

    $("#withdrawApplication").click(function(){
        guideSubmit("withdraw","main");
    });

    $("#").click(function () {
        guideSubmit("renew","main");
    });

    $("#resumeDraftApplication").attr('checked', 'true');

    function jumpToPagechangePage() {
        guideSubmit("resumePage","second");
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        guideSubmit("resumeSort","second");
    }
</script>

