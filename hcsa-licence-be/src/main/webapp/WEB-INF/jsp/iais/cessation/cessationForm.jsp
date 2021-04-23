<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
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
                                                                         name="${num.count}effectiveDate${uid.count}"
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
                                                        <iais:select name="${num.count}reason${uid.count}"
                                                                     id="${num.count}reasonId${uid.count}"
                                                                     options="reasonOption" firstOption="Please Select"
                                                                     onchange="javascirpt:changeReasonCessBe(this.value);"
                                                                     value="${appCessHci.reason}"
                                                                     cssClass="nice-select cessationReasons" needSort="false"/>
                                                    </iais:value>
                                                </iais:row>
                                                <div id="${num.count}reason${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Others" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" maxLength="200"
                                                                        name="${num.count}otherReason${uid.count}"
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
                                                                                   id="${num.count}radioYes${uid.count}"
                                                                                   type="radio"
                                                                                   name="${num.count}patRadio${uid.count}"
                                                                                   value="yes"
                                                                                   <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                                   onchange="javascirpt:changePatSelectCessBe(this.value);"
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
                                                                                   onchange="javascirpt:changePatSelectCessBe(this.value);"
                                                                                   aria-invalid="false">
                                                                            <label class="form-check-label"
                                                                                   for="${num.count}radioNo${uid.count}"><span
                                                                                    class="check-circle"></span>No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div><span id="error_${num.count}patRadio${uid.count}"
                                                                           name="iaisErrorMsg" class="error-msg"></span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </iais:value>
                                                </iais:row>
                                                <div id="${num.count}patYes${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field width="7"
                                                                    value="Who will take over your patients' case records?" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:select
                                                                    name="${num.count}patientSelect${uid.count}"
                                                                    options="patientsOption"
                                                                    firstOption="Please Select"
                                                                    id="${num.count}patientSelectId${uid.count}"
                                                                    onchange="javascirpt:changePatientCessBe(this.value);"
                                                                    value="${appCessHci.patientSelect}"
                                                                    cssClass="nice-select cessationTransfer" needSort="false"/>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patHciName${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="HCI Name / Code" mandatory="true"/>
                                                        <iais:value width="7">
                                                                <input type="text" maxLength="100"
                                                                       name="${num.count}patHciName${uid.count}"
                                                                       onblur="javascript:getHci(this);"
                                                                       id="${num.count}hciName${uid.count}"
                                                                       value="${appCessHci.patHciName}">
                                                                <span id="error_${num.count}patHciName${uid.count}"
                                                                      name="iaisErrorMsg" class="error-msg"></span>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}hciNamePat${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="HCI Name"/>
                                                        <iais:value width="7">
                                                            <div class="nameLoad"></div>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}hciCodePat${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="HCI Code"/>
                                                        <iais:value width="7">
                                                            <div class="codeLoad"></div>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}hciAddressPat${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="HCI Address"/>
                                                        <iais:value width="7">
                                                            <div class="addressLoad"></div>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patRegNo${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Professional Regn. No." mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" maxLength="20"
                                                                        name="${num.count}patRegNo${uid.count}"
                                                                        value="${appCessHci.patRegNo}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patOthersTakeOver${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Others" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patOthers}"
                                                                        maxLength="100"
                                                                        name="${num.count}patOthersTakeOver${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patOthersMobileNo${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Mobile No." mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.mobileNo}"
                                                                        maxLength="8"
                                                                        name="${num.count}patOthersMobileNo${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patOthersEmailAddress${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Email Address" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.emailAddress}"
                                                                        maxLength="66"
                                                                        name="${num.count}patOthersEmailAddress${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>
                                                <div id="${num.count}patNo${uid.count}" hidden>
                                                    <iais:row>
                                                        <iais:field value="Reason for no patients' records transfer" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <iais:input type="text" value="${appCessHci.patNoRemarks}"
                                                                        maxLength="200"
                                                                        name="${num.count}patNoRemarks${uid.count}"></iais:input>
                                                        </iais:value>
                                                    </iais:row>
                                                </div>

                                                <div class="form-group" id="${num.count}patNoConfirmID${uid.count}"
                                                     hidden>
                                                    <div class="col-xs-6 col-sm-4 col-md-6">
                                                        <div class="form-check">
                                                            <input class="form-check-input" id="patNoConfirm"
                                                                   type="checkbox"
                                                                   name="${num.count}patNoConfirm${uid.count}"
                                                                   <c:if test="${appCessHci.patNoConfirm != null}">checked</c:if>>
                                                            <label class="form-check-label" for="patNoConfirm"><span
                                                                    class="check-square"></span><iais:message key="CESS_DEC001"/><span style="color: red">*</span></label>
                                                        </div>
                                                        <span id="error_${num.count}patNoConfirm${uid.count}" name="iaisErrorMsg" class="error-msg"></span>
                                                    </div>
                                                </div>
                                                <c:if test="${isGrpLic}">
                                                    <iais:row>
                                                        <iais:field value="To Cease" mandatory="true"/>
                                                        <iais:value width="7">
                                                            <div class="form-check-gp">
                                                                <div class="row">
                                                                    <div class="col-xs-12 col-md-2">
                                                                        <div class="form-check">
                                                                            <input class="form-check-input"
                                                                                   id="icon5checkboxSample"
                                                                                   type="checkbox"
                                                                                   name="${num.count}whichTodo${uid.count}"
                                                                                   value="${appCessHci.premiseId}"
                                                                                   <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>
                                                                                   aria-invalid="false">
                                                                            <label class="form-check-label"
                                                                                   for="icon5checkboxSample"><span
                                                                                    class="check-square"></span></label>
                                                                            <span id="error_whichTodo"
                                                                                  name="iaisErrorMsg"
                                                                                  class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </iais:value>
                                                    </iais:row>
                                                </c:if>
                                                <c:if test="${!isGrpLic}">
                                                    <div hidden>
                                                        <input class="form-check-input" type="text"
                                                               name="${num.count}whichTodo${uid.count}"
                                                               value="${appCessHci.premiseId}">
                                                    </div>
                                                </c:if>
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
                    </c:forEach>
                </div>
                <br/>
                <div class="form-check" style="z-index:1">
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
                        <div class="col-xs-12 col-sm-6">
                            <a href="/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries/1/check"><em
                                    class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
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
                            <div class="col-md-12"><span style="font-size: 2rem;"><%=MessageUtil.getMessageDesc("GENERAL_ERR0048")%></span></div>
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
        if($("#transformNoID").css("display")=="block"){
            if ($('#confirmInfo').is(':checked')&&$('#confirmNo').is(':checked')) {
                submit(action);
            }else {
                $('#readInfo').show();
            }
        }else {
            if ($('#confirmInfo').is(':checked')) {
                submit(action);
            }else {
                $('#readInfo').show();
            }
        }
    }

    function back(action) {
        submit(action);
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



    function changeReasonCessBe() {
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

    function changePatientCessBe() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthersTakeOver" + j).show();
                    $("#" + i + "patOthersMobileNo" + j).show();
                    $("#" + i + "patOthersEmailAddress" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "hciNamePat" + j).hide();
                    $("#" + i + "hciCodePat" + j).hide();
                    $("#" + i + "hciAddressPat" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005"&&$('#' + i + 'radioYes' + j).is(':checked')) {
                    $( "#" + i + "hciName" + j).trigger('blur');
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "hciNamePat" + j).hide();
                    $("#" + i + "hciCodePat" + j).hide();
                    $("#" + i + "hciAddressPat" + j).hide();
                }
            }
        }
    }

    function changePatSelectCessBe() {
        changePatientCessBe();
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                    $("#"+ i +"patNoConfirmID"+ j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patNo" + j).show();
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#"+ i +"patNoConfirmID"+ j).show();
                    $("#" + i + "hciNamePat" + j).hide();
                    $("#" + i + "hciCodePat" + j).hide();
                    $("#" + i + "hciAddressPat" + j).hide();
                }
            }
        }
    }

    $(document).ready(function () {
        changePatientCessBe();
        changeReasonCessBe();
        changePatSelectCessBe();
    });

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
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