<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <%@include file="../cessation/head.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <br/>
                <div class="row">
                    <div class="col-lg-12 col-xs-12 cesform-box">
                        <div class="row">
                            <div class="license-info-box">
                                <div class="col-lg-6 col-xs-12">
                                    <div class="license-info">
                                        <p class="lic-no">Licence Number</p>
                                        <h4><c:out value="${appCess.licenceNo}"/></h4>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-xs-12">
                                    <div class="license-info">
                                        <p class="serv-name">Service Name</p>
                                        <h4><c:out value="${appCess.svcName}"/></h4>
                                    </div>
                                </div>
                            </div>
                            <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci">
                                <div class="col-lg-12 col-xs-12">
                                    <div class="table-gp tablebox">
                                        <div class="topheader">
                                            <c:if test="${appCessHci.hciName==null}">
                                                <h4><c:out value="${appCessHci.hciCode}"/></h4>
                                            </c:if>
                                            <c:if test="${appCessHci.hciName!=null}">
                                                <h4><c:out value="${appCessHci.hciName}"/> - <c:out
                                                        value="${appCessHci.hciCode}"/></h4>
                                            </c:if>
                                            <p><c:out value="${appCessHci.hciAddress}"/></p>
                                        </div>

                                        <iais:section title="" id="potentialAuditableHCIs">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <label style="font-size: 16px">Effective Date</label>
                                                </div>
                                                <div class="col-md-5">
                                                    <span style="font-size: 16px"><fmt:formatDate
                                                            value="${appCessHci.effectiveDate}"
                                                            pattern="dd/MM/yyyy"/></span>
                                                    <span style="font-size: 16px;float:right"><a
                                                            class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                            data-html="true"
                                                            data-original-title="${cess_ack002}">
                                                                i</a></span>
                                                </div>
                                            </div>
                                            <p></p><br><br>
                                            <iais:row>
                                                <iais:field value="Cessation Reasons"/>
                                                <iais:value width="7">
                                                    <iais:select disabled="true"
                                                                 id="reasonId"
                                                                 name="reason"
                                                                 options="reasonOption"
                                                                 value="${appCessHci.reason}"
                                                                 cssClass="nice-select cessationReasons"/>
                                                </iais:value>
                                            </iais:row>
                                            <div id="reason" style="display: none;">
                                                <iais:row>
                                                    <iais:field value="Others"/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="otherReason"
                                                                    value="${appCessHci.otherReason}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <iais:row>
                                                <iais:field value="Patients' Record will be transferred"/>
                                                <iais:value>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <div class="form-check-gp">
                                                            <div class="row">
                                                                <div class="col-xs-12 col-md-3">
                                                                    <div class="form-check disabled">
                                                                        <input class="form-check-input"
                                                                               type="radio"
                                                                               name="patRadio"
                                                                               value="yes"
                                                                               id="radioYes"
                                                                               <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                               disabled>
                                                                        <label class="form-check-label"
                                                                               for="radioYes"><span
                                                                                class="check-circle"></span>Yes</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-12 col-md-3">
                                                                    <div class="form-check disabled">
                                                                        <input class="form-check-input" type="radio"
                                                                               name="patRadio"
                                                                               value="no"
                                                                               id="radioNo"
                                                                               <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                               aria-invalid="false"
                                                                               disabled>
                                                                        <label class="form-check-label"
                                                                               for="radioNo"><span
                                                                                class="check-circle"></span>No</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </iais:value>
                                            </iais:row>
                                           <%-- <div id="patYes" hidden>
                                                <iais:row>
                                                    <iais:field width="7"
                                                                value="Who will take over your patients' case records?"/>
                                                    <iais:value width="7">
                                                        <iais:select disabled="true"
                                                                     name="patientSelect"
                                                                     options="patientsOption"
                                                                     firstOption="Please Select"
                                                                     id="patientSelectId"
                                                                     onchange="javascirpt:changePatient(this.value);"
                                                                     value="${appCessHci.patientSelect}"
                                                                     cssClass="nice-select cessationTransfer"/>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patHciName" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Name"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true"
                                                                    value="${appCessHci.patHciName}" maxLength="100"
                                                                    name="patHciName"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="${num.count}patHciName${uid.count}" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Name / Code" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true"
                                                                    value="${appCessHci.patHciName}" maxLength="100"
                                                                    name="${num.count}patHciName${uid.count}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciNamePat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Name"/>
                                                    <iais:value width="7">
                                                        <span><c:out value="${appCessHci.hciNamePat}"></c:out></span>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciCodePat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Code"/>
                                                    <iais:value width="7">
                                                        <span><c:out value="${appCessHci.hciCodePat}"></c:out></span>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciAddressPat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Address"/>
                                                    <iais:value width="7">
                                                        <span><c:out value="${appCessHci.hciAddressPat}"></c:out></span>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patRegNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Professional Regn. No."/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" cssClass="disabled"
                                                                    type="text"
                                                                    name="patRegNo"
                                                                    value="${appCessHci.patRegNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersTakeOver" hidden>
                                                <iais:row>
                                                    <iais:field value="Others"/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="patOthersTakeOver"
                                                                    value="${appCessHci.patOthers}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthers" hidden>
                                                <iais:row>
                                                    <iais:field value="Reason for no patients' records transfer"/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="patOthers"
                                                                    value="${appCessHci.patOthers}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersMobileNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Mobile No."/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true" value="${appCessHci.mobileNo}"
                                                                    maxLength="8"
                                                                    name="patOthersMobileNo"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersEmailAddress" hidden>
                                                <iais:row>
                                                    <iais:field value="Email Address"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true" value="${appCessHci.emailAddress}"
                                                                    maxLength="66"
                                                                    name="patOthersEmailAddress"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersMobileNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Mobile No." mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true" value="${appCessHci.mobileNo}"
                                                                    maxLength="8"
                                                                    name="patOthersMobileNo"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersEmailAddress" hidden>
                                                <iais:row>
                                                    <iais:field value="Email Address" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" needDisabled="true" value="${appCessHci.emailAddress}"
                                                                    maxLength="66"
                                                                    name="patOthersEmailAddress"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Reason for no patients' records transfer" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="patNoRemarks"
                                                                    value="${appCessHci.patNoRemarks}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div class="form-group" id="patNoConfirmID" hidden>
                                                <div class="col-xs-6 col-sm-4 col-md-6">
                                                    <div class="form-check disabled">
                                                        <input disabled class="form-check-input" id="patNoConfirm"
                                                               type="checkbox"
                                                               name="patNoConfirm"
                                                               <c:if test="${appCessHci.patNoConfirm != null}">checked</c:if>>
                                                        <label class="form-check-label" for="patNoConfirm"><span
                                                                class="check-square"></span><iais:message key="CESS_DEC001"/><span style="color: red">*</span></label>
                                                    </div>
                                                    <span id="error_patNoConfirm" name="iaisErrorMsg" class="error-msg"></span>
                                                </div>
                                            </div>--%>
                                            <div class="form-group" id="transferDetail" style="display: none;">
                                                <label class="col-xs-12 col-md-4">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                                                <div class="col-xs-6 col-sm-4 col-md-3">
                                                    <textarea name="transferDetail"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferDetail}</textarea>
                                                </div>
                                            </div>
                                            <div class="form-group" id="transferredWhere" style="display: none;">
                                                <label class="col-xs-12 col-md-4">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                                                <div class="col-xs-6 col-sm-4 col-md-3">
                                                    <textarea name="transferredWhere"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferredWhere}</textarea>
                                                </div>
                                            </div>
                                        </iais:section>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${specLicInfo !=null}">
                            <c:forEach items="${specLicInfo}" var="map">
                                <c:set var="licNo" value="${map.key}"></c:set>
                                <c:if test="${appCess.licenceNo==licNo}">
                                    <div><h4>The following specified healthcare services will also be ceased as
                                        their underlying <iais:code needLowerCase="true" code="CDN001"/>(s) is/are listed above.</h4>
                                    </div>
                                    <table aria-describedby="" class="table-gp tablebox">
                                        <tr style="text-align:center">
                                            <th scope="col" style="text-align:center;width: 0%">S/N</th>
                                            <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/> Licence No.
                                            </th>
                                            <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/> Name</th>
                                            <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN001"/> Licence No.</th>
                                            <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN001"/> Name</th>
                                        </tr>
                                        <c:forEach items="${map.value}" var="spec" varStatus="index">
                                            <tr style="text-align:center">
                                                <td>
                                                    <p><c:out value="${index.count}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${spec.specLicNo}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${spec.specSvcName}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${spec.baseLicNo}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${spec.baseSvcName}"/></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
                <br/>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href= "#" onclick="confirmBack('back')"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="button-group"><a class="btn btn-primary next" onclick="confirmSubmit('submit')">Submit</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<style>
    .col-md-5 {
        width: 30%;
    }

    .col-md-4 {
        width: 35%;
    }

    .main-content {
        margin-top: 99px;
        width: 90%;
        padding-left: 10px;
        padding-right: 10px;
    }

    ul li {
        list-style: disc;
        padding-left: 16px;
        position: relative;
        font-size: 1.6rem;
        margin-bottom: 10px;
    }

    ul {
        padding-left: 20px;
    }

    #effectiveDate {
        margin-bottom: 0px;
    }

    input[type='text'] {
        margin-bottom: 0px;
    }
</style>
<script type="text/javascript">

    function confirmSubmit(action) {
        showWaiting();
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function confirmBack(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function changeReason() {

        if ($("#reasonId").val() == "CES001") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
    }

    /*function changePatient() {
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthersTakeOver").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patRegNo").hide();
            $("#patOthersMobileNo").show();
            $("#patOthersEmailAddress").show();
        } else if ($("#patientSelectId").val() == "CES005"&&$('#radioYes').is(':checked')) {
            $("#patHciName").show();
            $("#hciName").show();
            $("#patOthersTakeOver").hide();
            $("#patRegNo").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
            $("#hciNamePat").show();
            $("#hciCodePat").show();
            $("#hciAddressPat").show();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersTakeOver").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
        }
    }*/

    function changePatSelect() {
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
            $("#transferDetail").hide();
            $("#transferredWhere").show();
        } else if ($('#radioNo').is(':checked')) {
            $("#transferDetail").show();
            $("#transferredWhere").hide();
        }
    }

    $(document).ready(function () {
        //changePatient();
        changeReason();
        changePatSelect();
    });
    $(document).ready(function () {
        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersTakeOver").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });

    $(document).ready(function () {
        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersTakeOver").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });

</script>