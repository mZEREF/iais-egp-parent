<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Availablity</h2>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Available:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <div class="form-check">
                                        <input class="form-check-input" name="ava"
                                        <c:if test="${ava != null && ava == 'true'}">
                                               checked="checked"
                                        </c:if>
                                               type="checkbox" value="">
                                        <label class="form-check-label"><span
                                                class="check-square"></span></label>
                                    </div>
                                </div>
                            </iais:value>
                        </div>

                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="text-right text-center-mobile"><button id="saveDis" type="button" class="btn btn-primary">SAVE</button></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $('#saveDis').click(function () {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm");
    });
</script>