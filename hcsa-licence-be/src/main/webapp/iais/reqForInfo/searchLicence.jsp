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
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria For Licence</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Licence No:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="licence_no" value="${SearchParam.filters['licence_no']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Licence Type"/>
                                            <iais:value width="18">
                                                <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Status"/>
                                            <iais:value width="18">
                                                <iais:select name="licence_status" options="licStatusOption" firstOption="Please select" value="${licStatusOption}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Period:"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="To:"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicSearch()">Search</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicBack()">Back</button>
                                            <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicClear()">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </iais:body>


</form>
<script type="text/javascript">
    function doLicSearch(){
        SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function doLicBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[name="licence_no"]').val("");
        $("#licence_type option:first").val("");
        $("#licence_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").val("");
        $("#licence_status option:first").prop("selected", 'selected');
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }
</script>