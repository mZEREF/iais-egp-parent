<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/16
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<style>
    @media only screen and (max-width : 1200px){
        .form-padding {
            padding: 35px 5%;
        }
    }
</style>
<div class="main-content">
    <div class="navigation-gp"></div>
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
            <%@ include file="/WEB-INF/jsp/iais/common/myinfoDownRemind.jsp" %>
        <div class="prelogin" style="background-image: url('/web/themes/fe/img/prelogin-masthead-banner.jpg');">
            <div class="tab-gp steps-tab">
                <div class="tab-content">
                    <div class="tab-pane active form-padding" id="premisesTab" role="tabpanel">
                        <div class="row">
                            <div class="col-xs-12">
                                <h2>Authorised Person Particulars Form</h2>
                                <div class="form-horizontal">
                                    <c:if test="${myinfo_sinpass_login_evaluate == 'Y'}">
                                        <%@include file="/WEB-INF/jsp/iais/common/myinfoInstructionsLinks.jsp"%>
                                        <c:set var="licensee" value="${myinfo_solo_dto_seesion}"/>
                                        <%@include file="/WEB-INF/jsp/iais/LicenseeDetail/licenseeDetailContent.jsp"%>
                                    </c:if>
                                    <c:if test="${empty myinfo_sinpass_login_evaluate}">
                                        <%@include file="/WEB-INF/jsp/iais/common/userForm.jsp"%>
                                    </c:if>
                                    <iais:row>
                                        <div class="col-xs-12 col-md-4 control-label"></div>
                                        <div align="right" class="col-sm-7 col-md-6 col-xs-10">
                                            <%-- <a class="btn btn-primary next" href="javascript:void(0);"
                                                onclick="Utils.submit('mainForm', 'doBack')">Back</a>--%>
                                          <c:if test="${myinfo_sinpass_login_evaluate == 'Y'}">
                                            <button type="button" class="btn btn-secondary" onclick="javascript:doClearInfo()">Clear</button>
                                          </c:if>
                                          <input type="hidden" name="refreshMyInfoData" id="refreshMyInfoData"/>
                                          <a class="btn btn-primary next" href="javascript:void(0);"
                                             onclick="Utils.submit('mainForm', 'doSubmit')">Submit</a>
                                        </div>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function reLoadMyInfoTodo() {
        if(verifyTaken()){
            $("#refreshMyInfoData").val("1");
            Utils.submit('mainForm', 'doSubmit');
        }else {
            // To obtain authorization
            showWaiting();
            callAuthoriseApi();
        }
    }

    $('#addrType').on('change',checkAddressManatory);

    function checkAddressManatory() {
        var addrType = $('#addrType').val();
        if ('ADDTY001' == addrType) {
            $('.blkNoLabel').append('<span class="mandatory">*</span>');
            $('.floorUnitLabel').append('<span class="mandatory">*</span>');
        } else {
            $('.blkNoLabel .mandatory').remove();
            $('.floorUnitLabel .mandatory').remove();
        }
    }

    $(document).ready(function() {
        checkAddressManatory();
    });

    function doClearInfo(){
        $("#refreshMyInfoData").val("clearMyInfo");
        Utils.submit('mainForm', 'doSubmit');
    }

</script>