<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <input type="hidden" name="draftNo"/>
                <div class="disabledPart">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
                <br/>
                <c:if test="${appSelectSvc.chooseBaseSvc}">
                    <div class="disabledPart">
                        <%@include file="comm/chooseBaseSvcContent.jsp"%>
                    </div>
                    <br/>
                </c:if>
                <%--<c:if test="${!appSelectSvc.chooseBaseSvc}">--%>
                    <%--<div class="disabledPart">--%>
                        <%--<%@include file="comm/chooseAlignContent.jsp"%>--%>
                    <%--</div>--%>
                    <%--<br/>--%>
                <%--</c:if>--%>
                <div>
                    <%@include file="comm/chooseLicContent.jsp"%>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12 col-md-3">
                    </div>
                    <div class="col-xs-12 col-md-4">
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" id="licContinue" href="javascript:void(0);">NEXT</a>
                        </div>
                    </div>
                </div>
                <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
                <input type="hidden" id="firstVisitFor" name="firstVisitFor" value="${firstVisitFor}">
                <c:if test="${ not empty selectDraftNo }">
                    <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"/>
                </c:if>
                <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
            </div>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideServiceMenuFoot.jsp" %>
<script type="text/javascript">
    $("#applyLicence").attr('checked', 'true');

    $("#submitService").hide();

    $(document).ready(function () {
        if( $('#draftsave').val()!=null && $('#draftsave').val()!='' && $('#firstVisitFor').val() != 'Y'){
            $('#saveDraft').modal('show');
        }

        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);

        $('#licBack').click(function () {
            submit('chooseAlign',null,'back');
        });

        $('#licContinue').click(function () {
            /*if( $('#draftsave').val()!=null|| $('#draftsave').val()!==''){
                $('#saveDraft').modal('show');
            }else {
                submit(null,'next','next');
            }*/
            submit(null,'next','next');
        });



    });

    function jumpToPagechangePage () {
        showWaiting();
        submit('chooseLic','doPage','doPage');
    }

    function saveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('continue');
        $('#mainForm').submit();
    }

    function cancelSaveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('resume');
        $('#mainForm').submit();
    }

</script>

