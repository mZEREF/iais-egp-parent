<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="licencePremiseResult" type="com.ecquaria.cloud.moh.iais.common.dto.SearchResult<com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto>"--%>
<%--@elvariable id="newTcuDate" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="newTcuDateRemarks" type="java.util.List<java.lang.String>"--%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="components">
                        <h3>
                            <span>Change TCU Date</span>
                        </h3>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" style="display: none"></th>
                                    <iais:sortableHeader style="width:5%" needSort="false" field="licence_no"
                                                         value="Licence No."/>
                                    <iais:sortableHeader style="width:5%" needSort="false" field="business_name"
                                                         value="Business Name"/>
                                    <iais:sortableHeader style="width:5%" needSort="false" field="hci_code"
                                                         value="HCI Code"/>
                                    <iais:sortableHeader style="width:10%" needSort="false" field="address"
                                                         value="Address"/>
                                    <iais:sortableHeader style="width:5%" needSort="false" field="svc_name"
                                                         value="Service Name"/>
                                    <iais:sortableHeader style="width:5%" needSort="false" field="tcu_date"
                                                         value="Previous TCU Date"/>
                                    <iais:sortableHeader style="width:10%" needSort="false" field="new_tcu_date"
                                                         value="New TCU Date"/>
                                    <iais:sortableHeader style="width:15%" needSort="false" field="remarks"
                                                         value="Remarks"/>
                                </tr>
                                </thead>
                                <tbody style="text-align: left">
                                <c:forEach var="licencePremise" items="${licencePremiseResult.rows}"
                                           varStatus="status">
                                    <tr>
                                        <td align="left"><c:out value="${licencePremise.licenceNo}"/></td>
                                        <td align="left"><c:out value="${licencePremise.businessName}"/></td>
                                        <td align="left"><c:out value="${licencePremise.hciCode}"/></td>
                                        <td align="left"><c:out value="${licencePremise.address}"/></td>
                                        <td align="left"><c:out value="${licencePremise.serviceName}"/></td>
                                        <td align="left">
                                            <fmt:formatDate value="${licencePremise.tcuDate}" pattern='dd/MM/yyyy'/>
                                        </td>
                                        <td align="left">
                                            <iais:datePicker id="newTcuDate${status.index}"
                                                             name="newTcuDate${status.index}"
                                                             value="${newTcuDate[status.index]}"/></td>
                                        <td align="left"><iais:input id="newTcuDateRemarks${status.index}"
                                                                     type="text" maxLength="300"
                                                                     name="newTcuDateRemarks${status.index}"
                                                                     value="${newTcuDateRemarks[status.index]}"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div>
                            <a style="float:left;color:#2199E8;" class="back" onclick="back();"><em
                                    class="fa fa-angle-left"></em> Back</a>

                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-primary" onclick="$('#confirm').modal('show')">SUBMIT</button>
                            </iais:action>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<iais:confirm msg="Do you wish to confirm the changes?" popupOrder="confirm"
              cancelFunc="$('#confirm').modal('hide')" cancelBtnCls="btn btn-secondary" cancelBtnDesc="NO"
              callBack="next()" yesBtnCls="btn btn-primary" yesBtnDesc="YES"/>
<script type="text/javascript">
    function back() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "premiseList");
    }

    function next() {
        $('#confirm').modal('hide');
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "submit")
    }
</script>