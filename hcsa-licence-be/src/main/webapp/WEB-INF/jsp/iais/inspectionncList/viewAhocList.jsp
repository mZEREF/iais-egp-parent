<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="row">
        <div class="col-xs-12">
            <div class="center-content">

                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab">
                            <h4 class="panel-title">Adhoc Item</h4>
                        </div>
                        <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                            <div class="panel-body">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th width="50%">Checklist Item</th>
                                        <th width="40%">Answer Type</th>
                                        <th width="10%">Risk Level</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:if test="${!empty adhocCheckListAttr}">
                                    <c:forEach var = "adhocItem" items = "${adhocCheckListAttr.allAdhocItem}" varStatus="status">
                                        <tr>
                                            <td>
                                                <p>${adhocItem.question}</p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${adhocItem.answerType}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${adhocItem.riskLvl}"></iais:code></p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </c:if>
                                       <c:if test="${empty adhocCheckListAttr || empty adhocCheckListAttr.allAdhocItem}">
                                           <tr> <td  colspan="3" align="center"> <iais:message key="ACK018" escape="true"/> </td>></tr>
                                       </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <div>
                    <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                    <div style="float:right">
                        <button class="btn btn-primary next" type="button" onclick="javascript:doEditAhoc();">Edit AHocs</button>
                    </div>
                </div>
            </div>
        </div>
          </div>
    </form>
</div>
<script type="text/javascript">
    function doBack(){
        // save
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }
    function doEditAhoc(){
        //
        SOP.Crud.cfxSubmit("mainForm", "add");
    }
</script>