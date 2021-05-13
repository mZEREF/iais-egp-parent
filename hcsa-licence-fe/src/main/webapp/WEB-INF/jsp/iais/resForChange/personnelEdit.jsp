<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                                            value="${onePersonnel.idNo}"/>&nbsp;(<iais:code code="${onePersonnel.idType}"/>)</p>
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
                                                        <p>Nominee</p>
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
                                    <a class="btn btn-secondary aMarginleft pull-left" href="javascript:void(0);">Edit</a>
                                    <span class="error-msg" name="errorMsg" id="error_editSelect"></span>
                                </div>
                                <br/>
                                <div class="form-check-gp" id="editSelect" hidden>
                                    <p class="form-check-title">What would you like to edit?<span style="color: red">*</span></p>
                                    <div class="form-check progress-step-check" style="width: 33%">
                                        <input class="form-check-input" id="checkitem1"
                                               <c:if test="${editSelectResult=='update'}">checked</c:if> type="radio"
                                               name="editSelect" value="update" aria-invalid="false">
                                        <label class="form-check-label" for="checkitem1"><span
                                                class="check-circle"></span>Change the Information of this Personnel</label>
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
                                        <iais:field value="Name " width="12" mandatory="true"/>
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
                                        <iais:field value="ID No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idTypeShow"
                                                         value="${personnelEditDto.idType}"
                                                         firstOption="Please Select"
                                                         codeCategory="CATE_ID_ID_TYPE" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" needDisabled="true"
                                                        type="text"
                                                        name="idNoShow" value="${personnelEditDto.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Designation " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select cssClass="designationSel" name="designationShow"
                                                             codeCategory="CATE_ID_DESIGNATION"
                                                             value="${personnelEditDto.designation}"
                                                             firstOption="Please Select" disabled="true"></iais:select>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                        name="mobileNoShow"
                                                        value="${personnelEditDto.mobileNo}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                            name="officeTelNoShow"
                                                            value="${personnelEditDto.officeTelNo}"
                                                            needDisabled="true"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text"
                                                        name="emailAddrShow"
                                                        value="${personnelEditDto.emailAddr}"
                                                        needDisabled="true"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                </div>
                                <div id="update" class="form-horizontal" hidden>
                                    <iais:row>
                                        <iais:field value="Name " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="salutation"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${personnelEditDto.salutation}"
                                                         firstOption="Please Select"></iais:select>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_salutation"></span>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="66" type="text"
                                                        name="psnName"
                                                        value="${personnelEditDto.psnName}"></iais:input>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_psnName"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idType"
                                                         value="${personnelEditDto.idType}"
                                                         firstOption="Please Select"
                                                         codeCategory="CATE_ID_ID_TYPE" disabled="true"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" needDisabled="true"
                                                        type="text"
                                                        name="idNo" value="${personnelEditDto.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO')}">
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
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo"
                                                        value="${personnelEditDto.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
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
                                <div id="replace" class="form-horizontal" hidden>
                                    <iais:row>
                                        <iais:field value="Assign or add another personnel " width="12"/>
                                        <iais:value width="7" cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select name="replaceName" id="replaceOptionsId"
                                                         options="replaceOptions"
                                                         value="${replaceName}"
                                                         onchange="addNew()" firstOption="Please Select"></iais:select>
                                        </iais:value>
                                    </iais:row>
                                </div>
                                <br/><br/><br/><br/>
                                <div id="newPerson" hidden class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name " width="12" mandatory="true"/>
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
                                        <iais:field value="ID No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select name="idType1"
                                                         value="${newPerson.idType}"
                                                         firstOption="Please Select"
                                                         codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input maxLength="9" type="text"
                                                        name="idNo1" value="${newPerson.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Designation " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select cssClass="designationSel" name="designation1"
                                                             codeCategory="CATE_ID_DESIGNATION"
                                                             value="${newPerson.designation}"
                                                             firstOption="Please Select"></iais:select>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input maxLength="8" type="text" name="mobileNo1"
                                                        value="${newPerson.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
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
                                <div id="newPersonExist" hidden class="form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select disabled="true" name="salutation2"
                                                         codeCategory="CATE_ID_SALUTATION"
                                                         value="${newPerson.salutation}"
                                                         firstOption="Please Select"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text"
                                                        name="psnName2"
                                                        value="${newPerson.psnName}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-3">
                                            <iais:select disabled="true" name="idType2"
                                                         value="${newPerson.idType}"
                                                         firstOption="Please Select"
                                                         codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                        </iais:value>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-5">
                                            <iais:input cssClass="needDisableI" maxLength="9" type="text"
                                                        name="idNo2" value="${newPerson.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Designation " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select disabled="true" cssClass="designationSel"
                                                             name="designation2"
                                                             codeCategory="CATE_ID_DESIGNATION"
                                                             value="${newPerson.designation}"
                                                             firstOption="Please Select"></iais:select>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                        name="mobileNo2"
                                                        value="${newPerson.mobileNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                            name="officeTelNo2"
                                                            value="${newPerson.officeTelNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Email Address " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:input cssClass="needDisableI" maxLength="66" type="text"
                                                        name="emailAddr2"
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
                                    <a class="btn btn-primary next" id="previewAndSub" href="javascript:void(0);">Preview and Submit</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
            <input type="text" style="display: none" value="${AckMessage}" id="ackMessage" name="ackMessage">
            <iais:confirm msg= "RFC_ERR0013" callBack="cancel()"  needCancel="false" popupOrder="ackMessageConfim"></iais:confirm>
            <iais:confirm msg= "RFC_ERR016" callBack="cancel()"  needCancel="false" popupOrder="ackMessageEdit"></iais:confirm>
        </div>
    </div>
