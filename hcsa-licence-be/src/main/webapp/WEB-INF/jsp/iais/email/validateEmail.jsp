<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body>
                        <div class="">
                            <div class="col-xs-12">
                                <div class="tab-gp dashboard-tab">

                                    <%@ include file="./navTabs.jsp" %>
                                    <div class="tab-content">
                                        <div class="tab-pane" id="tabInfo" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp" %>
                                        </div>

                                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp" %>
                                        </div>

                                        <div class="tab-pane active" id="tabLetter" role="tabpanel">
                                            <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                            <iais:section title="" id = "process_NcEmail">
                                                <iais:row>
                                                    <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
                                                    <div class="col-sm-9">
                                                        <p><input name="subject" type="text" id="subject"
                                                                  title="subject" readonly
                                                                  value="<c:out value="${insEmailDto.subject}"/>"></p>
                                                    </div>
                                                </iais:row>
                                                <iais:row>
                                                    <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
                                                    <div class="col-sm-9">
                                                                <textarea name="messageContent" cols="108" rows="50"
                                                                          id="htmlEditroArea"
                                                                          title="content">${insEmailDto.messageContent}</textarea>
                                                    </div>
                                                </iais:row>
                                            </iais:section>
                                            <p class="text-right text-center-mobile">
                                                <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>

                                                <iais:action style="text-align:right;">
                                                    <button type="button" class="btn btn-secondary"
                                                            onclick="javascript:doPreview();">Preview
                                                    </button>
                                                    &nbsp;
                                                </iais:action>
                                            </p>
                                        </div>
                                        <div class="tab-pane " id="tabProcessing" role="tabpanel">

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </iais:body>
        </div>
    </div>
</form>
</div>
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>

<script type="text/javascript">

    function doPreview() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "preview");

    }


</script>





