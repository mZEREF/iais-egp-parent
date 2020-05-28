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
<%--                    <iais:row>--%>
<%--                        <div class="col-xs-9 col-sm-5 col-md-3">--%>
<%--                            <button class="addNewRfi btn btn-primary" type="button">New</button>--%>
<%--                        </div>--%>
<%--                    </iais:row>--%>
<%--                    <br><br><br>--%>
                    <c:if test="${not empty newRfiPageListDtos}">
                        <c:forEach items="${newRfiPageListDtos}" var="newRfi" varStatus="status">
                            <div class="reqForInfoContent">
<%--                                <div class="panel panel-default">--%>
<%--                                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">--%>
<%--                                        <div class="panel-body">--%>
<%--                                            <div class="form-group">--%>
<%--                                                <div class="row">--%>
<%--                                                    <label class="col-xs-9 col-md-3 control-label"> Select Category<strong style="color:#ff0000;">*</strong></label>--%>
<%--                                                    <div class=" col-xs-11 col-sm-4 col-md-5">--%>
<%--                                                        <iais:select name="decision${status.index}" options="salutationList" firstOption="Please Select" value="${newRfi.decision}"></iais:select>--%>
<%--                                                    </div>--%>
<%--                                                    <div class=" col-xs-11 col-sm-4 col-md-3">--%>
<%--                                                        <div class="form-check removeRfiBtn">--%>
<%--                                                            <div class="fa fa-times-circle text-danger"></div>--%>
<%--                                                        </div>--%>
<%--                                                    </div>--%>
<%--                                                </div>--%>
<%--                                            </div>--%>
<%--                                            <div class="row">--%>
<%--                                                <label class="col-xs-9 col-md-3 control-label" ></label><span class="error-msg" name="iaisErrorMsg" id="error_rfiSelect${status.index}"></span>--%>
<%--                                            </div>--%>

                                            <div class="form-group">
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label" >Title<strong style="color:#ff0000;">*</strong></label>
                                                    <label >
                                                        <textarea id="rfiTitle${status.index}" class="textarea" style=" font-weight:normal;" maxlength="500" rows="8" cols="70" onchange="checkTitle()"  name="rfiTitle${status.index}" >${newRfi.rfiTitle}</textarea>
                                                        <span id="error_rfiTitle${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                    </label>
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label" > Licence No.<strong style="color:#ff0000;">*</strong></label>
                                                    <div class=" col-xs-7 col-sm-4 col-md-5">
                                                        <label>
                                                            <input type="text" maxlength="24" style="width:170%; font-weight:normal;" name="licenceNo${status.index}" value="${newRfi.licenceNo}"/>
                                                        </label>
                                                            <%--                                                        <iais:select name="licenceNo${status.index}" options="salutationLicList" firstOption="Please Select" value="${newRfi.licenceNo}"></iais:select>--%>
                                                    </div>
                                                    <span id="error_licenceNo${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                </div>
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label" >Due Date<strong style="color:#ff0000;">*</strong></label>
                                                    <label >
                                                            <iais:datePicker value="${newRfi.date}"  name="Due_date${status.index}"></iais:datePicker>
                                                    </label>
                                                    <span id="error_Due_date${status.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                </div>
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label">
                                                        <input type="checkbox" onchange="checkInfo()" value="information" name = "info${status.index}" <c:if test="${newRfi.reqType!=null}">checked</c:if> />&nbsp;Information
                                                    </label>
                                                </div>
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label">
                                                        <input type="checkbox" onchange="checkDoc()" value="documents" name = "doc${status.index}" <c:if test="${newRfi.reqType!=null}">checked</c:if> />&nbsp;Supporting Documents
                                                    </label>
                                                </div>
                                                <div class="row">
                                                    <label class="col-xs-9 col-md-3 control-label" ></label><span class="error-msg" name="iaisErrorMsg" id="error_rfiSelect${status.index}"></span>
                                                </div>
                                                <input type="hidden" name="lengths" value="${status.index}" />
                                                <div id="infohidden" class="hidden">
                                                    <div class="row" style="text-align:center;">
                                                        <div id="infoTitle"></div>
                                                    </div>
                                                    <div class="row" >
                                                        <label class="col-xs-9 col-md-3 control-label" ></label>
                                                        <div class=" col-xs-7 col-sm-4 col-md-5">
                                                            <label>
                                                                <textarea id="userReply_rfi" name="userReply" rows="8" style=" font-weight:normal;" cols="70">${licPreReqForInfoDto.userReply}</textarea><span id="error_userReply" name="iaisErrorMsg" class="error-msg" ></span>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="dochidden" class="hidden">
                                                    <div class="row" >
                                                        <div id="docTitle"></div>
                                                    </div>
                                                    <div class="row" >
                                                        <div class=" col-xs-7 col-sm-4 col-md-5">
                                                            <label>
                                                                <div class="file-upload-gp">
                                                                    <input class="selectedFile commDoc" id="commonDoc"  name = "UploadFile" type="file" style="display: none;" aria-label="selectedFile1" >
                                                                    <a class="btn btn-file-upload btn-secondary" >Attachment</a><span id="error_UploadFile" name="iaisErrorMsg" class="error-msg" ></span><br/>
                                                                </div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
<%--                                        </div>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
                            </div>
                        </c:forEach>
                    </c:if>
                    <div class="rfiFormMarkPoint">
                    </div>
                </div>
                <div class="row">
                    <iais:action style="text-align:left;">
                        <a  onclick="javascript:doBack()">< Back</a>
                    </iais:action>
                    <iais:action style="text-align:right;">
                        <button class="btn btn-primary" type="button"  onclick="javascript:doSubmit()">Submit</button>
                    </iais:action>
                </div>
                <iais:row >
                    <span id="error_LicencePending" name="iaisErrorMsg" class="error-msg" ></span>
                </iais:row>
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
    function doSubmit() {

        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "submit");

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

    function checkTitle(){
        var text=$('.textarea');
        $("#infoTitle").text(text.val());
        $("#docTitle").text(text.val())

    }

    function checkInfo(){

        if($('input[type = checkbox][name="info0"]')[0].checked){
            $("#infohidden").removeClass('hidden');
        }else {
            $("#infohidden").addClass('hidden');
        }
    }

    function checkDoc(){
        if($('input[type = checkbox][name="doc0"]')[0].checked){
            $("#dochidden").removeClass('hidden');
        }else {
            $("#dochidden").addClass('hidden');
        }
    }


</script>