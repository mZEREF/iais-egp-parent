<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-internet"/>

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
                                                            data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">
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
                                            <div id="reason" hidden>
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
                                            <div id="patYes" hidden>
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
                                            <div id="patRegNo" hidden>
                                                <iais:row>
                                                    <iais:field value="Professional Regn No."/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" cssClass="disabled"
                                                                    type="text"
                                                                    name="patRegNo"
                                                                    value="${appCessHci.patRegNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                            <div id="patOthers" hidden>
                                                <iais:row>
                                                    <iais:field value="Others"/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="patOthers"
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
                                            <div id="patNo" hidden>
                                                <iais:row>
                                                    <iais:field value=""/>
                                                    <iais:value width="7">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="patNoRemarks"
                                                                    value="${appCessHci.patNoRemarks}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
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
                                            <th style="text-align:center;width: 25%">Specified Service Licence No.
                                            </th>
                                            <th style="text-align:center;width: 25%">Specified Service Name</th>
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
                            <p> The ASO must notify the Director of Medical Services in writing at least 30 days
                                before
                                the cessation of operation, letting, sale or disposal of his private hospital, medical
                                clinic or clinical laboratory.</p>
                        </li>
                        <li>
                            <p> Any ASO of a licensed healthcare institution (For e.g a medical clinic) who
                                intends to
                                cease operating the medical clinic shall take all measures as are reasonable and
                                necessary
                                to ensure that the medical records of every patient are properly transferred to the
                                medical
                                clinic or other healthcare institution to which such patient is to be transferred.</p>
                        </li>
                    </ul>
                    <div class="form-check disabled">
                        <input class="form-check-input" id="confirmInfo" disabled type="radio"
                               name="whichTodo" checked aria-invalid="false">
                        <label class="form-check-label" for="confirmInfo"><span class="check-square"></span>I have read
                            and
                            agreed with the above information</label>
                    </div>
                </div>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a onclick="confirmBack('back')"><em class="fa fa-angle-left"></em> Back</a>
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

    function changePatient() {
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES005") {
            $("#patHciName").show();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#patOthers").hide();
        }
    }

    function changePatSelect() {
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
        } else if ($('#radioNo').is(':checked')) {
            $("#patNo").show();
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        }
    }


    $(document).ready(function () {
        if ($("#reasonId").val() == "CES001") {
            $("#reason").show();
        } else if ($("#reasonId").val() != "CES001") {
            $("#reason").hide();
        }
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
        } else if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#patNo").show();
        }
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES005") {
            $("#patHciName").show();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#patOthers").hide();
        }
    });

    $(document).ready(function () {
        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#patOthers").hide();
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
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });

</script>