<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<webui:setLayout name="iais-blank"/>
<style>
    .form-check input.form-check-input:checked + .form-check-label span.check-circle:before,
    .form-check input.form-check-input:active + .form-check-label span.check-circle:before {
        color: #147aab !important;
        background-color: #FFF;
        content: "\f111";
    font-family: FontAwesome, sans-serif;        position: absolute;
        font-size: 12px;
        top: 38%;
        left: 48%;
    }
</style>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <br/>
                    <div class="row">
                        <form id="mainForm" class="form-horizontal"
                              enctype="multipart/form-data"
                              action=<%=process.runtime.continueURL()%>>
                            <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
                            <div class="col-lg-12 col-xs-12 cesform-box">
                                <div class="row">
                                    <div class="col-lg-6 col-xs-6">
                                        <div class="license-info">
                                            <p class="lic-no">Licence Number</p>
                                            <h4><c:out value="${appCess.licenceNo}"/></h4>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-xs-6">
                                        <div class="license-info">
                                            <p class="serv-name">Service Name</p>
                                            <h4><c:out value="${appCess.svcName}"/></h4>
                                        </div>
                                    </div>
                                    <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                                        <div class="col-lg-12 col-xs-12">
                                            <div class="">
                                                <div class="topheader">
                                                    <c:if test="${appCessHci.hciName==null}">
                                                        <h4>HCI Name: <c:out value="${appCessHci.hciCode}"/></h4>
                                                    </c:if>
                                                    <c:if test="${appCessHci.hciName!=null}">
                                                        <h4>HCI Name: <c:out value="${appCessHci.hciName}"/> - <c:out
                                                                value="${appCessHci.hciCode}"/></h4>
                                                    </c:if>
                                                    <p>HCI Address: <c:out value="${appCessHci.hciAddress}"/></p>
                                                </div>
                                                <input type="hidden" name="sopEngineTabRef"
                                                       value="<%=process.rtStatus.getTabRef()%>">
                                                <input type="hidden" name="crud_action_type" value="">
                                                <input type="hidden" name="crud_action_value" value="">
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Effective
                                                        Date <span style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <fmt:formatDate value="${appCessHci.effectiveDate}"
                                                                        pattern="dd/MM/yyyy"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Cessation
                                                        Reasons <span style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <iais:select disabled="true"
                                                                     id="${num.count}reasonId${uid.count}"
                                                                     name="${num.count}reason${uid.count}"
                                                                     options="reasonOption"
                                                                     value="${appCessHci.reason}"
                                                                     cssClass="nice-select cessationReasons"/>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="${num.count}reason${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6 control-label ">Others <span
                                                            style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <iais:input needDisabled="true" type="text"
                                                                    name="${num.count}otherReason${uid.count}"
                                                                    value="${appCessHci.otherReason}"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Patient's Records will be transferred <span
                                                            style="color: red">*</span></label>
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
                                                                               onchange="javascirpt:changePatSelectCessFe();"
                                                                               aria-invalid="false" disabled>
                                                                        <c:if test="${appCessHci.patNeedTrans ==true}">
                                                                            <label class="form-check-label"
                                                                                   for=${num.count}radioYes${uid.count}">Yes</label></c:if>
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
                                                                               onchange="javascirpt:changePatSelectCessFe();"
                                                                               aria-invalid="false" disabled>
                                                                        <c:if test="${appCessHci.patNeedTrans !=true}">
                                                                            <label class="form-check-label"
                                                                                   for="${num.count}radioNo${uid.count}">No</label></c:if>

                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div><span
                                                                    id="error_${num.count}patRadio${uid.count}"
                                                                    name="iaisErrorMsg"
                                                                    class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="${num.count}transferDetail${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                                                    <div class="col-xs-12 <c:if test='${appCessHci.transferDetailCol == 30}'>col-sm-4 col-md-3 </c:if>">
                                                        <textarea name="${num.count}transferDetail${uid.count}"  cols="${appCessHci.transferDetailCol}" rows="${appCessHci.transferDetailRow}" maxLength="1000" readonly="readonly">${appCessHci.transferDetail}</textarea>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="${num.count}transferredWhere${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                                                    <div class="col-xs-12 <c:if test='${appCessHci.transferredWhereCol == 30}'>col-sm-4 col-md-3 </c:if>">
                                                        <textarea name="${num.count}transferredWhere${uid.count}"  cols="${appCessHci.transferredWhereCol}" rows="${appCessHci.transferredWhereRow}" maxLength="1000" readonly="readonly">${appCessHci.transferredWhere}</textarea>
                                                    </div>
                                                </div>
                                                <%--<c:if test="${isGrpLic}">
                                                    <div>
                                                        <div class="form-group">
                                                            <label class="col-xs-12 col-md-6 control-label">To
                                                                Cease <span style="color: red">*</span></label>
                                                            <div class="col-xs-6 col-sm-4 col-md-3">
                                                                <div class="form-check">
                                                                    <input class="form-check-input"
                                                                           id="icon5checkboxSample"
                                                                           type="checkbox"
                                                                           name="${num.count}whichTodo${uid.count}"
                                                                           value="${appCessHci.premiseId}"
                                                                           <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>
                                                                           aria-invalid="false" disabled>
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
                                                </c:if>--%>
                                                <c:if test="${!isGrpLic}">
                                                    <div style="display: none;">
                                                        <input class="form-check-input" type="text"
                                                               name="${num.count}whichTodo${uid.count}"
                                                               value="${appCessHci.premiseId}">
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <%--<c:if test="${specLicInfo !=null}">
                                    <c:forEach items="${specLicInfo}" var="map">
                                        <c:set var="licNo" value="${map.key}"></c:set>
                                        <c:if test="${appCess.licenceNo==licNo}">
                                            <div>
                                                <h4>The following specified healthcare services will also be ceased as
                                                    their
                                                    underlying <iais:code needLowerCase="true" code="CDN001"/>(s) is/are listed above.</h4>
                                            </div>
                                            <table aria-describedby="" class="table-gp tablebox">
                                                <tr>
                                                    <th scope="col" style="width: 1%">S/N</th>
                                                    <th scope="col" style="width: 25%"><iais:code code="CDN003"/>
                                                        Licence No.
                                                    </th>
                                                    <th scope="col" style="width: 25%"><iais:code code="CDN003"/>
                                                        Name
                                                    </th>
                                                    <th scope="col" style="width: 25%"><iais:code code="CDN001"/> Licence No.
                                                    </th>
                                                    <th scope="col" style="width: 25%"><iais:code code="CDN001"/> Name</th>
                                                </tr>
                                                <c:forEach items="${map.value}" var="spec" varStatus="index">
                                                    <tr>
                                                        <td style="width: 1%">
                                                            <p style="font-size: 12px"><c:out value="${index.count}"/></p>
                                                        </td>
                                                        <td style="width: 15%">
                                                            <p style="font-size: 12px"><c:out value="${spec.specLicNo}"/></p>
                                                        </td>
                                                        <td style="width: 15%">
                                                            <p style="font-size: 12px"><c:out value="${spec.specSvcName}"/></p>
                                                        </td>
                                                        <td style="width: 15%">
                                                            <p style="font-size: 12px"><c:out value="${spec.baseLicNo}"/></p>
                                                        </td>
                                                        <td style="width: 15%">
                                                            <p style="font-size: 12px"><c:out value="${spec.baseSvcName}"/></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </c:if>
                                    </c:forEach>
                                </c:if>--%>
                                </c:forEach>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                            <%@include file="../common/declarations/declarations.jsp"%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br/>
                    </div>
                </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
