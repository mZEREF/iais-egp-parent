<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<webui:setLayout name="iais-internet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%@include file="../cessation/head.jsp" %>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <h2>Please key in cessation information</h2>
                    <br/>
                    <form  method="post" id="mainForm" class=""
                           enctype="multipart/form-data"
                           action=<%=process.runtime.continueURL()%>>
                    <div class="row form-horizontal">
                            <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
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

                                                <input type="hidden" name="sopEngineTabRef"
                                                       value="<%=process.rtStatus.getTabRef()%>">
                                                <input type="hidden" name="crud_action_type" value="">
                                                <input type="hidden" name="crud_action_value" value="">
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Effective Date
                                                        <span style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <iais:datePicker id="effectiveDate"
                                                                         name="${num.count}effectiveDate${uid.count}"
                                                                         dateVal="${appCessHci.effectiveDate}"/>
                                                    </div>
                                                    <div class="col-xs-8 col-sm-2 col-md-2">
                                                        <a class="btn-tooltip styleguide-tooltip"
                                                           data-toggle="tooltip" data-html="true"
                                                           title="${cess_ack002}">i</a>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Cessation
                                                        Reasons <span style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <iais:select name="${num.count}reason${uid.count}"
                                                                     id="${num.count}reasonId${uid.count}"
                                                                     options="reasonOption"
                                                                     firstOption="Please Select"
                                                                     onchange="javascirpt:changeReasonCessFe(this.value);"
                                                                     value="${appCessHci.reason}"
                                                                     cssClass="nice-select cessationReasons"
                                                                     needSort="false"/>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="${num.count}reason${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6 control-label ">Others <span
                                                            style="color: red">*</span></label>
                                                    <div class="col-xs-12 col-sm-4 col-md-3">
                                                        <iais:input type="text" maxLength="200"
                                                                    name="${num.count}otherReason${uid.count}"
                                                                    value="${appCessHci.otherReason}"></iais:input>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-6 control-label">Patient's Records
                                                        will be transferred <span
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
                                                                               onchange="javascirpt:changePatSelectCessFe();"
                                                                               aria-invalid="false">
                                                                        <label class="form-check-label"
                                                                               for="${num.count}radioNo${uid.count}"><span
                                                                                class="check-circle"></span>No</label>
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
                                                <%--<div class="form-group" id="${num.count}patYes${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">Who will take
                                                        over your patients' case records?<span
                                                                style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <iais:select
                                                                name="${num.count}patientSelect${uid.count}"
                                                                options="patientsOption"
                                                                firstOption="Please Select"
                                                                id="${num.count}patientSelectId${uid.count}"
                                                                onchange="javascirpt:changePatientCessFe(this.value);"
                                                                value="${appCessHci.patientSelect}"
                                                                cssClass="nice-select cessationTransfer"
                                                                needSort="false"/>
                                                    </div>
                                                </div>--%>
                                                <%--<div class="form-group" id="${num.count}patHciName${uid.count}"
                                                     hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">HCI
                                                        Name / Code <span style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <input type="text" maxLength="100"
                                                               name="${num.count}patHciName${uid.count}"
                                                               onblur="javascript:getHci(this);"
                                                               id="${num.count}hciName${uid.count}"
                                                               value="${appCessHci.patHciName}">
                                                        <span id="error_${num.count}patHciName${uid.count}"
                                                              name="iaisErrorMsg" class="error-msg"></span>
                                                    </div>
                                                </div>--%>
                                                <%--<div class="form-group" id="${num.count}hciNamePat${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">HCI
                                                        Name </label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <span class="nameLoad"></span>
                                                    </div>
                                                </div>--%>
                                                <%--<div class="form-group" id="${num.count}hciCodePat${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">HCI Code </label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <span class="codeLoad"></span>
                                                    </div>
                                                </div>--%>
                                               <%-- <div class="form-group" id="${num.count}hciAddressPat${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">HCI
                                                        Address </label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <span class="addressLoad"></span>
                                                    </div>
                                                </div>--%>
                                                <%--<div class="form-group" id="${num.count}patRegNo${uid.count}"
                                                     hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">Professional
                                                        Regn. No. <span style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <iais:input type="text" maxLength="20"
                                                                    name="${num.count}patRegNo${uid.count}"
                                                                    value="${appCessHci.patRegNo}"/>
                                                    </div>
                                                    <span id="error_${num.count}patRegNo${uid.count}" name="iaisErrorMsg" class="error-msg"></span>
                                                </div>--%>
                                               <%-- <div class="form-group" id="${num.count}patOthersTakeOver${uid.count}"
                                                     hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">Others <span
                                                            style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <iais:input type="text"
                                                                    value="${appCessHci.patOthers}"
                                                                    maxLength="100"
                                                                    name="${num.count}patOthersTakeOver${uid.count}"></iais:input>
                                                    </div>
                                                </div>--%>
                                               <%-- <div class="form-group"
                                                     id="${num.count}patOthersMobileNo${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">Mobile
                                                        No. <span style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <iais:input type="text"
                                                                    value="${appCessHci.mobileNo}"
                                                                    maxLength="8"
                                                                    name="${num.count}patOthersMobileNo${uid.count}"/>
                                                    </div>
                                                </div>--%>
                                                <%--<div class="form-group"
                                                     id="${num.count}patOthersEmailAddress${uid.count}" hidden>
                                                    <label class="col-xs-12 col-md-6 control-label">Email Address <span style="color: red">*</span></label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <iais:input type="text"
                                                                    value="${appCessHci.emailAddress}"
                                                                    maxLength="66"
                                                                    name="${num.count}patOthersEmailAddress${uid.count}"/>
                                                    </div>
                                                </div>--%>
                                                <div class="form-group" id="${num.count}transferDetail${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <textarea name="${num.count}transferDetail${uid.count}"  cols="30" rows="2" maxLength="1000">${appCessHci.transferDetail}</textarea>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="${num.count}transferredWhere${uid.count}" style="display: none;">
                                                    <label class="col-xs-12 col-md-6">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                                                    <div class="col-xs-6 col-sm-4 col-md-3">
                                                        <textarea name="${num.count}transferredWhere${uid.count}"  cols="30" rows="2" maxLength="1000">${appCessHci.transferredWhere}</textarea>
                                                    </div>
                                                </div>
                                                <%--<div class="form-group" id="${num.count}patNoConfirmID${uid.count}"
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
                                                </div>--%>
                                                <c:if test="${isGrpLic}">
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
                                                                       aria-invalid="false">
                                                                <label class="form-check-label"
                                                                       for="icon5checkboxSample"><span
                                                                        class="check-square"></span></label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
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
                                <c:if test="${specLicInfo !=null}">
                                <div class="table-responsive">
                                    <c:forEach items="${specLicInfo}" var="map">
                                        <c:set var="licNo" value="${map.key}"></c:set>
                                        <c:if test="${appCess.licenceNo==licNo}">
                                            <div><h4>The following specified healthcare services will also be ceased as
                                                their
                                                underlying <iais:code needLowerCase="true" code="CDN001"/>(s) is/are listed above.</h4>
                                            </div>
                                            <table aria-describedby="" class="table-gp tablebox">
                                                <tr style="text-align:center">
                                                    <th scope="col" style="text-align:center;width: 0%">S/N</th>
                                                    <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/>
                                                        Licence No.
                                                    </th>
                                                    <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/>
                                                        Name
                                                    </th>
                                                    <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN001"/> Licence No.
                                                    </th>
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
                                </div>
                                </c:if>
                            </div>
                            </c:forEach>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                <%@include file="../common/declarations.jsp"%>
                            </div>
                        </div>
                    </div>
                </div>
                <br/>
                <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
                </form>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                                <span style="padding-right: 10%" class="components">
                        <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initLic"><em
                                class="fa fa-angle-left"></em> Back</a>
                    </span>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                                <span style="padding-left: 73%" class="components">
                       <a class="btn btn-primary next" href="javascript:void(0);"
                          onclick="submitSure('submit')">Next</a>
                    </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br/>
