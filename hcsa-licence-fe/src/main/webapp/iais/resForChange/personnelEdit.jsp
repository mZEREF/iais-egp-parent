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
    <%@include file="../common/dashboard.jsp" %>
    <%--Validation fields Start--%>
    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <%--Validation fields End--%>

    <div class="main-content">
        <div class="container">

            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <div class="tab-content">
                        <c:set value="${errorMap_premises}" var="errMsg"/>
                        <c:set var="onePersonnel" value="${PersonnelEditList.get(0)}"/>
                        <input type="hidden" id="premTypeVal" value="${appGrpPremisesDto.premisesType}"/>
                        <div class="row" id="mainPrem">
                            <div class="col-xs-12">
                                <div>
                                    <br/>
                                    <br/>
                                </div>
                                <div class="form-horizontal">
                                    <h3><c:out value="${onePersonnel.psnName}"/>,<c:out value="${onePersonnel.idNo}"/>(<c:out value="${onePersonnel.idType}"/>)</h3>
                                    <h4>Changes made will be applied to all licences associated with this personnel. Please note that payment is required for each affected licence.</h4>
                                </div>
                                <div class="form-horizontal"></div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_licenceStatus"></span>
                                <div>
                                </div>
                                <div class="">
                                    <div class="form-horizontal">
                                        <iais:row>
                                            <iais:field value="Name " width="12"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                                <iais:select cssClass="salutationSel"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${onePersonnel.salutation}" firstOption="Please Select"></iais:select>
                                            </iais:value>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                                <iais:input maxLength="66" type="text" needDisabled="true" name="emailAddress" value="${onePersonnel.psnName}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Id No. " width="12" />
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                                <iais:select cssClass="idTypeSel"  name="idType" value="${onePersonnel.idType}" options="IdTypeSelect"></iais:select>
                                            </iais:value>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                                <iais:input maxLength="66" needDisabled="true" type="text" name="emailAddress" value="${onePersonnel.idNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Designation " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select cssClass="designationSel" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${onePersonnel.designation}" firstOption="Please Select"></iais:select>
                                            </iais:value>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_designation"></span>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Professional  Regn Type " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select cssClass="professionTypeSel" name="professionType" codeCategory="CATE_ID_PROFESSIONAL_TYPE" value="${onePersonnel.professionType}" firstOption="Please Select"></iais:select>
                                            </iais:value>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_professionType"></span>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="20" type="text" name="professionRegnNo" value="${onePersonnel.professionRegnNo}"></iais:input>
                                            </iais:value>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_professionRegnNor"></span>
                                        </iais:row>


                                        <iais:row>
                                            <iais:field value="Email " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="66" type="text" name="emailAddress" value="${onePersonnel.emailAddr}"></iais:input>
                                            </iais:value>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr"></span>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Mobile " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="mobileNo" value="${onePersonnel.mobileNo}"></iais:input>
                                            </iais:value>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_mobileNo"></span>
                                        </iais:row>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 ">
                                <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div class="col-xs-12 col-sm-6 text-right">
                                <div class="button-group">
                                    <a class="btn btn-primary next" id="previewAndSub">Preview and Submit</a>
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

        $('#back').click(function () {
            doBack('prePersonnelList');
        });

        $('.idTypeSel').addClass('disabled');
        $('.salutationSel').addClass('disabled');
    });


</script>