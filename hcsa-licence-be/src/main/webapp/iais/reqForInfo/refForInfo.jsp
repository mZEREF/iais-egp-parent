<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br><br><br>
        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <div class="table-gp">
                        <table class="table">
                            <div class="container">
                                <div class="col-xs-12">
                                    <div class="components">
                                        <h3>
                                            <span>Request For Information</span>
                                        </h3>
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr align="center">
                                                    <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                                    <iais:sortableHeader needSort="true"  field="REQ_TYPE" value="Request Type"></iais:sortableHeader>
                                                    <iais:sortableHeader needSort="true"  field="OFFICER_REMARKS" value="Officer Remarks"></iais:sortableHeader>
                                                    <iais:sortableHeader needSort="true"  field="USER_REPLY" value="User Reply"></iais:sortableHeader>
                                                    <iais:sortableHeader needSort="true"  field="DOCUMENT" value="Document"></iais:sortableHeader>
                                                    <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:choose>
                                                    <c:when test="${empty SearchResult.rows}">
                                                        <tr>
                                                            <td colspan="7">
                                                                <iais:message key="ACK018" escape="true"></iais:message>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                                            <tr>
                                                                <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                                                                <td><c:out value="${pool.applicationType}"/></td>
                                                                <td><c:out value="${pool.licenceNo}"/></td>
                                                                <td><c:out value="${pool.hciCode}"/></td>
                                                                <td><c:out value="${pool.hciCode}"/></td>
                                                                <td>
                                                                    <iais:action style="text-align:center;">
                                                                        <button type="button"  class="btn btn-default" onclick="javascript:doReqForInfo();" >Cancel</button>
                                                                    </iais:action>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <div class="container">
                                <div class="col-xs-12">
                                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                        <br><br><br><br>
                                        <h3>
                                            <span>Additional Request Information</span>
                                        </h3>
                                        <div class="panel panel-default">
                                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                                <div class="panel-body">
                                                    <div class="panel-main-content">
                                                        <iais:section title="" id = "supPoolList">
                                                            <iais:row>
                                                                <iais:field value="Select Category"/>
                                                                <iais:value width="18">
                                                                    <iais:select name="Select_category" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                                                </iais:value>
                                                            </iais:row>
                                                            <iais:row>
                                                                <iais:field value="message/instructions"/>
                                                                <iais:value width="18">
                                                                    <label>
                                                                        <input type="text" name="message_or_instructions" value="${searchNo}" />
                                                                    </label>
                                                                </iais:value>
                                                            </iais:row>
                                                            <iais:row>
                                                                <iais:value width="18">
                                                                    <label>
                                                                        <input type="radio" name="licence_no"  />need to upload file
                                                                    </label>
                                                                </iais:value>
                                                            </iais:row>
                                                            <iais:action style="text-align:center;">
                                                                <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicSearch()">Remind</button>
                                                                <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicBack()">Submit Request</button>
                                                            </iais:action>
                                                        </iais:section>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>