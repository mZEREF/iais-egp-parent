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
                                    <p style="font-size: 3rem">
                                        <c:out value="${oldPersonnelDto.psnName}"/>,&nbsp
                                        <c:out value="${oldPersonnelDto.idNo}"/>&nbsp;
                                        (<iais:code code="${oldPersonnelDto.idType}"/>)
                                    </p>
                                    <h4>Changes made will be applied to all licences associated with this personnel.
                                        Please note that payment is required for each affected licence.</h4>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_licenceStatus"></span>
                                <br/>
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr style="font-size: 2rem">
                                        <th scope="col" >Licence</th>
                                        <th scope="col" >Licence No.</th>
                                        <th scope="col" >Roles</th>
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
                                                    <c:when test="${'CD'==psnType}">
                                                        <p> </p>
                                                    </c:when>
                                                    <c:when test="${'KAH'==psnType}">
                                                        <p> </p>
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
                                                    <c:when test="${'CD'==psnType}">
                                                        <p>Clinical Governance Officer</p>
                                                    </c:when>
                                                    <c:when test="${'KAH'==psnType}">
                                                        <p>Key Appointment Holder</p>
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
                                <div class="form-check-gp" id="editSelect" style="display:none;">
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
                                    <a class="cancel" id="cancel" href="javascript:void(0);">Cancel</a>
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
                                            <iais:input cssClass="needDisableI" maxLength="20" needDisabled="true"
                                                        type="text"
                                                        name="idNoShow" value="${personnelEditDto.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row cssClass="nationalityDiv">
                                        <iais:field value="Country of issuance " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select name="nationalityShow" firstOption="Please Select"
                                                         codeCategory="CATE_ID_NATIONALITY" cssClass="nationalitySel"
                                                         value="${personnelEditDto.nationality}" disabled="true"/>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO') ||psnTypes.contains('CD')}">
                                        <iais:row>
                                            <iais:field value="Designation " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                                <iais:select cssClass="designationSel" name="designationShow"
                                                             codeCategory="CATE_ID_DESIGNATION"
                                                             value="${personnelEditDto.designation}"
                                                             firstOption="Please Select" disabled="true"></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${personnelEditDto.designation=='DES999'}">
                                            <iais:row>
                                                <iais:field value="" width="12" />
                                                <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 ">
                                                    <iais:input cssClass=" needDisableI" name="otherDesignationShow"
                                                                maxLength="100" type="text"
                                                                value="${personnelEditDto.otherDesignation}" needDisabled="true"></iais:input>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                            name="mobileNoShow"
                                                            value="${personnelEditDto.mobileNo}"
                                                            needDisabled="true"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
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
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Email Address " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="320" type="text"
                                                            name="emailAddrShow"
                                                            value="${personnelEditDto.emailAddr}"
                                                            needDisabled="true"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                </div>
                                <div id="update" class="form-horizontal" style="display:none;">
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
                                    <iais:row cssClass="nationalityDiv">
                                        <iais:field value="Country of issuance " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="nationalitySel" name="nationality"
                                                         codeCategory="CATE_ID_NATIONALITY" value="${personnelEditDto.nationality}"
                                                         firstOption="Please Select" disabled="true"/>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO') ||psnTypes.contains('CD')}">
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
                                            <iais:field value="" width="12" />
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 ">
                                                <iais:input cssClass="otherDesignation hidden" name="otherDesignation"
                                                            maxLength="100" type="text"
                                                            value="${personnelEditDto.otherDesignation}" ></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="mobileNo"
                                                            value="${personnelEditDto.mobileNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="officeTelNo"
                                                            value="${personnelEditDto.officeTelNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Email Address " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="320" type="text" name="emailAddr"
                                                            value="${personnelEditDto.emailAddr}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                </div>
                                <div id="replace" class="form-horizontal" style="display:none;">
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
                                <div id="newPerson" style="display:none;" class="form-horizontal">
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
                                            <iais:input maxLength="20" type="text"
                                                        name="idNo1" value="${newPerson.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row cssClass="nationalityDiv">
                                        <iais:field value="Country of issuance " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                            <iais:select cssClass="nationalitySel" name="nationality1"
                                                         codeCategory="CATE_ID_NATIONALITY" value="${newPerson.nationality}"
                                                         firstOption="Please Select"/>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO') || psnTypes.contains('CD')}">
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
                                            <iais:field value="" width="12" />
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 ">
                                                <iais:input cssClass="otherDesignation1 hidden" name="otherDesignation1"
                                                            maxLength="100" type="text"
                                                            value="${newPerson.otherDesignation}" ></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="mobileNo1"
                                                            value="${newPerson.mobileNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${psnTypes.contains('PO') || psnTypes.contains('DPO')}">
                                        <iais:row>
                                            <iais:field value="Office Telephone No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="8" type="text" name="officeTelNo1"
                                                            value="${newPerson.officeTelNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Email Address " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input maxLength="320" type="text" name="emailAddr1"
                                                            value="${newPerson.emailAddr}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                </div>
                                <div id="newPersonExist" style="display:none;" class="form-horizontal">
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
                                            <iais:input cssClass="needDisableI" maxLength="20" type="text"
                                                        name="idNo2" value="${newPerson.idNo}"></iais:input>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row cssClass="nationalityDiv">
                                        <iais:field value="Country of issuance " width="12" mandatory="true"/>
                                        <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 input-with-label">
                                            <iais:select name="nationality2" firstOption="Please Select"
                                                         codeCategory="CATE_ID_NATIONALITY" cssClass="nationalitySel"
                                                         value="${newPerson.nationality}" disabled="true"/>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${psnTypes.contains('CGO') || psnTypes.contains('PO') || psnTypes.contains('DPO') ||psnTypes.contains('CD')}">
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
                                            <iais:row>
                                                <iais:field value="" width="12" />
                                                <iais:value cssClass="col-xs-12 col-sm-7 col-md-8 ">
                                                    <iais:input cssClass="otherDesignation2 needDisableI hidden" name="otherDesignation2"
                                                                maxLength="100" type="text"
                                                                value="${newPerson.otherDesignation}" ></iais:input>
                                                </iais:value>
                                            </iais:row>

                                    </c:if>
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Mobile No. " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="8" type="text"
                                                            name="mobileNo2"
                                                            value="${newPerson.mobileNo}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
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
                                    <c:if test="${onlyKAH != '1'}">
                                        <iais:row>
                                            <iais:field value="Email Address " width="12" mandatory="true"/>
                                            <iais:value cssClass="col-xs-12 col-sm-7 col-md-8">
                                                <iais:input cssClass="needDisableI" maxLength="320" type="text"
                                                            name="emailAddr2"
                                                            value="${newPerson.emailAddr}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 ">
                                <a class="back" id="back" href="javascript:void(0);"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div class="col-xs-12 col-sm-6 text-right">
                                <div class="button-group">
                                    <a class="btn btn-primary next" id="previewAndSub" href="javascript:void(0);">Preview</a>
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
<iais:confirm msg="${showOtherError}" callBack="$('#showOtherError').modal('hide');" popupOrder="showOtherError"
              needCancel="false" needEscapHtml="false"/>
