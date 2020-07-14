<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-internet"/>
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
                    <c:forEach items="${appCessationDtos}" var="appCess" varStatus="num">
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
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                                    <div class="col-lg-12 col-xs-12">
                                        <div class="table-gp tablebox">
                                            <div class="topheader">
                                                <h4><c:out value="${appCessHci.hciName}"/> - <c:out
                                                        value="${appCessHci.hciCode}"/></h4>
                                                <p><c:out value="${appCessHci.hciAddress}"/></p>
                                            </div>

                                            <iais:section title="" id="potentialAuditableHCIs">
                                                <iais:row>
                                                    <iais:field value="Effective Date"/>
                                                    <iais:value width="7">
                                                        <iais:datePicker id="effectiveDate"
                                                                         name="${num.count}effectiveDate${uid.count}"
                                                                         dateVal="${appCessHci.effectiveDate}"/>
                                                    </iais:value>
                                                    <iais:value>
                                                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                           data-html="true"
                                                           data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Cessation Reasons"/>
                                                    <iais:value width="7">
                                                        <iais:select name="${num.count}reason${uid.count}"
                                                                     id="${num.count}reasonId${uid.count}"
                                                                     options="reasonOption" firstOption="Please Select"
                                                                     onchange="javascirpt:changeReason(this.value);"
                                                                     value="${appCessHci.reason}"/>
                                                    </iais:value>
                                                </iais:row>
                                                <div id="${num.count}reason${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" maxLength="200"
                                                                        name="${num.count}otherReason${uid.count}"
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
                                                                        <div class="form-check">
                                                                            <input class="form-check-input"
                                                                                   id="${num.count}radioYes${uid.count}"
                                                                                   type="radio"
                                                                                   name="${num.count}patRadio${uid.count}"
                                                                                   value="yes"
                                                                                   <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                                   onchange="javascirpt:changePatSelect(this.value);"
                                                                                   aria-invalid="false">
                                                                            <label class="form-check-label"
                                                                                   for=${num.count}radioYes${uid.count}"><span
                                                                                    class="check-circle"></span>Yes</label>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-xs-12 col-md-3">
                                                                        <div class="form-check">
                                                                            <input class="form-check-input"
                                                                                   id="${num.count}radioNo${uid.count}"
                                                                                   type="radio"
                                                                                   name="${num.count}patRadio${uid.count}"
                                                                                   value="no"
                                                                                   <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                                   onchange="javascirpt:changePatSelect(this.value);"
                                                                                   aria-invalid="false">
                                                                            <label class="form-check-label"
                                                                                   for="${num.count}radioNo${uid.count}"><span
                                                                                    class="check-circle"></span>No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div> <span id="error_${num.count}patRadio${uid.count}" name="iaisErrorMsg" class="error-msg"></span></div>
                                                            </div>
                                                        </div>
                                                    </iais:value>
                                                </iais:row>
                                                <div id="${num.count}patYes${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field width="7"
                                                                    value="Who will take over your patients' case records?"/>
                                                        <iais:value width="7">
                                                            <iais:select
                                                                    name="${num.count}patientSelect${uid.count}"
                                                                    options="patientsOption"
                                                                    firstOption="Please Select"
                                                                    id="${num.count}patientSelectId${uid.count}"
                                                                    onchange="javascirpt:changePatient(this.value);"
                                                                    value="${appCessHci.patientSelect}"/>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patHciName${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patHciName}"
                                                                        maxLength="100"
                                                                        name="${num.count}patHciName${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patRegNo${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" maxLength="20"
                                                                        name="${num.count}patRegNo${uid.count}"
                                                                        value="${appCessHci.patRegNo}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patOthers${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patOthers}"
                                                                        maxLength="100"
                                                                        name="${num.count}patOthers${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patOthers${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patOthers}"
                                                                        maxLength="100"
                                                                        name="${num.count}patOthers${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patNo${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value=""/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patNoRemarks}"
                                                                        maxLength="200"
                                                                        name="${num.count}patNoRemarks${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <iais:row>
                                                    <iais:field value="To Cease"/>
                                                    <iais:value width="7">
                                                        <div class="form-check-gp">
                                                            <div class="row">
                                                                <div class="col-xs-12 col-md-2">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input"
                                                                               id="icon5checkboxSample" type="checkbox"
                                                                               name="${num.count}whichTodo${uid.count}"
                                                                               value="${appCessHci.premiseId}"
                                                                               <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>
                                                                               aria-invalid="false">
                                                                        <label class="form-check-label"
                                                                               for="icon5checkboxSample"><span
                                                                                class="check-square"></span></label>
                                                                        <span id="error_whichTodo" name="iaisErrorMsg"
                                                                              class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </iais:value>
                                                </iais:row>
                                            </iais:section>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <c:if test="${specLicInfo !=null}">
                                <div><h4>The following specified healthcare services will also be ceased as their underlying licensable healthcare service(s) is/are listed above.</h4></div>
                                <table class="table-gp tablebox">
                                <tr style="text-align:center">
                                    <th style="text-align:center;width: 0%">S/N</th>
                                    <th style="text-align:center;width: 25%">Specified Service Licence No.</th>
                                    <th style="text-align:center;width: 25%">Specified Service Name</th>
                                    <th style="text-align:center;width: 25%">Base Service Licence No.</th>
                                    <th style="text-align:center;width: 25%">Base Service Name</th>
                                </tr>
                                <tr style="text-align:center">
                                    <c:forEach items="${specLicInfo}" var="spec" varStatus="index">
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
                        </div>
                    </c:forEach>
                </div>
                <br/>
                <div class="form-check">
                    <ul>
                        <li>
                            <p> The Applicant must notify the Director of Medical Services in writing at least 30 days before
                                the cessation of operation, letting, sale or disposal of his private hospital, medical
                                clinic or clinical laboratory.</p>
                        </li>
                        <li>
                            <p> Any Applicant of a licensed healthcare institution (For e.g a medical clinic) who intends to
                                cease operating the medical clinic shall take all measures as are reasonable and necessary
                                to ensure that the medical records of every patient are properly transferred to the medical
                                clinic or other healthcare institution to which such patient is to be transferred.</p>
                        </li>
                    </ul>
                    <input class="form-check-input" id="confirmInfo" type="checkbox" name="readInfo"
                           <c:if test="${readInfo != null}">checked</c:if> aria-invalid="false">
                    <label class="form-check-label" for="confirmInfo"><span class="check-square"></span>I have read and
                        agreed with the above information</label>
                </div>
                <div id="readInfo" hidden><span class="error-msg">Please agree to the declaration statement</span></div>
                <div><span id="error_choose" name="iaisErrorMsg" class="error-msg"/></div>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="/hcsa-licence-web/eservice/INTRANET/MohLicenceManagement"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="button-group"><a class="btn btn-primary next" onclick="submitSure('submit')">Next</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
<style>
    .col-md-5 {
        width: 26%;
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
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "reasonId" + j).val() == "CES001") {
                    $("#" + i + "reason" + j).show();
                } else {
                    $("#" + i + "reason" + j).hide();
                }
            }
        }
    }

    function changePatient() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthers" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005") {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                }
            }
        }
    }

    function changePatSelect() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patNo" + j).show();
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                }
            }
        }
    }

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "reasonId" + j).val() == "CES001") {
                    $("#" + i + "reason" + j).show();
                } else if ($("#" + i + "reasonId" + j).val() != "CES001") {
                    $("#" + i + "reason" + j).hide();
                }
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "patNo" + j).show();
                }
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthers" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005") {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                }
            }
        }
    });

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
        }
    });

</script>