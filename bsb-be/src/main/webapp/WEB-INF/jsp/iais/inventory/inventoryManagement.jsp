<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
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
                                    <iais:select name="scheduleType" id="scheduleType" codeCategory="CATE_ID_BSB_SCHEDULE_TYPE" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Biological Agent/Toxin"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="biologicalAgent" options="biologicalAgent" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Type of Transaction"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="transactionType" codeCategory="CATE_ID_BSB_TYPE_OF_TRANSACTION" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Date of Transaction From"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker name = "transactionDateFrom"></iais:datePicker>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Date of Transaction To"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker name = "transactionDateTo"></iais:datePicker>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Facility Name"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="facilityName" options="facilityName"  firstOption="Please Select" multiSelect="true"></iais:select>
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
                                        <iais:datePicker name = "transactionDateFrom"></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Transaction Date To "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:datePicker name = "transactionDateTo"></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Sending Facility "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="sendFacility" options="sendingFacility"  firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Receiving Facility"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="recFacility" options="receivingFacility" firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Name of Biological Agent/Toxin"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="biologicalAgent" options="biologicalAgent"  firstOption="Please Select"></iais:select>
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
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">



</script>