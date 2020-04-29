<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br>
                    <h3>
                        <span>Request For Information</span>
                    </h3>
                    <iais:row>
                        <div class="col-xs-9 col-sm-5 col-md-4">
                            <button class="addNewRfi btn btn-primary" type="button">Add Request For Information</button>
                        </div>
                    </iais:row>
                    <br><br><br>
                    <c:if test="${not empty newRfiPageListDtos}">
                        <c:forEach items="${newRfiPageListDtos}" var="newRfi" varStatus="status">
                            <div class="reqForInfoContent">
                                <div class="panel panel-default">
                                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <p>
                                                    <label class="col-xs-9 col-md-4 control-label">Select Category</label>
                                                <div class=" col-xs-11 col-sm-4 col-md-5">
                                                <iais:select name="decision${status.index}" options="salutationList" firstOption="Please Select" value="${newRfi.decision}"></iais:select>
                                                </div>
                                                <div class=" col-xs-11 col-sm-4 col-md-3">
                                                    <div class="form-check removeRfiBtn">
                                                        <div class="fa fa-times-circle text-danger"></div>
                                                    </div>
                                                </div>
                                                </p>
                                            </div>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_rfiSelect${status.index}"></span>
                                            <br>
                                            <div class="form-group">
                                                <p>
                                                    <label class="col-xs-9 col-md-4 control-label" >Title</label>
                                                    <label >
                                                        <textarea id="rfiTitle${status.index}"  maxlength="500" rows="8" cols="95"  name="rfiTitle${status.index}" >${newRfi.rfiTitle}</textarea><span id="error_rfiTitle${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                    </label>
                                                </p>
                                                <p>
                                                    <label class="col-xs-9 col-md-4 control-label" >Licence No.</label>
                                                    <label col-xs-11 col-sm-4 col-md-5>
                                                        <input type="text" value="${newRfi.licenceNo}" style="width:170%; font-weight:normal;" maxlength="30" name="licenceNo${status.index}" style=" font-weight:normal;" ><span id="error_licenceNo${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                    </label>
                                                </p>
                                                <p>
                                                    <label class="col-xs-9 col-md-4 control-label" >Due Date</label>
                                                    <label col-xs-11 col-sm-4 col-md-5>
                                                        <iais:datePicker value="${newRfi.date}"  name="Due_date${status.index}"></iais:datePicker>
                                                        <span id="error_Due_date${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                    </label>
                                                </p>
                                                <p>
                                                    <label class="col-xs-9 col-md-4 control-label">
                                                        <input type="checkbox" name = "reqType${status.index}" <c:if test="${newRfi.reqType!=null}">checked</c:if> /> File Upload <input type="hidden" name="lengths" value="${status.index}" />
                                                    </label>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>

                    <div class="rfiFormMarkPoint">
                    </div>

                    <iais:action style="text-align:left;">
                        <a  onclick="javascript:doBack()">< Back</a>
                    </iais:action>
                    <iais:action style="text-align:right;">
                        <button class="btn btn-primary" type="button"  onclick="javascript:doSubmit('${licenceNo}')">Submit</button>
                    </iais:action>
                    <iais:row >
                        <div class="col-sm-9" style="display: none" id="isAlert">
                            <p style="color:#ff0000;">
                                Licence is still pending Applicant's input.Please do not submit any new Requset For Information.
                            </p>
                        </div>
                    </iais:row>
                </div>
            </div>
        </div>

    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">


    $(document).ready(function() {

        addRfi();
        removeRFI()
    })

    function doBack(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doSubmit(licenceNo) {

        if(licenceNo!=""){
            $("#isAlert").show();
        }
        else {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "submit");
        }

    }

    var reqForInfoContentLength=$('div.reqForInfoContent').length;
    var length =0+reqForInfoContentLength;

    var addRfi = function () {
        $('.addNewRfi').click(function () {

            var $contentDivEle = $(this).closest('div.panel-group');

            var jsonData={
                'Length': length
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/new-rfi-html',
                'dataType':'text',
                'data':jsonData,
                'type':'GET',
                'success':function (data) {
                    if(data == null){
                        return;
                    }
                    <!--use ph mark point -->
                    $contentDivEle.find('div.rfiFormMarkPoint').addClass('rfiContent');
                    <!--add html -->
                    $contentDivEle.find('div.rfiContent').before(data);
                    <!--init ph mark point -->
                    $contentDivEle.find('div.rfiFormMarkPoint').removeClass('rfiContent');
                    <!--change hidden length value -->
                    //Prevent duplicate binding
                    $('.removeRfiBtn').unbind('click');
                    removeRFI();
                    $('.date_picker').datepicker({
                        format:"dd/mm/yyyy"
                    });
                    length=length+1;
                },
                'error':function () {
                }
            });

        });
    }

    var removeRFI = function () {
        $('.removeRfiBtn').click(function () {
            var $rfiContentEle = $(this).closest('div.reqForInfoContent');
            $rfiContentEle.remove();
            length=length-1;
        });
    }

</script>