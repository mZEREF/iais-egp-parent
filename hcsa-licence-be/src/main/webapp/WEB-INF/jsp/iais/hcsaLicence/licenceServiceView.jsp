<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
      <%@include file="viewPremises.jsp"%>
    </c:otherwise>
  </c:choose>
</form>
<style>

  *{
    word-wrap: break-word
  }
</style>
<script type="text/javascript">
    document.title = 'HALP';
    $(document).ready(function () {
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
            var newEle='';
            if($(this).parent().children('.'+newValClass).length>0){
                 newEle = $(this).parent().children('.'+newValClass);
            }else {
                newEle = $(this).parent().prev().find('.' + newValClass);
            }
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if($('#oldAppSubmissionDto').val()=='false'){
                if (oldVal.length > 0 || newVal.length > 0) {
                    if (oldVal != newVal) {
                        $(this).show();
                        var newHtml ;
                        if($(this).parent().children('.'+newValClass).length>0){
                            newHtml= $(this).parent().children('.' + newValClass).html();
                        }else {
                            newHtml= $(this).parent().prev().find('.' + newValClass).html();
                        }
                        var oldHtml=$(this).html();
                        $(this).html(newHtml);
                        if($(this).parent().children('.'+newValClass).length>0){
                            $(this).parent().children('.' + newValClass).html(oldHtml);
                        }else {
                            $(this).parent().prev().find('.' + newValClass).html(oldHtml);
                        }

                        $(this).attr("class","newVal compareTdStyle");
                    } else if(oldVal.length > 0 && newVal.length <= 0){
                        $(this).hide();
                    }
                }
            }

        });
    }

</script>
