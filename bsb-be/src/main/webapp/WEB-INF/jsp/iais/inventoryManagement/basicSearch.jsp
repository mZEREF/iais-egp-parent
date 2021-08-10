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
                                               name="searchNo" maxlength="100"/>
                                    </label>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:value width="18" >
                                    <input id="biologicalAgentChk" type="radio"
                                           <c:if test="${count=='1'}">checked</c:if>
                                           name="searchChk"
                                           value="1"/>
                                    <label for="biologicalAgentChk">&nbsp;Search by Biological Agent/Toxin</label>
                                </iais:value>
                                <iais:value width="18">
                                    <input id="transactionDateChk" type="radio"
                                           name="searchChk" value="2"
                                           <c:if test="${count=='2'}">checked</c:if> />
                                    <label for="transactionDateChk">&nbsp; Search by Transaction Date</label>
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
                                    <button type="button" class="btn btn-secondary" type="button"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary" type="button"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                    <button type="button" class="btn btn-primary" type="button"
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
            SOP.Crud.cfxSubmit("mainForm", "doSearch");
        }
    }
    function doHisInfo() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "adjust");
    }
</script>