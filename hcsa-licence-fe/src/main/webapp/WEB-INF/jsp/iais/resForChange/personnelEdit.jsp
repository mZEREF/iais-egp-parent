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
                                <div id="update" class="form-horizontal">
                                        <iais:row>
                                            <iais:field value="Name " width="12"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                                <iais:select cssClass="salutationSel" name="salutation"
                                                             codeCategory="CATE_ID_SALUTATION"
                                                             value="${onePersonnel.salutation}"
                                                             firstOption="Please Select"></iais:select>
                                            </iais:value>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                                <iais:input cssClass="needDisabled" maxLength="66" type="text"
                                                            needDisabled="true"
                                                            name="psnName"
                                                            value="${onePersonnel.psnName}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="ID No. " width="12"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                                <iais:select cssClass="idTypeSel" name="idType"
                                                             value="${onePersonnel.idType}"
                                                             options="IdTypeSelect"></iais:select>
                                            </iais:value>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                                <iais:input cssClass="needDisabled" maxLength="66" needDisabled="true"
                                                            type="text"
                                                            name="idNo" value="${onePersonnel.idNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="designationSel" name="designation"
                                                         codeCategory="CATE_ID_DESIGNATION"
                                                         value="${onePersonnel.designation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_designation"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Type " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="professionTypeSel" name="professionType"
                                                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                         value="${onePersonnel.professionType}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_professionType"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="20" type="text" name="professionRegnNo"
                                                        value="${onePersonnel.professionRegnNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Specialty " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="specialty" name="specialty" firstOption="Please Select" options="SpecialtySelectList" value="${onePersonnel.speciality}" ></iais:select>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Subspeciality or relevant qualification " width="12"
                                                    mandatory="false"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="100" type="text" name="subspeciality" value="${onePersonnel.subSpeciality}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${onePersonnel.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="officeTelNo"
                                                        value="${onePersonnel.officeTelNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="66" type="text" name="emailAddr"
                                                        value="${onePersonnel.emailAddr}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                </div>
                                <div id="replace" style="width: 40%" hidden>
                                    <iais:select name="replaceName" id="replaceOptionsId" options="replaceOptions" value="" onchange="addNew()" firstOption="Please Select"></iais:select>
                                </div>
                                <br/><br/><br/><br/>
                                <div id="newPerson" hidden class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="salutation"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${onePersonnel.salutation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="66" type="text"
                                                        name="psnName"
                                                        value="${onePersonnel.psnName}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idType"
                                                         value="${onePersonnel.idType}"
                                                         options="IdTypeSelect"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="66" type="text"
                                                        name="idNo" value="${onePersonnel.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="designationSel" name="designation"
                                                         codeCategory="CATE_ID_DESIGNATION"
                                                         value="${onePersonnel.designation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_designation"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Type " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select cssClass="professionTypeSel" name="professionType"
                                                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                         value="${onePersonnel.professionType}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_professionType"></span>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Professional Regn No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="20" type="text" name="professionRegnNo"
                                                        value="${onePersonnel.professionRegnNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Specialty " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${onePersonnel.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Subspeciality or relevant qualification " width="12"
                                                    mandatory="false"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="100" type="text" name="qualification"
                                                        value="${currentCgo.subSpeciality}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${onePersonnel.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${onePersonnel.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="66" type="text" name="emailAddr"
                                                        value="${onePersonnel.emailAddr}"></iais:input>
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
    $(document).ready(function () {
        $('#previewAndSub').click(function () {
            $("#menuListForm").submit();
        });
        $('#back').click(function () {
            $("[name='actionType']").val("back");
            $("#menuListForm").submit();
        });
        $('.idTypeSel').addClass('disabled');
        $('.salutationSel').addClass('disabled');

        $('.needDisable').css('border-color', '#ededed');
        $('.needDisabled').css('color', '#999');

        $('#edit').click(function () {
            $("#editSelect").show();
            $('#edit').hide();
        });

        $('#cancel').click(function () {
            $("#editSelect").hide();
            $('#edit').show();
        });cancel

        $('#checkitem1').click(function () {
            $("#update").show();
            $('#replace').hide();
        });
        $('#checkitem2').click(function () {
            $("#update").hide();
            $('#replace').show();
        });
        if ($('#checkitem2').val() == 'replace') {
            $("#update").hide();
            $('#newPerson').show();
        }
        if ($('#checkitem1').val() == 'update') {
            $("#update").show();
            $('#newPerson').hide();
        }
        $('#cancel').click(function () {
            $("#update").show();
            $('#replace').hide();
            $('#newPerson').hide();
        });
        if ($('#replaceOptionsId').val() == 'new') {
            $("#update").hide();
            $('#replace').show();
        }
        $('#replaceOptionsId').click(function () {
            $("#update").hide();
            $('#newPerson').show();
        });
    });
        function addNew() {
            if ($('#replaceOptionsId').val() == 'new') {
                $("#update").hide();
                $('#newPerson').show();
            }else {
                $("#update").hide();
                $('#newPerson').hide();
            }
        }

</script>