<input type="hidden" value="${not empty showOtherError ? '1' : ''}" id="showOtherErrorCheck">
<script>

    $('.needDisableI').css('border-color', '#ededed');
    $('.needDisableI').css('color', '#999');
    function cancel(){
        $('#ackMessageConfim').modal('hide');
        $('#ackMessageEdit').modal('hide');
    }
    $(document).ready(function () {
        if($('#showOtherErrorCheck').val()!=''){
            $('#showOtherError').modal('show');
        }
        var flag=false;
        $('.designationSel').each(function (){
            if($(this).val()=='DES999'){
                flag=true;
            }
        });
        if(flag){
            if($("input[type='radio']:checked").val() == 'replace'){
                $('.designationSel').closest('.form-group').next('.form-group').find('.otherDesignation1').removeClass('hidden');
            }else if($("input[type='radio']:checked").val() == 'update'){
                $('.designationSel').closest('.form-group').next('.form-group').find('.otherDesignation').removeClass('hidden');
            }
        }else {
            $('.designationSel').closest('.form-group').next('.form-group').find('.otherDesignation').addClass('hidden');
            $('.designationSel').closest('.form-group').next('.form-group').find('.otherDesignation1').addClass('hidden');
        }

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
            toggleOnSelect($('#update').find('select[name="idType"]'), 'IDTYPE003', $('#update').find('.nationalityDiv'));
            $("#update").show();
            $('#replace').hide();
            $('#newPerson').hide();
            $('#show').hide();
            $('#newPersonExist').hide();
            $('.designationSel').trigger('change');
        });

        $('#checkitem2').click(function () {
            $("#update").hide();
            $('#show').hide();
            // init value
            clearFields('#replaceOptionsId');
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
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            loadSelectPerson(person, nationality, idType, idNo);
        }

        initNationality('#newPerson', 'select[name="idType1"]', '.nationalityDiv');
        toggleOnSelect($('#show').find('select[name="idTypeShow"]'), 'IDTYPE003', $('#show').find('.nationalityDiv'));
    });

    $('.designationSel').change(function (){
        if($(this).val()=='DES999'&&$("input[type='radio']:checked").val() == 'replace'){
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation1').removeClass('hidden');
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation2').removeClass('hidden');
        }else if($(this).val()=='DES999'&&$("input[type='radio']:checked").val() == 'update') {
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation').removeClass('hidden');
        }else if($(this).val()!='DES999'){
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation').addClass('hidden');
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation1').addClass('hidden');
            $(this).closest('.form-group').next('.form-group').find('.otherDesignation2').addClass('hidden');
        }
    });

    function addNew() {
        const personSelect = $('#replaceOptionsId').val();
        clearFields("#newPerson");
        toggleOnSelect($("#newPerson").find('select[name="idType1"]'), 'IDTYPE003', $("#newPerson").find(".nationalityDiv"));
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
            toggleOnSelect($(ele).find(idTypeTag), 'IDTYPE003', $(ele).find(nationalityDiv));
        } else {
            $('#newPersonExist').show();
            $('#newPerson').hide();
            var person = $('#newPersonExist');
            var arr = personSelect.split(",");
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            loadSelectPerson(person, nationality, idType, idNo);
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
        fillValue($CurrentPsnEle.find('select[name="idType2"]'), data.idType);
        <!-- idNo-->
        $CurrentPsnEle.find('input[name="idNo2"]').val(data.idNo);
        <!-- Nationality -->
        fillValue($CurrentPsnEle.find('select[name="nationality2"]'), data.nationality);
        toggleOnSelect($CurrentPsnEle.find('select[name="idType2"]'), 'IDTYPE003', $CurrentPsnEle.find('.nationalityDiv'));

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
        if(designation=='DES999'){
            $('.otherDesignation2').removeClass('hidden');
        }else {
            $('.otherDesignation2').addClass('hidden');
        }
        var otherDesignation = data.otherDesignation;
        $CurrentPsnEle.find('input[name="otherDesignation2"]').val(otherDesignation);
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
    var loadSelectPerson = function ($CurrentPsnEle, nationality, idType, idNo, psnType) {
        var spcEle = $CurrentPsnEle.find('.specialty');
        var jsonData = {
            'nationality': nationality,
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

    function initNationality(parent, idTypeTag, nationalityDiv) {
        $(parent).find(idTypeTag).on('change', function () {
            var $content = $(this).closest(parent.replace(':last', ''));
            toggleOnSelect(this, 'IDTYPE003', $content.find(nationalityDiv));
        });
        $(parent).each(function (index, ele) {
            toggleOnSelect($(ele).find(idTypeTag), 'IDTYPE003', $(ele).find(nationalityDiv));
        });
    }

</script>