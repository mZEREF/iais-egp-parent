<div class="main-content">
    <div class="container">
        <br/>
        <div class="row">
            <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
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
                                                   data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Cessation Reasons"/>
                                            <iais:value width="7">
                                                <iais:select disabled="true"
                                                             id="${num.count}reasonId${uid.count}"
                                                             name="${num.count}reason${uid.count}"
                                                             options="reasonOption"
                                                             value="${appCessHci.reason}"/>
                                            </iais:value>
                                        </iais:row>
                                        <div id="${num.count}reason${uid.count}" hidden>
                                            <iais:row>
                                                <iais:field value="Others"/>
                                                <iais:value width="7">
                                                    <iais:input needDisabled="true" type="text"
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
                                                                           type="radio"
                                                                           name="${num.count}patRadio${uid.count}"
                                                                           value="yes"
                                                                           id="${num.count}radioYes${uid.count}"
                                                                           <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                           aria-invalid="false"
                                                                           disabled>
                                                                    <label class="form-check-label"
                                                                           for=${num.count}radioYes${uid.count}"><span
                                                                            class="check-circle"></span>Yes</label>
                                                                </div>
                                                            </div>
                                                            <div class="col-xs-12 col-md-3">
                                                                <div class="form-check">
                                                                    <input class="form-check-input" type="radio"
                                                                           name="${num.count}patRadio${uid.count}"
                                                                           value="no"
                                                                           id="${num.count}radioNo${uid.count}"
                                                                           <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                           aria-invalid="false"
                                                                           disabled>
                                                                    <label class="form-check-label"
                                                                           for="${num.count}radioNo${uid.count}"><span
                                                                            class="check-circle"></span>No</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <div id="${num.count}patYes${uid.count}" hidden>
                                            <iais:row>
                                                <iais:field width="7"
                                                            value="Who will take over your patients' case records?"/>
                                                <iais:value width="7">
                                                    <iais:select disabled="true"
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
                                                <iais:field value="HCI Name"/>
                                                <iais:value width="7">
                                                    <iais:input type="text" needDisabled="true"
                                                                value="${appCessHci.patHciName}" maxLength="100"
                                                                name="${num.count}patHciName${uid.count}"></iais:input>
                                                </iais:value>
                                            </iais:row>
                                        </div>
                                        <div id="${num.count}patRegNo${uid.count}" hidden>
                                            <iais:row>
                                                <iais:field value="Professional Regn No."/>
                                                <iais:value width="7">
                                                    <iais:input needDisabled="true" cssClass="disabled"
                                                                type="text"
                                                                name="${num.count}patRegNo${uid.count}"
                                                                value="${appCessHci.patRegNo}"></iais:input>
                                                </iais:value>
                                            </iais:row>
                                        </div>
                                        <div id="${num.count}patOthers${uid.count}" hidden>
                                            <iais:row>
                                                <iais:field value="Others"/>
                                                <iais:value width="7">
                                                    <iais:input needDisabled="true" type="text"
                                                                name="${num.count}patOthers${uid.count}"
                                                                value="${appCessHci.patOthers}"></iais:input>
                                                </iais:value>
                                            </iais:row>
                                        </div>
                                        <div id="${num.count}patNo${uid.count}" hidden>
                                            <iais:row>
                                                <iais:field value="Reason for no patients' records transfer"/>
                                                <iais:value width="7">
                                                    <iais:input needDisabled="true" type="text"
                                                                name="${num.count}patNoRemarks${uid.count}"
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
            </c:forEach>
        </div>
<%--        <div class="row">--%>
<%--            <div>--%>
<%--                    <span style="padding-right: 10%" class="components">--%>
<%--                        <a onclick="confirmBack('back')"><em class="fa fa-angle-left"></em> Back</a>--%>
<%--                    </span>--%>
<%--            </div>--%>
<%--        </div>--%>
        <br/>
    </div>
</div>
</form>

<style>
    .col-md-5 {
        width: 26%;
    }

    .col-md-4 {
        width: 35%;
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

    $(document).ready(function () {
        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
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