package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.springframework.stereotype.Component;

/**
 * AntiSamyFilter
 *
 * @author Jinhua
 * @date 2020/4/22 13:15
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "antiSamyFilter")
@Slf4j
public class AntiSamyFilter implements Filter {
    /**
     * AntiSamy is unfortunately not immutable, but is threadsafe if we only call
     * {@link AntiSamy#scan(String taintedHTML, int scanType)}
     */
    private final AntiSamy antiSamy;
    private static final List<String> xmlParams;
    private static boolean debuglog;

    static {
        debuglog = false;
        xmlParams = Collections.singletonList("messageContent");
    }

    public AntiSamyFilter() {
        antiSamy = getAntiSamy("antisamy-default.xml");
    }

    private AntiSamy getAntiSamy(String policyFile) {
        AntiSamy as = null;
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(policyFile);
            Policy policy = Policy.getInstance(url.getFile());
            as = new AntiSamy(policy);
        } catch (PolicyException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return as;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            Map escParamMap = getEscParamMap();
            CleanServletRequest cleanRequest = new CleanServletRequest((HttpServletRequest) request, antiSamy);
            cleanRequest.setEscParamMap(escParamMap);
            chain.doFilter(cleanRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     * Wrapper for a {@link HttpServletRequest} that returns 'safe' parameter values by
     * passing the raw request parameters through the anti-samy filter. Should be private
     */
    public static class CleanServletRequest extends HttpServletRequestWrapper {

        private final AntiSamy antiSamy;
        private Map escInputMap;
        private static final String ATTR_MULTIPART_PARAMS = "multipart.params";

        private CleanServletRequest(HttpServletRequest request, AntiSamy antiSamy) {
            super(request);
            this.antiSamy = antiSamy;
        }

        public void setEscParamMap(Map map) {
            this.escInputMap = map;
        }

        /**
         * overriding getAttribute functions in {@link HttpServletRequestWrapper}
         */
        @Override
        public Object getAttribute(String name) {
            Object obj = super.getAttribute(name);
            if (obj==null) {
                return null;
            }
            if (obj instanceof String) {
                String potentiallyDirtyParameter = (String)obj;
                String strClean =filterString(potentiallyDirtyParameter);
                if (debuglog) {
                    log.debug(StringUtil.changeForLog("dirtyValue:" + name + " = " +potentiallyDirtyParameter));
                    log.debug(StringUtil.changeForLog("cleanValue:" + name + " = " + strClean));
                }
            }else if (ATTR_MULTIPART_PARAMS.equals(name)) {
                Hashtable newParam = new Hashtable<String, Vector<String>>();
                getValuesFromRequest((Hashtable)obj,newParam);
                obj = newParam;
            }
            return obj;
        }

        protected void getValuesFromRequest(Hashtable param, Hashtable newParam)
        {
            Enumeration<?> e = param.keys();
            Enumeration<?> ele = param.elements();
            while (e.hasMoreElements()) {
                String name = (String)e.nextElement();
                Vector values = (Vector<String>)ele.nextElement();
                if (values!= null) {
                    Vector<String> v = new Vector<String>();
                    for (int i = 0; i < values.size(); i++) {
                        String oriValue = (String)values.get(i);
                        String strClean = filterString(oriValue);
                        v.addElement(strClean);
                    }
                    newParam.put(name, v);
                }
            }
        }
        /**
         * overriding getParameterValues functions in {@link HttpServletRequestWrapper}
         */
        @Override
        public String[] getParameterValues(String name) {
            String[] originalValues = super.getParameterValues(name);
            if (originalValues == null) {
                return null;
            }
            List<String> newValues = new ArrayList<String>(originalValues.length);
            if (escInputMap.get(name)!=null) {
                if (debuglog) {
                    log.debug(StringUtil.changeForLog("escape paramName: " + name));
                }
                return originalValues;
            }else {
                int i = 0;
                if (debuglog) {
                    log.debug("filterString");
                }
                for (String value : originalValues) {
                    String strClean =filterString(value);
                    newValues.add(strClean);
                    if (debuglog) {
                        log.debug(StringUtil.changeForLog("originalValue: " + name + "[" + i + "]=" + value));
                        log.debug(StringUtil.changeForLog("cleanValue: " + name + "[" + i + "]=" +  strClean));
                    }
                    i++;
                }

            }
            return newValues.toArray(new String[newValues.size()]);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map getParameterMap() {
            Map<String, String[]> originalMap = super.getParameterMap();
            Map<String, String[]> filteredMap = new ConcurrentHashMap<String, String[]>(originalMap.size());
            for (String name : originalMap.keySet()) {
                filteredMap.put(name, getParameterValues(name));
            }
            return Collections.unmodifiableMap(filteredMap);
        }

        @Override
        public String getParameter(String name) {
            String potentiallyDirtyParameter = super.getParameter(name);
            if (debuglog) {
                log.debug(StringUtil.changeForLog("originalValue [" + name + "]= " +potentiallyDirtyParameter));
            }
            String strClean = null;
            if (escInputMap.get(name)!=null) {
                if (debuglog) {
                    log.debug(StringUtil.changeForLog("escape paramName: " + name));
                }
                return potentiallyDirtyParameter;
            }else {
                strClean =filterString(potentiallyDirtyParameter);
                if (debuglog) {
                    log.debug(StringUtil.changeForLog("cleanValue [" + name + "]= " + strClean));
                }
            }
            return strClean;
        }

        /**
         * This is only here so we can see what the original parameters were, you should delete this method!
         *
         * @return original unwrapped request
         */
        @Deprecated
        public HttpServletRequest getOriginalRequest() {
            return (HttpServletRequest) super.getRequest();
        }

        /**
         * @param potentiallyDirtyParameter string to be cleaned
         * @return a clean version of the same string
         */
        private String filterString(String potentiallyDirtyParameter) {
            if (potentiallyDirtyParameter == null) {
                return null;
            }

            try {
                String tmpDirty = potentiallyDirtyParameter;
                CleanResults cr = antiSamy.scan(tmpDirty, AntiSamy.DOM);
                if (cr.getNumberOfErrors() > 0) {
                    log.warn(StringUtil.changeForLog("antisamy encountered problem with input: " + cr.getErrorMessages()));
                }
                String strClean = cr.getCleanHTML();
                return strClean;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

    }

    private final static Map getEscParamMap() {
        Map map = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(xmlParams)) {
            return map;
        }

        for (String paramName : xmlParams) {
            map.put(paramName, paramName);
        }
        return map;
    }
}
