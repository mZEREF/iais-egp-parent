<c:choose>
  <c:when test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange!='RFC_eqHciNameChange'&&renew_rfc_show!='Y'}"></c:when>
  <c:when test="${isSingle=='N'&&AppSubmissionDto.appType == 'APTY004'}">
    <%--
    <c:set var="pageShowFileDtos" value="${selectedRENEWFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
    <c:set var="sec" value="RENEW" scope="request"/>
    <%@include file="../declarations/preliminaryQuestion.jsp"%>
    <c:if test="${viewPrint != 'Y'}">
      <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
    </c:if>
    <c:if test="${viewPrint == 'Y'}">
      <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
    </c:if>
    <%@include file="../declarations/bankruptcy.jsp"%>
    <%@include file="../declarations/competencies.jsp"%>
    <%@include file="../declarations/crimAndPassApp.jsp"%>
    <%@include file="../declarations/generalAccuracy.jsp"%>
    --%>
  </c:when>
  <c:when test=">${licenceView=='licenceView'}"></c:when>
  <c:otherwise>
    <div class="panel panel-default">
      <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed a-panel-collapse" style="text-decoration: none;" role="button" data-toggle="collapse" href="#declarations" aria-expanded="true" aria-controls="declarations" name="printControlNameForApp">Declarations</a></h4>
      </div>
      <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="declarations" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <%-- New Application --%>
          <c:if test="${AppSubmissionDto.appType == 'APTY002'}">
            <c:set var="pageShowFileDtos" value="${selectedNewFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
            <c:set var="sec" value="New" scope="request"/>
            <%@include file="../declarations/preliminaryQuestion.jsp"%>
            <c:if test="${viewPrint != 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
            </c:if>
            <c:if test="${viewPrint == 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
            </c:if>
            <%@include file="../declarations/bankruptcy.jsp"%>
            <%@include file="../declarations/competencies.jsp"%>
            <%@include file="../declarations/crimAndPassApp.jsp"%>
            <%@include file="../declarations/generalAccuracy.jsp"%>
          </c:if>
          <c:set var="rfc_from_renew" value="${AppSubmissionDto.appGroupAppType == 'APTY005' && (not empty AppSubmissionDto.appDeclarationMessageDto && AppSubmissionDto.appDeclarationMessageDto.appType == 'APTY004') ? 'Y' : 'N'}"/>
          <c:if test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange=='RFC_eqHciNameChange' && rfc_from_renew != 'Y'}">
            <c:set var="pageShowFileDtos" value="${selectedRFCFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
            <c:set var="sec" value="RFC" scope="request"/>
            <%@include file="../declarations/preliminaryQuestion.jsp"%>
            <c:if test="${viewPrint != 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
            </c:if>
            <c:if test="${viewPrint == 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
            </c:if>
              <c:if test="${group_renewal_app_rfc == '1'}">
                <input type="hidden" name="group_renewal_app_rfc" id="group_renewal_app_rfc" value="1"/>
                <%@include file="../declarations/bankruptcy.jsp"%>
                <%@include file="../declarations/competencies.jsp"%>
                <%@include file="../declarations/crimAndPassApp.jsp"%>
                <%@include file="../declarations/generalAccuracy.jsp"%>
              </c:if>
              <c:if test="${group_renewal_app_rfc != '1'}">
               <%@include file="../declarations/statements.jsp"%>
              </c:if>
          </c:if>

          <c:if test="${AppSubmissionDto.appType == 'APTY005' && rfc_from_renew == 'Y'}">
            <c:set var="pageShowFileDtos" value="${selectedRENEWFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
            <c:set var="sec" value="RENEW" scope="request"/>
            <%@include file="../declarations/preliminaryQuestion.jsp"%>
            <c:if test="${viewPrint != 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
            </c:if>
            <c:if test="${viewPrint == 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
            </c:if>
            <%@include file="../declarations/bankruptcy.jsp"%>
            <%@include file="../declarations/competencies.jsp"%>
            <%@include file="../declarations/crimAndPassApp.jsp"%>
            <%@include file="../declarations/generalAccuracy.jsp"%>
          </c:if>
          <c:if test="${renewDto.appSubmissionDtos.size()>=1 && renewDto.appSubmissionDtos[0].appType=='APTY004'}">
            <c:set value="${renewDto.appSubmissionDtos[0]}" var="AppSubmissionDto"></c:set>
            <c:set var="pageShowFileDtos" value="${selectedRENEWFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
            <c:set var="sec" value="RENEW" scope="request"/>
            <%@include file="../declarations/preliminaryQuestion.jsp"%>
            <c:if test="${viewPrint != 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
            </c:if>
            <c:if test="${viewPrint == 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
            </c:if>
            <%@include file="../declarations/bankruptcy.jsp"%>
            <%@include file="../declarations/competencies.jsp"%>
            <%@include file="../declarations/crimAndPassApp.jsp"%>
            <%@include file="../declarations/generalAccuracy.jsp"%>
          </c:if>
          <c:if test="${declaration_page_is == 'cessation' }">
            <c:set value="${appCessationDtos[0]}" var="AppSubmissionDto" scope="request"></c:set>
            <c:set var="pageShowFileDtos" value="${selectedCessFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
            <c:set var="sec" value="Cess" scope="request"/>
            <%@include file="../declarations/preliminaryQuestion.jsp"%>
            <c:if test="${viewPrint != 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocument.jsp"%>
            </c:if>
            <c:if test="${viewPrint == 'Y'}">
              <%@include file="../declarations/proofOfAuthorisationDocumentView.jsp"%>
            </c:if>
            <%@include file="../declarations/cessationQuestion.jsp"%>
          </c:if>
        </div>
      </div>
    </div>
  </c:otherwise>
</c:choose>
<script type="text/javascript">
  $(document).ready(function(){
    $('#declarations').find('.error-msg').on('DOMNodeInserted', function(){
      if ($(this).not(':empty')) {
         $('#declarations').collapse('show');
      }
    });
    <c:if test="${viewPrint == 'Y'}">
    $(':input', '#declarations').prop('disabled', true);
    </c:if>
  });
</script>
