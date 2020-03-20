<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
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
                        <span>Request For Information</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="">
                                        <iais:row>
                                            <iais:field value="Title:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <textarea id="rfiTitle" maxlength="500" rows="8" cols="70" style=" font-weight:normal;" name="rfiTitle" ></textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence No:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" style="width:170%; font-weight:normal;" maxlength="30" name="licenceNo" style=" font-weight:normal;" value="${licenceNo}">
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date:"/>
                                            <iais:value width="18" style="width:30%; font-weight:normal;">
                                                <%Date dueDate ;
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.add(Calendar.DATE,14);
                                                    dueDate = calendar.getTime();%>
                                                <iais:datePicker   name = "Due_date" dateVal="<%=dueDate%>" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox"  /> Information
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" name="reqType" /> Supporting Documents
                                                </label>

                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:left;">
                                            <a  onclick="javascript:doBack()">< Back</a>
                                        </iais:action>
                                        <iais:action style="text-align:right;">
                                            <button class="btn btn-primary" type="button"  onclick="javascript:doSubmit('${licenceNo}')">Submit</button>
                                        </iais:action>
                                        <iais:row >
                                            <div class="col-sm-9" style="display: none" id="isAlert">
                                                <p style="color:#ff0000;">
                                                    Licence is still pending Applicant's input.Please do not submit any new Requset For Information.
                                                </p>
                                            </div>
                                        </iais:row>
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
    function doBack(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doSubmit(licenceNo) {

        if(licenceNo!=""){
            $("#isAlert").show();
        }
        else {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "submit");
        }

    }

</script>