<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item assessment-level-1 completed">
    <div class="renewLicence">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/renewLicence.jsp" %>
        </div>
    </div>

    <div class="amendLicence hidden">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence.jsp" %>
        </div>
    </div>

    <div class="submitDataMoh hidden">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/submitDataMoh.jsp" %>
        </div>
    </div>

    <div class="updateAdminPersonnel hidden">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/updateAdminPers/updateAdminPers.jsp" %>
        </div>
    </div>
</div>
<div class="self-assessment-item assessment-level-2">
    <div class="renewLicence1">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/assessmentGuide/renewLicence/renewLicenceContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {
        $("#renewLicence").attr('checked', 'true');
        $("#renewLicence1").attr('checked', 'true');
    });

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("renewLicPage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("renewLicSort");
        $("#mainForm").submit();
    }
</script>

