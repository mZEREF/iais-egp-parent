<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">

    <form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row">
        <div class="col-xs-12">
            <div class="center-content">
                      <br/>
                      <br/>
                      <br/>
                      <br/>
                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab">
                            <h4 class="panel-title">Adhoc Item</h4>
                        </div>
                        <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                            <div class="panel-body">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" width="50%"><p>Checklist Item</p></th>
                                        <th scope="col" width="40%"><p>Answer Type</p></th>
                                        <th scope="col" width="10%"><p>Risk Level</p></th>
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
                                           <tr>  <td  colspan="3" align="left"> <p> <iais:message key="GENERAL_ACK018" escape="true"/></p> </td></tr>
                                       </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <div>
                    <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                    <div style="float:right">
                        <button class="btn btn-primary next" type="button" onclick="javascript:doEditAhoc();">Edit Adhocs</button>
                    </div>
                </div>
            </div>
        </div>
          </div>
    </form>
</div>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        // save
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }
    function doEditAhoc(){
        showWaiting();
        //
        SOP.Crud.cfxSubmit("mainForm", "add");
    }
</script>