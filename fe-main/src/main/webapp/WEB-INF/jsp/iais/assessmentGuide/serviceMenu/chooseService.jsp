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
<%@include file="../assessmentGuideServiceMenuFoot.jsp" %>
<script>
    $("#applyLicence").attr('checked', 'true');

    $(document).ready(function () {
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('#Back').click(function(){
            submit('toInbox',null,'back');
        });

        $('#submitService').click(function(){
            submit('chooseSvc',null,'next');
        });
    });
</script>