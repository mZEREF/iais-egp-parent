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
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Create Public Holiday</h2>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >Holiday Description</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="Description" type="text" name="Description">
                                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >From Date</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="sub_date" name="sub_date"/>
                                    <span id="error_sub_date" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">To Date</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="to_date" name="to_date"/>
                                    <span id="error_to_date" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-2 col-sm-2">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#" onclick="submit('back')">BACK</a></div>
                            </div>
                            <div class="col-xs-10 col-sm-10">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myCreateModal" id="createholiday">SUBMIT</button></div>
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
    $('#createholiday').click(function(){
        SOP.Crud.cfxSubmit("mainForm", "");
    });

</script>