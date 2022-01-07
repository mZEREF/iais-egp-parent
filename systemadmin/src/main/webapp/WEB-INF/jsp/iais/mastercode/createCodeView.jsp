<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content" style="min-height: 73vh;">
    <form class="form-horizontal" method="post" id="MasterCodeCategoryCreateForm" action=<%=process.runtime.continueURL()%>>
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
                            <label class="col-xs-12 col-md-4 control-label">Master Code Category</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <p><iais:code code="${MasterCodeView.codeCategory}"/></p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Filter Value</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <p>${MasterCodeView.filterValue}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Version</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <c:choose>
                                    <c:when test="${empty MasterCodeView.version}">
                                        <p>-</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>${ MasterCodeView.version}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Value" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeCategoryValue" type="text" name="codeCategoryValue" maxlength="25" value="${param.codeCategoryValue}">
                                    <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Description" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea id="codeCategoryDescription" style="width: 100%;margin-bottom: 15px;" rows="6" name="codeCategoryDescription"
                                              maxlength="255">${param.codeCategoryDescription}</textarea>
                                    <span id="error_codeDescription" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Sequence" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeCategorySequence" type="text" name="codeCategorySequence" maxlength="3" value="${param.codeCategorySequence}">
                                    <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="codeCategoryRemarks">Remarks</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <form><textarea style="width: 100%" rows="6" id="codeCategoryRemarks"  name="codeCategoryRemarks" maxlength="255">${param.codeCategoryRemarks}</textarea></form>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="codeCategoryStatus" id="codeCategoryStatus"  value="${param.codeCategoryStatus}" options="codeStatusSelectList" firstOption="Please Select"/>
                                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective Start Date" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="categoryEsd" name="categoryEsd" value="${param.categoryEsd}"/>
                                    <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective End Date" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="categoryEed" name="categoryEed" value="${param.categoryEed}"/>
                                    <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="row">
                        <div class="row">
                            <div class="col-xs-2 col-sm-2">
                                <div><a href="/system-admin-web/eservice/INTRANET/MohMasterCode"><em class="fa fa-angle-left"></em> Back</a></div>
                            </div>
                            <div class="col-xs-10 col-sm-10">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" onclick="submitCategoryAction('save')">Create</button></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>

<script>
    function submitCategoryAction(action){
        showWaiting();
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeCategoryCreateForm").submit();
    }
</script>