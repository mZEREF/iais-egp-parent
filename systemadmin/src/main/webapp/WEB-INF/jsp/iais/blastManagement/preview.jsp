<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style type="text/css">
    .flowDisplay{
        display: flex;
    }
    .flowHidden{
        display: none;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" id="crud_action_type_value" name="crud_action_type_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Preview</h2>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message ID"/>
                                    <p class="col-xs-4 col-md-4 control-label">${edit.messageId}</p>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message Name" />
                                    <p  class="col-xs-4 col-md-4 control-label">${edit.getMsgName()}</p>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" />
                                    <p  class="col-xs-4 col-md-4 control-label">${edit.mode}</p>
                        </div>

                        <div class="form-group">
                            <iais:field value="Send date and time" />
                                    <p  class="col-xs-4 col-md-4 control-label"><fmt:formatDate value="${edit.schedule}"
                                                           pattern="dd/MM/yyyy HH:mm:ss"/></p>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" />
                                    <p class="col-xs-4 col-md-4 control-label"><iais:code code="${edit.status}"></iais:code> </p>
                        </div>

                        <c:choose>
                            <c:when test='${"Email".equals(edit.mode)}'>
                                <div class="form-group">
                                    <iais:field value="Subject" />
                                        <p class="col-xs-4 col-md-4 control-label">${edit.getSubject()}</p>
                                </div>

                                <div class="form-group">
                                    <iais:field value="Content" />
                                    <div class="col-xs-8 col-md-8 panel-body">
                                                ${edit.getMsgContent()}
                                    </div>
                                </div>

                                <div class="form-group">
                                    <iais:field value="File" />
                                        <p class="col-xs-4 col-md-4 control-label"><c:out value="${fileName}"/></p>
                                </div>

                                <div class="form-group">
                                    <iais:field value="Distribution Name" />
                                        <p class="col-xs-4 col-md-4 control-label">${edit.distributionName}</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <iais:field value="Header"/>
                                    <p class="col-xs-4 col-md-4 control-label" >${edit.getSubject()}</p>
                                </div>

                                <div class="form-group">
                                    <iais:field value="Distribution Name" />
                                        <p class="col-xs-4 col-md-4 control-label">${edit.distributionName}</p>
                                </div>

                                <div class="form-group">
                                    <iais:field value="Text" />
                                            <p  class="col-xs-8 col-md-8 control-label">${edit.msgContent}</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>


<script type="text/javascript">
    $("#back").click(function () {
        $("#crud_action_type_value").val("back")
        SOP.Crud.cfxSubmit("mainForm");
    })
</script>