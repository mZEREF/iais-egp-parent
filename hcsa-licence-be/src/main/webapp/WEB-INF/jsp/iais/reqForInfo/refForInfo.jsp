<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <br><br><br>

                            <div class="col-xs-12">
                                <div class="components">
                                    <h3>
                                        <span>Request For Information List</span>
                                    </h3>
                                    <div class="table-gp">
                                        <iais:row style="text-align:left;">
                                            <label class="col-xs-9 col-md-6">
                                                <button class="btn btn-primary" type="button"  onclick="javascript:doNew()">New</button>
                                            </label>
                                            <label class="col-xs-9 col-md-4">
                                                <input type="text"  style=" font-weight:normal;" name="searchEmail" maxlength="100" value="${searchEmail}" />
                                            </label>
                                            <label class="col-xs-9 col-md-2">
                                                <button class="btn btn-primary" type="button"  onclick="SOP.Crud.cfxSubmit('mainForm', 'search');">Search</button>
                                            </label>
                                        </iais:row>
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr >
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                                <iais:sortableHeader needSort="false"  field="Email" value="Email"></iais:sortableHeader>
                                                <iais:sortableHeader needSort="false"  field="REQUEST_DATE" value="Start Date"></iais:sortableHeader>
                                                <iais:sortableHeader needSort="false"  field="DUE_DATE" value="Due Date"></iais:sortableHeader>
                                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <c:when test="${empty licPreReqForInfoDtoList}">
                                                    <tr>
                                                        <td colspan="7">
                                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="pool" items="${licPreReqForInfoDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                                            <td><c:out value="${pool.licenceNo}"/></td>
                                                            <td><c:out value="${pool.email}"/></td>
                                                            <td><fmt:formatDate value="${pool.requestDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                                            <td><fmt:formatDate value="${pool.dueDateSubmission}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                                            <td>
                                                                <iais:action >
                                                                    <a href="#" onclick="javascript:doView('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.id)}');" >View</a>
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
                            <iais:action style="text-align:left;">
                                <a href="/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries/1/check" style="margin-bottom: 1%;margin-left: 1%"><em class="fa fa-angle-left"></em> Back</a>
                            </iais:action>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    function doNew(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "new");
    }
    function doView(reqInfoId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "view",reqInfoId);
    }

</script>