<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <%@include file="../cessation/cessationViewHead.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="row">
                    <div class="col-lg-12 col-xs-12 cesform-box">
                        <div class="row">
                            <div class="license-info-box">
                                <div class="col-lg-6 col-xs-12">
                                    <div class="license-info">
                                        <p class="lic-no">Licence Number</p>
                                        <h4><c:out value="${confirmDto.licenceNo}"/></h4>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-xs-12">
                                    <div class="license-info">
                                        <p class="serv-name">Service Name</p>
                                        <h4><c:out value="${confirmDto.svcName}"/></h4>
                                    </div>
                                </div>
                            </div>
                            <c:forEach items="${confirmDto.appCessHciDtos}" var="appCessHci">
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
                                                <iais:field value="Effective Date"/>
                                                <iais:value width="7">
                                                    <fmt:formatDate value="${appCessHci.effectiveDate}"
                                                                    pattern="dd/MM/yyyy"/>
                                                </iais:value>
                                                <iais:value>
                                                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                       data-html="true"
                                                       data-original-title="<p>The ASO must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Cessation Reasons"/>
                                                <iais:value width="7">
                                                    <iais:select disabled="true"
                                                                 id="reasonId"
                                                                 name="reason"
                                                                 options="reasonOption"
                                                                 value="${appCessHci.reason}"/>
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
                                                                <div class="col-xs-12 col-md-4">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input"
                                                                               type="radio"
                                                                               name="patRadio"
                                                                               value="yes"
                                                                               id="radioYes"
                                                                               <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                               aria-invalid="false"
                                                                               disabled>
                                                                        <label class="form-check-label"
                                                                               for="radioYes"><span
                                                                                class="check-circle"></span>Yes</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-12 col-md-4">
                                                                    <div class="form-check">
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
                                                                     value="${appCessHci.patientSelect}"/>
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
                                                    <iais:field value="Professional Regn. No."/>
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
                                                    <iais:field value="Reason for no patients' records transfer"/>
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
                            <div><h4>The following specified healthcare services will also be ceased as their
                                underlying licensable healthcare service(s) is/are listed above.</h4></div>
                            <table class="table-gp tablebox">
                                <tr style="text-align:center">
                                    <th style="text-align:center;width: 0%">S/N</th>
                                    <th style="text-align:center;width: 25%">Specified Service Licence No.</th>
                                    <th style="text-align:center;width: 25%">Specified Service Name</th>
                                    <th style="text-align:center;width: 25%">Base Service Licence No.</th>
                                    <th style="text-align:center;width: 25%">Base Service Name</th>
                                </tr>
                                <c:forEach items="${specLicInfo}" var="spec" varStatus="index">
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
                    </div>
                </div>
                <br/>
            </div>
        </div>
    </div>
    <div class="modal fade" id="singlePremise" tabindex="-1" role="dialog" aria-labelledby="singlePremise"
         style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <div class="modal-title" id="gridSystemModalLabel" style="font-size:2rem;">Confirmation Box</div>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12"><span style="font-size: 2rem">Please confirm the cessation of this licence</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${rfi=='rfi'}">
        <div class="row">
            <div class="col-xs-12 col-sm-10" style="margin-bottom: 1%">
                <div class="text-right text-center-mobile">
                    <a class="btn btn-primary" href="#" id="submit">Submit</a>
                </div>
            </div>
        </div>
    </c:if>
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