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
    <form class="form-horizontal" method="post" id="MasterCodeCreateForm" action=<%=process.runtime.continueURL()%>>
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
                            <iais:field value="Master Code Key." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeKey = request.getParameter("codeKey")==null?"":request.getParameter("codeKey");%>
                                    <input id="codeKey" type="text" name="codeKey" value="<%=codeKey%>">
                                    <span id="error_masterCodeKey" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Value." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeValue = request.getParameter("codeValue")==null?"":request.getParameter("codeValue");%>
                                    <input id="codeValue" type="text" name="codeValue" value="<%=codeValue%>">
                                    <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Category." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeCategory" type="text" name="codeCategory" value="${codeCategory}" readonly="readonly">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Description." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeDescription = request.getParameter("codeDescription")==null?"":request.getParameter("codeDescription");%>
                                    <input id="codeDescription" type="text" name="codeDescription" value="<%=codeDescription%>" maxlength="255">
                                    <span id="error_codeDescription" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="filterValue">Filter Value.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String filterValue = request.getParameter("filterValue")==null?"":request.getParameter("filterValue");%>
                                    <input id="filterValue" type="text" name="filterValue" value="<%=filterValue%>"/>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Sequence." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeSequence = request.getParameter("codeSequence")==null?"":request.getParameter("codeSequence");%>
                                    <input id="codeSequence" type="text" name="codeSequence" value="<%=codeSequence%>" oninput= "this.value=this.value.replace(/[^\d]/g,'')" maxlength="3"/>
                                    <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeRemarks">Remark.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeRemarks = request.getParameter("codeRemarks")==null?"":request.getParameter("codeRemarks");%>
                                    <input id="codeRemarks" type="text" name="codeRemarks" value="<%=codeRemarks%>" maxlength="255"/>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Version." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeVersion = request.getParameter("codeVersion")==null?"":request.getParameter("codeVersion");%>
                                    <input id="codeVersion" type="text" name="codeVersion" value="<%=codeVersion%>" oninput= "this.value=this.value.replace(/[^\d]/g,'')"/>
                                    <span id="error_version" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String codeStatus = request.getParameter("codeStatus")==null?"":request.getParameter("codeStatus");%>
                                    <iais:select name="codeStatus" id="codeStatus" options="codeStatus" value="<%=codeStatus%>"></iais:select>
                                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective Start Date." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String esd = request.getParameter("esd")==null?"":request.getParameter("esd");%>
                                    <iais:datePicker id="esd" name="esd" value="<%=esd%>"/>
                                    <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective End Date." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <%String eed = request.getParameter("eed")==null?"":request.getParameter("eed");%>
                                    <iais:datePicker id="eed" name="eed" value="<%=eed%>"/>
                                    <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-10 col-sm-10">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myCreateModal">SUBMIT</button></div>
                            </div>
                        </div>
                    </div>

                    <div class="modal fade" id="myCreateModal" tabindex="-1" role="dialog" aria-labelledby="myCreateModal" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">Do you confirm the modification ?</span></div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="button" class="btn btn-primary" onclick="submitAction('save')">Confirm</button>
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
    function submitAction(action){
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeCreateForm").submit();
    }
</script>