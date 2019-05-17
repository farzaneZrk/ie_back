package Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static Service.UserService.prepareResponse;

@WebFilter("/*")
public class ArabicEncodingFilter implements Filter {
    private boolean doBeforeProcessing(ServletRequest request, ServletResponse response)

            throws IOException, ServletException {
        System.out.println("in before");

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            System.out.println(url);
            String [] arrOfStr = url.split("/");
            System.out.println(arrOfStr[arrOfStr.length - 1]);
            String servletName = arrOfStr[arrOfStr.length - 1];

            if (servletName.equals("login") || servletName.equals("registerUser") || servletName.equals("checkUsername"))
                return true;
            else{
                return validateJWT(request, response);
            }


        }

        return false;

    }

    private boolean validateJWT(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        Map<String, Object> resMap = new LinkedHashMap<>();
        String jwt = request.getParameter("jwt");
        if (jwt == null) {
            resMap.put("msg", "no jwt sent.");
            resMap.put("errorCode", "401");
            JSONObject json = new JSONObject(resMap);
            prepareResponse((HttpServletResponse) response, json, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
            // todo: send 401
        }
        System.out.println(jwt);
        try {

            Claims claims = Jwts.parser().setSigningKey(DigestUtils.sha256Hex("joboonja")).parseClaimsJws(jwt).getBody();
            System.out.println("Issuer: " + claims.getIssuer());
            if(!claims.getIssuer().equals("joboonja.ut.ac.ir")){
                resMap.put("msg", "jwt is not valid");
                resMap.put("errorCode", "403");
                JSONObject json = new JSONObject(resMap);
                prepareResponse((HttpServletResponse) response, json, HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            String userId = (String) claims.get("userId");
            System.out.println(userId);
            request.setAttribute("loggedInUserId", userId);
            return true;

        } catch (JwtException e) {
            if(e.getMessage().contains("signature"))
                resMap.put("msg", "jwt is not valid");
            else if(e.getMessage().contains("expired"))
                resMap.put("msg", "jwt is expired");
            System.out.println(e.getMessage());
            System.out.println("get exception");
            resMap.put("errorCode", "403");
            JSONObject json = new JSONObject(resMap);
            prepareResponse((HttpServletResponse) response, json, HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        System.out.println("in after");

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("hello");

        boolean checkJWT = doBeforeProcessing(request, response);
        System.out.println("///////validation of jwt is " + checkJWT);

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
            if(checkJWT)
                chain.doFilter(request, response);

        } catch(Throwable t) {

            problem = t;

            t.printStackTrace();

        }

        doAfterProcessing(request, response);

    }
}
