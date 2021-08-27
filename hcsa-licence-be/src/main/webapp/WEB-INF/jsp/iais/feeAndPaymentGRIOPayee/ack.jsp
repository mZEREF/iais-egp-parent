<%--
  Created by IntelliJ IDEA.
  User: mjy
  Date: 2021/3/2
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="bg-title col-xs-12 col-md-12">
                        <h2>
                            <span>Add a GIRO Payee</span>
                        </h2>
                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <div class=" col-xs-12 col-md-12">
                                    You have successfully added a GIRO Payee.
                                </div>
                            </iais:row>
                        </div>

                        <div align="left"><span><a  href="#" onclick="$('#mainForm').submit();"><em class="fa fa-angle-left"> </em> Back</a></span></div>

                    </div>
                </div>
            </div>
        </div>
    </form>
</div>