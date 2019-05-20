<%

String pageIdForEdit = request.getParameter("pageIdForEdit");
String pageEditSkipWizard = request.getParameter("pageEditSkipWizard");
session.setAttribute("pageIdForEdit", pageIdForEdit);
session.setAttribute("pageEditSkipWizard", pageEditSkipWizard);

%>