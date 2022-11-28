<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <br>
                <div class="disabledPart">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
                <br/>
                <div>
                    <input type="hidden" name="MSNoteShow" value="${!existPendMS?1:0}"/>
                    <c:forEach items="${notContainedSvc}" var="service" varStatus="status">
                        <c:choose>
                            <c:when test="${service.svcCode==AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY}">
                                <c:if test="${notShowCLB!=1}">
                                    <%@include file="comm/chooseBaseSvcContent.jsp"%>
                                </c:if>
                            </c:when>
                            <c:when test="${service.svcCode==AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES}">
                                <c:if test="${notShowRDS!=1}">
                                    <%@include file="comm/chooseBaseSvcContent.jsp"%>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <%@include file="comm/chooseBaseSvcContent.jsp"%>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${!status.last}">
                            <br>
                        </c:if>
                    </c:forEach>
                    <br>
                    <c:if test="${bundleAchOrMs}">
                        <div class="row svcNote">
                            <div class="col-xs-12 col-md-6">
                                <div class="self-assessment-checkbox-gp gradient-light-grey">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-12">
                                            <strong>The minimum suite of services for Acute Hospital are</strong>
                                        </div>
                                        <div class="col-xs-12 col-md-12">
                                            <ul style="padding-left: 20px">
                                                <li>${AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}</li>
                                                <li>${AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}</li>
                                            </ul>
                                        </div>
                                        <div class="col-xs-12 col-md-12">
                                            <p><strong><iais:message key="NEW_ACK046"/></strong></p>
                                        </div>
                                        <div class="col-xs-12 col-md-12">
                                            <div class="form-check clbNote <c:if test="${notContainedCLB!=1}">hidden</c:if>">
                                                <input class="form-check-input" disabled checked="checked" type="checkbox" aria-invalid="false">
                                                <label class="form-check-label"><span class="check-square"></span>${AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}</label>
                                            </div>
                                            <div class="form-check rdsNote <c:if test="${notContainedRDS!=1}">hidden</c:if>">
                                                <input class="form-check-input" disabled checked="checked" type="checkbox" aria-invalid="false">
                                                <label class="form-check-label"><span class="check-square"></span>${AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <c:if test="${!empty chooseBaseErr}">
                                <span class="error-msg">${chooseBaseErr}</span>
                            </c:if>
                        </div>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12 col-md-2">
                        <div class="text-center-mobile">
                            <a class="back" id="baseBack"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                    </div>
                    <div class="col-xs-12 col-md-4">
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" id="baseContinue" href="javascript:void(0);">NEXT</a>
                        </div>
                    </div>
                </div>

                <br>
                <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
                <c:if test="${ not empty selectDraftNo }">
                    <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
                </c:if>
                <iais:confirm msg="NEW_ACK047" popupOrder="saveApplicationAddress" needCancel="false" yesBtnDesc="OK" yesBtnCls="btn btn-primary" callBack="baseContinue()"></iais:confirm>
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
        //first enter
        var init = 0;

        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);
        $('.existing-base').find('input[type="radio"]').prop('disabled',true);


        $('#baseBack').click(function () {
            showWaiting();
            submit('chooseSvc',null,'back');
        });
        $('#baseContinue').click(function () {
            var flag = $('input[name="MSNoteShow"]').val();
            if (flag==1){
                showWaiting();
                submit('chooseAlign',null,'next');
            }else {
                $('#saveApplicationAddress').modal('show');
            }
        });

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }
        svcNoteFunction();
    });

    function jumpToPagechangePage () {
        showWaiting();
        submit('chooseBaseSvc','doPage','doPage');
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


    function baseContinue() {
        $('#saveApplicationAddress').modal('hide');
        $('input[name="MSNoteShow"]').val('1');
    }

    function svcNoteFunction() {
        $('input[type="radio"]').on('click', function (){
            checkSvcNoteSelect();
        });
    }
    function checkSvcNoteSelect() {
        var maxCount=${notContainedSvcSize};
        var checkedCount=0;
        $("input[type=radio]:checked").each(function() {
            var attr = $(this).attr("id");
            var svcCode= attr.substring(0,3);
            var index= attr.substring(attr.length-1,attr.length);
            if (index!=0 && (svcCode=="CLB"||svcCode=="RDS")){
                checkedCount+=1;
            }
            if (svcCode=="CLB"){
                toggleTag($('div.clbNote'),index==0)
            }
            if (svcCode=="RDS"){
                toggleTag($('div.rdsNote'),index==0)
            }
        })
        console.log("checkedCount :"+checkedCount)
        console.log("maxCount :"+maxCount)
        toggleTag($('div.svcNote'),checkedCount<maxCount)
    }

    function doAfterInitMemoryPage(){
        svcNoteFunction();
        checkSvcNoteSelect();
    }

    function toggleTag(ele, show) {
        var $ele = getJqueryNode(ele);
        if (isEmptyNode($ele)) {
            return;
        }
        if (show) {
            $ele.show();
            $ele.removeClass('hidden');
        } else {
            $ele.hide();
            $ele.addClass('hidden');
        }
    }



</script>
