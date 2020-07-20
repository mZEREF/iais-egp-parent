<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <div id="svcStep1">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideMenuFoot.jsp" %>
<script>
    $(function () {
        $(".assessment-level-2").attr("hidden","true")
    });

    $("#applyLicence").attr('checked', 'true');

    $(document).ready(function () {
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('#submitService').click(function(){
            $("#mainForm").submit();
        });
    });

</script>