</form>
<script>

    $('.needDisableI').css('border-color', '#ededed');
    $('.needDisableI').css('color', '#999');
    function cancel(){
        $('#ackMessageConfim').modal('hide');
        $('#ackMessageEdit').modal('hide');
    }
    $(document).ready(function () {

        if($('#ackMessage').val()=='personnelAck'){
            $('#ackMessageConfim').modal('show');
        }
        if($('#ackMessage').val()=='personnelEdit'){
            $('#ackMessageEdit').modal('show');
        }

        $('#previewAndSub').click(function () {
            $("#menuListForm").submit();
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
            $('#newPersonExist').hide();
        });
        $('#checkitem2').click(function () {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
            $('#newPersonExist').hide();
        });

        $('#cancel').click(function () {
            $("#update").hide();
            $('#replace').hide();
            $('#newPerson').hide();
            $('#show').show();
            $('#newPersonExist').hide();
        });
        if ($('#replaceOptionsId').val() != "") {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
            $('#newPerson').show();
            $('#newPersonExist').hide();
        }

        $('#replaceOptionsId').click(function () {
            $("#update").hide();
            $('#show').hide();
            $('#newPerson').show();
            $('#newPersonExist').hide();
        });

        if ($("input[type='radio']:checked").val() == 'update') {
            $("#update").show();
            $('#show').hide();
            $('#replace').hide();
            $('#newPerson').hide();
            $("#editSelect").show();
            $('#edit').hide();
            $('#newPersonExist').hide();
        } else if ($("input[type='radio']:checked").val() == 'replace') {
            $("#update").hide();
            $('#show').hide();
            $('#replace').show();
            $('#newPerson').show();
            $("#editSelect").show();
            $('#edit').hide();
            $('#newPersonExist').hide();
        }

        if ($("input[type='radio']:checked").val() == 'replace' && $('#replaceOptionsId').val() != "") {
            $("#update").hide();
            $('#newPerson').hide();
            $('#show').hide();
            $('#newPersonExist').hide();
            $('#edit').hide();
            $('#newPersonExist').hide();
        }

        const personSelect = $('#replaceOptionsId').val();
        if (personSelect == 'new' && $("input[type='radio']:checked").val() == 'replace') {
            $("#update").hide();
            $('#newPerson').show();
            $('#show').hide();
            $('#newPersonExist').hide();
        }
        if (personSelect == '' && $("input[type='radio']:checked").val() == 'replace') {
            $('#newPerson').hide();
        }

        if (personSelect != '' && personSelect != 'new') {
            $('#newPersonExist').show();
            $('#newPerson').hide();
            var person = $('#newPersonExist');
            var arr = personSelect.split(",");
            var idType = arr[0];
            var idNo = arr[1];
            loadSelectPerson(person, idType, idNo);
        }

    });

    function addNew() {
        const personSelect = $('#replaceOptionsId').val();
        if (personSelect == 'new') {
            $("#update").hide();
            $('#newPerson').show();
            $('#show').hide();
            $('#newPersonExist').hide();
        } else if (personSelect == "") {
            $("#update").hide();
            $('#newPerson').hide();
            $('#show').hide();
            $('#newPersonExist').hide();
        } else {
            $('#newPersonExist').show();
            $('#newPerson').hide();
            var person = $('#newPersonExist');
            var arr = personSelect.split(",");
            var idType = arr[0];
            var idNo = arr[1];
            loadSelectPerson(person, idType, idNo);
        }
    }

    var fillPersonForm = function ($CurrentPsnEle, data) {
        <!--salutation-->
        var salutation = data.salutation;
        if (salutation == null || salutation == 'undefined' || salutation == '') {
            salutation = '';
        }
        $CurrentPsnEle.find('select[name="salutation2"]').val(salutation);
        var salutationVal = $CurrentPsnEle.find('option[value="' + salutation + '"]').html();
        $CurrentPsnEle.find('select[name="salutation2"]').next().find('.current').html(salutationVal);
        <!--name-->
        $CurrentPsnEle.find('input[name="psnName2"]').val(data.name);

        <!-- idType-->
        var idType = data.idType;
        if (idType == null || idType == 'undefined' || idType == '') {
            idType = '';
        }
        $CurrentPsnEle.find('select[name="idType2"]').val(idType);
        var idTypeVal = $CurrentPsnEle.find('option[value="' + idType + '"]').html();
        $CurrentPsnEle.find('select[name="idType2"]').next().find('.current').html(idTypeVal);
        <!-- idNo-->
        $CurrentPsnEle.find('input[name="idNo2"]').val(data.idNo);
        $CurrentPsnEle.find('input[name="mobileNo2"]').val(data.mobileNo);
        $CurrentPsnEle.find('input[name="emailAddr2"]').val(data.emailAddr);
        var officeTelNo = data.officeTelNo;
        if (officeTelNo != null && officeTelNo != '') {
            $CurrentPsnEle.find('input[name="officeTelNo2"]').val(officeTelNo);
        } else {
            $CurrentPsnEle.find('input[name="officeTelNo2"]').val('');
        }
        <!--Designation  -->
        var designation = data.designation;
        if (designation == null || designation == '') {
            designation = '';
        }
        $CurrentPsnEle.find('select[name="designation2"]').val(designation);
        var designationVal = $CurrentPsnEle.find('option[value="' + designation + '"]').html();
        $CurrentPsnEle.find('select[name="designation2"]').next().find('.current').html(designationVal);


        <!-- professionType-->
        var professionType = data.professionType;
        if (professionType == null || professionType == 'undefined' || professionType == '') {
            professionType = '';
        }
        $CurrentPsnEle.find('select[name="professionType2"]').val(professionType);
        var professionTypeVal = $CurrentPsnEle.find('option[value="' + professionType + '"]').html();
        $CurrentPsnEle.find('select[name="professionType2"]').next().find('.current').html(professionTypeVal);
        <!-- professionRegoNo-->
        var professionRegoNo = data.profRegNo;
        if (professionRegoNo != null && professionRegoNo != '') {
            $CurrentPsnEle.find('input[name="professionRegnNo2"]').val(professionRegoNo);
        } else {
            $CurrentPsnEle.find('input[name="professionRegnNo2"]').val('');
        }
    }
    <!--cgo,medAlert -->
    var loadSelectPerson = function ($CurrentPsnEle, idType, idNo, psnType) {
        var spcEle = $CurrentPsnEle.find('.specialty');
        var jsonData = {
            'idType': idType,
            'idNo': idNo,
            'psnType': psnType
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/psn-select-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (data == null) {
                    return;
                }
                fillPersonForm($CurrentPsnEle, data, psnType);
            },
            'error': function () {
            }
        });
    }


</script>