package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ArabicEncodingFilter implements Filter {
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)

            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("hello");

        doBeforeProcessing(request, response);

        Throwable problem = null;

        try {

            System.out.println("in my filter");

            HttpServletResponse res = (HttpServletResponse) response;
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.addHeader("Access-Control-Allow-Methods", "PUT,GET,POST,DELETE,OPTIONS");
            String allow_headers = "Referer,Accept,Origin,User-Agent,Content-Type";
            res.addHeader("Access-Control-Allow-Headers", allow_headers);
            res.addHeader("Accept", "application/json");
            res.addHeader("Content-Type", "application/x-www-form-urlencoded");
            chain.doFilter(request, response);

        } catch(Throwable t) {

            problem = t;

            t.printStackTrace();

        }

        doAfterProcessing(request, response);

    }
}
