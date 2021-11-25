<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Basic Search Criteria</h2>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition" aria-expanded="true">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="searchCondition" class="collapse" aria-expanded="true" >
                            <iais:row>
                                <iais:field value="Keyword search or part of"/>
                                <iais:value width="18">
                                    <label>
                                        <input type="text"
                                               style="width:180%; font-weight:normal;"
                                               name="searchNo" maxlength="100"
                                               value="${searchNo}"/>
                                    </label>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="hciChk" type="radio"
                                           name="searchChk" value="1"
                                           <c:if test="${count=='1'}">checked</c:if> /><label for="hciChk">&nbsp;HCI Name</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="applicationChk" type="radio"
                                           <c:if test="${count=='2'}">checked</c:if>
                                           name="searchChk"
                                           value="2"/><label for="applicationChk">&nbsp;Application No</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="licenceChk" type="radio"
                                           <c:if test="${count=='3'}">checked</c:if> value="3"
                                           name="searchChk"/><label for="licenceChk">&nbsp;Licence No</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="licenseeChk" type="radio"
                                           <c:if test="${count=='4'}">checked</c:if> value="4"
                                           name="searchChk"/><label for="licenseeChk">&nbsp;Licensee Name</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="servicePersonnelChk" type="radio"
                                           value="5"
                                           <c:if test="${count=='5'}">checked</c:if>
                                           name="searchChk"/><label for="servicePersonnelChk">&nbsp;Service Personnel Name</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="uenChk" type="radio"
                                           value="6"
                                           <c:if test="${count=='6'}">checked</c:if>
                                           name="searchChk"/><label for="uenChk">&nbsp;UEN</label>
                                </iais:value>
                            </iais:row>
                            <iais:row id="selectSearchChkMsg" style="display: none">
                                <div class="row" height="1"
                                     style="font-size: 1.6rem; color: #D22727; padding-left: 20px"
                                     id="selectSearchChkMsg">
                                    <iais:message key="OEN_ERR004"
                                                  escape="flase"></iais:message>
                                </div>
                            </iais:row>
                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doAdvancedSearch();">Advanced Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>
                    <%@ include file="searchResults.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">


    function controlCease(isAso) {
        var checkOne = false;
        var checkBox = $("input[name='appIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;
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
        ;
    }

    function doClear() {
        $("#selectSearchChkMsg").hide();
        $('input[name="searchNo"]').val("");
        $('input[type="radio"]').prop("checked", false);
    }

    function doAdvancedSearch() {
        showWaiting();
        var chk = $("[name='searchChk']:checked");
        var dropIds = new Array();
        chk.each(function () {
            dropIds.push($(this).val());
        });
        if (dropIds.length === 0) {
            $("#selectSearchChkMsg").show();
            dismissWaiting();
        } else {
            SOP.Crud.cfxSubmit("mainForm", "advSearch");
        }
    }

    function jumpToPagechangePage() {
        search();
    }

    function doSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        var chk = $("[name='searchChk']:checked");
        var dropIds = new Array();
        chk.each(function () {
            dropIds.push($(this).val());
        });
        if (dropIds.length === 0) {
            $("#selectSearchChkMsg").show();
            dismissWaiting();
        } else {
            SOP.Crud.cfxSubmit("mainForm", "basicSearch");
        }
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }

    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details", licenceId);

    }

    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails", appCorrId);
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