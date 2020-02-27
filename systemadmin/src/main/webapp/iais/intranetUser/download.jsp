<%@page import = "java.io.OutputStream" %>
<%@page import = "java.io.BufferedOutputStream" %>
<%
    byte[] content = (byte[])request.getAttribute("content");
    String fileName = "user.xml" ;
    response.addHeader("Content-Disposition", "attachment;filename="+fileName);
//response.addHeader("Content-Disposition", "attachment;filename=111.txt");
    response.addHeader("Content-Length", "" + content.length);
    out.clear();
    OutputStream ops = new BufferedOutputStream(response.getOutputStream());
    response.setContentType("application/x-octet-stream");
    ops.write(content);
    ops.flush();
    ops.close();
%>