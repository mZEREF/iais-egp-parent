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
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>New Distribution List</h2>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label" >Name:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="name" type="text" name="name" value="${distribution.getDisname()}">
                                    <span id="error_disname" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Recipients Roles:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="role" type="text" name="role" value="${distribution.getRole()}">
                                    <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Service:</label>
                                <iais:section title="" id="supPoolList">
                                    <iais:row>
                                        <iais:value width="20">
                                            <iais:select name="service" options="service"
                                                         firstOption="${firstOption}" value=""></iais:select>
                                        </iais:value>
                                    </iais:row>
                                </iais:section>
                            <span id="error_service" name="iaisErrorMsg" class="error-msg"></span>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Mode of Delivery:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <div class="form-check">
                                        <input class="form-check-input" name="mode"
                                        <c:if test="${distribution.getMode() != null && distribution.getMode() == 'email'}">
                                               checked="checked"
                                               </c:if>
                                               type="checkbox" value="email">
                                        <label class="form-check-label"><span
                                                class="check-square"></span>Email</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"
                                        <c:if test="${distribution.getMode() != null && distribution.getMode() == 'sms'}">
                                               checked="checked"
                                        </c:if>
                                               name="mode" value="sms">
                                        <label class="form-check-label"><span
                                                class="check-square"></span>Sms</label>
                                    </div>
                                    <span id="error_mode" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="text-right text-center-mobile"><button id="saveDis" type="button" class="btn btn-primary">SAVE</button></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input hidden name="distributionId" value="${distribution.getId()}">
    </form>
    <%@include file="/include/validation.jsp"%>
</div>



<script type="text/javascript">
    $('#saveDis').click(function(){
        SOP.Crud.cfxSubmit("mainForm");
    });

</script>