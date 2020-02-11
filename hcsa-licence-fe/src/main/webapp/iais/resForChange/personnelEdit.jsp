<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">

    <%@include file="dashboard.jsp" %>
    <%--Validation fields Start--%>
    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <%--Validation fields End--%>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane active" id="premisesTab" role="tabpanel">
                                <c:set value="${errorMap_premises}" var="errMsg"/>
                                <input type="hidden" id="premTypeVal" value="${appGrpPremisesDto.premisesType}"/>
                                <div class="row" id="mainPrem">
                                    <div class="col-xs-12">
                                        <div>
                                            <h1 style="margin-top: 10px;">Personnel Amendment</h1>
                                        </div>
                                        <div>
                                            <h2></h2>
                                        </div>
                                        <c:set var="onePersonnel" value="${PersonnelEditList.get(0)}"/>
                                        <div class="">
                                            <div class="form-horizontal">
                                                <iais:row>
                                                    <iais:field value="Email " width="12" mandatory="true"/>
                                                    <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                        <iais:input maxLength="66" type="text" name="emailAddress" value="${onePersonnel.emailAddr}"></iais:input>
                                                        <span class="error-msg" name="iaisErrorMsg" id="error_mobileNo"></span>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Mobile " width="12" mandatory="true"/>
                                                    <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                        <iais:input maxLength="8" type="text" name="mobileNo" value="${onePersonnel.mobileNo}"></iais:input>
                                                        <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr"></span>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-12 col-sm-6 ">
                                                <p><a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a></p>
                                            </div>
                                            <div class="col-xs-12 col-sm-6">
                                                <div class="button-group">
                                                    <a class="btn btn-primary next" id="previewAndSub">Preview and Submit</a>
                                                </div>
                                                <%--<div class="color-small-block" style="border: 0.5px solid rgb(25, 137, 191); border-image: none; background-color: rgb(25, 137, 191);">
                                                  <p style="color: rgb(255, 255, 255);">Dark blue #1989BF</p>
                                                </div>--%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--Validation Field--%>
            <%@ include file="/include/validation.jsp" %>
        </div>
    </div>
</form>


<script>
    $(document).ready(function () {
        $('#previewAndSub').click(function () {
            doSubmitForm('preAck','', '');
        });

    });


</script>