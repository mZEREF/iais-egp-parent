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
        $(".assessment-level-2").attr("hidden","true");

        $("#ceaseLicence").attr('checked', 'true');
    });

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("ceaseLicPage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("ceaseLicSort");
        $("#mainForm").submit();
    }
</script>

