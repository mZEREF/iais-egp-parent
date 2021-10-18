<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
    String webRootCommon = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>
    tr {padding: 8px}
    td {padding: 8px}
</style>
<input style="display: none" value="${NOT_VIEW}" id="view">
<c:set var="appEdit" value="${appEditSelectDto}"/>
<c:set value="${pageAppEditSelectDto}" var="pageEdit"></c:set>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <div class="tab-content">
                        <div class="tab-pane active" id="previewTab" role="tabpanel">
                            <div class="preview-gp">
                                <div class="row">
                                    <div class="col-xs-12 col-md-2 text-right">
                                        <br>
                                        <br>
                                        <%--   <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>--%>
                                    </div>
                                </div>
                                <div class="hidden" id="errorMessage">
                                    <iais:error>
                                        <div class="error">
                                            <h2><iais:message key="PRF_ERR002" escape="true"></iais:message></h2>
                                        </div>
                                    </iais:error>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                            <%@include file="section/viewLicensee.jsp" %>
                                            <%@include file="section/viewPremises.jsp" %>
                                            <%@include file="section/viewPrimaryDoc.jsp" %>
                                            <%@include file="section/viewKeyRoles.jsp" %>
                                            <div class="panel panel-default svc-content">

                                                <div class="panel-heading" id="headingServiceInfo0" role="tab">
                                                    <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                                                               href="#collapseServiceInfo0" aria-expanded="true"
                                                                               aria-controls="collapseServiceInfo">Service Related Information
                                                        - ${hcsaServiceDto.svcName}</a></h4>
                                                </div>

                                                <div class=" panel-collapse collapse" id="collapseServiceInfo0" role="tabpanel"
                                                     aria-labelledby="headingServiceInfo0">
                                                    <div class="panel-body">
                                                        <p class="text-right">

                                                            <c:if test="${appEdit.serviceEdit}">
                                                                <c:if test="${rfi=='rfi'}">
                                                                    <input class="form-check-input" <c:if test="${pageEdit.serviceEdit}">checked</c:if> id="serviceCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="service">
                                                                </c:if>
                                                            </c:if>
                                                        </p>
                                                        <%--<iframe class="svc-iframe" title=""
                                                                src="${pageContext.request.contextPath}/eservice/INTRANET/MOHServiceView"
                                                                id="elemId-0" width="100%" height="100%"></iframe>--%>
                                                        <%@include file="section/viewServiceInfo.jsp" %>
                                                        <!--scrolling="no" scrollbar="no" -->
                                                    </div>
                                                </div>
                                            </div>
                                            <%@include file="section/viewDeclaration.jsp" %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <iais:confirm msg="GENERAL_ACK018"  needCancel="false" callBack="tagConfirmCallbacksupport()" popupOrder="support" ></iais:confirm>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                    </div>
                                    <c:if test="${rfi=='rfi'}">
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="next btn btn-primary" id="previewNext">SUBMIT </a></div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function closeThis(obj){
        $(obj).closest('div.disciplinary-record').hide();
    }

    $(document).ready(function () {
        <c:if test="${pageAppEditSelectDto.docEdit}">
        $('#primaryCheckbox').closest("div.panel-body").attr("style","");
        </c:if>
        <c:if test="${pageAppEditSelectDto.premisesEdit}">
        $('#premisesCheckbox').closest("div.panel-body").attr("style","");
        </c:if>
        <c:if test="${pageAppEditSelectDto.serviceEdit}">
        $('#serviceCheckbox').closest("div.panel-body").attr("style","");
        </c:if>
        <c:if test="${pageAppEditSelectDto.licenseeEdit}">
        $('#licenseeCheckbox').closest("div.panel-body").attr("style","");
        </c:if>

        $('input[name="editCheckbox"]').click(changeSectionStyle);
    });

    function changeSectionStyle() {
        let $this = $(this);
        let target = $this.closest("div.panel-body");
        if ($this.is(":checked")) {
            target.attr("style", "");
        } else {
            target.attr("style", "background-color: #999999;");
        }
    }

    function showThisTableNew(obj) {
        $(obj).closest('div.img-show').next("div.new-img-show").show();
    }
    function showThisTableOld(obj) {
        $(obj).closest('div.img-show').next("div.old-img-show").show();
    }
    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId":verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if(data != null ){
                    if(data.verify == 'N'){
                        $('#support').modal('show');
                    }else {
                        $("#"+verify+"Down").click();
                    }
                    dismissWaiting();
                }
            }
        )
    }

    function tagConfirmCallbacksupport(){
        $('#support').modal('hide');
    }

</script>