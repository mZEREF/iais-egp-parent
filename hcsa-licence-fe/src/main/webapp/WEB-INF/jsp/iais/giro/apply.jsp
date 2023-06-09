<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form  method="post" id="mainForm"   action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>


    <div class="container">
        <div class="row">
            <div class="col-xs-12">

                <div class="preview-gp">
                    <div class="row">
                        <div class="col-xs-12">
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            &nbsp;
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-xs-12">
                                Click <a  onclick="showPopupWindow('https://www.moh.gov.sg/docs/librariesprovider5/licensing-terms-and-conditions/ib_giro_form.pdf')">here</a> to download the GIRO application form.
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                Click <a href="${pageContext.request.contextPath}/file-repo-Authorisation">here</a> to download the Authorisation Letter.
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                Click <a href="${pageContext.request.contextPath}/file-repo-DCA">here</a> to download the DCA form.
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                &nbsp;
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</form>


