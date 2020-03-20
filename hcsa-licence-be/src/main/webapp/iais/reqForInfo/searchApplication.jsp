<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <iais:body >
        <div class="container">
            <div class="col-xs-14">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria For Application</span>
                    </h3>

                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Application No."/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" style="width:180%; font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Type"/>
                                            <iais:value width="18">
                                                <iais:select  name="application_type" options="appTypeOption" firstOption="Please Select" value="${SearchParam.filters['appType']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Status"/>
                                            <iais:value width="18">
                                                <iais:select  name="application_status" options="appStatusOption" firstOption="Please Select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date From"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date To"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row >
                                            <p style="color:#ff0000; display: none" id="submittedDateError">
                                                &nbsp;System detects that user enters Effective end date before Effective start date in predefined format.
                                            </p>
                                        </iais:row>
                                        <iais:action style="text-align:right;">
                                            <button class="btn btn-secondary" type="button"  onclick="javascript:doAppClear()">Clear</button>
                                            <button class="btn btn-secondary" type="button"  onclick="javascript:doAppBack()">Back</button>
                                            <button class="btn btn-primary" type="button"  onclick="javascript:doAppSearch()">Search</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>

                </div>
            </div>
        </div>
    </iais:body>

</form>
<script type="text/javascript">
    function doAppSearch(){
        showWaiting();
        var to=$('#to_date').val();
        var sub=$('#sub_date').val();
        if(sub>to){
            $("#submittedDateError").show();
            dismissWaiting();
        }
        else {
            SOP.Crud.cfxSubmit("mainForm", "search");
        }
    }
    function doAppBack(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doAppClear(){
        $('input[name="application_no"]').val("");
        $("#application_type option[text = 'Please Select']").val("selected", "selected");
        $("#application_status option[text = 'Please Select']").val("selected", "selected");
        $(".current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }
</script>