<style>
    #effectiveDate {
        margin-bottom: 0px;
    }

    input[type='text'] {
        margin-bottom: 0px;
    }
</style>
<script type="text/javascript">

    function changeReasonCessFe() {
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

    /*function changePatientCessFe() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthersTakeOver" + j).show();
                    $("#" + i + "patOthersMobileNo" + j).show();
                    $("#" + i + "patOthersEmailAddress" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005" && $('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "hciName" + j).show();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "hciNamePat" + j).show();
                    $("#" + i + "hciCodePat" + j).show();
                    $("#" + i + "hciAddressPat" + j).show();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                }
            }
        }
    }*/

    function changePatSelectCessFe() {
       // changePatientCessFe();
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                    //$("#" + i + "patNoConfirmID" + j).hide();
                    $("#" + i + "transferDetail" + j).hide();
                    $("#" + i + "transferredWhere" + j).show();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "transferDetail" + j).show();
                    $("#" + i + "transferredWhere" + j).hide();
                    /*$("#" + i + "patNo" + j).show();
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "patNoConfirmID" + j).show();*/
                }
            }
        }
    }

    $(document).ready(function () {
        $(':input', '#declarations').prop('disabled', true);

        var btn = $('.file-upload-gp a', '#declarations');
        if (btn.length > 0) {
            btn.each(function(index, ele) {
                $(ele).parent().html($(ele).text());
            });
        }

        changeReasonCessFe();
        //changePatientCessFe();
        changePatSelectCessFe();

        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
        }

        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
        }
        doPrint();
    });


    var doPrint = function () {
        $('a').prop('disabled',true);
        window.print();
        window.close();
    }
</script>