<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Advanced Search Criteria</h2>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="searchCondition" class="collapse">
                            <%@ include file="advancedFilter.jsp" %>
                        </div>
                    </div>
                    <br>
                    <%@ include file="searchResults.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function controlCease(isAso) {
        var checkOne = false;
        var checkBox = $("input[name='appIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
        }
        if (checkOne) {
            $('.ReqForInfoBtn').prop('disabled', false);
        } else {
            $('.ReqForInfoBtn').prop('disabled', true);
        }
        if (checkOne && isAso === "1") {
            $('.CeaseBtn').prop('disabled', false);
        } else {
            $('.CeaseBtn').prop('disabled', true);
        }
    }

    function jumpToPagechangePage() {
        licSearch()
    }

    function doLicSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        licSearch()
    }

    function checkAll(isAso) {
        if ($('#checkboxAll').is(':checked')) {
            $('input[name="appIds"]').prop("checked", true);
            var chk = $("[name='appIds']:checked");
            var dropIds = new Array();
            chk.each(function () {
                dropIds.push($(this).val());
            });
            if (dropIds.length !== 0) {
                $('.ReqForInfoBtn').prop('disabled', false);
                if (isAso === "1") {
                    $('.CeaseBtn').prop('disabled', false);
                }
            }

        } else {
            $('input[name="appIds"]').prop("checked", false);
            $('.CeaseBtn').prop('disabled', true);
            $('.ReqForInfoBtn').prop('disabled', true);
        }
    }

    function licSearch() {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "search");

    }

    function doLicBack() {
        showWaiting();
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doLicClear() {
        $('input[type="text"]').val("");
        $("#service_licence_type option:first").prop("selected", 'selected');
        $("#service_sub_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").prop("selected", 'selected');
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#personnelRoleOption option:first").prop("selected", 'selected');
        $("#service_licence_type .current").text("Please Select");
        $("#service_sub_type .current").text("Please Select");
        $("#licence_status .current").text("Please Select");
        $("#application_type .current").text("Please Select");
        $("#application_status .current").text("Please Select");
        $("#personnelRoleOption .current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
        $('input[name="start_date"]').val("");
        $('input[name="start_to_date"]').val("");
        $('input[name="expiry_start_date"]').val("");
        $('input[name="expiry_date"]').val("");
        $("#error_to_date").hide();
        $("#error_start_to_date").hide();
        $("#error_expiry_date").hide();
    }

    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details", licenceId);

    }

    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails", appCorrId);
    }

    function doCessation() {
        showWaiting();
        $("#selectDecisionMsg").hide();
        $("#selectDecisionMsgActive").hide();
        var chk = $("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function () {
            dropIds.push($(this).val());
        });
        var flog = true;
        for (var i = 0; i < dropIds.length; i++) {
            var str3 = dropIds[i].split('|')[3];
            var str1 = dropIds[i].split('|')[1];
            if (str1 === '2') {
                $("#selectDecisionMsg").show();
                $("#selectDecisionMsgActive").hide();
                flog = false;
            }
            if (str1 === '0') {
                $("#selectDecisionMsgActive").show();
                $("#selectDecisionMsg").hide();
                flog = false;
            }
            if(!(str1 === '1')){
                flog = false;
            }
        }
        if (flog) {
            SOP.Crud.cfxSubmit("mainForm", "cessation");
        } else {
            dismissWaiting();
        }
    }
    function keyPress() {
        var keyCode = event.keyCode;
        if (keyCode >= 48 && keyCode <= 57) {
            event.returnValue = true;
        }else {
            event.returnValue = false;
        }
    }
    function doReqForInfo() {
        showWaiting();
        $("#selectDecisionMsg").hide();
        $("#selectDecisionMsgActive").hide();
        var chk = $("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function () {
            dropIds.push($(this).val());
        });
        var flog = true;
        for (var i = 0; i < dropIds.length; i++) {
            var str = dropIds[i].split('|')[3];
            if (str !== 'Active') {
                flog = false;
            }
        }
        if (flog) {
            SOP.Crud.cfxSubmit("mainForm", "reqForInfo");
        } else {
            $("#selectDecisionMsgActive").show();
            dismissWaiting();
        }

    }

</script>