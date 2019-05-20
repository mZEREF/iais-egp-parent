<%@page contentType="text/javascript" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<egov-mc:translateMsg id="clearQns">Are you sure you want to clear?</egov-mc:translateMsg>

<egov-mc:commonLabel id="okLabel">OK</egov-mc:commonLabel>
<egov-mc:commonLabel id="cancelLabel">Cancel</egov-mc:commonLabel>
<egov-mc:commonLabel id="confirmLabel">Confirm</egov-mc:commonLabel>

if (!window.EGOV) window.EGOV = {};
if (!EGOV.Message) EGOV.Message = {};

(function(namespace) {
    namespace.clearQns = '<egov-core:escapeJavaScript value="${clearQns}"/>';
    namespace.okLabel = '<egov-core:escapeJavaScript value="${okLabel}"/>';
    namespace.cancelLabel = '<egov-core:escapeJavaScript value="${cancelLabel}"/>';
    namespace.confirmLabel = '<egov-core:escapeJavaScript value="${confirmLabel}"/>';
})(EGOV.Message);