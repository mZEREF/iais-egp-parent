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
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence.jsp" %>
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
<div class="self-assessment-item assessment-level-2 completed">
    <div class="amendLicence1 ">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence1.jsp" %>
        </div>
    </div>

    <div class="amendLicence3 hidden">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence3.jsp" %>
        </div>
    </div>
    <div class="amendLicence4 hidden">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence4.jsp" %>
        </div>
    </div>
</div>
<div class="self-assessment-item assessment-level-3">
    <div class="amendLicence1_2">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/amendLicence/updateDetailes/amendUpdateDetailesRemoveContent.jsp" %>
        </div>
    </div>
</div>
<%@include file="../../assessmentGuideMenuFoot.jsp" %>
<script type="application/javascript">
    $("#amendLicence").attr('checked', 'true');
    $("#amendLicence1").attr('checked', 'true');
    $("#amendLicence1_2").attr('checked', 'true');

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("removeServicePage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("removeServiceSort");
        $("#mainForm").submit();
    }
</script>