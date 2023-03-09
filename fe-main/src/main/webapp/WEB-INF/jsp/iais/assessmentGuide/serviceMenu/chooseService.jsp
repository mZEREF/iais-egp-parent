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
<input type="text" style="display: none" id="draftsave" name="draftsave" value="<c:out value="${selectDraftNo}"/>">
<input type="hidden" name="MSNoteShow" value="1"/>
<c:if test="${ not empty selectDraftNo }">
    <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
</c:if>
<iais:confirm msg="NEW_ACK047" popupOrder="saveApplicationAddress" needCancel="false" yesBtnDesc="OK" yesBtnCls="btn btn-primary" callBack="baseContinue()"></iais:confirm>
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
            var flag = $('input[name="MSNoteShow"]').val();
            if (flag==1){
                showWaiting();
                submit('chooseSvc',null,'next');
            }else {
                $('#saveApplicationAddress').modal('show');
            }
        });
        svcNoteFunction();
    });
    function saveDraft() {
        let val = $('#draftsave').val();
        $("[name='crud_action_additional']").val(val);
        $("[name='crud_action_value']").val('continue');
        $('#mainForm').submit();
    }

    function cancelSaveDraft() {
        let val = $('#draftsave').val();
        $("[name='crud_action_additional']").val(val);
        $("[name='crud_action_value']").val('resume');
        $('#mainForm').submit();
    }

    function sameAddressContinue() {
        $('#existSameAddress').modal('hide');
    }

    function svcNoteFunction() {
        $('input[type="checkbox"]').on('click', function (){
            var svcNameList=new Array();
            $('input[type="checkbox"]:checked').each(function (i, x) {
                svcNameList.push($(x).next().text());
            })
            var data = {
                'svcNameList': svcNameList
            };
            var opt = {
                url: '${pageContext.request.contextPath}' + "/feAdmin/checkIsExistPendMs",
                type: 'GET',
                data: data
            };
            callCommonAjax(opt, "checkSvcNoteSelCallBack");
        });
    }

    function checkSvcNoteSelCallBack(data) {
        console.log(data);
        if (data==true){
            $('input[name="MSNoteShow"]').val(0);
        }else{
            $('input[name="MSNoteShow"]').val(1);
        }
    }

    function baseContinue() {
        $('#saveApplicationAddress').modal('hide');
        $('input[name="MSNoteShow"]').val('1');
    }
</script>