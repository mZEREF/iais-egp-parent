<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<c:set var="individualType" value="LICTSUB002" />
<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form method="post" enctype="multipart/form-data" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="maxFile" id ="maxFile" value="${maxFile}">
  <div class="row">
    <div class="container">
      <div class="col-xs-12">
        <iais:section title="">
          <iais:row>
            <iais:field width="7" value="Licence No."></iais:field>
            <iais:value width="10"><p>${prepareTranfer.licenceNo}</p></iais:value>
          </iais:row>
          <iais:row>
            <iais:field width="7" value="Service Name"></iais:field>
            <iais:value width="10"><p>${prepareTranfer.serviceName}</p></iais:value>
          </iais:row>
          <c:if test="${prepareTranfer.groupLic}">
          <iais:row>
            <iais:field width="7" value="Select Mode of Service Delivery"></iais:field>
            <iais:value width="10">
              <p>
              <c:forEach items="${prepareTranfer.appGrpPremisesDtoList}" var="premises">
                <div class="form-check">
                  <input class="form-check-input" id="premisesInput" type="checkbox" name="premisesInput" aria-invalid="false" value="${premises.premisesIndexNo}" disabled>
                  <label class="form-check-label" for="premisesInput"><span class="check-square"></span>${premises.tranferSelect} </label>
                </div>
              </c:forEach>
              <span  class="error-msg" name="iaisErrorMsg" id="error_premisesError"></span>
              </p>
            </iais:value>
          </iais:row>
          </c:if>
          <iais:row>
          <iais:field width="7" value="UEN of Licensee to transfer licence to"></iais:field>
          <iais:value width="10">
            <p>
            <c:out value="${UEN}"></c:out>
              <span  class="error-msg" name="iaisErrorMsg" id="error_uenError"></span>
            </p>
          </iais:value>
        </iais:row>
          <c:if test="${hasSubLicensee}">
          <iais:row>
            <iais:field width="7" value="Licensee to transfer to"></iais:field>
            <iais:value width="10">
              <p>
            <c:out value="${subLicenseeDto.getDisplayName()}"></c:out>
                <span  class="error-msg" name="iaisErrorMsg" id="error_subLicenseeError"></span>
              </p>
            </iais:value>
          </iais:row>
          </c:if>
          <c:if test="${hasNewSubLicensee}">
            <iais:row>
              <iais:field width="7" value="Add a new licensee"></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getIdNumber()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <%--<iais:row>--%>
              <%--<iais:field width="7" value="ID No."></iais:field>--%>
              <%--<iais:value width="10">--%>
                <%--<p>--%>
                <%--<c:out value="${subLicenseeDto.getIdNumber()}"></c:out>--%>
                <%--</p>--%>
              <%--</iais:value>--%>
            <%--</iais:row>--%>
            <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
              <iais:field width="5" value="ID Type"/>
              <iais:value width="7" display="true">
                <iais:code code="${subLicenseeDto.idType}" />
              </iais:value>
            </iais:row>
            <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
              <iais:field width="5" value="ID No."/>
              <iais:value width="7" display="true">
                <c:out value="${subLicenseeDto.idNumber}" />
              </iais:value>
            </iais:row>

            <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == individualType
            && subLicenseeDto.idType == 'IDTYPE003'? '' : 'hidden'}">
              <iais:field width="5" value="Country of issuance"/>
              <iais:value width="7" display="true">
                <iais:code code="${subLicenseeDto.nationality}" />
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Licensee Name"></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getLicenseeName()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Postal Code"></iais:field>
              <iais:value width="10">
                <p>
                  <c:out value="${subLicenseeDto.getPostalCode()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Address Type"></iais:field>
              <iais:value width="10">
                <p>
                  <iais:code code="${subLicenseeDto.getAddrType()}" />
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Block / House No."></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getBlkNo()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Floor No."></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getFloorNo()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Unit No."></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getUnitNo()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Street Name"></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getStreetName()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Building Name"></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getBuildingName()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Mobile No."></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getTelephoneNo()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
            <iais:row>
              <iais:field width="7" value="Email Address"></iais:field>
              <iais:value width="10">
                <p>
                <c:out value="${subLicenseeDto.getEmailAddr()}"></c:out>
                </p>
              </iais:value>
            </iais:row>
          </c:if>
         <iais:row>
            <iais:field width="7" value="Reason for licence transfer"></iais:field>
            <iais:value width="10">
              <p>
              <c:out value="${reason}"></c:out>
              </p>
            </iais:value>
         </iais:row>
          <iais:row>
            <iais:field width="7" value="Email address of transferee" mandatory="true"></iais:field>
            <iais:value width="10">
              <p>
              <c:out value="${email}"></c:out>
              </p>
            </iais:value>
          </iais:row>
          <iais:row>
            <iais:field width="7" value="Letter of Undertaking" mandatory="true"></iais:field>
            <iais:value width="10">
              <p>
              <c:forEach items="${pageShowFileDtos}" var="pageShowFileDto">
                  <a href="/hcsa-licence-web/download-session-file?fileAppendIdDown=selectedFile&fileIndexDown=${pageShowFileDto.index}" title="Download" class="downloadFile" style="text-decoration:none;">
                     <c:out value="${pageShowFileDto.fileName}"></c:out></a>
              </c:forEach>
              </p>
            </iais:value>
          </iais:row>
          <iais:row>
           <iais:message key="RFC_ERR0018"></iais:message>
          </iais:row>
          <div class="form-check">
            <input class="form-check-input" id="confirm" type="checkbox" name="confirm" aria-invalid="false" value="1" checked="checked" disabled="disabled">
            <label class="form-check-label" for="confirm"><span class="check-square"></span><iais:message key="ACK_DEC001" escape="false" /></label>
            <p><span name="iaisErrorMsg" class="error-msg" id="error_confirmError"></span></p>
          </div>
        </iais:section>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="container">
      <div class="col-xs-12 col-md-6 text-left">
        <a class="back" href="/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/preparePage"><em class="fa fa-angle-left"></em> Back</a>
      </div>
      <div class="col-xs-12 col-md-6 text-right">
        <a class="btn btn-primary next premiseId" id="Next" href="javascript:void(0);">Next</a>
      </div>
    </div>
  </div>
  <br/>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  <%@ include file="../appeal/FeFileCallAjax.jsp" %>
</form>




<script>
    $(document).ready(function(){
        $("input[name='premisesInput']").each(function(){
            if('${selectCheakboxs}'.indexOf($(this).val()) != -1){
                $(this).prop("checked",true);
            }
        });
    });
    $("#Next").click(function () {
        showWaiting();
        document.getElementById("mainForm").submit();
    });
    function  showWaiting(){
        $.blockUI({message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}});
    }

</script>