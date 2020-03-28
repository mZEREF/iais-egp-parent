<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>

                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>

                            <div class="tab-pane active" id="tabLetter" role="tabpanel" >
                                <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
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
                                                <textarea name="messageContent" cols="108" rows="50" class="wenbenkuang" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea>
                                            </div>
                                        </td>
                                    </tr>

                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">
                                    <iais:action style="text-align:center;">
                                        <button type="button" class="btn btn-primary" onclick="javascript:doPreview();">Preview</button>&nbsp;
                                        <button type="button" class="btn btn-primary" onclick="javascript:doReload();">Reload</button>
                                    </iais:action >
                                </p>
                            </div>
                            <div class="tab-pane " id="tabProcessing" role="tabpanel" >

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function doReload(){
        var r=confirm("Are you sure you want to regenerate the Inspection NC/BP Outcome email?");
        if (r==true){
            $.ajax({
                'url':'${pageContext.request.contextPath}/reload-nc-email',
                'type':'GET',
                'success':function (data) {
                    showWaiting();
                    SOP.Crud.cfxSubmit("mainForm", "emailView");
                }
            });
        }
    }

    function doPreview(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "preview");

    }


</script>





