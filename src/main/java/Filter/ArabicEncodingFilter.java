package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "ArabicEncodingFilter")
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

            chain.doFilter(request, response);

        } catch(Throwable t) {

            problem = t;

            t.printStackTrace();

        }

        doAfterProcessing(request, response);

    }
}
