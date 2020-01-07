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
    <form class="form-horizontal" method="post" id="IntranetUserForm" action=<%=process.runtime.continueURL()%>>

        <%@ include file="/include/formHidden.jsp" %>
        <%--Validation fields Start--%>
        <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.MohIntranetUserDelegator"/>
        <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
        <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

<%--        <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator"/>--%>
<%--        <input type="hidden" name="valProfiles" id="valProfiles" value=""/>--%>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Create Intranet User</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="userId">User ID.</label>
                            <span style="color:red">*</span>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="userId" type="text" name="userId" value="${orgUserDto.userId}">
                                    <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="displayName">Display Name.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="displayName" type="text" name="displayName" value="${orgUserDto.displayName}">
                                    <span id="error_displayName" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="startDate">Account Activation Start.</label>
                            <span style="color:red">*</span>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    value="${orgUserDto.userId}"
                                    <iais:datePicker id="startDate" name="startDate" dateVal="${orgUserDto.accountActivateDatetime}"/>
                                    <span id="error_accountActivateDatetime" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="endDate">Account Activation End.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
<%--                                    <td>--%>
<%--                                        <fmt:formatDate value="${orgUserDto.accountActivateDatetime}" pattern="dd/MM/yyyy"></fmt:formatDate>--%>
<%--                                    </td>--%>
                                    <iais:datePicker id="endDate" name="endDate" dateVal="${orgUserDto.accountDeactivateDatetime}"/>
                                    <span id="error_accountDeactivateDatetime" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Salutation.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="salutation" options="salutation" firstOption="Please select" value="${orgUserDto.salutation}"></iais:select>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="firstName">First Name.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="firstName" type="text" name="firstName" value="${orgUserDto.firstName}">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="lastName">Last Name.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="lastName" type="text" name="lastName" value="${orgUserDto.lastName}">
                                </div>
                            </iais:value>
                        </div>
<%--                        <div class="form-group">--%>
<%--                            <label class="col-xs-12 col-md-4 control-label" for="organization">Organization.</label>--%>
<%--                            <iais:value>--%>
<%--                                <div class="col-xs-8 col-sm-6 col-md-5">--%>
<%--                                    <input id="organization" type="text" name="organization">--%>
<%--                                    <span id="error_codeCategory" name="iaisErrorMsg" class="error-msg"></span>--%>
<%--                                </div>--%>
<%--                            </iais:value>--%>
<%--                        </div>--%>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="division">Division.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="division" type="text" name="division" value="${orgUserDto.division}">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="branch">Branch/Unit.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="branch" type="text" name="branch" value="${orgUserDto.branchUnit}">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="email">Email</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="email" type="text" name="email" value="${orgUserDto.email}">
                                    <span id="error_email" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="mobileNo">Mobile No.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="mobileNo" type="number" name="mobileNo" value="${orgUserDto.mobileNo}">
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="officeNo">Office No.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="officeNo" type="number" name="officeNo" value="${orgUserDto.officeTelNo}">
                                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="remarks">Remarks.</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="remarks" type="text" name="remarks" value="${orgUserDto.remarks}">
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
                                <div class="text-right text-center-mobile"><a type="button" class="btn btn-primary" data-toggle="modal" data-target="#createUser">SUBMIT</a></div>
                            </div>
                        </div>
                    </div>
                    <!-- Modal -->
                    <div class="modal fade" id="createUser" tabindex="-1" role="dialog" aria-labelledby="createUser" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
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
                                    <button type="button" class="btn btn-primary" onclick="doCreate()">Confirm</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--Modal End-->
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp"%>
</div>

<script type="text/javascript">
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#IntranetUserForm").submit();
    }


    function doCreate(){
        submit('doSave');
    }

</script>