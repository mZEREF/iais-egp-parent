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
                        <p class="assessment-title"><iais:code code="CDN002"/></p>
                        <div class="form-check-gp">
                            <c:forEach var="base" items="${baseService}">
                                <div class="form-check">
                                    <input class="form-check-input"
                                    <c:if test="${baseServiceChecked != null}">
                                    <c:forEach var="checked" items="${baseServiceChecked}">
                                           <c:if test="${base.getId().equals(checked)}">checked="checked"</c:if>
                                    </c:forEach>
                                    </c:if>
                                           name="basechk"  type="checkbox" aria-invalid="false"
                                           value="${base.getId()}">

                                    <label class="form-check-label"><span
                                            class="check-square"></span>${base.getSvcName()}</label>
                                </div>
                            </c:forEach>
                        </div>
                        <p class="assessment-title">Special Licensable Services</p>
                        <div class="form-check-gp">
                            <c:forEach var="specified" items="${specifiedService}">
                                <div class="form-check">
                                    <input class="form-check-input"
                                    <c:if test="${specifiedServiceChecked != null}">
                                    <c:forEach var="specifiedchecked" items="${specifiedServiceChecked}">
                                           <c:if test="${specified.getId().equals(specifiedchecked)}">checked="checked"</c:if>
                                    </c:forEach>
                                    </c:if>
                                           name="sepcifiedchk"  type="checkbox"
                                           aria-invalid="false" value="${specified.getId()}">
                                    <label class="form-check-label" ><span
                                            class="check-square"></span>${specified.getSvcName()}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="submitService" href="javascript:void(0);">Continue</a>
                    </div>
                </div>
            </div>
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
            <c:if test="${ not empty selectDraftNo }">
                 <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    $(document).ready(function () {
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('#submitService').click(function () {
            SOP.Crud.cfxSubmit("mainForm", "validation");
        });
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
