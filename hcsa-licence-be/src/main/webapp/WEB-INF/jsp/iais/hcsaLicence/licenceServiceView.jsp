<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet-view"/>
<style>
  *{
    font-size: 16px;
  }
  .title-font-size {
    font-size: 2.2rem;
  }
  .postion-relative {
    position: relative;
  }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" id="oldAppSubmissionDto" value="${appSubmissionDto.oldAppSubmissionDto==null}">
  <c:set value="${applicationViewDto.applicationDto}" var="applicationDto"/>
  <c:choose>
    <c:when test="${applicationDto.applicationType == 'APTY001'}">
      <%@include file="../appeal/appealForm.jsp"%>
    </c:when>
    <c:when test="${applicationDto.applicationType == 'APTY008'}">
      <%@include file="../cessation/cessationViewConfirm.jsp"%>
    </c:when>
    <c:when test="${applicationDto.applicationType == 'APTY006'}">
      <%@include file="../withdrawViewForm/withdrawForm.jsp"%>
    </c:when>
    <c:otherwise>
      <%@include file="viewApplication.jsp"%>
    </c:otherwise>
  </c:choose>
  <div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-body" >
          <div class="row">
            <div class="col-md-12"><span style="font-size: 2rem;"><c:out value="${beEicGatewayClient}"/></span></div>
          </div>
        </div>
        <div class="row " style="margin-top: 5%;margin-bottom: 5%">
          <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal">
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</form>
<script type="text/javascript">
    document.title = 'HALP';
    $(document).ready(function () {
        <c:if test="${not empty beEicGatewayClient}">
        $('#PRS_SERVICE_DOWN').modal('show');
        </c:if>
        //Binding method
        $('#previewNext').click(function () {
          if(validateCheckBox()){
            // var mainForm = document.getElementById("mainForm");
            // mainForm.submit();
            callAjaxSubmit();
          }else{
            $('#errorMessage').removeClass("hidden");
          }
          //withdrawal and cessation
          if(${applicationDto.applicationType == 'APTY006' || applicationDto.applicationType == 'APTY008'}){
            window.opener=null;
            window.open('','_self');
            window.close();
          }
        });

        $('.svc-pannel-collapse').click(function () {
            $svcContenEle = $(this).closest('div.svc-content');
            $svcContenEle.find('.svc-iframe').css('height', '400px');

        });
    });

    function callAjaxSubmit(){
      $('#errorMessage').addClass("hidden");
      var form = $('#mainForm').serialize();
      console.log('111');
      $.ajax({
        type: "post",
        url:  "${pageContext.request.contextPath}/callRfiSubmit",
        data: form,
        dataType: "text",
        success: function (data) {
          $('#selectDetail',window.opener.document).html(data);
          $('#rfiSelect',window.opener.document).show();
          window.close();
        },
        error: function (msg) {
        }
      });
    }

    function validateCheckBox(){
      //editCheckbox
      var flag = false;
      if($("input:checkbox[name='editCheckbox']:checked").length > 0){
        flag = true;
      }
      return flag;
    }
    hideImg('newVal', 'oldVal');
    function hideImg(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().prev().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if(oldVal.length>0 && newVal.length<=0){
                newEle.children('img').attr("style","display: none");
            }else if(oldVal.length<=0 && newVal.length >0){
                $(this).children('img').attr("style","display: none");
            }else {
                newEle.children('img').removeAttr("style");
            }
        });
    }

    hightLightChangeVal('newVal', 'oldVal');

    function hightLightChangeVal(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().children('.'+newValClass);
            if (newEle.length <= 0) {
                newEle = $(this).parent().prev().find('.' + newValClass);
            }
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if ($('#oldAppSubmissionDto').val() == 'false') {
                if (oldVal.length > 0 || newVal.length > 0) {
                    if (oldVal != newVal) {
                        $(this).show();
                        var newHtml = '';
                        if (newEle.length > 0) {
                            newHtml = newEle.html();
                        }
                        var oldHtml=$(this).html();
                        $(this).html(newHtml);
                        if (newEle.length > 0) {
                            newEle.html(oldHtml);
                        }
                        $(this).attr("class","newVal compareTdStyle");
                    } else {
                        $(this).hide();
                    }
                }
            }

        });
    }

</script>
