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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                            <label class="col-xs-12 col-md-4 control-label" >Year</label>
                            <div class="col-md-4">
                                <iais:select name="year" options="yearOption" cssClass="yearOption"
                                             value="${param.year}"></iais:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >From Date</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="sub_date" name="sub_date"/>
                                    <span id="error_sub_date" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                        </div>


                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >Holiday Description</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea id="Description" type="text" name="Description" style="width: 100%;height: 150px"></textarea>
                                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Status</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="status" options="statusOption" cssClass="statusOption" firstOption="Please Select"
                                                 value="${param.status}"></iais:select>
                                    <span id="error_to_date" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-sm-11">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myCreateModal" id="createholiday">SUBMIT</button></div>
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
    $('#createholiday').click(function(){
        SOP.Crud.cfxSubmit("mainForm", "");
    });

</script>