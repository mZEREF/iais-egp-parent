<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="MasterCodeCreateForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                            <iais:field value="Master Code Category" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="codeCategory" firstOption="Pelect Select" options="codeCategory"
                                                 value="${param.codeCategory}"/>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Master Code Key" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeKey" type="text" name="codeKey" value="${param.codeKey}"
                                           maxlength="50">
                                    <span id="error_masterCodeKey" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Value" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeValue" type="text" name="codeValue" value="${param.codeValue}"
                                           maxlength="25">
                                    <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Description" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea id="codeDescription" cols="65" rows="6" name="codeDescription"
                                              maxlength="255">${param.codeDescription}</textarea>
                                    <span id="error_codeDescription" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Sequence"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeSequence" type="text" name="codeSequence"
                                           value="${param.codeSequence}" maxlength="3"/>
                                    <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeRemarks">Remarks</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <form><textarea cols="65" rows="6" id="codeRemarks" name="codeRemarks"
                                                    maxlength="255">${param.codeRemarks}</textarea></form>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Version" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeVersion" type="text" name="codeVersion" value="${param.codeVersion}"
                                           onkeyup="this.value=this.value.replace(/^\D*(\d{0,1}(?:\.\d{0,2})?).*$/g, '$1')"/>
                                    <span id="error_version" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="codeStatus" id="codeStatus" options="codeStatus"
                                                 value="${param.codeStatus}"/>
                                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective Start Date" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="esd" name="esd" value="${param.esd}"/>
                                    <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective End Date" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="eed" name="eed" value="${param.eed}"/>
                                    <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2 col-sm-2">
                            <div><a href="/system-admin-web/eservice/INTRANET/MohMasterCode"><em
                                    class="fa fa-angle-left"></em> Back</a></div>
                        </div>
                        <div class="col-xs-10 col-sm-10">
                            <div class="text-right text-center-mobile">
                                <button type="button" class="btn btn-primary" onclick="submitAction('save')">Create
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>

<script>
    function submitAction(action) {
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeCreateForm").submit();
    }
</script>