<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<div class="dashboard" >
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <br><br><br>
                            <div class="col-xs-12">
                                <div class="panel-group" id="accordion" role="tablist" id="collapseOne" aria-multiselectable="true">
                                    <br><br>
                                    <div class="panel panel-default">
                                        <div class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                            <div class="panel-body">
                                                <div class="panel-main-content">
                                                    <br><br><br>
                                                    <iais:section title="" >
                                                        <iais:row>
                                                            <iais:field value="Reference No."/>
                                                            <iais:value width="18">
                                                                sadfsdfsdf
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Submission Type"/>
                                                            <iais:value width="18">
                                                                sdfsdfsdfd
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Title"/>
                                                            <iais:value width="18">
                                                                asdfsdfsdf
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Due Date" />
                                                            <iais:value width="18">
                                                                <iais:datePicker value="${newReqInfo.dueDate}" name="dueDate"></iais:datePicker>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Status"/>
                                                            <iais:value width="18">
                                                                <iais:select cssClass="statusDropdown" id="rfiStatus"  name="status" firstOption="Please Select"></iais:select>
                                                            </iais:value>
                                                        </iais:row>
                                                        <div class="row" >
                                                            <label class="col-xs-9 col-md-3 control-label">
                                                                <input type="checkbox" disabled checked
                                                                       onchange="checkTitleInfo()"
                                                                       value = "0"
                                                                       name = "info" />&nbsp;Information
                                                            </label>
                                                        </div>
                                                        <div class="row">
                                                            <label class="col-xs-9 col-md-3 control-label">
                                                                <input type="checkbox" disabled checked
                                                                       onchange="checkTitleDoc()"
                                                                       value = "0"
                                                                       name ="doc" />&nbsp;Supporting Documents
                                                            </label>
                                                        </div>
                                                        <br/>
                                                        <br/>
                                                        <br/>
                                                        <br/>
                                                        <div class="row" >
                                                            <div class="col-sm-12 col-md-11 col-xs-12" style="margin-left: 15px;padding-left: 30px;">
                                                                <iais:row>
                                                                    <label class="col-xs-9 col-md-6 control-label" >
                                                                        <div class="infoTitIndex">
                                                                            More info title
                                                                        </div>
                                                                    </label>
                                                                </iais:row>
                                                                <iais:row>
                                                                    <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                                                        <textarea  rows="8" maxlength="500" class="form-control"></textarea>
                                                                    </iais:value>
                                                                </iais:row>
                                                                <br/>
                                                                <iais:row>
                                                                    <label class="col-xs-9 col-md-6 control-label" >
                                                                        <div class="infoTitIndex">
                                                                            More info Supp Doc
                                                                        </div>
                                                                    </label>
                                                                </iais:row>
                                                                <iais:row>
                                                                    <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                                                                        dsfsdfsdf
                                                                    </iais:value>
                                                                </iais:row>
                                                            </div>
                                                        </div>
                                                        <br/>
                                                        <br/>
                                                        <iais:row>
                                                            <iais:action style="text-align:right;">
                                                                <button class="btn btn-secondary" type="button"  onclick="javascript:doBack()">Cancel</button>
                                                                <button class="btn btn-primary" type="button" style="margin-left: 50px"  onclick="javascript:doSubmit()  ">Submit</button>
                                                            </iais:action>
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
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("[name='action_type']").val("cancel");
        $("#mainForm").submit();
    }
</script>

