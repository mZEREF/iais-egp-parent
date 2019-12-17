<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Create Master Code</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeKey">Master Code Key</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeKey" type="text" name="codeKey">
                                    <span id="error_masterCodeKey" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeValue">Code Value</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeValue" type="text" name="codeValue">
                                    <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeCategory">Code Category.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeCategory" type="text" name="codeCategory">
                                    <span id="error_codeCategory" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeDescription">Code Description.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeDescription" type="text" name="codeDescription">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="filterValue">Filter Value.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="filterValue" type="text" name="filterValue">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeSequence">Sequence</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeSequence" type="text" name="codeSequence">
                                    <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeRemarks">Remark.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeRemarks" type="text" name="codeRemarks">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeVersion">Version.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeVersion" type="text" name="codeVersion">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeStatus">Status.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeStatus" type="text" name="codeStatus">
                                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="esd" name="esd"/>
                                    <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="eed" name="esd"/>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-2 col-sm-2">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('back')">BACK</a></div>
                            </div>
                            <div class="col-xs-10 col-sm-10">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('save')">SUBMIT</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp"%>
</div>

<script>
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeForm").submit();
    }
</script>