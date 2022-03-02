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
    <form class="form-horizontal" method="post" id="InternetUserEditForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="user_action" value="edit">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Edit Intranet User</h2>
                        </div>
                        <div class="tab-gp dashboard-tab">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="<c:if test="${activeTab==1 || activeTab==null}">active</c:if> <c:if test="${activeTab!=1 && activeTab!=null}">complete</c:if> " role="presentation"><a href="#tabAccount" aria-controls="tabAccount"
                                                                                                                                                                                                  role="tab" data-toggle="tab">Account
                                    Information</a></li>
                                <li class="<c:if test="${activeTab!=2}">complete</c:if> <c:if test="${activeTab==2}">active</c:if>" role="presentation"><a href="#tabPersonal"
                                                                                                                                                           aria-controls="tabPersonal" role="tab"
                                                                                                                                                           data-toggle="tab">Personal Information</a>
                                </li>
                                <li class="<c:if test="${activeTab!=3}">complete</c:if> <c:if test="${activeTab==3}">active</c:if>" role="presentation"><a href="#tabContact"
                                                                                                                                                           aria-controls="tabContact" role="tab"
                                                                                                                                                           data-toggle="tab">Contact Information</a>
                                </li>
                            </ul>
                        </div>
                        <br/><br/>
                        <div class="tab-content">
                            <div class="tab-pane <c:if test="${activeTab==1}">active</c:if> <c:if test="${activeTab==null}">active</c:if>" id="tabAccount" role="tabAccount">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="userId">User ID <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="userId" style="border-color: rgb(237, 237, 237); color: rgb(153, 153, 153);" type="text" maxlength="20" name="userId" value="${orgUserDto.userId}" disabled>
                                            <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label" for="displayName">Display Name <span style="color:red">*</span></label>
                                        <iais:value>
                                            <div class="col-xs-8 col-sm-6 col-md-5">
                                                <input id="displayName" type="text" maxlength="20" name="displayName" value="${orgUserDto.displayName}">
                                                <span id="error_displayName" name="iaisErrorMsg" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label" for="startDate">Account Activation Start <span style="color:red">*</span></label>
                                        <iais:value>
                                            <div class="col-xs-8 col-sm-6 col-md-5">
                                                <iais:datePicker id="startDate" name="startDate" dateVal="${orgUserDto.accountActivateDatetime}"/>
                                                <span id="error_accountActivateDatetime" name="iaisErrorMsg" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label" for="endDate">Account Activation End <span style="color:red">*</span></label>
                                        <iais:value>
                                            <div class="col-xs-8 col-sm-6 col-md-5">
                                                <iais:datePicker id="endDate" name="endDate" dateVal="${orgUserDto.accountDeactivateDatetime}"/>
                                                <span id="error_accountDeactivateDatetime" name="iaisErrorMsg" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="endDate">Status <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <iais:select id="status" options="statusOptions" firstOption="Please Select" name="status" value="${orgUserDto.status}"/>
                                            <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="remarks">Available </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="available" type="checkbox" name="available" <c:if test="${orgUserDto.available == true}">checked</c:if> >
                                        </div>
                                    </iais:value>
                                </div>
                            </div>
                            <div class="tab-pane <c:if test="${activeTab==2}">active</c:if>" id="tabPersonal" role="tabPersonal">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Salutation </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <iais:select name="salutation" options="salutation" firstOption="Please Select" value="${orgUserDto.salutation}"></iais:select>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="firstName">Name <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="firstName" minlength="10" maxlength="66" type="text" name="firstName" value="${orgUserDto.firstName}">
                                            <span id="error_firstName" name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group hidden">
                                    <label class="col-xs-12 col-md-4 control-label" for="lastName">Last Name <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="lastName" type="text"  minlength="10" maxlength="66" name="lastName" value="${orgUserDto.lastName}">
                                            <span id="error_lastName" name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="organization">Organization </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="organization" type="text" maxlength="20" name="organization" value="${orgUserDto.organization}">
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="division">Division </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="division" type="text" maxlength="20" name="division" value="${orgUserDto.division}">
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="branch">Branch / Unit </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="branch" type="text" maxlength="20" name="branch" value="${orgUserDto.branchUnit}">
                                        </div>
                                    </iais:value>
                                </div>
                            </div>
                            <div class="tab-pane <c:if test="${activeTab==3}">active</c:if>" id="tabContact" role="tabContact">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="email">Email <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="email" type="text" maxlength="66" name="email" value="${orgUserDto.email}">
                                            <span id="error_email" name="iaisErrorMsg" class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="mobileNo">Mobile No. <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="mobileNo" maxlength="8" type="text" name="mobileNo" value="${orgUserDto.mobileNo}">
                                            <span id="error_mobileNo" name="iaisErrorMsg" class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="officeNo">Office No. <span style="color:red">*</span></label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="officeNo" type="text" maxlength="8" name="officeNo" value="${orgUserDto.officeTelNo}">
                                            <span id="error_officeTelNo" name="iaisErrorMsg" class="error-msg"></span>
                                        </div>
                                    </iais:value>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label" for="remarks">Remarks </label>
                                    <iais:value>
                                        <div class="col-xs-8 col-sm-6 col-md-5">
                                            <input id="remarks" maxlength="100" type="text" name="remarks"
                                                   value="${orgUserDto.remarks}">
                                        </div>
                                    </iais:value>
                                </div>
                            </div>
                        </div>
                            <iais:action>
                                <a href="#" style="margin-left: 0%" class="back <c:if test="${PERSONAL_PROFILE}">hidden</c:if>" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
                                <a style="margin-left: 90%" class="btn btn-primary" data-toggle="modal" data-target= "#editUser">SUBMIT</a>
                            </iais:action>
                    </div>
                </div>

                <!-- Modal -->
                <div class="modal fade" id="editUser" tabindex="-1" role="dialog" aria-labelledby="editUser">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
<%--                            <div class="modal-header">--%>
<%--                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span--%>
<%--                                        aria-hidden="true">&times;</span></button>--%>
<%--                                <div class="modal-title" id="gridSystemModalLabel" style="font-size: 2rem;">Confirmation Box</div>--%>
<%--                            </div>--%>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">Do you confirm the modification ?</span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="doCreate()">Confirm</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Modal End-->
            </div>
        </div>
</div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>

<script type="text/javascript">
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#InternetUserEditForm").submit();
    }

    function doCreate() {
        submit('doSave');
    }
</script>