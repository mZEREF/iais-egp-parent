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
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h3>
                        <span>Additional Request Information</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Select Category"/>
                                            <iais:value width="18">
                                                <iais:select name="Select_category"  options="selectOptions" firstOption="Please select"  value="${selectOptions}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="RFI Title"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input id="rfiTitle" type="text" name="rfiTitle" >
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence No"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="licenceNo">
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="radio" name="rfiType" value="Ad-hoc" />Ad-hoc
                                                </label>
                                                <label>
                                                    <input type="radio" name="rfiType" value="Workflow" />Workflow
                                                </label>
                                            </iais:value>
                                        </iais:row>

                                        <iais:row>
                                            <iais:field value="File upload"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" name="need_doc"  />Yes
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due date for submission"/>
                                            <iais:value width="18">
                                                <iais:datePicker  name = "Due_date" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doRemind()">Remind</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doSubmit()">Submit Request</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</form>
<script type="text/javascript">
    function doRemind(){
        SOP.Crud.cfxSubmit("mainForm", "remind");
    }
    function doAccept(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "accept",reqInfoId);
    }
    function doCancel(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "cancel",reqInfoId);
    }
    function doSubmit() {
        SOP.Crud.cfxSubmit("mainForm", "submit");
    }
    $(document).ready(function(){
        $("select").change(function(){
            $("#rfiTitle").val($(this).val());
        });
    });
</script>