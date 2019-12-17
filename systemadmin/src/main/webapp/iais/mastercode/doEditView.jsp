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
    <form class="form-horizontal" method="post" id="MasterCodeEditForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Edit Master Code</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="category">Master Code Key</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="MC_Key" type="text" value="${MasterCodeDto.masterCodeKey}" name="codeKey">
                                <span id="error_masterCodeKey" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="category">Code Value</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="codeValue" type="text" value="${MasterCodeDto.codeValue}" name="codeValue">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="category">Code Category.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="category" type="text" value="${MasterCodeDto.codeCategory}" name="codeCategory">
                                <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Code Description.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="description" type="text" value="${MasterCodeDto.codeDescription}" name="codeDescription">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Filter Value.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="filterValue" type="text" value="${MasterCodeDto.filterValue}" name="filterValue">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Sequence</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="sequence" type="text" value="${MasterCodeDto.sequence}" name="codeSequence">
                                <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Remark.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="remark" type="text" value="${MasterCodeDto.remarks}" name="codeRemarks">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Version.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="version" type="text" value="${MasterCodeDto.version}" name="codeVersion">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="status">Status.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="status" type="text" value="${MasterCodeDto.status}" name="codeStatus">
                                <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:datePicker id="esd" name="esd" value="${MasterCodeDto.effectiveFrom}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:datePicker id="eed" name="eed" value="${MasterCodeDto.effectiveTo}" ></iais:datePicker>
                                <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="row">
                                <div class="col-xs-2 col-sm-2">
                                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('back')">BACK</a></div>
                                </div>
                                <div class="col-xs-10 col-sm-10">
                                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="doEdit(${MasterCodeDto.masterCodeId})">SUBMIT</a></div>
                                </div>
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
        $("#MasterCodeEditForm").submit();
    }

    function doEdit(mcId){
        $("[name='crud_action_value']").val(mcId);
        submit("edit");
    }

</script>