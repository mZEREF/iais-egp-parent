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


                    <div class="reqForInfo">

                        <div class="form-group">
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label" >Title<strong style="color:#ff0000;">*</strong></label>
                                <label >
                                    <textarea id="rfiTitle" class="textarea" style=" font-weight:normal;" maxlength="500" rows="8" cols="70"   name="rfiTitle" >${newRfi.rfiTitle}</textarea>
                                    <span id="error_rfiTitle" name="iaisErrorMsg" class="error-msg" ></span>
                                </label>
                            </div>
                            <br>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label" > Licence No.<strong style="color:#ff0000;">*</strong></label>
                                <label class="col-xs-9 col-md-5 control-label">
                                    <input type="text" maxlength="24" style=" font-weight:normal;" name="licenceNo" value="${newRfi.licenceNo}"/>
                                </label>
                                <span id="error_licenceNo" name="iaisErrorMsg" class="error-msg" ></span>
                            </div>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label" >Due Date<strong style="color:#ff0000;">*</strong></label>
                                <label class="col-xs-9 col-md-5 control-label">
                                        <iais:datePicker value="${newRfi.date}"  cssClass="font-weight:normal;" name="Due_date"></iais:datePicker>
                                </label>
                                <span id="error_Due_date" name="iaisErrorMsg" class="error-msg" ></span>
                            </div>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label" > Status</label>
                                <div class="col-xs-9 col-md-5 control-label">
                                    <iais:select id="rfiStatus" name="status" options="salutationStatusList" firstOption="Please Select" filterValue="${newRfi.status}"></iais:select>
                                </div>
                            </div>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label">
                                    <input type="checkbox" onchange="checkInfo()" value="information" name = "info" <c:if test="${newRfi.infoChk!=null}">checked</c:if> />&nbsp;Information
                                </label>
                            </div>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label">
                                    <input type="checkbox" onchange="checkDoc()" value="documents" name = "doc" <c:if test="${newRfi.docChk!=null}">checked</c:if> />&nbsp;Supporting Documents
                                </label>
                            </div>
                            <div class="row">
                                <label class="col-xs-9 col-md-3 control-label" ></label><span class="error-msg" name="iaisErrorMsg" id="error_rfiSelect"></span>
                            </div>


                            <div id="infohidden" <c:if test="${not empty newRfi.infoTitle}">class="hidden" </c:if> >
                                <div class="reqForInfoContentInfo">
                                    <input type="hidden" name="lengthsInfo" value="0" />
                                    <div class="col-xs-9 col-sm-5 col-md-1">
                                        <button class="addNewRfiInfo btn btn-secondary" type="button">+</button>
                                    </div>
                                    <div class="panel panel-default">
                                        <div class="panel-collapse collapse in" id="collapseOneInfo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <div class="row" >
                                                        <div class="row" style="text-align:center;">
                                                            <div >  Title of Information Required <strong style="color:#ff0000;">*</strong></div>
                                                        </div>
                                                        <label class="col-xs-9 col-md-3 control-label" ></label>

                                                        <label>
                                                            <textarea  name="infoTitle0" rows="8" style=" font-weight:normal;" maxlength="500" cols="70">${newRfi.infoTitle[0]}</textarea><span id="error_infoTitle0" name="iaisErrorMsg" class="error-msg" ></span>
                                                        </label>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <br><br><br>
                                </div>

                                <c:if test="${not empty newRfi.infoTitle}">
                                    <c:forEach items="${newRfi.infoTitle}" var="newRfiInfo" varStatus="statusInfo">
                                        <c:if test="${statusInfo.index!=0}">
                                            <div class="reqForInfoContentInfo">
                                                <input type="hidden" name="lengthsInfo" value="${statusInfo.index}" />
                                                <div class=" col-xs-11 col-sm-4 col-md-1">
                                                    <div class="form-check removeRfiInfoBtn">
                                                        <button class=" btn btn-secondary" type="button">-</button>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                                        <div class="panel-body">
                                                            <div class="form-group">
                                                                <div class="row" >
                                                                    <div class="row" style="text-align:center;">
                                                                        <div > ${statusInfo.index} Title of Information Required <strong style="color:#ff0000;">*</strong></div>
                                                                    </div>
                                                                    <label class="col-xs-9 col-md-3 control-label" ></label>

                                                                    <label>
                                                                        <textarea  name="infoTitle${statusInfo.index}" maxlength="500" rows="8" style=" font-weight:normal;" cols="70">${newRfiInfo}</textarea><span id="error_infoTitle${statusInfo.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                                    </label>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>

                                <div class="rfiFormMarkPointInfo">
                                </div>
                            </div>

                            <div id="dochidden" <c:if test="${not empty newRfi.docTitle}">class="hidden"</c:if> >
                                <div class="reqForInfoContent">
                                    <input type="hidden" name="lengths" value="0" />
                                    <div class="col-xs-9 col-sm-5 col-md-1">
                                        <button class="addNewRfi btn btn-secondary" type="button">+</button>
                                    </div>
                                    <div class="panel panel-default">
                                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <div class="row" >
                                                        <div class="row" style="text-align:center;">
                                                            <div >  Title of Supporting Documents <strong style="color:#ff0000;">*</strong></div>
                                                        </div>
                                                        <label class="col-xs-9 col-md-3 control-label" ></label>

                                                        <label>
                                                            <textarea  name="docTitle0" rows="8" style=" font-weight:normal;" maxlength="500" cols="70">${newRfi.docTitle[0]}</textarea><span id="error_docTitle0" name="iaisErrorMsg" class="error-msg" ></span>
                                                        </label>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <br><br><br>
                                </div>

                                <c:if test="${not empty newRfi.docTitle}">
                                    <c:forEach items="${newRfi.docTitle}" var="newRfiDoc" varStatus="statusDoc">
                                        <c:if test="${statusDoc.index!=0}">
                                            <div class="reqForInfoContent">

                                                <input type="hidden" name="lengths" value="${statusDoc.index}" />
                                                <div class=" col-xs-11 col-sm-4 col-md-1">
                                                    <div class="form-check removeRfiBtn">
                                                        <button class=" btn btn-secondary" type="button">-</button>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                                        <div class="panel-body">
                                                            <div class="form-group">
                                                                <div class="row" >
                                                                    <div class="row" style="text-align:center;">
                                                                        <div > ${statusDoc.index} Title of Supporting Documents <strong style="color:#ff0000;">*</strong></div>
                                                                    </div>
                                                                    <label class="col-xs-9 col-md-3 control-label" ></label>

                                                                    <label>
                                                                        <textarea  name="docTitle${statusDoc.index}" maxlength="500" rows="8" style=" font-weight:normal;" cols="70">${newRfiDoc}</textarea><span id="error_docTitle${statusDoc.index}" name="iaisErrorMsg" class="error-msg" ></span>
                                                                    </label>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </div>
                            <div class="rfiFormMarkPoint">
                            </div>
                        </div>
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
        removeRFI();
        addRfiInfo();
        removeRFIInfo()
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
            if(length<4){
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
            }
        });
    }

    var removeRFI = function () {
        $('.removeRfiBtn').click(function () {
            var $rfiContentEle = $(this).closest('div.reqForInfoContent');
            $rfiContentEle.remove();
            length=length-1;
        });
    }

    function checkDoc(){
        if($('input[type = checkbox][name="doc"]')[0].checked){
            $("#dochidden").removeClass('hidden');
        }else {
            $("#dochidden").addClass('hidden');
        }
    }


    var reqForInfoContentInfoLength=$('div.reqForInfoContentInfo').length;
    var lengthInfo =0+reqForInfoContentInfoLength;

    var addRfiInfo = function () {
        $('.addNewRfiInfo').click(function () {

            var $contentDivEle = $(this).closest('div.panel-group');

            var jsonData={
                'Length': lengthInfo
            };
            if(lengthInfo<4){
                $.ajax({
                    'url':'${pageContext.request.contextPath}/new-rfi-info-html',
                    'dataType':'text',
                    'data':jsonData,
                    'type':'GET',
                    'success':function (data) {
                        if(data == null){
                            return;
                        }
                        <!--use ph mark point -->
                        $contentDivEle.find('div.rfiFormMarkPointInfo').addClass('rfiContentInfo');
                        <!--add html -->
                        $contentDivEle.find('div.rfiContentInfo').before(data);
                        <!--init ph mark point -->
                        $contentDivEle.find('div.rfiFormMarkPointInfo').removeClass('rfiContentInfo');
                        <!--change hidden length value -->
                        //Prevent duplicate binding
                        $('.removeRfiInfoBtn').unbind('click');
                        removeRFIInfo();
                        $('.date_picker').datepicker({
                            format:"dd/mm/yyyy"
                        });
                        lengthInfo=lengthInfo+1;
                    },
                    'error':function () {
                    }
                });
            }


        });
    }

    var removeRFIInfo = function () {
        $('.removeRfiInfoBtn').click(function () {
            var $rfiContentEle = $(this).closest('div.reqForInfoContentInfo');
            $rfiContentEle.remove();
            lengthInfo=lengthInfo-1;
        });
    }

    function checkInfo(){
        if($('input[type = checkbox][name="info"]')[0].checked){
            $("#infohidden").removeClass('hidden');
        }else {
            $("#infohidden").addClass('hidden');
        }
    }


</script>