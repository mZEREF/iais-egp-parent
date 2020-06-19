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
            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/updateAdminPersonnel.jsp" %>
        </div>
    </div>
</div>
<div class="self-assessment-item assessment-level-2">
    <div class="amendLicence1">
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
    <div class="amendLicence1_1">
        <div class="form-check-gp">
            <%@include file="/WEB-INF/jsp/iais/assessmentGuide/amendLicence/updateDetailes/amendUpdateDetailContents.jsp" %>
        </div>
    </div>
</div>
<%@include file="../../assessmentGuideMenuFoot.jsp" %>
<script type="application/javascript">
    $("#amendLicence").attr('checked', 'true');
    $("#amendLicence1").attr('checked', 'true');
    $("#amendLicence1_1").attr('checked', 'true');

    $("#withdrawApplication").click(function(){
        guideSubmit("withdraw","main");
    });

    $("#resumeDraftApplication").click(function () {
        guideSubmit("resume","main");
    });

    $("#renewLicence2").click(function () {
        guideSubmit("renewUp","main");
    });

    $("#ceaseLicence").click(function(){
        guideSubmit("cease","main");
    });

    $("#amendLicence2").click(function(){
        guideSubmit("amend2","main");
    });
</script>