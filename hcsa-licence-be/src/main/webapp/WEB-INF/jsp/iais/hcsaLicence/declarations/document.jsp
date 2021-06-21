<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel-body">
  <div class="row col-xs-12 form-group">
    <p>If you have selected that <span style="font-style: italic">'I am duly authorised by the Applicant to make this application on its behalf and the Applicant will be the licensee if the application is granted'</span>, please attach proof of your authorisation below:</p>
  </div>
  <br>
  <div class="row">
    <h3>Proof of Authorisation </h3>
    <c:forEach items="${appSubmissionDto.appDeclarationDocDtos}" var="appDeclarationDocDto" varStatus="inx">
      <div class="col-xs-12">
        <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${appDeclarationDocDto.fileRepoId}" docName="${appDeclarationDocDto.docName}"/>
        <c:out value="(${appDeclarationDocDto.docSize} KB)"/>
      </div>
    </c:forEach>

  </div>
</div>
