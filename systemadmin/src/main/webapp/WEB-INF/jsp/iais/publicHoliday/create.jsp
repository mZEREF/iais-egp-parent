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
                                <iais:select id="year" name="year" options="yearOption" cssClass="yearOption"
                                             value="${param.year}"></iais:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >Non-working Date</label>
                            <div class="col-xs-8 col-sm-6 col-md-5" id="fromdatediv">
                                <iais:datePicker id="sub_date"  name="sub_date" dateVal="${holiday.fromDate}"/>
                                <span id="error_sub_date" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" >Holiday Description</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:select id="phCode" name="phCode" codeCategory="CATE_ID_PUBLIC_HOLIDAY" cssClass="yearOption" firstOption="Please Select"
                                             value="${holiday.phCode}"></iais:select>
                                <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Status</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="status" options="statusOption" cssClass="statusOption" firstOption="Please Select"
                                                 value="${holiday.status}"></iais:select>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myCreateModal" id="editholiday">SUBMIT</button></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="action" id="action" value="default">
        <input type="hidden" name="holidayId" id="holidayId" value="${holiday.id}" >
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>



<script type="text/javascript">
    $("#back").click(function () {
        $("#action").val("back");
        SOP.Crud.cfxSubmit("mainForm");
    })
    $('#editholiday').click(function(){
        SOP.Crud.cfxSubmit("mainForm");
    });

    $("#year").change(function () {
        var date =  $("#sub_date").val();
        if(date != ""){
            var time = date.substring(0,6);
            time = time + $(this).val();
            $("#sub_date").val(time)
            // $("#fromdatediv").html("<input type=\"text\" autocomplete=\"off\" class=\"date_picker form-control\" name=\"sub_date\" id=\"sub_date\" data-date-start-date=\"01/01/1900\" value=\""+time+"\" placeholder=\"dd/mm/yyyy\" maxlength=\"10\"><span id=\"error_sub_date\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>")

        }
    })

</script>