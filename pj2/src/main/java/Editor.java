import blog.*;

import java.io.IOException;
import java.sql.* ;
import java.util.*;
import java.util.regex.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import com.mysql.jdbc.Driver;

/**
 * Servlet implementation class for Servlet: ConfigurationTest
 *
 */
public class Editor extends HttpServlet {
    /**
     * The Servlet constructor
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    private static final String DATABASE_HOST = "jdbc:mysql://localhost:3306/CS144";

    private static HashMap<String, Integer> nextidMap;

    public Editor() {}

    public void init() throws ServletException
    {
        /*  write any servlet initialization code here or remove this function */
        nextidMap = new HashMap<>();
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            String sql = "SELECT username, max(postid) AS postid FROM Posts GROUP BY username";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String username = rs.getString("username");
                int postid = rs.getInt("postid");
                nextidMap.put(username, postid);
            }
        } catch (SQLException ex){
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
    }
    
    public void destroy()
    {
        /*  write any servlet cleanup code here or remove this function */
    }

    /**
     * Handles HTTP GET requests
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
        String action = String.valueOf(request.getParameter("action"));
        String statusCode = "";
        if(action.equals("open")){
            handleOpen(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handlePreview(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/preview.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("status", "invalid-Do");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    

    /**
     * Handles HTTP POST requests
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
        String action = String.valueOf(request.getParameter("action"));
        String statusCode = "";
        if(action.equals("open")){
            handleOpen(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handlePreview(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/preview.jsp").forward(request, response);
            }
        } else if(action.equals("save")){
            handleSave(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            } 
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("delete")){
            handleDelete(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("pass")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("status", "invalid-Post");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }


    public void handleList(HttpServletRequest request, HttpServletResponse response){
        String[] parameters = new String[1];
        if (!validUsernamePostid(request, parameters)) {
            return;
        }
        String username = parameters[0];

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            String sql = "SELECT * FROM Posts WHERE username = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()){
                Blog blog = new Blog();
                blog.username = rs.getString("username");
                blog.postid = rs.getInt("postid");
                blog.title = rs.getString("title");
                blog.body = rs.getString("title");
                blog.created = rs.getString("created");
                blog.modified = rs.getString("modified");
                blogs.add(blog);
            }
            Collections.sort(blogs);
            if (blogs.size() > 0) {
                nextidMap.put(username, blogs.get(blogs.size()-1).postid);
            }
            request.setAttribute("blogs", blogs);
            request.setAttribute("status", "pass");
        } catch (SQLException ex){
            request.setAttribute("status", "SQL execution Error!");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }


    public void handleOpen(HttpServletRequest request, HttpServletResponse response){
        String[] parameters = new String[2];
        if (!validUsernamePostid(request, parameters)) {
            return;
        }
        String username = parameters[0];
        String postid = parameters[1];
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        Blog[] a_blog = new Blog[1];
        if (!retrivePost(username, postid, a_blog, request)) {
            request.setAttribute("status", "SQL execution Error!");
            return;
        }
        Blog blog = a_blog[0];
        if (blog == null) {
            blog = new Blog();
            java.sql.Timestamp currStamp = getCurrTime();
            blog.username = username;
            blog.postid = Integer.parseInt(postid);
            blog.title = "";
            blog.body = "";
            blog.created = String.valueOf(currStamp);
            blog.modified = String.valueOf(currStamp);
        }
        
        if (title != null) {
            blog.title = title;
        }
        if (body != null) {
            blog.body = body;
        }

        request.setAttribute("blog", blog);
        request.setAttribute("status", "pass");
        return;
    }


    public void handlePreview(HttpServletRequest request, HttpServletResponse response){
        String[] parameters = new String[2];
        if (!validUsernamePostid(request, parameters)) {
            return;
        }
        String username = parameters[0];
        String postid = parameters[1];
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        if(title == null || body == null){
            request.setAttribute("get","no title or body");
            Blog[] a_blog = new Blog[1];
            if (!retrivePost(username, postid, a_blog, request)) {
                request.setAttribute("status", "SQL execution Error!");
                return;
            }
            Blog blog = a_blog[0];
            if (blog == null) {
                request.setAttribute("get","no title or body; db no match");
                if (title == null) {
                    title = "";
                }
                if (body == null) {
                    body = "";
                }
            } else {
                if (title == null) {
                    title = blog.title;
                }
                if (body == null) {
                    body = blog.body;
                }
            }
        }
        request.setAttribute("title_h", title);
        request.setAttribute("body_h", body);
        // valid request
        Parser parser = Parser.builder().build();
        Node titleN = parser.parse(title);
        Node bodyN = parser.parse(body);
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();
        String title_html = renderer.render(titleN);
        String body_html = renderer.render(bodyN);
        request.setAttribute("title_html", title_html);
        request.setAttribute("body_html", body_html);
        request.setAttribute("status", "pass");
        return;
    }


    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        String[] parameters = new String[2];
        if (!validUsernamePostid(request, parameters)) {
            return;
        }
        String username = parameters[0];
        String postid = parameters[1];
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        if(title == null){
            title = "";
        }
        if(body == null){
            body = "";
        }
        // valid request
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            int postId = Integer.parseInt(postid);
            java.sql.Timestamp currStamp = getCurrTime();
            String sql = "UPDATE Posts SET title = (?), body = (?), modified = (?) WHERE username = ? AND postid = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, body);
            pst.setTimestamp(3, currStamp);
            pst.setString(4, username);
            pst.setInt(5, postId);
            int num = pst.executeUpdate();
            // System.out.println("--- "+num);
            if (num == 0) {
                postId = updateNextid(username, -postId);
                sql = "INSERT INTO Posts (username, postid, title, body, modified, created) VALUES (?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                pst.setInt(2, postId);
                pst.setString(3, title);
                pst.setString(4, body);
                pst.setTimestamp(5, currStamp);
                pst.setTimestamp(6, currStamp);
                pst.executeUpdate();
            } else {
                updateNextid(username, postId);
            }
            request.setAttribute("status", "pass");
        } catch (SQLException ex){
            request.setAttribute("status", "SQL execution Error!");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }


    public void handleDelete(HttpServletRequest request, HttpServletResponse response){
        String[] parameters = new String[2];
        if (!validUsernamePostid(request, parameters)) {
            return;
        }
        String username = parameters[0];
        String postid = parameters[1];
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        if(title == null){
            title = "";
        }
        if(body == null){
            body = "";
        }
        // valid request
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            String sql = "DELETE FROM Posts WHERE username = ? AND postid = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setInt(2, Integer.parseInt(postid));
            int num = pst.executeUpdate();
            request.setAttribute("status", "pass");
        } catch (SQLException ex){
            request.setAttribute("status", "SQL execution Error!");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }

    private boolean retrivePost (String username, String postid, Blog[] res, HttpServletRequest request) {
        res[0] = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            String sql = "SELECT * FROM Posts WHERE username = ? AND postid = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setInt(2, Integer.parseInt(postid));
            rs = pst.executeQuery();
            if(rs.next()){
                request.setAttribute("sql", "have data");
                res[0] = new Blog();
                res[0].username = rs.getString("username");
                res[0].postid = rs.getInt("postid");
                res[0].title = rs.getString("title");
                res[0].body = rs.getString("body");
                res[0].created = rs.getString("created");
                res[0].modified = rs.getString("modified");
            }
        } catch (SQLException ex){
            SQLException_Handle(ex);
            return false;
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return true;
    }

    private int updateNextid (String username, int postid) {
        if(!nextidMap.containsKey(username)){
            nextidMap.put(username, 1);
            return 1;
        } else if (postid >0) {                     // if get a id can be find in db
            int curMax = nextidMap.get(username);
            if (postid > curMax) {
                postid = curMax+1;
                nextidMap.put(username, postid);
            }
            return postid;
        } else {                                    // if get a id does not exist in db, use max
            int curMax = nextidMap.get(username);
            postid = curMax+1;
            nextidMap.put(username, postid);
            return postid;
        }
    }

    private boolean validUsernamePostid (HttpServletRequest request, String[] parameters) {
        // username is null
        String username = request.getParameter("username");
        if (username == null || username.trim().length() == 0) {
            request.setAttribute("status", "err: username invalid!");
            return false;
        }
        parameters[0] = username;
        
        if (parameters.length > 1) {
            String postid = request.getParameter("postid");
            if(postid == null || postid.trim().length() == 0){
                request.setAttribute("status", "err: postid invalid!");
                return false;
            }
            if (!Pattern.matches("-?[0-9]+", postid)) {
                request.setAttribute("status", "err: postid can only be numbers!");
                return false;
            }
            int num = Integer.parseInt(postid);
            if (num < 0) {
                return false;
            }
            parameters[1] = postid;
        }
        return true;
    }

    private void SQLException_Handle (SQLException ex) {
        System.out.println("SQLException caught");
        System.out.println("---");
        while ( ex != null ) {
            System.out.println("Message   : " + ex.getMessage());
            System.out.println("SQLState  : " + ex.getSQLState());
            System.out.println("ErrorCode : " + ex.getErrorCode());
            System.out.println("---");
            ex = ex.getNextException();
        }
    }

    private java.sql.Timestamp getCurrTime() {
        Calendar cld = Calendar.getInstance();
        java.util.Date now = cld.getTime();
        java.sql.Timestamp currStamp = new java.sql.Timestamp(now.getTime());
        return currStamp;
    }
}
