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
            if(!statusCode.equals("o0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handlePreview(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("p0")){
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
            if(!statusCode.equals("o0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handlePreview(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("p0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/preview.jsp").forward(request, response);
            }
        } else if(action.equals("save")){
            handleSave(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("s0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            } 
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("delete")){
            handleDelete(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("d0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            handleList(request, response);
            statusCode = String.valueOf(request.getAttribute("status"));
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("status", "invalid-Post");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }


    /*
    Method to handle open request
    Invalid condition: 
    o0 stands for successfully extracted from database, or, postid <= 0, coresponding condition is "new post"
    o1 stands for username missing
    o2 stands for postid missing
    o3 stands for database retrive exception
    */
    public void handleOpen(HttpServletRequest request, HttpServletResponse response){
        Object usrnme = request.getParameter("username");
        if(username == null){
            request.setAttribute("status", "s1");
            return;
        }
        String username = String.valueOf(usrnme);
        String postid = String.valueOf(request.getParameter("postid"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "o1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "o2");
            return;
        }
        boolean matches = Pattern.matches("-?[0-9]+", postid);
        if(!matches){
            request.setAttribute("status", "o2");
            return;
        }

        if(Integer.parseInt(postid) <= 0){
            request.setAttribute("status", "o0");
            if(!nextidMap.containsKey(username)){
                nextidMap.put(username, 1);
            }
            java.sql.Timestamp currStamp = getCurrTime();
            int nextid = nextidMap.get(username);
            nextidMap.put(username, nextid+1);

            Blog newBlog = new Blog();
            newBlog.username = username;
            newBlog.postid = nextid;
            newBlog.title = "";
            newBlog.body = "";
            newBlog.created = String.valueOf(currStamp);
            newBlog.modified = String.valueOf(currStamp);
            request.setAttribute("blog", newBlog);
            request.setAttribute("newPost", "true");
            return;
        }

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
            //  "Cannot find JDBC Driver."
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
            Blog blog = null;
            if(rs.next()){
                blog = new Blog();
                blog.username = rs.getString("username");
                blog.postid = rs.getInt("postid");
                blog.title = rs.getString("title");
                blog.body = rs.getString("body");
                blog.created = rs.getString("created");
                blog.modified = rs.getString("modified");
                request.setAttribute("newPost", "false");
            }
            if(blog == null){
                if(!nextidMap.containsKey(username)){
                    nextidMap.put(username, 1);
                }
                java.sql.Timestamp currStamp = getCurrTime();
                int nextid = nextidMap.get(username);
                nextidMap.put(username, nextid+1);
                blog = new Blog();
                blog.username = username;
                blog.postid = nextid;
                blog.title = "";
                blog.body = "";
                blog.created = String.valueOf(currStamp);
                blog.modified = String.valueOf(currStamp);
                request.setAttribute("newPost", "true");
            }
            request.setAttribute("status", "o0");
            request.setAttribute("blog", blog);
            
        } catch (SQLException ex){
            request.setAttribute("status","o3");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }


    /*
    Function to handle list request
    L0 stands for successful get a list of blog from database
    L1 stands for username missing
    L2 stands for exception happening when getting data from database
    */
    public void handleList(HttpServletRequest request, HttpServletResponse response){
        Object usrnme = request.getParameter("username");
        if(username == null){
            request.setAttribute("status", "s1");
            return;
        }
        String username = String.valueOf(usrnme);
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "L1");
            return;
        }
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("cannot connect to jdbc");
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
            request.setAttribute("blogs", blogs);
            request.setAttribute("status", "L0");
        } catch (SQLException ex){
            request.setAttribute("status", "L2");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }


    /*
    p0: correct parsed all the parameters; 
    p1: username missing; 
    p2: postid missing
    */
    public void handlePreview(HttpServletRequest request, HttpServletResponse response){
        Object usrnme = request.getParameter("username");
        if(username == null){
            request.setAttribute("status", "s1");
            return;
        }
        String username = String.valueOf(usrnme);
        String postid = String.valueOf(request.getParameter("postid"));
        String title = String.valueOf(request.getParameter("title"));
        String body = String.valueOf(request.getParameter("body"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "p1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "p2");
            return;
        }
        boolean matches = Pattern.matches("-?[0-9]+", postid);
        if(!matches){
            request.setAttribute("status", "p2");
            return;
        }
        Parser parser = Parser.builder().build();
        Node titleN = parser.parse(title);
        Node bodyN = parser.parse(body);
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();
        String title_html = renderer.render(titleN);
        String body_html = renderer.render(bodyN);
        request.setAttribute("title_html", title_html);
        request.setAttribute("body_html", body_html);
        request.setAttribute("status", "p0");
        return;
    }


    /*
    d0: successfully executed
    d1: username missing
    d2: postid missing
    d3: exception happened when doing db operations
    */
    public void handleDelete(HttpServletRequest request, HttpServletResponse response){
        Object usrnme = request.getParameter("username");
        if(username == null){
            request.setAttribute("status", "s1");
            return;
        }
        String username = String.valueOf(usrnme);
        String postid = String.valueOf(request.getParameter("postid"));
        String title = String.valueOf(request.getParameter("title"));
        String body = String.valueOf(request.getParameter("body"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "d1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "d2");
            return;
        }
        // Condition when postid < 0
        boolean matches = Pattern.matches("-?[0-9]+", postid);
        if(!matches){
            request.setAttribute("status", "d2");
            return;
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
            request.setAttribute("status", "d0");
        } catch (SQLException ex){
            request.setAttribute("status", "d3");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }

    /*
    s0: successfully executed
    s1: username invalid or missing
    s2: postid missing
    s3: exception happened when doing db operations
    */
    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        Object usrnme = request.getParameter("username");
        if(username == null){
            request.setAttribute("status", "s1");
            return;
        }
        String username = String.valueOf(usrnme);
        String postid = String.valueOf(request.getParameter("postid"));
        String title = String.valueOf(request.getParameter("title"));
        String body = String.valueOf(request.getParameter("body"));
        String npost = request.getParameter("newPost");
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "s1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "s2");
            return;
        }
        // invalid postid
        boolean matches = Pattern.matches("-?[0-9]+", postid);
        if(!matches){
            request.setAttribute("status", "s2");
            return;
        }
        // valid request
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex){
            ex.printStackTrace();
            //  "Cannot find JDBC Driver."
        }
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            int postId = Integer.parseInt(postid);

            boolean newPost = false;
            if(npost != null && npost.equals("true")){
                newPost = true;
            }
            java.sql.Timestamp currStamp = getCurrTime();
            String sql = null;
            if(newPost){
                sql = "INSERT INTO Posts (username, postid, title, body, modified, created) VALUES (?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                pst.setInt(2, postId);
                pst.setString(3, title);
                pst.setString(4, body);
                pst.setTimestamp(5, currStamp);
                pst.setTimestamp(6, currStamp);
            } else {
                sql = "UPDATE Posts SET title = (?), body = (?), modified = (?) WHERE username = ? AND postid = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, title);
                pst.setString(2, body);
                pst.setTimestamp(3, currStamp);
                pst.setString(4, username);
                pst.setInt(5, postId);
            }
            int num = pst.executeUpdate();
            request.setAttribute("status", "s0");
        } catch (SQLException ex){
            request.setAttribute("status", "s3");
            SQLException_Handle(ex);
        } finally {
            try{conn.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
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