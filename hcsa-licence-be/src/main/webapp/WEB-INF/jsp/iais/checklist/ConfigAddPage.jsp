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
                <iais:field value="Common" id="commonField"/>
                <div class="col-md-5">
                    <p>
                    <input class="form-check-input" id="common" type="radio" name="common" aria-invalid="false" value="${common}"
                           style="margin-top: 18px;">
                    <label for="common" style="margin-left: 20px;">General Regulation</label>
                    </p>
                </div>
            </div>

            <div class="serviceFieldGroup">
                <div class="form-group">
                    <iais:field value="Service" id="serviceNameField"></iais:field>
                    <div class="col-md-5">
                        <iais:select name="svcName" id="svcName" options="checklist_svc_name_select" firstOption="Please Select"
                                     value="${svcName}"></iais:select>
                    </div>
                </div>

                <div class="form-group">
                    <iais:field value="Service Sub-Type" id="subTypeField"></iais:field>
                    <div class="col-md-5">
                        <iais:select name="svcSubType" id="svcSubType" options="checklist_config_subtype_select"
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

                <div class="form-group">
                    <iais:field value="Inspection Entity" id="inspectionEntityField"></iais:field>
                    <div class="col-md-3">
                        <iais:select name="inspectionEntity" id="inspectionEntity" codeCategory="CATE_ID_INSPECTION_ENTITY_TYPE"
                            firstOption="Please Select" value="${inspectionEntity}"></iais:select>
                    </div>
                </div>

            </div>

            <div class="form-group">
                <iais:field value="HCI Code"></iais:field>
                <div class="col-md-3">
                    <input type="text" class="needDisableI" id="hciCode" name="hciCode" maxlength="7" value="${hciCode}"
                           />
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
                <a href="#" class="back" onclick="doBack();"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" onclick="javascript: clearInput();">Clear</a>
                    <a class="btn btn-primary next" onclick="javascript: doNext();">Next</a>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">

    $(document).ready(function(){
        checkInputStatus();
    })

    let hasClick = false;
    function checkInputStatus(){
        let commonVal = $('#common').val();
        if ('1' == commonVal && !hasClick) {
            setCommon();
            disableInput();
        }else {
            openInput();
        }
    }

    function setCommon(){
        $('#common').attr("checked", "checked");
        $('#common').val(1);
        $('#commonField').append("<span style=\"color: red\"> *</span>");
        hasClick = true;
    }

    function clearInput(){
        $('#commonField span').hide();
        $('#common').prop('checked', false);
        $('#common').attr('disabled', null);
        Utils.clearClickStatus('form-horizontal');
        $('#common').attr('disabled', null)
        $('#serviceNameField span').hide();
        $('#moduleField span').hide();
        $('#typeField span').hide();
        $('#hciCode').attr('disabled', null);
        checkInputStatus()
    }

    common.onclick = function () {
        if (!hasClick){
            setCommon();
            disableInput();
        }
    }

    $(".form-horizontal select").change(function () {
        $('#common').attr('disabled', 'disabled')
    });

    function disableInput() {
        $('.nice-select').addClass('disabled');
        $('#hciCode').attr('disabled', 'disabled');
        $('#hciCode').val(null);
        $('#serviceNameField span').hide();
        $('#moduleField span').hide();
        $('#typeField span').hide();

        $('.needDisableI').css('border-color', '#ededed');
        $('.needDisableI').css('color', '#999');

        Utils.clearClickStatus('serviceFieldGroup')
    }

    function openInput() {
        let selectVal = $(".form-horizontal select").val();
        if (selectVal != '') {
            $('#common').attr('disabled', 'disabled')
            $('#common').val()
        }

        $('#serviceNameField').append("<span style=\"color: red\"> *</span>");
        $('#moduleField').append("<span style=\"color: red\"> *</span>");
        $('#typeField').append("<span style=\"color: red\"> *</span>");

        $('.nice-select').removeClass('disabled');
        $('#hciCode').attr('disabled', null);
        $("#hciCode").removeAttr("style","");
        hasClick = false;
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
