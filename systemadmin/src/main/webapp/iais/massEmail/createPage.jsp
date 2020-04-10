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
                            <iais:field value="Distribution Name" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="name" type="text" name="name" maxlength="500" value="${distribution.getDisname()}">
                                    <span id="error_disname" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Recipients Roles" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="role" options="roleSelection" value="${distribution.getRole()}"></iais:select>
                                    <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label" >Add Email Addresses</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea cols="50" rows="10" name="email" class="textarea" id="email" title="content">${emailAddress}</textarea>
                                    <span id="error_addr" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Service" required="true"/>
                            <iais:value>
                                <iais:value width="10">
                                            <iais:select name="service" options="serviceSelection"
                                                         value="${distribution.getService()}"></iais:select>
                                </iais:value>
                            </iais:value>
                            <span id="error_service" name="iaisErrorMsg" class="error-msg"></span>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" required="true"/>
                            <iais:value>
                                <iais:value width="10">
                                    <iais:select name="mode" options="modeSelection"  value="${distribution.getMode()}"></iais:select>
                                </iais:value>
                            </iais:value>
                        </div>

                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-sm-11">
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