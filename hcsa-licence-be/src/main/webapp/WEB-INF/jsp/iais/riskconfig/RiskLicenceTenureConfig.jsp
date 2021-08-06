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
<div class="main-content">
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


            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">

                        <h2>Licence Tenure Configuration</h2>

                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr style="border-style: hidden;">
                                        <th scope="col" width="10%" rowspan="2">Service Name</th>
                                        <th scope="col" width="22%" rowspan="2">Effective Start Date</th>
                                        <th scope="col" width="22%" rowspan="2">Effective End Date</th>
                                        <th scope="col" width="8%" rowspan="2">Minimum Risk Score (greater than)</th>
                                        <th scope="col" width="8%" rowspan="2">Maximum Risk Score (less than or equal to)</th>
                                        <th scope="col" width="20%" colspan="2">Licence Tenure</th>
                                        <th scope="col" width="5%" rowspan="2">Action</th>
                                    </tr>
                                    <tr style="border-style: hidden;">
                                    <th scope="col" width="10%">Year(s)</th>
                                    <th scope="col" width="10%">Month(s)</th>
                                    </tr>
                                    <tr/>
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
                                            <td width="22%"><iais:datePicker id = "${ten.svcCode}instartdate" name = "${ten.svcCode}instartdate" value="${ten.doEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td width="22%"><iais:datePicker id = "${ten.svcCode}inenddate" name = "${ten.svcCode}inenddate" value="${ten.doEndDate}"></iais:datePicker>
                                            </td>
                                            <td  width="8%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id = "<c:out value="${tenName}leftdiv"/>">
                                                                    <input type="text" maxlength="4" name="<c:out value="${tenName}left"/>" value="<c:out value="${sub.columLeft}"/>" oninput="doChangeValFor(this)">
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                            <td  width="8%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id="<c:out value="${tenName}right"/>div">
                                                                    <input type="text" maxlength="4" name="<c:out value="${tenName}right"/>" value="<c:out value="${sub.columRight}"/>" oninput="doChangeValFor(this)">
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
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
                                                            <div style="width: 120px;height:50px;margin-bottom:15px;" id = "<c:out value="${tenName}ltdiv"/>">
                                                                <iais:select name="${tenName}ltYear" options="yearSelectOptions" firstOption="--" value="${sub.yearNum}" ></iais:select>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 120px;height:50px;margin-bottom:15px;">
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
                                                                <iais:select name="${tenName}ltMonth" options="monthSelectOptions" firstOption="--" value="${sub.monthNum}" ></iais:select>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 120px;height:50px;margin-bottom:15px;">
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td  width="5%">
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div style="width: 100px;height:50px;margin-bottom:15px;padding-top: 5px;">
                                                                <c:if test="${ten.maxSubOrderNum>=0}">
                                                                    <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                    <span class="removeBtn" onclick="removeColum(<c:out value="'${tenName}'"/>)"><em class="fa fa-minus-circle"></em></span>
                                                            </c:if>
                                                                <c:if test="${status.index == 0}">
                                                                    <span class="removeBtn" onclick="addColum(<c:out value="'${ten.svcCode}'"/>)"><em class="fa fa-plus-circle"></em></span>
                                                                </c:if>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 100px;height:50px;margin-bottom:15px;padding-top: 5px;">
                                                            <span class="removeBtn" onclick="addColum(<c:out value="'${ten.svcCode}'"/>)"><em class="fa fa-plus-circle"></em></span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  colspan="7">
                                                <c:set value = "error_${ten.svcCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                                <span class="error-msg" id="error_<c:out value="${ten.svcCode}lefterr"/>" name="iaisErrorMsg"></span>
                                                <span class="error-msg" id="<c:out value="error_${ten.svcCode}righterr"/>" name="iaisErrorMsg"></span>
                                                <span class="error-msg" id="<c:out value="error_${ten.svcCode}lterr"/>" name="iaisErrorMsg"></span>
                                                <span class="error-msg" id="<c:out value="error_${ten.svcCode}timeerr"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}maxsort" var = "maxsort"/>
                                                <span class="error-msg" id="<c:out value="${maxsort}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}maxminsort" var = "maxminsort"/>
                                                <span class="error-msg" id="<c:out value="${maxminsort}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}ltsort" var = "ltsort"/>
                                                <span class="error-msg" id="<c:out value="${ltsort}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}maxSubList" var = "ltsort"/>
                                                <span class="error-msg" id="<c:out value="${ltsort}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${ten.svcCode}maxAddList" var = "ltsort"/>
                                                <span class="error-msg" id="<c:out value="${ltsort}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>

                    </div>
                    <div>
                     <c:if test="${backButtonNeed == 'Y'}">
                        <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                     </c:if>
                         <div style="float:right">
                            <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Submit</button>
                        </div>
                    </div>
                </div>
            </div>


</form>
</div>
<%@ include file="validationForRisk.jsp" %>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }

    function removeColum(str){
        showWaiting();
        $("#removeValue").val(str);
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function addColum(str){
        showWaiting();
        $("#addValue").val(str);
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function doChangeValFor(obj) {
        if( $(obj).val() == null || $(obj).val() == "")
            return;
        var isInput = false;
        var max = 3;
        var input = "";
        if($(obj).val().length == 1 &&($(obj).val() == 1 || $(obj).val() ==0 || $(obj).val() == 2 || $(obj).val() == 3)){
            isInput = true;
        }else if($(obj).val().length == 2 && ($(obj).val().indexOf(".") == 1)){
            isInput = true;
        }else if(($(obj).val().length == 3 ) && ($(obj).val().indexOf(".") == 1)){
            try {
                var s = $(obj).val().split(".");
                if(s.length != 2){
                    $(obj).val(input);
                    return;
                }
                var isN1 = isNaN(s[0]);
                var isN2 = isNaN(s[1]);
                var num =  parseInt(s[0]);
                var num1 =  parseInt(s[1]);
                if( !isN1 && num > max){
                    isInput = false;
                }else if( (!isN1 && num == max) && (!isN2 && num1 >0)){
                    isInput = false;
                    input = max;
                }else if( !isN1 && !isN2){
                    isInput = true;
                    $(obj).val((isNaN(num) ? "" : num) +"."+ (isNaN(num1)? "" : num1 ) );
                }
            }catch (e) {
                isInput = false;
            }
        }else if($(obj).val().length == 4 && $(obj).val().indexOf(".") == 1){
            try {
                var s = $(obj).val().split(".");
                if(s.length != 2){
                    $(obj).val(input);
                    return;
                }
                var isN1 = isNaN(s[0]);
                var isN2 = isNaN(s[1]);
                var num =  parseInt(s[0]);
                var num1 =  parseInt(s[1]);
                if( !isN1 && num > 3){
                    isInput = false;
                }else if( (!isN1 && num == 3) && (!isN2 && num1 >0)){
                    isInput = false;
                }else if( !isN1 && !isN2){
                    isInput = true;
                    $(obj).val((isNaN(num) ? "" : num) +"."+ (isNaN(num1)? "" :( s[1].indexOf("0") == 0 ? "0" + num1 : num1) ) );
                }
            }catch (e) {
                isInput = false;
            }
        }
        if(!isInput){
            $(obj).val(input);
        }
    }

    function isNaN(arg) {
        if (arg.trim == ""){
            return true;
        }
        argChar = "0123456789";
        for(var i = 0;i<arg.length;i++){
            if(argChar.indexOf(arg.substring(i, i + 1)) == -1) {
                return true;
            }
        }
        return false;
    }

</script>
