<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<style>
    .btn.btn-sm {
        font-size: .775rem;
        font-weight: 500;
        padding: 6px 10px;
        text-transform: uppercase;
        border-radius: 30px;
    }
</style>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>${feusertitle} Account</h2>
                        </div>

                        <iais:row>
                            <iais:field value="UEN" width="11" required="true"/>
                            <c:choose>
                                <c:when test="${'Create'.equals(feusertitle)}">
                                    <iais:value width="11">
                                        <iais:select id="organizationId" name="organizationId" options="uenSelection"  cssClass="designationOption" firstOption="Please Select"
                                                     value="${inter_user_attr.orgId}"></iais:select>
                                        <span class="error-msg" name="errorMsg" id="error_organizationId"></span>
                                    </iais:value>
                                </c:when>
                                <c:otherwise>
                                    <iais:value width="11">
                                        <input name="uenNo" id="uenNo" type="text" value="${uenNo}" readonly/>
                                        <input name="organizationId" id="organizationId" type="text" value="${organizationId}" hidden/>
                                    </iais:value>
                                </c:otherwise>
                            </c:choose>

                        </iais:row>

                                    <iais:row>
                                        <iais:field value="Name" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="name" id="name" maxLength="66" value="${inter_user_attr.displayName}"/>
                                            <span class="error-msg" name="errorMsg" id="error_displayName"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Salutation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="Salutation" name="salutation" id="salutation" value="${inter_user_attr.salutation}"
                                                         codeCategory="CATE_ID_SALUTATION"  firstOption="Please Select" />
                                            <span class="error-msg" name="errorMsg" id="error_salutation"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID Type" width="11" required="true"/>
                                            <iais:value width="11">
                                                <iais:select name="idType" id="idType" value="${inter_user_attr.idType}"
                                                             codeCategory="CATE_ID_ID_TYPE" firstOption="Please Select"/>
                                                <span class="error-msg" name="errorMsg" id="error_idType"></span>
                                            </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="ID No" width="11" required="true"/>
                                        <iais:value width="11">
                                                <input type="text" name="idNo" id="idNo" value="${inter_user_attr.identityNo}" maxlength="9"/>
                                                <span class="error-msg" name="errorMsg" id="error_identityNo"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${inter_user_attr.designation}" firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="error_designation"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mobile No" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="mobileNo" id="mobileNo" maxLength="8" value="${inter_user_attr.mobileNo}"/>
                                            <span class="error-msg" name="errorMsg" id="error_mobileNo"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office/Telephone No" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="officeNo" id="officeNo" maxLength="8" value="${inter_user_attr.officeTelNo}"/>
                                            <span class="error-msg" name="errorMsg" id="error_officeTelNo"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:input type="text" name="email" id="email" maxLength="66" value="${inter_user_attr.email}"/>
                                            <span class="error-msg" name="errorMsg" id="error_email"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row  style="margin-bottom:4px">
                                        <iais:field value="Is Administrator" id="userRole" width="11"/>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="admin" name="role" <c:if test="${inter_user_attr.userRole=='ORG_ADMIN'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >Yes</label>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role" <c:if test="${inter_user_attr.userRole!='ORG_ADMIN'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >No</label>
                                        </div>
                                    </iais:row>
                                    <div>
                                        <iais:field value="" width="11"/>
                                        <span style="padding-left: 15px;" class="error-msg" name="errorMsg" id="error_userRole"></span>
                                    </div>
                                    <iais:row>
                                        <iais:field value="Is Active" width="11"/>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="active" name="active" <c:if test="${inter_user_attr.status == 'CMSTAT001'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >Yes</label>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="inactive" name="active" <c:if test="${inter_user_attr.status != 'CMSTAT001'}">checked</c:if>></div>
                                            <label class="col-md-2 control-label" >No</label>
                                        </div>
                                    </iais:row>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back" onclick="cancel()"><em class="fa fa-angle-left" ></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <button id="saveDis" type="button" onclick="save()" class="btn btn-primary">SAVE</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input hidden name="distributionId" value="<c:out value="${distribution.getId()}"/>">
        <input id="action" hidden name="action" value="">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>
<script type="text/javascript">
    function save() {
        $("#action").val("save");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }
    function cancel() {
        $("#action").val("back");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }

</script>