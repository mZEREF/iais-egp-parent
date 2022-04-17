<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Request For Information List</h2>
                        </div>
                        <iais:row>
                            <iais:field value="Facility No." width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="LFA210707UCF001" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Submission Type" width="15" required="false"/>
                            <iais:value width="10">
                                <c:out value="Facility Registration" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Title" width="15"  mandatory="true"/>
                            <iais:value width="10">
                                <textarea id="rfiTitle"
                                          class="textarea"
                                          style=" font-weight:normal;"
                                          maxlength="500"
                                          rows="8"
                                          cols="64"
                                          name="rfiTitle" >
                                </textarea>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Due Date" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:datePicker value="" name="DueDate"></iais:datePicker>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Status" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:select cssClass="statusDropdown" id="rfiStatus" name="status" firstOption="please Select" ></iais:select>
                            </iais:value>
                        </iais:row>
                        <div class="row">
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox"  value="information" name = "info" />&nbsp;Information
                            </label>
                        </div>
                        <div class="row">
                            <label class="col-xs-9 col-md-3 control-label">
                                <input type="checkbox" value="documents" name ="doc" />&nbsp;Supporting Documents
                            </label>
                        </div>
                        <iais:row>
                            <iais:action style="text-align:right;">
                                <button class="btn btn-secondary" type="button"  onclick="javascript:doBack()">Cancel</button>
                                <button class="btn btn-primary" type="button"   onclick="javascript:doSubmit()  ">Submit</button>
                            </iais:action>
                        </iais:row>
                    </div>
                </div>
            </div>
            </div>
        </div>
    </form>
</div>
<script>
    function doBack(){
        showWaiting();
        $("[name='action_type']").val("cancel");
        $("#mainForm").submit();
    }
    function doSubmit(){
        showWaiting();
        $("[name='action_type']").val("submit");
        $("#mainForm").submit();
    }
</script>