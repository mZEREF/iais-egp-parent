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
<div class="self-assessment-item assessment-level-2 completed">
    <div class="amendLicence1 hidden">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence1.jsp" %>
        </div>
    </div>
    <div class="amendLicence2 hidden">
    </div>
    <div class="amendLicence3">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence3.jsp" %>
        </div>
    </div>
    <div class="amendLicence4 hidden">
        <div class="form-check-gp">
            <%@include
                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence4.jsp" %>
        </div>
    </div>
</div>
<div class="self-assessment-item assessment-level-3">
    <div class="amendLicence3_2">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/amendLicence/updateLicenceInfo/amendUpdateLicenceesInfoCompanyContent.jsp" %>
        </div>
    </div>
</div>
<%@include file="../../assessmentGuideMenuFoot.jsp" %>
<script type="application/javascript">
    $(function () {
        $("#amendLicence").attr('checked', 'true');
        $("#amendLicence3").attr('checked', 'true');
        $("#amendLicence3_3").attr('checked', 'true');
    });

    function jumpToPagechangePage() {
        $("[name='guide_action_type']").val("updateLicsPage");
        $("#mainForm").submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='guide_action_type']").val("updateLicsSort");
        $("#mainForm").submit();
    }
</script>