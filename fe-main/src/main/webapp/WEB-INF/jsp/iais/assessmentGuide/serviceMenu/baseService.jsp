<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <div class="component-gp">
        <br>
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
            <%--Validation fields Start--%>

            <input type="hidden" name="paramController" id="paramController"
                   value="com.ecquaria.cloud.moh.iais.action.ServiceMenuDelegator"/>
            <input type="hidden" name="valEntity" id="valEntity"
                   value="com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto"/>

            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <h3>
                        Select the service(s) for which you wish to make this licence application
                    </h3>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <c:if test="${!empty err}">
                        <span class="error-msg">${err}</span>
                    </c:if>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                    <c:if test="${not empty baseService}">
                        <p class="assessment-title"><iais:code code="CDN002"/></p>
                        <div class="form-check-gp">
                            <c:forEach var="base" items="${baseService}">
                                <div class="form-check">
                                    <input
                                    <c:if test="${baseServiceChecked != null}">
                                    <c:forEach var="checked" items="${baseServiceChecked}">
                                           <c:if test="${base.getId().equals(checked)}">checked="checked"</c:if>
                                    </c:forEach>
                                    </c:if>
                                           name="basechk"  type="radio" aria-invalid="false"
                                           value="${base.getId()}">

                                    <label class="form-check-label" style="padding-left: 0px;">${base.getSvcName()}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                    </div>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-md-4">
                    <div class="text-left text-center-mobile col-md-6">
                        <a class="back" href="#" id="back" ><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                    <div class="text-right text-center-mobile col-md-6">
                        <a class="btn btn-primary next" id="submitService" href="javascript:void(0);">NEXT</a>
                    </div>
                </div>
            </div>
            <input hidden id="action" name="action" value="">
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#back').click(function () {
            $("#action").val("back");
            SOP.Crud.cfxSubmit("mainForm");
        })
        $('#submitService').click(function () {
            $("#action").val("submit");
            SOP.Crud.cfxSubmit("mainForm");
        });
    });
</script>
