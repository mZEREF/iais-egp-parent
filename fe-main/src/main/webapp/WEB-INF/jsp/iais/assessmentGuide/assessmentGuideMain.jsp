<%@include file="assessmentGuideMenuHead.jsp" %>
<%@include file="assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<%@include file="assessmentGuideMenuLevel/assessmentGuideMenuLevel1.jsp" %>
<%@include file="assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<div class="self-assessment-item"></div>
<%@include file="assessmentGuideMenuFoot.jsp" %>
<script>
    $("#ceaseLicence").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "cease");
    });
    $("#withdrawApplication").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "withdraw");
    });
    $("#resumeDraftApplication").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "resume");
    });
    $("#renewLicence1").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "renew");
    });
</script>