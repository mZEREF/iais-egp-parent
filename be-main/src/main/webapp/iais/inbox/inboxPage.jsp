<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot= IaisEGPConstant.BE_CSS_ROOT;
%>
<style type="text/css">
    .border-div{
        padding-left: 0px;
        padding-right: 0px;
        background-color: #FFFFFF;
        border-top: 1px solid #CFCFCF;
        border-bottom: 1px solid #CFCFCF;
        margin-top: -1px;
        height: 3.5rem;
        vertical-align:middle;
        display:table;
    }
    .border-div-cell{
        border-left: 1px solid #CFCFCF;
        display:table-cell;
        height: 3.5rem;
    }
    .border-div-cell-last{
        border-right: 1px solid #CFCFCF;
    }
    .app-groups{
        padding-left: 0px;
        padding-right: 0px;
        border-top: 1px solid #CFCFCF;
        border-bottom: 1px solid #CFCFCF;
        vertical-align:middle;
        height: 3.5rem;
        margin-top: -1px;
    }
    .app-groups div{
        border-left: 1px solid #CFCFCF;
        display:table-cell;
        height: 3.5rem;
    }

    .app-in-group {
        padding-left: 0px;
        padding-right: 0px;
        margin-bottom: 1px;
        border-top: 1px solid #CFCFCF;
        vertical-align:middle;
        margin-top: -1px;
    }
    .app-in-group div{
        padding-left: 0px;
        padding-right: 0px;
        border: 1px solid #797979;
        display:table-cell;
        height: 3.5rem;
    }
    .app-in-group-head{
        border: 1px solid #FFFFFF !important;
        padding: 0px;
        margin: -1px;
    }
    .app-in-group-second{
        margin-left: 2px;
        border-left: 2px solid #797979 !important;
    }
</style>
<div class="main-content">
     <br>
     <br>


