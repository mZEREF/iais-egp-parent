<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <br/><br/><br/><br/>
    <%@include file="../cessation/head.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <h2>Please key in cessation information</h2>
                </div>
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
                                            <iais:row>
                                                <iais:field value="Effective Date" mandatory="true"/>
                                                <iais:value width="7">
                                                    <iais:datePicker id="effectiveDate"
                                                                     name="effectiveDate"
                                                                     dateVal="${appCessHci.effectiveDate}"/>
                                                </iais:value>
                                                <iais:value>
                                                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                       data-html="true"
                                                       data-original-title="<p>The ASO must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Cessation Reasons" mandatory="true"/>
                                                <iais:value width="7">
                                                    <iais:select name="reason"
                                                                 id="reasonId"
                                                                 options="reasonOption" firstOption="Please Select"
                                                                 onchange="javascirpt:changeReason(this.value);"
                                                                 value="${appCessHci.reason}"
                                                                 cssClass="nice-select cessationReasons" needSort="false"/>
                                                </iais:value>
                                            </iais:row>
                                            <div id="reason" hidden>
                                                <iais:row>
                                                    <iais:field value="Others" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" maxLength="200"
                                                                    name="otherReason"
                                                                    value="${appCessHci.otherReason}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <iais:row>
                                                <iais:field value="Patients' Record will be transferred" mandatory="true"/>
                                                <iais:value>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <div class="form-check-gp">
                                                            <div class="row">
                                                                <div class="col-xs-12 col-md-3">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input"
                                                                               id="radioYes"
                                                                               type="radio"
                                                                               name="patRadio"
                                                                               value="yes"
                                                                               <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                               onchange="javascirpt:changePatSelect(this.value);"
                                                                               aria-invalid="false">
                                                                        <label class="form-check-label"
                                                                               for="radioYes"><span
                                                                                class="check-circle"></span>Yes</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-12 col-md-3">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input"
                                                                               id="radioNo"
                                                                               type="radio"
                                                                               name="patRadio"
                                                                               value="no"
                                                                               <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                               onchange="javascirpt:changePatSelect(this.value);"
                                                                               aria-invalid="false">
                                                                        <label class="form-check-label"
                                                                               for="radioNo"><span
                                                                                class="check-circle"></span>No</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div><span id="error_patRadio"
                                                                       name="iaisErrorMsg" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </iais:value>
                                            </iais:row>
                                            <div id="patYes" hidden>
                                                <iais:row>
                                                    <iais:field width="7"
                                                                value="Who will take over your patients' case records?" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:select
                                                                name="patientSelect"
                                                                options="patientsOption"
                                                                firstOption="Please Select"
                                                                id="patientSelectId"
                                                                onchange="javascirpt:changePatient(this.value);"
                                                                value="${appCessHci.patientSelect}"
                                                                cssClass="nice-select cessationTransfer" needSort="false"/>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patHciName" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Name / Code" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <input type="text" maxLength="100"
                                                               name="patHciName"
                                                               onblur="javascript:getHci(this);"
                                                               id="hciName"
                                                               value="${appCessHci.patHciName}">
                                                        <span id="error_patHciName"
                                                              name="iaisErrorMsg" class="error-msg"></span>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciNamePat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Name"/>
                                                    <iais:value width="7">
                                                        <div class="nameLoad"></div>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciCodePat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Code"/>
                                                    <iais:value width="7">
                                                        <div class="codeLoad"></div>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="hciAddressPat" hidden>
                                                <iais:row>
                                                    <iais:field value="HCI Address"/>
                                                    <iais:value width="7">
                                                        <div class="addressLoad"></div>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patRegNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Professional Regn. No." mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" maxLength="20"
                                                                    name="patRegNo"
                                                                    value="${appCessHci.patRegNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersTakeOver" hidden>
                                                <iais:row>
                                                    <iais:field value="Others" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" value="${appCessHci.patOthers}"
                                                                    maxLength="100"
                                                                    name="patOthersTakeOver"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersMobileNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Mobile No." mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" value="${appCessHci.mobileNo}"
                                                                    maxLength="8"
                                                                    name="patOthersMobileNo"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthersEmailAddress" hidden>
                                                <iais:row>
                                                    <iais:field value="Email Address" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" value="${appCessHci.emailAddress}"
                                                                    maxLength="66"
                                                                    name="patOthersEmailAddress"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Reason for no patients' records transfer" mandatory="true"/>
                                                    <iais:value width="7">
                                                        <iais:input type="text" value="${appCessHci.patNoRemarks}"
                                                                    maxLength="200"
                                                                    name="patNoRemarks"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div class="form-group" id="patNoConfirmID"
                                                 hidden>
                                                <div class="col-xs-6 col-sm-4 col-md-6">
                                                    <div class="form-check">
                                                        <input class="form-check-input" id="patNoConfirm"
                                                               type="checkbox"
                                                               name="patNoConfirm"
                                                               <c:if test="${appCessHci.patNoConfirm != null}">checked</c:if>>
                                                        <label class="form-check-label" for="patNoConfirm"><span
                                                                class="check-square"></span><iais:message key="CESS_DEC001"/><span style="color: red">*</span></label>
                                                    </div>
                                                    <span id="error_patNoConfirm" name="iaisErrorMsg" class="error-msg"></span>
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
                                        their underlying licensable healthcare service(s) is/are listed above.</h4>
                                    </div>
                                    <table class="table-gp tablebox">
                                        <tr style="text-align:center">
                                            <th style="text-align:center;width: 0%">S/N</th>
                                            <th style="text-align:center;width: 25%">Special Licensable Service Licence No.
                                            </th>
                                            <th style="text-align:center;width: 25%">Special Licensable Service Name</th>
                                            <th style="text-align:center;width: 25%">Base Service Licence No.</th>
                                            <th style="text-align:center;width: 25%">Base Service Name</th>
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
                <div class="form-check">
                    <ul>
                        <li>
                            <p> The ASO must notify the Director of Medical Services in writing at least 30 days before
                                the cessation of operation, letting, sale or disposal of his private hospital, medical
                                clinic or clinical laboratory.</p>
                        </li>
                        <li>
                            <p> Any ASO of a licensed healthcare institution (For e.g a medical clinic) who intends to
                                cease operating the medical clinic shall take all measures as are reasonable and
                                necessary
                                to ensure that the medical records of every patient are properly transferred to the
                                medical
                                clinic or other healthcare institution to which such patient is to be transferred.</p>
                        </li>
                    </ul>
                    <input class="form-check-input" id="confirmInfo" type="checkbox" name="readInfo"
                           <c:if test="${readInfo != null}">checked</c:if> aria-invalid="false">
                    <label class="form-check-label" for="confirmInfo"><span class="check-square"></span>I have read and
                        agreed with the above information</label>
                </div>
                <div id="readInfo" hidden><span class="error-msg"><iais:message key="CESS_ERR001"/></span></div>
                <div><span id="error_choose" name="iaisErrorMsg" class="error-msg"/></div>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12">
                            <div class="button-group"><a class="btn btn-primary next" onclick="submitSure('submit')">Next</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel"
             style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-body" style="text-align: center;">
                        <div class="row">
                            <div class="col-md-12"><span style="font-size: 2rem;">PRS  mock server down</span></div>
                        </div>
                    </div>
                    <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                        <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                                data-dismiss="modal" onclick="cancel()">OK
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT">
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
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


    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function submitSure(action) {
        if ($('#confirmInfo').is(':checked')) {
            submit(action);
        } else {
            $('#readInfo').show();
        }
    }

    function back(action) {
        submit(action);
    }

    function changeReason() {
        if ($("#reasonId").val() == "CES001") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
    }

    function changePatient() {
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthersTakeOver").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patRegNo").hide();
            $("#patOthersMobileNo").show();
            $("#patOthersEmailAddress").show();
            $("#hciNamePat").hide();
            $("#hciCodePat").hide();
            $("#hciAddressPat").hide();
        } else if ($("#patientSelectId").val() == "CES005"&&$('#radioYes').is(':checked')) {
            $( "#hciName").trigger('blur');
            $("#patHciName").show();
            $("#hciName").show();
            $("#patOthersTakeOver").hide();
            $("#patRegNo").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersTakeOver").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
            $("#hciNamePat").hide();
            $("#hciCodePat").hide();
            $("#hciAddressPat").hide();
        }
    }

    function getHci(obj) {
        var value = $(obj).val();
        loadHci(value, obj);
    }

    const loadHci = function (hciNameCode, obj) {
        const jsonData = {
            'hciNameCode': hciNameCode,
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/hci-info?stamp='+new Date().getTime(),
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                loadJsp(data,obj);
            },
            'error': function (data) {
                deleteJsp(data,obj);
            }
        });
    };

    const loadJsp = function (data, obj) {
        var hciNme = $(obj).closest('.form-group').parent().next();
        hciNme.show();
        var hciCode = hciNme.next();
        hciCode.show();
        var hciAddress = hciCode.next();
        hciAddress.show();
        hciNme.find('.nameLoad').html(data.hciName);
        hciCode.find('.codeLoad').html(data.hciCode);
        hciAddress.find('.addressLoad').html(data.hciAddress);
    }

    const deleteJsp = function (data, obj) {
        var hciNme = $(obj).closest('.form-group').next('.form-group');
        hciNme.hide();
        var hciCode = hciNme.next();
        hciCode.hide();
        var hciAddress = hciCode.next();
        hciAddress.hide();
    }

    function changePatSelect() {
        changePatientAjax();
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
            $("#patNoConfirmID").hide();
        } else if ($('#radioNo').is(':checked')) {
            $("#patNo").show();
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersTakeOver").hide();
            $("#patRegNo").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
            $("#hciNamePat").hide();
            $("#hciCodePat").hide();
            $("#hciAddressPat").hide();
            $("#patNoConfirmID").show();
        }
    }

    $(document).ready(function () {
        changePatient();
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

    function cancel() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }

    $(document).ready(function () {
        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
    });

</script>