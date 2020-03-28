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
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>

                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <div class="tab-pane active" id="tabProcessing" role="tabpanel">
                                <table class="table">
                                    <tbody>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Subject:
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
                                                Content:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <textarea name="messageContent" cols="108" rows="50" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Processing Decision:<strong style="color:#ff0000;"> *</strong>
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <select id="decision_merge_email" name="decision" onchange="thisTime()" >
                                                    <option value="Select" selected>Please Select</option>
                                                    <c:forEach items="${appTypeOption}" var="decision">
                                                        <option  value="${decision.value}">${decision.text}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1" style="display: none" id="selectDecisionMsg">
                                        <td class="col-xs-2" >
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p style="color:#ff0000;">
                                                    This field is mandatory
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1" style="display: none" id="selectReviseNc">
                                        <td class="col-xs-2" >
                                            <p >
                                                Need Revise:<strong style="color:#ff0000;"> *</strong>
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
                                    <tr height="1" style="display: none" id="selectDecisionMsgRevise">
                                        <td class="col-xs-2" >
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p style="color:#ff0000;">
                                                    The field is mandatory
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">

                                    <iais:action style="text-align:center;">
                                        <button type="button" class="btn btn-primary" onclick="javascript:doSend();">Submit</button>&nbsp;
                                        <button type="button" class="btn btn-primary" onclick="javascript:doPreview();">Preview</button>
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
            $("#selectDecisionMsg").show();
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
                    showWaiting();
                    SOP.Crud.cfxSubmit("mainForm", "send");
                } else {
                    $("#selectDecisionMsgRevise").show();
                };

            }
            else {
                showWaiting();
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