<form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%> >
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="SearchSwitchType" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <input type="hidden" id="inspector_name" name="inspector_name" value="">

    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>Search Criteria</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="HCI Code"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_code" value="${supTaskSearchParam.filters['hci_code']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Name"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_name" value="${supTaskSearchParam.filters['hci_name']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Address"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_address" value="${supTaskSearchParam.filters['blk_no']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application No."/>
                                            <iais:value width="18">
                                                <input type="text" name="application_no" value="${supTaskSearchParam.filters['application_no']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Type"/>
                                            <iais:value width="18">
                                                <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_type']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Status"/>
                                            <iais:value width="18">
                                                <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_status']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskSearch()">Search</button>
                                            <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskClear()">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <iais:pagination  param="supTaskSearchParam" result="supTaskSearchResult"/>
        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <h3>
                        <span>Search Result</span>
                    </h3>
                    <div class="table-gp" class="col-xs-12  col-lg-12">
                        <div class="table">
                            <div class="col-xs-12  col-lg-12 border-div">
                                <div class="col-xs-1 col-lg-1 border-div-cell"><iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader></div>
                                <div class="col-xs-3  col-lg-3 border-div-cell"><iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader></div>
                                <div class="col-xs-2  col-lg-2 border-div-cell"><iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"></iais:sortableHeader></div>
                                <div class="col-xs-2  col-lg-2 border-div-cell"><iais:sortableHeader needSort="false"  field="SUB_TYPE" value="Submission Type"></iais:sortableHeader></div>
                                <div class="col-xs-2  col-lg-2 border-div-cell"><iais:sortableHeader needSort="false"  field="APP_DATE" value="Application Date(dd/mm/yyyy)"></iais:sortableHeader></div>
                                <div class="col-xs-2  col-lg-2 border-div-cell border-div-cell-last"><iais:sortableHeader needSort="false"  field="PAY_STATUS" value="Payment Status"></iais:sortableHeader></div>
                            </div>
                            <c:choose>
                                <c:when test="${empty supTaskSearchResult.rows}">
                                    <div>
                                        <div  class="col-xs-12">
                                            <iais:message key="No Result!" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${supTaskSearchResult.rows}" varStatus="status">
                                        <div  class="col-xs-12 app-groups">
                                            <div  class="col-xs-1  col-lg-1 border-div-cell"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></div>
                                            <div class="col-xs-3  col-lg-3 border-div-cell"><c:out value="${pool.applicationGroupNo}"/>
                                                <p class="btn btn-default btn-sm" data-toggle="collapse" data-target="#advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}" onclick="getAppByGroupId('${pool.applicationGroupNo}','${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}')" >
                                                    show more
                                                </p>
                                            </div>
                                            <div class="col-xs-2  col-lg-2 border-div-cell"><c:out value="${pool.submitDate}"/></div>
                                            <div class="col-xs-2  col-lg-2 border-div-cell"><c:out value="${pool.applicationType}"/></div>
                                            <div class="col-xs-2  col-lg-2 border-div-cell"><c:out value="${pool.paymentstatus}"/></div>
                                            <div class="col-xs-2  col-lg-2 border-div-cell-last"><c:out value="${pool.paymentstatus}"/></div>
                                        </div>
                                        <div id="advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}" class="collapse table col-xs-12 col-lg-12 app-in-group">
                                            <div class="col-xs-12 col-lg-12 ">
                                                <div class="col-xs-1  col-lg-1 app-in-group-head"></div>
                                                <div class="col-xs-2  col-lg-2 app-in-group-second"><iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader></div>
                                                <div class="col-xs-2  col-lg-2"><iais:sortableHeader needSort="false"  field="SERVICE" value="Service"></iais:sortableHeader></div>
                                                <div class="col-xs-2  col-lg-2"><iais:sortableHeader needSort="false"  field="LICENE_EXPIRY_DATE" value="Licence Expiry Date"></iais:sortableHeader></div>
                                                <div class="col-xs-2  col-lg-2"><iais:sortableHeader needSort="false"  field="APPLICATION_STATUS" value="Application Status"></iais:sortableHeader></div>
                                                <div class="col-xs-1  col-lg-1"><iais:sortableHeader needSort="false"  field="HCICODE" value="HCI Code"></iais:sortableHeader></div>
                                                <div class="col-xs-2  col-lg-2"><iais:sortableHeader needSort="false"  field="HCIADDRESS" value="HCI Address"></iais:sortableHeader></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </iais:body>
</form>
</div>
<script type="text/javascript">
    var dividlist = [];
    function inspectorSearchTask_optionNameAuto(value){
        if(value != null && value != null){
            $("#inspectorSearchTask_inspectorName").val(value);
        }
        doInspectorSearchTaskSelect();
    }

    function doInspectorSearchTaskSelect(){
        var options=$("#inspectorSearchTask_inspectorName option:selected");
        $("#inspector_name").val(options);
    }

    function doInspectorSearchTaskAssign(taskId) {
        $("#taskId").val(taskId);
        submit('assign');
    }

    function doInspectorSearchTaskClear() {
        $('input[name="application_no"]').val("");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $('input[name="hci_code"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="hci_address"]').val("");
    }
    function submit(action){
        $("[name='SearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainSupForm');
        mainPoolForm.submit();
    }
    function doInspectorSearchTaskSearch() {
        submit('search');
    }

    function getAppByGroupId(applicationGroupNo, divid){
        var excute = dividlist.indexOf(divid);
        if(excute < 0){
            $.post(
                '/be-main/backend/appGroup.do',
                {groupno:applicationGroupNo},
                function (data,status) {
                    var res = data.ajaxResult;
                    var url = data.appNoUrl;
                    for(var i = 0; i < res.rowCount; i++){
                        var address = res.rows[i].blkNo + " " + res.rows[i].streetName + " " + res.rows[i].buildingName +
                            " # " + res.rows[i].floorNo + "-" + res.rows[i].unitNo + ", " + res.rows[i].postalCode;
                        $('#advfilter'+divid).append('<div class="col-xs-12 col-lg-12">' +
                            '<div class="col-xs-1  col-lg-1 app-in-group-head"></div>' +
                            '<div class="col-xs-2  col-lg-2 app-in-group-second"><a href='+url[res.rows[i].applicationNo]+'>'+res.rows[i].applicationNo+'</a></div>' +
                            '<div class="col-xs-2  col-lg-2">'+res.rows[i].serviceId+'</div>' +
                            '<div class="col-xs-2  col-lg-2">'+res.rows[i].applicationNo+'</div>' +
                            '<div class="col-xs-2  col-lg-2">'+res.rows[i].status+'</div>' +
                            '<div class="col-xs-1  col-lg-1">'+res.rows[i].hciCode+'</div>' +
                            '<div class="col-xs-2  col-lg-2">'+address+'</div>' +
                            '</div>');
                    }
                    dividlist.push(divid);
                }
            )

        }

    }

</script>
