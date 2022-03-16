<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
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
                            <c:choose>
                                <c:when test="${'Create'.equals(feusertitle)}">
                                    <iais:field value="UEN" width="12" required="true"/>
                                    <iais:value width="12">
                                        <input name="uenNo" id="uenNo" type="text" value="${inter_user_attr.uenNo}" />
                                        <span class="error-msg" name="errorMsg" id="error_uenNo"></span>
                                    </iais:value>
                                </c:when>
                                <c:when test="${empty inter_user_attr.uenNo}">
                                    <iais:field value="UEN" width="12"/>
                                    <iais:value width="12">
                                        <p>-</p>
                                        <input name="organizationId" id="organizationId" type="hidden" value="<iais:mask name="organizationId" value="${organizationId}"/>"/>
                                    </iais:value>
                                </c:when>
                                <c:otherwise>
                                    <iais:field value="UEN" width="12" required="true"/>
                                    <iais:value width="12">
                                        <p><c:out value="${inter_user_attr.uenNo}"></c:out></p>
                                        <input name="organizationId" id="organizationId" type="hidden" value="<iais:mask name="organizationId" value="${organizationId}"/>"/>
                                        <span class="error-msg" name="errorMsg" id="error_uenNo"></span>
                                    </iais:value>
                                </c:otherwise>
                            </c:choose>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Name" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:input type="text" name="name" id="name" maxLength="110" value="${inter_user_attr.displayName}"/>
                                <span class="error-msg" name="errorMsg" id="error_displayName"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Salutation" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:select cssClass="Salutation" name="salutation" id="salutation" value="${inter_user_attr.salutation}"
                                             codeCategory="CATE_ID_SALUTATION"  firstOption="Please Select" />
                                <span class="error-msg" name="errorMsg" id="error_salutation"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row cssClass="solo">
                            <iais:field value="ID Type" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:select name="idType" id="idType" value="${inter_user_attr.idType}"
                                             codeCategory="CATE_ID_ID_TYPE" firstOption="Please Select" filterValue="IDTYPE003"/>
                                <span class="error-msg" name="errorMsg" id="error_idType"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row cssClass="solo">
                            <iais:field value="ID No" width="12" required="true"/>
                            <iais:value width="12">
                                    <input type="text" name="idNo" id="idNo" value="${inter_user_attr.identityNo}" maxlength="9"/>
                                    <span class="error-msg" name="errorMsg" id="error_identityNo"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Designation" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:select cssClass="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${inter_user_attr.designation}"
                                             firstOption="Please Select" onchange="toggleOnSelect('designation', 'DES999', 'designationOther')" />
                                <iais:input type="text" name="designationOther" id="designationOther" value="${inter_user_attr.designationOther}"
                                            maxLength="100" style="${inter_user_attr.designation eq 'DES999' ? '' : 'display:none'}"/>
                                <span class="error-msg" name="errorMsg" id="error_designation"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Mobile No" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:input type="text" name="mobileNo" id="mobileNo" maxLength="8" value="${inter_user_attr.mobileNo}" />
                                <span class="error-msg" name="errorMsg" id="error_mobileNo"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Office/Telephone No" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:input type="text" name="officeNo" id="officeNo" maxLength="8" value="${inter_user_attr.officeTelNo}"/>
                                <span class="error-msg" name="errorMsg" id="error_officeTelNo"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Email" width="12" required="true"/>
                            <iais:value width="12">
                                <iais:input type="text" name="email" id="email" maxLength="320" value="${inter_user_attr.email}"/>
                                <span class="error-msg" name="errorMsg" id="error_email"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row  style="margin-bottom:0px">
                            <iais:field value="Is Administrator" id="userRole" width="12"/>
                            <c:choose>
                            <c:when test="${!('Edit' eq feusertitle && empty inter_user_attr.uenNo)}">
                            <div class="col-md-3" style="padding-left: 0px;">
                                <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="admin" name="role" <c:if test="${inter_user_attr.userRole=='ORG_ADMIN'}">checked</c:if>></div>
                                <label class="col-md-2 control-label" >Yes</label>
                            </div>
                            <div class="col-md-3" style="padding-left: 0px;">
                                <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="user" name="role" <c:if test="${inter_user_attr.userRole!='ORG_ADMIN'}">checked</c:if>></div>
                                <label class="col-md-2 control-label" >No</label>
                            </div>
                            <br>
                            <div class="col-xs-12 col-md-4 control-label">&nbsp;</div>
                            <div class="col-md-3" style="padding-left: 0px;">
                                <span style="padding-left: 15px;" class="error-msg" name="errorMsg" id="error_userRole"></span>
                            </div>
                            </c:when>
                                <c:otherwise>
                                    <iais:value width="12">
                                        <p>No</p>
                                        <input type="hidden" value="user" name="role" id="role" oninput="doChangeUserRole()" readonly>
                                    </iais:value>
                                </c:otherwise>
                            </c:choose>
                        </iais:row>
                        <p></p>
                        <iais:row>
                            <iais:field value="Is Active" width="12"/>
                            <div class="col-md-3" style="padding-left: 0px;">
                                <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="active" name="active" <c:if test="${inter_user_attr.status == 'CMSTAT001'}">checked</c:if>></div>
                                <label class="col-md-2 control-label" >Yes</label>
                            </div>
                            <div class="col-md-3" style="padding-left: 0px;">
                                <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="inactive" name="active" <c:if test="${inter_user_attr.status != 'CMSTAT001'}">checked</c:if>></div>
                                <label class="col-md-2 control-label" >No</label>
                            </div>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Service" width="5" required="false" />
                            <iais:value width="7" cssClass="col-md-7">
                                <c:set var="selectServices" value="${inter_user_attr.selectServices}"/>
                                <c:set var="serviceSize" value="${AllServicesForHcsaRole.size()}"/>
                                <c:set var="allServicesSelect" value="${selectServices == 'All_Services'}"/>
                                <c:forEach var="service" items="${AllServicesForHcsaRole}" varStatus="status">
                                    <c:set var="value" value="${service.value}"/>
                                    <div class="form-check col-xs-7" style="padding-left: 0px;">
                                        <input class="form-check-input" type="checkbox"
                                               name="service"
                                               value="${value}"
                                               id="service${status.index}"
                                               <c:if test="${StringUtil.stringContain(selectServices,value)}">checked</c:if>
                                               aria-invalid="false"   <c:if test="${!empty selectServices && (allServicesSelect && status.index != 0 || !allServicesSelect && status.index ==0)}">disabled</c:if>
                                               onchange="mutualExclusionServiceCheckBox('${value}',${serviceSize})">
                                        <label class="form-check-label"
                                               for="service${status.index}"><span
                                                class="check-square"></span>
                                            <c:out value="${service.text}"/></label>
                                    </div>
                                </c:forEach>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4"/>
                            <iais:value width="3" cssClass="col-md-3">
                                <span id="error_service" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="javascript:void(0);" class="back" id="back" onclick="cancel()"><em class="fa fa-angle-left" ></em> Back</a>
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
    $(document).ready(function(){
        <c:if test="${'Create' != feusertitle && inter_user_attr.solo}">
        disableContent('.solo');
        </c:if>
    });

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

    function doChangeUserRole(){
       $('#role').val('user');
    }

    function mutualExclusionServiceCheckBox(key1,key2){
        if("All_Services" == key1){
            if($("#service0").is(':checked')){
                for (let i = 1; i <key2 ; i++) {
                    $("#service"+i).attr("disabled",true);
                }
            }else {
                for (let i = 1; i <key2 ; i++) {
                    $("#service"+i).attr("disabled",false);
                }
            }
        }else {
            let checkAll = false;
            for (let i = 1; i <key2 ; i++) {
                if($("#service"+i).is(':checked')){
                    $("#service0").attr("disabled",true);
                    checkAll = true;
                    break;
                }
            }
            if(!checkAll){
                $("#service0").attr("disabled",false);
            }
        }
    }

</script>