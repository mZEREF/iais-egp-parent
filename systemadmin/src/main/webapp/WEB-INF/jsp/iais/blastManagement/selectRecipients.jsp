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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row" style="min-height: 600px">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                            <li class="tracker-item active">Select Recipients to send</li>
                        </ul>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Use a distribution list</label>
                            <iais:value width="10">
                                <iais:select name="distribution" options="distribution" value=""></iais:select>
                                <span class="error-distribution" name="iaisErrorMsg" id="error_distribution"></span>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-sm-11">
                                <div class="text-right">
                                    <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                                    <a class="btn btn-primary" id="saveDis" >Send</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>



<script type="text/javascript">
    $('#saveDis').click(function(){
        SOP.Crud.cfxSubmit("mainForm","");

    });
    $('#back').click(function(){
        SOP.Crud.cfxSubmit("mainForm","back");
    });
</script>