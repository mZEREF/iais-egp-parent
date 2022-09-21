<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="Amendment"/>
<c:set var="smallTitle" value="You are Amending for Assisted Reproduction"/>

<%@ include file="common/header.jsp" %>
<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <div class="col-xs-12 col-md-10">
                        <strong style="font-size: 2rem;">Please provide details of the patient below</strong>
                    </div>
                    <br>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <strong>Patient</strong>
                            </h4>
                        </div>
                        <div id="arDataSubmission" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="panel-main-content form-horizontal">

                                    <c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}"/>
                                    <c:set var="patient" value="${patientInfoDto.patient}"/>
                                    <c:set var="previous" value="${patientInfoDto.previous}"/>
                                    <c:set var="husband" value="${patientInfoDto.husband}"/>


                                    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Patient</p>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Name (as per NRIC/FIN/Passport)" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:input maxLength="66" type="text" name="name" value="${patient.name}"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Date of Birth" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:datePicker name="birthDate" value="${patient.birthDate}"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Nationality" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                                         value="${patient.nationality}"
                                                         cssClass="nationalitySel" onchange="checkMantory(this, '#ptEthnicGroupLabel', 'NAT0001')"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Ethnic Group" id="ptEthnicGroupLabel"
                                                    mandatory="${patient.nationality eq 'NAT0001' ? true : false}"/>
                                        <iais:value width="12">
                                            <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                                                         value="${patient.ethnicGroup}"
                                                         cssClass="ethnicGroupSel"
                                                         onchange="toggleOnSelect(this, 'ETHG005', 'ptEthnicGroupOtherDiv')"/>
                                        </iais:value>
                                    </iais:row>

                                    <div class="form-group" id="ptEthnicGroupOtherDiv"
                                         style="<c:if test="${patient.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
                                        <iais:field cssClass="col-md-6" value="Ethnic Group (Others)" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:input maxLength="20" type="text" name="ethnicGroupOther" value="${patient.ethnicGroupOther}"/>
                                        </iais:value>
                                    </div>

                                    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Patient's identification details used for previous AR/IUI treatment: </p>
                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="ID No."/>
                                        <iais:value width="12">
                                            <iais:input maxLength="20" type="text" name="preIdNumber"
                                                        value="${previous.idNumber}"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Nationality"/>
                                        <iais:value width="12">
                                            <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                                         value="${previous.nationality}"
                                                         cssClass="nationalitySel"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Name"/>
                                        <iais:value width="12">
                                            <iais:input maxLength="20" type="text" name="preName" value="${previous.name}"/>
                                        </iais:value>
                                    </iais:row>

                                    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Husband</p>
                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Name (as per NRIC/FIN/Passport)" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:input maxLength="66" type="text" name="nameHbd" value="${husband.name}"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Date of Birth" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:datePicker name="birthDateHbd" value="${husband.birthDate}"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Nationality" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:select name="nationalityHbd" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                                         value="${husband.nationality}"
                                                         cssClass="nationalitySel"
                                                         onchange="checkMantory(this, '#hubEthnicGroupLabel', 'NAT0001')"/>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field cssClass="col-md-6" value="Ethnic Group" id="hubEthnicGroupLabel"
                                                    mandatory="${husband.nationality eq 'NAT0001' ? true : false}"/>
                                        <iais:value width="12">
                                            <iais:select name="ethnicGroupHbd" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                                                         value="${husband.ethnicGroup}"
                                                         cssClass="ethnicGroupSel"
                                                         onchange="toggleOnSelect(this, 'ETHG005', 'hubEthnicGroupOtherDiv')"/>
                                        </iais:value>
                                    </iais:row>

                                    <div class="form-group" id="hubEthnicGroupOtherDiv"
                                         style="<c:if test="${husband.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
                                        <iais:field cssClass="col-md-6" value="Ethnic Group (Others)" mandatory="true"/>
                                        <iais:value width="12">
                                            <iais:input maxLength="20" type="text" name="ethnicGroupOtherHbd"
                                                        value="${husband.ethnicGroupOther}"/>
                                        </iais:value>
                                    </div>

                                    <iais:confirm msg="${ageMsg}" callBack="$('#ageMsgDiv').modal('hide');" popupOrder="ageMsgDiv" needCancel="false"
                                                  yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
                                                  needFungDuoJi="false" />
                                    <iais:confirm msg="${hbdAgeMsg}" callBack="$('#hbdAgeMsgDiv').modal('hide');" popupOrder="hbdAgeMsgDiv" needCancel="false"
                                                  yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
                                                  needFungDuoJi="false" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@include file="common/footer.jsp" %>
            </div>
        </div>
    </div>
</form>