</div>
</div>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12"><span style="font-size: 2rem;"><%=MessageUtil.getMessageDesc("GENERAL_ERR0048")%></span></div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancel()">OK</button>
            </div>
        </div>
    </div>
</div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT">
<style>
    #effectiveDate {
        margin-bottom: 0px;
    }

    input[type='text'] {
        margin-bottom: 0px;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
        $('#declarations').collapse('show');
        $("textarea[maxlength]").blur(function() {
            var area=$(this);
            var max = parseInt(area.attr("maxlength"),10);
            if (max > 0) {
                var value = area.val();
                var valueLength = area.val().length;
                if (value && value.indexOf("\n") > -1) {
                    valueLength += value.split('\n').length - 1;
                }
                if (valueLength > max) {
                    var targetVal = value.substr(0, max);
                    if (value && value.indexOf("\n") > -1) {
                        targetVal = value.replaceAll("\n", "\r\n").substr(0, max).replaceAll("\r\n", "\n");
                    }
                    area.val(targetVal);
                }
            }
        });
    });

    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function submitSure(action) {
        submit(action);
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
        var hciNme = $(obj).closest('.form-group').next('.form-group');
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
        var hciAddress = hciCode.next();
        hciCode.hide();
        hciAddress.hide();
    }

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
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "hciNamePat" + j).hide();
                    $("#" + i + "hciCodePat" + j).hide();
                    $("#" + i + "hciAddressPat" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005"&&$('#' + i + 'radioYes' + j).is(':checked')) {
                    var aaa = $('#' + i + 'radioYes' + j).is(':checked');
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
    }*/

    function changePatSelectCessFe() {
       // changePatientCessFe();
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $( "#" + i + "hciName" + j).trigger('blur');
                    //$("#" + i + "patYes" + j).show();
                    $("#" + i + "transferDetail" + j).hide();
                    $("#" + i + "transferredWhere" + j).show();
                   // $("#" + i + "patNoConfirmID" + j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "transferDetail" + j).show();
                    $("#" + i + "transferredWhere" + j).hide();
                    //$("#" + i + "patYes" + j).hide();
                   // $("#" + i + "patHciName" + j).hide();
                   //  $("#" + i + "patOthersTakeOver" + j).hide();
                   //  $("#" + i + "patRegNo" + j).hide();
                   //  $("#" + i + "patOthersMobileNo" + j).hide();
                   //  $("#" + i + "patOthersEmailAddress" + j).hide();
                   //  $("#" + i + "patNoConfirmID" + j).show();
                   //  $("#" + i + "hciNamePat" + j).hide();
                   //  $("#" + i + "hciCodePat" + j).hide();
                   //  $("#" + i + "hciAddressPat" + j).hide();
                }
            }
        }
    }

    $(document).ready(function () {
        //changePatientCessFe();
        changeReasonCessFe();
        changePatSelectCessFe();
    });

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    //$("#" + i + "patYes" + j).hide();
                   // $("#" + i + "patHciName" + j).hide();
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

    function uploadFileValidate() {
        var configFileSize = $("#configFileSize").val();
        var error = validateUploadSizeMaxOrEmpty(configFileSize, 'selectedFile');
        if (error == "Y") {
            $('#error_litterFile_Show').html("");
            $("#delFile").removeAttr("hidden");
            let fileName = $("#selectedFile").val();
            let pos = fileName.lastIndexOf("\\");
            $("#fileName").html(fileName.substring(pos + 1));
        } else if(error == "E"){
            $('#error_litterFile_Show').html("");
            $('#error_file').html("");
        } else{
            $("#selectedFile").val("");
            $('#error_litterFile_Show').html($("#fileMaxMBMessage").val());
            $("#fileName").html("");
        }
    }
</script>