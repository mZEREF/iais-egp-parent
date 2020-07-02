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
    $("#renewLicence2").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "renewUp");
    });
    $("#amendLicence2").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend2");
    });
    $("#amendLicence1_1").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend1_1");
    });
    $("#amendLicence1_2").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend1_2");
    });
    $("#amendLicence3_1").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend3_1");
    });
    $("#amendLicence3_2").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend3_2");
    });

    $("#amendLicence4_1").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend4_1");
    });

    $("#amendLicence4_2").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "amend4_2");
    });

    $("#updateAdminPersonnel").click(function(){
        SOP.Crud.cfxSubmit("mainForm", "upAdmin");
    });
</script>