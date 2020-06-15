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

    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity"
           value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="actionType" id="actionType" value=""/>
    <div class="main-content">
        <div class="container">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <div class="tab-content">
                        <c:set value="${errorMap_premises}" var="errMsg"/>
                        <c:set var="onePersonnel" value="${personnelEditDto}"/>
                        <input type="hidden" id="premTypeVal" value="${appGrpPremisesDto.premisesType}"/>
                        <div class="row" id="mainPrem">
                            <div class="col-xs-12">
                                <br/>
                                <br/>
                                <div>
                                    <p style="font-size: 3rem"><c:out value="${onePersonnel.psnName}"/>,&nbsp;<c:out
                                            value="${onePersonnel.idNo}"/>&nbsp;(<c:out value="${onePersonnel.idType}"/>)</p>
                                    <h4>Changes made will be applied to all licences associated with this personnel.
                                        Please note that payment is required for each affected licence.</h4>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_licenceStatus"></span>
                                <br/>
                                <table class="table">
                                    <thead>
                                    <tr style="font-size: 2rem">
                                        <th>Licence</th>
                                        <th>Licence No.</th>
                                        <th>Roles</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="map" value="${onePersonnel.licPsnTypeDtoMaps}"/>
                                    <c:forEach items="${map}" var="map1">
                                    <tr>
                                        <td>
                                            <c:set var="dto" value="${map1.value}"/>
                                            <p><c:out value="${dto.licSvcName}"/></p>
                                            <c:forEach var="psnType" items="${dto.psnTypes}">
                                                <c:choose>
                                                    <c:when test="${'CGO'==psnType}">
                                                        <p>&nbsp;</p>
                                                    </c:when>
                                                    <c:when test="${'PO'==psnType}">
                                                        <p>&nbsp;</p>
                                                    </c:when>
                                                    <c:when test="${'DPO'==psnType}">
                                                        <p>&nbsp;</p>
                                                    </c:when>
                                                    <c:when test="${'MAP'==psnType}">
                                                        <p>&nbsp;</p>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <p><c:out value="${map1.key}"></c:out></p>
                                        </td>
                                        <td>
                                            <c:set var="dto" value="${map1.value}"/>
                                            <c:forEach var="psnType" items="${dto.psnTypes}">
                                                <c:choose>
                                                    <c:when test="${'CGO'==psnType}">
                                                        <p>Clinical Governance Officer</p>
                                                    </c:when>
                                                    <c:when test="${'PO'==psnType}">
                                                        <p>Principal Officer</p>
                                                    </c:when>
                                                    <c:when test="${'DPO'==psnType}">
                                                        <p>Deputy Principal Officer</p>
                                                    </c:when>
                                                    <c:when test="${'MAP'==psnType}">
                                                        <p>MedAlert</p>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                            <p>&nbsp;</p>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div id="edit">
                                    <a class="btn btn-secondary aMarginleft pull-left">Edit</a>
                                    <span class="error-msg" name="errorMsg" id="error_editSelect"></span>
                                </div>
                                <br/>
                                <div class="form-check-gp" id="editSelect" hidden>
                                    <p class="form-check-title">What would you like to edit?</p>
                                    <div class="form-check progress-step-check" style="width: 33%">
                                        <input class="form-check-input" id="checkitem1"
                                               <c:if test="${editSelectResult=='update'}">checked</c:if> type="radio"
                                               name="editSelect" value="update" aria-invalid="false">
                                        <label class="form-check-label" for="checkitem1"><span
                                                class="check-circle"></span>Change the Details of this Personnel</label>
                                    </div>
                                    <div class="form-check progress-step-check" style="width: 33%">
                                        <input class="form-check-input" id="checkitem2" type="radio"
                                               <c:if test="${editSelectResult=='replace'}">checked</c:if>
                                               name="editSelect" value="replace" aria-invalid="false">
                                        <label class="form-check-label" for="checkitem2"><span
                                                class="check-circle"></span>Replace with Another Personnel</label>
                                    </div>
                                    <a class="cancel" id="cancel">Cancel</a>
                                </div>
                                <br/><br/>
                                <div id="show" class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="salutationShow"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${personnelEditDto.salutation}"
                                                         firstOption="Please Select" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text"
                                                        needDisabled="true"
                                                        name="psnNameShow"
                                                        value="${personnelEditDto.psnName}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idTypeShow"
                                                         value="${personnelEditDto.idType}"
                                                         options="IdTypeSelect" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" needDisabled="true"
                                                        type="text"
                                                        name="idNoShow" value="${personnelEditDto.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="designationSel" name="designationShow"
                                                         codeCategory="CATE_ID_DESIGNATION"
                                                         value="${personnelEditDto.designation}"
                                                         firstOption="Please Select" disabled="true"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Type " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="professionTypeSel" name="professionTypeShow"
                                                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                         value="${personnelEditDto.professionType}"
                                                         firstOption="Please Select" disabled="true"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="20" type="text" name="professionRegnNoShow"
                                                        value="${personnelEditDto.professionRegnNo}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Specialty " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="specialty" name="specialtyShow"
                                                         firstOption="Please Select" options="SpecialtySelectList"
                                                         value="${personnelEditDto.speciality}"
                                                         disabled="true"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Subspeciality or relevant qualification " width="12"
                                                    mandatory="false"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="100" type="text" name="subspecialityShow"
                                                        value="${personnelEditDto.subSpeciality}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="8" type="text" name="mobileNoShow"
                                                        value="${personnelEditDto.mobileNo}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${personnelEditDto.officeTelNo!=null}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="8" type="text" name="officeTelNoShow"
                                                            value="${personnelEditDto.officeTelNo}"
                                                            needDisabled="true"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text" name="emailAddrShow"
                                                        value="${personnelEditDto.emailAddr}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                </div>
                                <div id="update" class="form-horizontal" hidden>
                                    <iais:row>
                                        <iais:field value="Name " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="salutation"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${personnelEditDto.salutation}"
                                                         firstOption="Please Select" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text"
                                                        needDisabled="true"
                                                        name="psnName"
                                                        value="${personnelEditDto.psnName}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idType"
                                                         value="${personnelEditDto.idType}"
                                                         options="IdTypeSelect" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" needDisabled="true"
                                                        type="text"
                                                        name="idNo" value="${personnelEditDto.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="designationSel" name="designation"
                                                         codeCategory="CATE_ID_DESIGNATION"
                                                         value="${personnelEditDto.designation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_designation"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Type " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="professionTypeSel" name="professionType"
                                                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                         value="${personnelEditDto.professionType}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_professionType"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="20" type="text" name="professionRegnNo"
                                                        value="${personnelEditDto.professionRegnNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Specialty " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="specialty" name="specialty"
                                                         firstOption="Please Select" options="SpecialtySelectList"
                                                         value="${personnelEditDto.speciality}"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Subspeciality or relevant qualification " width="12"
                                                    mandatory="false"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="100" type="text" name="subspeciality"
                                                        value="${personnelEditDto.subSpeciality}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${personnelEditDto.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${personnelEditDto.officeTelNo!=null}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="officeTelNo"
                                                            value="${personnelEditDto.officeTelNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="66" type="text" name="emailAddr"
                                                        value="${personnelEditDto.emailAddr}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                </div>
                                <div id="replace" style="width: 40%" hidden>
                                    <iais:select name="replaceName" id="replaceOptionsId" options="replaceOptions"
                                                 value="${replaceName}"
                                                 onchange="addNew()" firstOption="Please Select"></iais:select>
                                </div>
                                <br/><br/><br/><br/>
                                <div id="newPerson" hidden class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="salutation1"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${newPerson.salutation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="66" type="text"
                                                        name="psnName1"
                                                        value="${newPerson.psnName}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idType1"
                                                         value="${newPerson.idType}"
                                                         options="IdTypeSelect"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="9" type="text"
                                                        name="idNo1" value="${newPerson.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="designationSel" name="designation1"
                                                         codeCategory="CATE_ID_DESIGNATION"
                                                         value="${newPerson.designation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Type " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="professionTypeSel" name="professionType1"
                                                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                         value="${newPerson.professionType}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="20" type="text" name="professionRegnNo1"
                                                        value="${newPerson.professionRegnNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Specialty " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="specialty" name="specialty1"
                                                         firstOption="Please Select" options="SpecialtySelectList"
                                                         value="${newPerson.speciality}"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Subspeciality or relevant qualification " width="12"
                                                    mandatory="false"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="100" type="text" name="qualification1"
                                                        value="${newPerson.subSpeciality}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo1"
                                                        value="${newPerson.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${personnelEditDto.officeTelNo!=null}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="officeTelNo1"
                                                            value="${newPerson.officeTelNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="66" type="text" name="emailAddr1"
                                                        value="${newPerson.emailAddr}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                        <br/>
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
            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
        </div>
    </div>
</form>
<script>

    $('.needDisableI').css('border-color', '#ededed');
    $('.needDisableI').css('color', '#999');

    $(document).ready(function () {
        $('#previewAndSub').click(function () {
            var val = $("#replaceOptionsId").val();
            if(val!=""){
                $("#menuListForm").submit();
            }
        });
        $('#back').click(function () {
            $("[name='actionType']").val("back");
            $("#menuListForm").submit();
        });

        $('#edit').click(function () {
            $("#editSelect").show();
            $('#edit').hide();
        });
        $('#cancel').click(function () {
            $("#editSelect").hide();
            $('#edit').show();
        });
        $('#checkitem1').click(function () {
            $("#update").show();
            $('#replace').hide();
            $('#newPerson').hide();
            $('#show').hide();
        });
        $('#checkitem2').click(function () {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
        });

        $('#cancel').click(function () {
            $("#update").hide();
            $('#replace').hide();
            $('#newPerson').hide();
            $('#show').show();
        });
        if ($('#replaceOptionsId').val() !="") {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
            $('#newPerson').show();
        }

        $('#replaceOptionsId').click(function () {
            $("#update").hide();
            $('#show').hide();
            $('#newPerson').show();
        });

        if ($("input[type='radio']:checked").val() == 'update') {
            $("#update").show();
            $('#show').hide();
            $('#replace').hide();
            $('#newPerson').hide();
        } else if ($("input[type='radio']:checked").val() == 'replace') {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
            $('#newPerson').show();
        }
    });

    function addNew() {
        if ($('#replaceOptionsId').val() !="") {
            $("#update").hide();
            $('#newPerson').show();
            $('#show').hide();
        } else {
            $("#update").hide();
            $('#newPerson').hide();
            $('#show').show();
        }
    }


    var rfcPersonForm = function ($CurrentPsnEle,data,psnTYpe) {
    <!--salutation-->
    var salutation  = data.salutation;
    if( salutation == null || salutation =='undefined' || salutation == ''){
        salutation = '';
    }
    $CurrentPsnEle.find('select[name="salutation"]').val(salutation);
    var salutationVal = $CurrentPsnEle.find('option[value="' + salutation + '"]').html();
    $CurrentPsnEle.find('select[name="salutation"]').next().find('.current').html(salutationVal);
    <!--name-->
    $CurrentPsnEle.find('input[name="name"]').val(data.name);

    <!-- idType-->
    var idType  = data.idType;
    if(idType == null || idType =='undefined' || idType == ''){
        idType = '';
    }
    $CurrentPsnEle.find('select[name="idType"]').val(idType);
    var idTypeVal = $CurrentPsnEle.find('option[value="' + idType + '"]').html();
    $CurrentPsnEle.find('select[name="idType"]').next().find('.current').html(idTypeVal);
    <!-- idNo-->
    $CurrentPsnEle.find('input[name="idNo"]').val(data.idNo);

    $CurrentPsnEle.find('input[name="mobileNo"]').val(data.mobileNo);
    $CurrentPsnEle.find('input[name="emailAddress"]').val(data.emailAddr);


    <!--     ====================    -->
    <!--       diff page column      -->
    <!--     ====================    -->

    <!-- officeTelNo-->
    var officeTelNo = data.officeTelNo;
    if(officeTelNo != null && officeTelNo != ''){
        $CurrentPsnEle.find('input[name="officeTelNo"]').val(officeTelNo);
    }else{
        $CurrentPsnEle.find('input[name="officeTelNo"]').val('');
    }
    <!--Designation  -->
    var designation = data.designation;
    if(designation == null || designation == ''){
        designation = '';
    }
    $CurrentPsnEle.find('select[name="designation"]').val(designation);
    var designationVal = $CurrentPsnEle.find('option[value="' + designation + '"]').html();
    $CurrentPsnEle.find('select[name="designation"]').next().find('.current').html(designationVal);


    <!-- professionType-->
    var professionType = data.professionType;
    if(professionType == null || professionType =='undefined' || professionType == ''){
        professionType = '';
    }
    $CurrentPsnEle.find('select[name="professionType"]').val(professionType);
    var professionTypeVal = $CurrentPsnEle.find('option[value="' + professionType + '"]').html();
    $CurrentPsnEle.find('select[name="professionType"]').next().find('.current').html(professionTypeVal);
    <!-- professionRegoNo-->
    var professionRegoNo = data.profRegNo;
    if(professionRegoNo != null && professionRegoNo != ''){
        $CurrentPsnEle.find('input[name="professionRegoNo"]').val(professionRegoNo);
    }else{
        $CurrentPsnEle.find('input[name="professionRegoNo"]').val('');
    }
    <!-- speciality-->
    var speciality = data.speciality;
    if('CGO' == psnTYpe){
        $CurrentPsnEle.find('div.specialtyDiv').html(data.specialityHtml);
        showSpecialty();
    }else{
        if(speciality == null || speciality =='undefined' || speciality == ''){
            speciality = '-1';
        }
        var specialityVal = $CurrentPsnEle.find('option[value="' + speciality + '"]').html();
        if(specialityVal =='undefined'){
            speciality = '';
            specialityVal = $CurrentPsnEle.find('option[value="' + speciality + '"]').html();
        }
        $CurrentPsnEle.find('select[name="specialty"]').val(speciality);
        $CurrentPsnEle.find('select[name="specialty"]').next().find('.current').html(specialityVal);
    }
    if('other' == speciality){
        $CurrentPsnEle.find('input[name="specialtyOther"]').removeClass('hidden');
        var specialityOther = data.specialityOther;
        if(specialityOther != null && specialityOther != ''){
            $CurrentPsnEle.find('input[name="specialtyOther"]').val(specialityOther);
        }else{
            $CurrentPsnEle.find('input[name="specialtyOther"]').val('');
        }
    }else{
        $CurrentPsnEle.find('input[name="specialtyOther"]').addClass('hidden');
    }
    <!--Subspeciality or relevant qualification -->
    var qualification = data.subSpeciality;
    if(qualification != null && qualification != ''){
        $CurrentPsnEle.find('input[name="qualification"]').val(qualification);
    }else{
        $CurrentPsnEle.find('input[name="qualification"]').val('');
    }
    <!--preferredMode -->
    var preferredMode = data.preferredMode;
    if(preferredMode != null && preferredMode !='undefined' && preferredMode != ''){
        if('3' == preferredMode){
            $CurrentPsnEle.find('input.preferredMode').prop('checked',true);
        }else{
            $CurrentPsnEle.find('input.preferredMode').each(function () {
                if(preferredMode == $(this).val()){
                    $(this).prop('checked',true);
                }
            });
        }
    }else{
        $CurrentPsnEle.find('input.preferredMode').prop('checked',false);
    }

    var isLicPerson = data.licPerson;
    if('1' == isLicPerson){
        if('CGO' == psnTYpe){
            disabledPartPage($CurrentPsnEle.find('.new-officer-form'));
        }else{
            disabledPartPage($CurrentPsnEle.find('.medAlertPerson'));
        }
        $CurrentPsnEle.find('input[name="licPerson"]').val('1');
        $CurrentPsnEle.find('input[name="existingPsn"]').val('1');
    }else{
        if('CGO' == psnTYpe){
            unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
        }else{
            unDisabledPartPage($CurrentPsnEle.find('.medAlertPerson'));
        }
        $CurrentPsnEle.find('input[name="licPerson"]').val('0');
        $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
    }

    }

</script>