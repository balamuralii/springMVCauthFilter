package com.tutorialspoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
        urlPatterns = { "/*" }
       
)
public class AuthTokenFilter implements Filter {
	private static final Logger logger = Logger.getLogger(AuthTokenFilter.class.getName());

    private String username = "bala";

    private String password = "Patimurali";

private String realm = "Protected";

    @Override
    public void destroy() {
      System.out.println("print context");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    	System.out.println("initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	System.out.println(AuthTokenFilter.class.getName()+"inside dofilter");
    	
    	System.out.println("request.getMethod():"+((HttpServletRequest) request).getMethod());
    	
    	
    	
    	System.out.println( "print all the headers");
    	Enumeration<String> headerNames = ((HttpServletRequest) request).getHeaderNames();
    	while(headerNames.hasMoreElements()) {
    	  String headerName = headerNames.nextElement();
    	  System.out.println("Header Name - " + headerName + ", Value - " + ((HttpServletRequest) request).getHeader(headerName));
    	}
    	
    	
    	System.out.println( "print all the request params");
    	Enumeration<String> params = request.getParameterNames(); 
    	while(params.hasMoreElements()){
    	 String paramName = params.nextElement();
    	 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
    	}
    	
    	
    	final String authorization = ((HttpServletRequest) request).getHeader("authorization");
    	System.out.println("authorization:"+authorization);
        if (authorization != null) {
            StringTokenizer st = new StringTokenizer(authorization);
            if (st.hasMoreTokens()) {
            	 String basic = st.nextToken();
            	if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        String credentials = new String(Base64.getDecoder().decode(st.nextToken()));
                       
                        System.out.println("credentials: " + credentials);
                        int p = credentials.indexOf(":");
                        System.out.println(p);
                        if (true) {
                            String _username = credentials.substring(0, p).trim();
                            logger.info(_username);  
                            String _password = credentials.substring(p + 1).trim();
                            logger.info(_password);
                            if (!username.equals(_username) || !password.equals(_password)) {
                                unauthorized(response, "Bad credentials");
                            }

                            chain.doFilter(request, response);
                        } else {
                            unauthorized(response, "Invalid authentication token");
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new Error("Couldn't retrieve authentication", e);
                    }
                }else {unauthorized(response, "Invalid authentication type");}
            }
        } else {
            unauthorized(response, authorization);}
        }
        
        private void unauthorized(ServletResponse response, String message) throws IOException {
            ((HttpServletResponse) response).setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
            ((HttpServletResponse) response).sendError(401, message);
        }


}
