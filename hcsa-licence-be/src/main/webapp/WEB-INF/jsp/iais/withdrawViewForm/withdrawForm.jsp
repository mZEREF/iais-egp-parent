<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
    String webRootCommon = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<input style="display: none" value="${NOT_VIEW}" id="view">
<c:set var="appEdit" value="${applicationViewDto.appEditSelectDto}"/>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <div class="tab-content">
                        <div class="tab-pane active" id="previewTab" role="tabpanel">
                            <div class="preview-gp">
                                <div class="row">
                                    <div class="col-xs-12 col-md-2 text-right">
                                        <br>
                                        <br>
                                        <%--   <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>--%>
                                    </div>
                                </div>
                                <c:if test="${not empty errorMsg}">
                                    <iais:error>
                                        <div class="error">
                                                ${errorMsg}
                                        </div>
                                    </iais:error>
                                </c:if>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <%@include file="withdrawContentForm.jsp" %>
                                    </div>
                                </div>
                            </div>

                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                    </div>
                                    <c:if test="${rfi=='rfi'}">
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="next btn btn-primary" id="previewNext">SUBMIT </a>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $('#hciNameClick').click(function () {
        let jQuery = $('#hciNameShowOrHidden').attr('style');
        if (jQuery.match("display: none")) {
            $('#hciNameShowOrHidden').show();
        } else {
            $('#hciNameShowOrHidden').hide();
        }

    });

    $(function () {
        var
    })
</script>