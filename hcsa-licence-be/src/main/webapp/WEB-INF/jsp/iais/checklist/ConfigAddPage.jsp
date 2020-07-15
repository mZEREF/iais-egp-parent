<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    .form-check-gp {
        width: 50%;
        float: left;
    }

</style>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" id="commonSelect" name="commonSelect" value="${common}">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br><br>
        <div>
            <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>
        </div>
        <br><br>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Common" id="commonField"></iais:field>
                <div class="col-md-3">
                    <input class="form-check-input" id="common" type="radio" name="common" aria-invalid="false"
                           value="${common}"> General Regulation
                </div>
            </div>

            <div class="serviceFieldGroup">
                <div class="form-group">
                    <iais:field value="Service Name" id="serviceNameField"></iais:field>
                    <div class="col-md-5">
                        <iais:select name="svcName" id="svcName" options="svcNameSelect" firstOption="Please Select"
                                     value="${svcName}"></iais:select>
                    </div>
                </div>


                <div class="form-group">
                    <iais:field value="Service Sub-Type" id="subTypeField"></iais:field>
                    <div class="col-md-5">
                        <iais:select name="svcSubType" id="svcSubType" options="subtypeSelect"
                                     firstOption="Please Select" value="${svcSubType}"></iais:select>
                    </div>
                </div>

                <div class="form-group">
                    <iais:field value="Module" id="moduleField"></iais:field>
                    <div class="col-md-3">
                        <iais:select name="module" id="module" codeCategory="CATE_ID_CHECKLIST_MODULE"
                                     firstOption="Please Select" value="${module}"></iais:select>
                    </div>
                </div>

                <div class="form-group">
                    <iais:field value="Type" id="typeField"></iais:field>
                    <div class="col-md-3">
                        <iais:select name="type" id="type" codeCategory="CATE_ID_CHECKLIST_TYPE"
                                     firstOption="Please Select" value="${type}"></iais:select>
                    </div>
                </div>

            </div>

            <div class="form-group">
                <iais:field value="HCI Code"></iais:field>
                <div class="col-md-3">
                    <input type="text" id="hciCode" name="hciCode" maxlength="7" value="${hciCode}"
                           style="border-color: white"/>
                    <span id="error_hciCode" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>


            <div class="form-group">
                <iais:field value="Effective Start Date" required="true"></iais:field>
                <div class="col-md-3">
                    <iais:datePicker name="eftStartDate" value="${eftStartDate}"></iais:datePicker>
                    <span id="error_eftStartDate" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Effective End Date" required="true"></iais:field>
                <div class="col-md-3">
                    <iais:datePicker name="eftEndDate" value="${eftEndDate}"></iais:datePicker>
                    <span id="error_eftEndDate" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>


        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <a class="back" onclick="doBack();"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" onclick="javascript: doNext();">Next</a>
                </div>
            </div>
        </div>
    </
    >
</div>

<script type="text/javascript">
    $(document).ready(function(){
        checkInputStatus();
    })

    function checkInputStatus() {
        if ($('#common').val() == '1') {
            $('#common').attr("checked", "checked");
            $('#commonField').append("<span style=\"color: red\"> *</span>");
            $('#serviceNameField span').hide();
            $('#moduleField span').hide();
            $('#typeField span').hide();
            Utils.clearClickStatus('serviceFieldGroup')
            disableInput();
        } else {

            var aaa = $(".form-horizontal select").val();
            if (aaa != '') {
                $('#common').attr('disabled', 'disabled')
                $('#common').val()
            }

            $('#serviceNameField').append("<span style=\"color: red\"> *</span>");
            $('#moduleField').append("<span style=\"color: red\"> *</span>");
            $('#typeField').append("<span style=\"color: red\"> *</span>");
        }
    }

    $(".form-horizontal select").change(function () {
        $('#common').attr('disabled', 'disabled')
    });

    var flag = false;
    common.onclick = function () {
        var checkedStatus = $('#common').attr("checked");
        if (checkedStatus && !flag) {
            $('#common').val(1);
            $('#commonField').append("<span style=\"color: red\"> *</span>");

            disableInput();

            Utils.clearClickStatus('serviceFieldGroup')
            flag = true;
        }
    }

    function disableInput() {
        $('.nice-select').addClass('disabled');
        $('#hciCode').attr('disabled', 'disabled');
        $('#serviceNameField span').hide();
        $('#moduleField span').hide();
        $('#typeField span').hide();
    }

    function doNext() {
        SOP.Crud.cfxSubmit("mainForm", "nextPage");
    }

    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "backLastPage");
    }

</script>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
