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
                            <h2>New Blast Management List</h2>
                        </div>
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                            <li class="tracker-item active">Select Recipients to send</li>
                        </ul>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label" >Add Email Addresses:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea cols="30" rows="10" name="email" class="textarea" id="email" title="content">
                                    ${emailAddress}
                                    </textarea>
                                </div>
                            </iais:value>
                        </div>

                        <button class="btn btn-lg" id="addbtn" type="button"
                                style="background:#2199E8; color: white">Add
                        </button>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Or use a distribution list:</label>
                            <iais:value width="10">
                                <iais:select name="distribution" options="distribution" value=""></iais:select>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="text-right text-center-mobile"><button id="saveDis" type="button" class="btn btn-primary">Send</button></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp"%>
</div>



<script type="text/javascript">
    $('#saveDis').click(function(){
        SOP.Crud.cfxSubmit("mainForm");
    });

</script>