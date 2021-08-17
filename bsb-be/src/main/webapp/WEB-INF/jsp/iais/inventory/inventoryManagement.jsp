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

<%
    String webroot= IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <c:if test="${count == '1'}">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                                Search by Biological Agent/Toxin<strong>&nbsp;
                                <input type="radio" <c:if test="${count=='1'}">checked</c:if>   value="1"  name="searchChk" />
                            </strong>
                        </div>
                        <iais:section title="">
                            <iais:row>
                                <iais:field value="Schedule Type"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:select name="scheduleType" id="scheduleType" options="scheduleType" firstOption="Please Select"></iais:select>
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
                                    <iais:select name="type_of_transaction" options="tot" firstOption="Please Select"></iais:select>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Date of Transaction From"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker></iais:datePicker>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Date of Transaction To"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker></iais:datePicker>
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
                    <c:if test="${count == '2'}">
                        <div class="row form-horizontal">
                            <div class="bg-title col-xs-12 col-md-12">
                                Search by Transaction Date<strong>&nbsp;
                                    <input type="radio" <c:if test="${count=='2'}">checked</c:if>   value="2"  name="searchChk"  />
                                </strong>
                            </div>
                            <iais:section title="">
                                <iais:row>
                                    <iais:field value="Transaction Date From"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:datePicker></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Transaction Date To "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:datePicker></iais:datePicker>
                                    </div >
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Sending Facility "/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="sendingFacility " options="sendingFacility"  firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Receiving Facility"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="receivingFacility" options="receivingFacility" firstOption="Please Select"></iais:select>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Name of Biological Agent/Toxin"/>
                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                        <iais:select name="name_of_biological_agent/Toxin" options="noba"  firstOption="Please Select"></iais:select>
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
                                    onclick="javascript:doSearch();">Search
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
    function doClear() {
        $('input[type="text"]').val("");
        $("#scheduleType option:first").prop("selected", 'selected');
    }

    function doSearch() {
        showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function doBack() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doHisInfo() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "adjust");
    }


</script>