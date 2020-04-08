<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet-blank"/>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="prelogin" style="background-image: url('/web/themes/be/img/prelogin-masthead-banner.jpg');">
            <div class="container">

                <div class="col-xs-5">
                    <div class="center-content">
                        <br>
                        <div class="login-area">
                            <div id="errorMessage" class="error-login" style="display:block"></div>
                            <div class="linebreak"></div>
                        </div>
                        <div id="login" class="login-area" style="display:block;">
                            <div class="form-group">
                                <label class="sr-only" for="entityId">USER ID<</label>
                                <input id="entityId" name="entityId"  class="custom-tag-to-uppercase form-control" required="required" type="text" value="" autocomplete="off">
                                <a class="topposition tooltipclick" title="" data-placement="left" data-toggle="tooltip" data-trigger="click" tabindex="-1" href="#" data-original-title="Unique Entity Number (UEN) or system generated Entity ID registered to your CorpPass account."><span class="icon-info-login-main login-info-padding"></span></a>
                            </div>

                            <div class="form-group">
                                <button type="button"  class="btn btn-primary btn-block" onclick="Utils.submit('mainForm')" >Login  <i class="fa fa-caret-right" aria-hidden="true"></i></button>
                            </div>
                            <div class="checkbox">
                                <label class="login-label">
                                    <a class="remTooltip topposition tooltipclick" title="" data-placement="left" data-toggle="tooltip" data-trigger="click" tabindex="-1" href="#" data-original-title="This is for CorpPass to remember your Entity ID so you do not have to enter it the next time.">
                                        <span class="icon-remEntityID icon-info-login-main login-info-padding"></span></a>
                                </label>

                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_login"></span>

                        </div>
                    </div>

                </div>

            </div>
        </div>
    </form>
</div>
<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
