<%@page contentType="text/javascript" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<egov-smc:message id="clearQns" key="clearqns">Are you sure you want to clear?</egov-smc:message>

<egov-smc:commonLabel id="okLabel">OK</egov-smc:commonLabel>
<egov-smc:commonLabel id="cancelLabel">Cancel</egov-smc:commonLabel>
<egov-smc:commonLabel id="confirmLabel">Confirm</egov-smc:commonLabel>

if (!window.EGOV) window.EGOV = {};
if (!EGOV.Message) EGOV.Message = {};

(function(namespace) {
    namespace.clearQns = '<egov-core:escapeJavaScript value="${clearQns}"/>';
    namespace.okLabel = '<egov-core:escapeJavaScript value="${okLabel}"/>';
    namespace.cancelLabel = '<egov-core:escapeJavaScript value="${cancelLabel}"/>';
    namespace.confirmLabel = '<egov-core:escapeJavaScript value="${confirmLabel}"/>';
})(EGOV.Message);