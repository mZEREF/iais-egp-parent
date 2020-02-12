<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <br><br><br>
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="complete" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="active" role="presentation"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab"
                                                                        data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>

                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><strong>Submission Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Application No. (Overall)
                                                        </td>
                                                        <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application No.</td>
                                                        <td>${applicationViewDto.applicationDto.applicationNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Clinical Laboratory</td>
                                                        <td>${applicationViewDto.serviceType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Submission Date</td>
                                                        <td>${applicationViewDto.submissionDate}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Current Status</td>
                                                        <td>${applicationViewDto.currentStatus}</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div align="center">
                                    <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${applicationViewDto.applicationDto.id}" target="_blank">
                                        <button type="button" class="btn btn-primary">
                                            View Application
                                        </button>
                                    </a>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Applicant Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">HCI Code</td>
                                                        <td class="col-xs-6">-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Name</td>
                                                        <td>${applicationViewDto.hciName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Address</td>
                                                        <td>${applicationViewDto.hciAddress}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Telephone</td>
                                                        <td>${applicationViewDto.telephone}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Supporting Document</h4>
                                </strong></div>
                                <div id="u8522_text" class="text ">
                                    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                                                           var="appSupDocDto">
                                                    <tr>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.file}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><a href="#"><c:out value="${appSupDocDto.document}"></c:out></a></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>

                                            </table>
                                            <div class="alert alert-info" role="alert"><strong>
                                                <h4>Internal Document</h4>
                                            </strong></div>
                                            <div class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                                                </p>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td colspan="5" align="center">
                                                        <p>No record found.</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="tab-pane active" id="tabProcessing" role="tabpanel">
                                <table class="table">
                                    <tbody>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                subject:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><input name="subject" type="text" id="subject" title="subject"  readonly value="${insEmailDto.subject}"></p>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                content:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><textarea name="messageContent" cols="108" rows="50" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea></p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Processing Decision:<b style="color:#ff0000;">*</b>
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <select id="decision_merge_email" name="decision" onchange="thisTime()" >
                                                    <option selected>Select</option>
                                                    <c:forEach items="${appTypeOption}" var="decision">
                                                        <option  value="${decision.value}">${decision.text}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1" style="display: none" id="selectReviseNc">
                                        <td class="col-xs-2" >
                                            <p >
                                                Need Revise:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <c:forEach items="${svcNames}" var="revise" varStatus="index">
                                                    <input type="checkbox"  name="revise${index.index+1}" value="${revise}">${revise}</input>
                                                </c:forEach>

                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">

                                    <iais:action style="text-align:center;">
                                        <button type="button" class="search btn" onclick="javascript:doSend();">Submit</button>
                                        <button type="button" class="search btn" onclick="javascript:doPreview();">Preview</button>
                                    </iais:action>
                                </p>


                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script type="text/javascript">
    function doPreview(){
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend(){
        if($('#decision_merge_email option:selected').val()=="Select"){
            ;
        }else {
            if($('#decision_merge_email option:selected').val()=="REDECI005"){
                var checkOne = false;
                var checkBox = $('input[type = checkbox]');
                for (var i = 0; i < checkBox.length; i++) {
                    if (checkBox[i].checked) {
                        checkOne = true;
                    };
                };

                if (checkOne) {
                    SOP.Crud.cfxSubmit("mainForm", "send");
                } else {
                    alert("Sorry: at least choose one.");
                };

            }
            else {
                SOP.Crud.cfxSubmit("mainForm", "send");
            }
        }

    }

    function thisTime() {

        if($('#decision_merge_email option:selected').val()=="REDECI005"){
            $("#selectReviseNc").show();
        }
        else {
            $("#selectReviseNc").hide();
        }
    }
</script>



