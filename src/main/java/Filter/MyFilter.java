package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/zz")
public class MyFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("in real my filter");

//        HttpServletResponse res = (HttpServletResponse) response;
//        res.addHeader("Access-Control-Allow-Origin", "*");
//        res.addHeader("Access-Control-Allow-Methods", "PUT,GET,POST,DELETE,OPTIONS");
//        String allow_headers = "Referer,Accept,Origin,User-Agent,Content-Type";
//        res.addHeader("Access-Control-Allow-Headers", allow_headers);
//        res.addHeader("Accept", "application/json");
//        res.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        chain.doFilter(request, response);

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
