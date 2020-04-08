<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="prelogin" style="background-image: url('/web/themes/be/img/prelogin-masthead-banner.jpg');">
            <div class="container">

                <div class="col-xs-5">
                    <div class="center-content">
                        <br>
                        <div class="login-area">
                            <div id="errorMessage" class="error-role" style="display:block"></div>
                            <div class="linebreak"></div>
                        </div>
                        <div id="login" class="login-area" style="display:block;">
                            <div class="form-group">
                                <label class="sr-only" for="login_role">Role<</label>
                                <select id="login_role" name="decisionRole">
                                    <option value="Select" selected>Please Select
                                    </option>
                                    <c:forEach items="${roleTypeOption}" var="decision">
                                        <option value="${decision.value}">${decision.text}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_role"></span>

                            <br>
                            <div class="form-group">
                                <button type="button"  class="btn btn-primary btn-block" onclick="Utils.submit('mainForm')" >Go InBox  <i class="fa fa-caret-right" aria-hidden="true"></i></button>
                            </div>

                        </div>
                    </div>

                </div>

            </div>
        </div>
    </form>
</div>
<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
