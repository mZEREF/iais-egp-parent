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
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/updateAdminPersonnel.jsp" %>
        </div>
    </div>
</div>
<div class="self-assessment-item assessment-level-2">
    <div class="amendLicence1">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/assessmentGuide/renewLicence/renewLicenceWithUpdateContent.jsp" %>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {

        $("#renewLicence").attr('checked', 'true');
        $("#renewLicence2").attr('checked', 'true');

        $("#withdrawApplication").click(function(){
            guideSubmit("withdraw","main");
        });

        $("#resumeDraftApplication").click(function () {
            guideSubmit("resume","main");
        });

        $("#renewLicence1").click(function () {
            guideSubmit("renew","main");
        });

        $("#ceaseLicence").click(function(){
            guideSubmit("cease","main");
        });
    });

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("renewPage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("renewSort");
        $("#mainForm").submit();
    }
</script>

