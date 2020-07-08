<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <div class="component-gp">
        <br>
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
            <%--Validation fields Start--%>

            <input type="hidden" name="paramController" id="paramController"
                   value="com.ecquaria.cloud.moh.iais.action.ServiceMenuDelegator"/>
            <input type="hidden" name="valEntity" id="valEntity"
                   value="com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto"/>

            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <h3>
                        Select the service(s) for which you wish to make this licence application
                    </h3>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <c:if test="${!empty err}">
                        <span class="error-msg">${err}</span>
                    </c:if>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                        <div class="form-check-gp">
                            <div class="form-check">
                                <input style="position: absolute;" type="radio" name="licenceJudge" value="1">
                                <label class="form-check-label" style="padding-left: 0px;margin-left: 30px">Existing ${baseName} licences</label>
                            </div>
                            <c:forEach var="item" items="${licence.getRows()}">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           name="licence"  type="checkbox" aria-invalid="false"
                                           value="${item.getId()}" disabled="disabled">
                                    <label class="form-check-label"><span
                                            class="check-square"></span>${item.getAddress()}</label>

                                </div>
                            </c:forEach>
                            <div class="form-check">
                                <input style="position: absolute;" type="radio" name="licenceJudge" value="0">
                                <label class="form-check-label" style="padding-left: 0px;margin-left: 30px">${baseName} at a different premises</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-md-4">
                    <div class="text-left text-center-mobile col-md-6">
                        <a class="back" href="#" id="back" ><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                    <div class="text-right text-center-mobile col-md-6">
                        <a class="btn btn-primary next" id="submitService">Continue</a>
                    </div>
                </div>
            </div>
            <input hidden id="action" name="action" value="">
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
            <c:if test="${ not empty selectDraftNo }">
                <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    $(document).ready(function () {
        $('#back').click(function () {
            $("#action").val("back");
            SOP.Crud.cfxSubmit("mainForm");
        });
        $('#submitService').click(function () {
            $("#action").val("submit");
            SOP.Crud.cfxSubmit("mainForm");
        });
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }
    });

    $('input:radio[name="licenceJudge"]').click(function(){
        var checkValue = $('input:radio[name="licenceJudge"]:checked').val();
        if(checkValue == 0){
            $('input:checkbox[name="licence"]').attr("disabled",true);
            $('input:checkbox[name="licence"]').removeAttr("checked");
        }else{
            $('input:checkbox[name="licence"]').removeAttr("disabled");
        }
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
</script>
