<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    .removeBtn{
        color:red;
        cursor: pointer;
    }
    .new-premise-form-conv{
        border-top:1px solid #BABABA;
        padding-top:40px;
    }
    .underLine{
        border-top:1px solid #BABABA;
        padding-top:40px;
    }
</style>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskLicenceTenureConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskLicTenValidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <input type="hidden" id="removeValue" name ="removeValue" >
    <input type="hidden" id="addValue" name ="addValue" >
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">

                        <h2>LicenceTenure Risk Configuration</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th width="10%">Service Name</th>
                                        <th width="15%">Effective Start Date</th>
                                        <th width="15%">Effective End Date</th>
                                        <th width="10%">Minimum Risk Score (greater than)</th>
                                        <th width="10%">Maximum Risk Score (less than or equal to)</th>
                                        <th width="10%">Licence Tenure</th>
                                        <th width="10%">DateType</th>
                                        <th width="10%">Remove</th>
                                        <th width="10%">Add</th>

                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="ten" items="${tenShowDto.licenceTenureDtoList}" varStatus="status">
                                        <tr>
                                            <td width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.edit}">
                                                        <p><strong>${ten.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${ten.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td width="15%"><iais:datePicker id = "${ten.svcCode}instartdate" name = "${ten.svcCode}instartdate" value="${ten.doEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${ten.svcCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td width="15%"><iais:datePicker id = "${ten.svcCode}inenddate" name = "${ten.svcCode}inenddate" value="${ten.doEndDate}"></iais:datePicker>
                                                <c:set value = "error_${ten.svcCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td  width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id = "<c:out value="${tenName}leftdiv"/>">
                                                                    <input type="text" maxlength="5" name="<c:out value="${tenName}left"/>" value="<c:out value="${sub.columLeft}"/>">
                                                                    <span class="error-msg" id="error_<c:out value="${tenName}lefterr"/>" name="iaisErrorMsg"></span>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
                                                                <input type="text" maxlength="5" name="<c:out value="${sub.id}left"/>" value="">
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                            <td  width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id="<c:out value="${tenName}right"/>div">
                                                                    <input type="text" maxlength="5" name="<c:out value="${tenName}right"/>" value="<c:out value="${sub.columRight}"/>">
                                                                    <span class="error-msg" id="<c:out value="error_${tenName}righterr"/>" name="iaisErrorMsg"></span>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
                                                                <input type="text" maxlength="5" name="<c:out value="${sub.id}right"/>" value="<c:out value="${sub.columRight}"/>">
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>

                                            <td  width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                            <div style="width: 100px;" id = "<c:out value="${tenName}ltdiv"/>">
                                                                <input type="text" maxlength="5" name="<c:out value="${tenName}lt"/>" value="<c:out value="${sub.licenceTenure}"/>">
                                                                <span class="error-msg" id="<c:out value="error_${tenName}lterr"/>" name="iaisErrorMsg"></span>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 100px;">
                                                            <input type="text" maxlength="5" name="<c:out value="${sub.id}right"/>" value="<c:out value="${sub.licenceTenure}"/>">
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td  width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                            <div style="width: 120px;height:50px;margin-bottom:15px;" id ="<c:out value="${tenName}timediv"/>">
                                                                <iais:select name="${tenName}type" options="timeType" firstOption="Please Select" value="${sub.dateType}" ></iais:select>
                                                            </div>
                                                            <span class="error-msg" id="<c:out value="error_${tenName}timeerr"/>" name="iaisErrorMsg"></span>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 120px;height:50px;margin-bottom:15px;">
                                                            <iais:select name="${sub.id}type" options="timeType" firstOption="Please Select" value="${sub.dateType}" ></iais:select>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td  width="10%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <c:if test="${ten.maxSubOrderNum>0}">
                                                                <div style="width: 100px;height:50px;margin-bottom:15px;padding-top: 5px;">
                                                                    <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                    <span class="removeBtn" onclick="removeColum(<c:out value="'${tenName}'"/>)">remove</span>&nbsp;&nbsp;&nbsp;
                                                                </div>
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 100px;height:50px;margin-bottom:15px;padding-top: 5px;">
                                                            <span class="removeBtn"></span>&nbsp;&nbsp;&nbsp;
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><div style="width: 100px;height:50px;margin-bottom:15px;padding-top: 5px;">
                                                <span class="removeBtn" onclick="addColum(<c:out value="'${ten.svcCode}'"/>)">Add</span>&nbsp;&nbsp;&nbsp;
                                            </div></td>

                                        </tr>
                                        <tr>
                                            <td  width="10%"></td>
                                            <td  width="20%"></td>
                                            <td width="20%"></td>
                                            <td width="10%"></td>
                                            <td width="10%">

                                                <c:set value = "error_${ten.svcCode}maxsort" var = "maxsort"/>
                                                <span class="error-msg" id="<c:out value="${maxsort}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td width="10%">
                                                <c:set value = "error_${ten.svcCode}maxminsort" var = "maxminsort"/>
                                                <span class="error-msg" id="<c:out value="${maxminsort}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td width="10%">
                                                <c:set value = "error_${ten.svcCode}ltsort" var = "ltsort"/>
                                                <span class="error-msg" id="<c:out value="${ltsort}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}maxSubList" var = "ltsort"/>
                                                <span class="error-msg" id="<c:out value="${ltsort}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td width="10%"></td>
                                            <td width="10%"></td>

                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                        <div style="float:right">
                            <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }

    function removeColum(str){
        $("#removeValue").val(str);
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function addColum(str){
        $("#addValue").val(str);
        SOP.Crud.cfxSubmit("mainForm","next");
    }
</script>
