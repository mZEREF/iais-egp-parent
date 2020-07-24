<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

    <br><br>
    <div class="bg-title"><h2>Preview</h2></div>
        <div class="form-horizontal">
          <div class="form-group">
            <iais:field value="Type" />
            <div class="col-xs-5 col-md-3" >
              <iais:select name="domainType"  disabled="true" codeCategory="CATE_ID_ERR_MSG_TYPE" firstOption="Please select" value="${msgRequestDto.domainType}" ></iais:select>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Message Type" />
            <div class="col-xs-5 col-md-3">
              <iais:select name="msgType" disabled="true" codeCategory="CATE_ID_WRONG_TYPE"  filterValue="WRNTYPE003" firstOption="Please select" value="${msgRequestDto.msgType}" ></iais:select>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Module" />
            <div class="col-xs-5 col-md-3">
              <iais:select name="module" disabled="true" codeCategory="CATE_ID_ERR_MSG_MODULE" filterValue="MSGMD010" firstOption="Please select" value="${msgRequestDto.module}"></iais:select>
            </div>
          </div>


          <div class="form-group">
            <iais:field value="Description"  />
            <div class="col-xs-10 col-md-7">
              <textarea cols="70" rows="7" name="description" maxlength="255" disabled="disabled" id="description"><c:out value="${msgRequestDto.description}"></c:out></textarea>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Message"  />
            <div class="col-xs-5 col-md-7">
              <textarea cols="70" rows="7" name="message" maxlength="255" disabled="disabled" id="message"><c:out value="${msgRequestDto.message}"></c:out></textarea>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Status"  />
            <div class="col-xs-5 col-md-3">
              <iais:select name="status" id="status" disabled="true" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" value="${msgRequestDto.status}" filterValue="CMSTAT002,CMSTAT004"></iais:select>
            </div>
          </div>


    </div>

    <div class="row">
      <div class="col-xs-12 col-sm-8">
        <a class="back" href="#" id="crud_cancel_link"  value = "doCancel"><em class="fa fa-angle-left"></em> Back</a>
      </div>
      <div class="col-xs-12 col-sm-4">
        <div class="button-group">
          <a class="btn btn-primary next" onclick="Utils.submit('mainForm','editSubmit')">Submit</a>
        </div>
      </div>
    </div>


  </form>
</div>


<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
