<%@include file="../../assessmentGuideMenuHead.jsp" %>
<%@include file="../../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item assessment-level-1 completed">
    <div class="renewLicence hidden">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/renewLicence.jsp" %>
        </div>
    </div>

    <div class="amendLicence">
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
    <div class="amendLicence2">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/assessmentGuide/amendLicence/updateHCI/amendUpdateHCIContent.jsp" %>
        </div>
    </div>
    <%@include file="../../assessmentGuideMenuLevel/assessmentGuideMenuLevel2_1.jsp" %>
</div>
<%@include file="../../assessmentGuideMenuFoot.jsp" %>
<script type="application/javascript">
    $(function () {
        $("#amendLicence").attr('checked', 'true');
        $("#amendLicence2").attr('checked', 'true');
    });

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("updateHCIPage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("updateHCISort");
        $("#mainForm").submit();
    }
</script>