<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inventory.js"></script>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot= IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <c:if test="${count == 'agent'}">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                                Search by Biological Agent/Toxin<strong>&nbsp;
                                <input type="radio" <c:if test="${count=='agent'}">checked</c:if>   value="agent"  name="searchChk" />
                            </strong>
                        </div>
                        <iais:section title="">
                            <iais:row>
                                <iais:field value="Schedule Type"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="scheduleType" id="scheduleType" codeCategory="CATE_ID_BSB_SCHEDULE_TYPE" value="${inventoryParam.scheduleType}" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Biological Agent/Toxin"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="bioName" options="bioName" value="${inventoryParam.bioName}" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Type of Transaction"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="transactionType" codeCategory="CATE_ID_BSB_TYPE_OF_TRANSACTION" value="${inventoryParam.transactionType}" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <div class="col-sm-5 col-md-4 control-label">
                                    <label for="transactionDateTo">Date of Transaction From</label>
                                </div>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <input type="text" autocomplete="off" name="transactionDateFrom" id="transactionDateFrom" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" value="${inventoryParam.transactionDtFrom}" class="date_picker form-control" />
                                </div >
                            </iais:row>
                            <iais:row>
                               <div class="col-sm-5 col-md-4 control-label">
                                   <label for="transactionDateTo">Date of Transaction To</label>
                               </div>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <input type="text" autocomplete="off" name="transactionDateTo" id="transactionDateTo" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" value="${inventoryParam.transactionDtTo}" class="date_picker form-control" />
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Facility Name"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="facilityName" options="facilityName" multiValues="${inventoryParam.facilityName}"  firstOption="Please Select" multiSelect="true"></iais:select>
                                </div >
                            </iais:row>
                        </iais:section>
                    </div>
                    </c:if>
                    <c:if test="${count == 'date'}">
                        <div class="row form-horizontal">
                            <div class="bg-title col-xs-12 col-md-12">
                                Search by Transaction Date<strong>&nbsp;
                                    <input type="radio" <c:if test="${count=='date'}">checked</c:if>   value="date"  name="searchChk"  />
                                </strong>
                            </div>
                            <iais:section title="">
                                <iais:row>
                                    <iais:field value="Transaction Date From"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:datePicker name = "transactionDateFrom" dateVal="${inventoryParam.transactionDtFrom}"></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Transaction Date To "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:datePicker name = "transactionDateTo" dateVal="${inventoryParam.transactionDtTo}"></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Sending Facility "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="sendFacility" options="facilityNameOps" value="${inventoryParam.sendFacility}" firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Receiving Facility"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="recFacility" options="facilityNameOps" value="${inventoryParam.recFacility}" firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Name of Biological Agent/Toxin"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="bioName" options="bioName" value="${inventoryParam.bioName}" firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                            </iais:section>
                        </div>
                    </c:if>
                    <div class="col-xs-12 col-md-12">
                        <iais:action style="text-align:right;">
                            <a style=" float:left;padding-top: 1.1%;text-decoration:none;" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>
                            <button type="button" class="btn btn-secondary" type="button"
                                    onclick="javascript:doClear();">Clear
                            </button>
                            <button type="button" class="btn btn-primary" type="button"
                                    onclick="javascript:doAdvSearch();">Search
                            </button>
                        </iais:action>
                    </div>
                    <br>
                    <%@ include file="searchResults.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>
