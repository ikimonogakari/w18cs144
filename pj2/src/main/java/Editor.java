import java.io.IOException;
import java.sql.* ;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

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
        String action = String.valueOf(request.getAttribute("action"));
        if(action.equals("open")){
            handleOpen(request, response);
            String statusCode = request.getAttribute("status");
            if(!statusCode.equals("o0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handleList(request, response);
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
        String action = String.valueOf(request.getAttribute("action"));
        if(action.equals("open")){
            handleOpen(request, response);
            String statusCode = request.getAttribute("status");
            if(!statusCode.equals("o0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
        } else if(action.equals("list")){
            handleList(request, response);
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("preview")){
            handleList(request, response);
            if(!statusCode.equals("p0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/preview.jsp").forward(request, response);
            }
        } else if(action.equals("save")){
            handleSave(request, response);
            if(!statusCode.equals("s0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            } 
            handleList(request, response);
            if(!statusCode.equals("L0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response); 
            } else {
                request.getRequestDispatcher("/list.jsp").forward(request, response);
            }
        } else if(action.equals("delete")){
            handleDelete(request, response);
            if(!statusCode.equals("d0")){
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            handleList(request, response);
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
    o0 stands for successfully extracted from database
    o1 stands for username missing
    o2 stands for postid missing
    o3 stands for postid < 0, coresponding condition is "new post"
    */
    public void handleOpen(HttpServletRequest request, HttpServletResponse response){
        Sting username = String.valueOf(request.getAttribute("username"));
        String postid = String.valueOf(request.getAttribute("postid"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "o1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "o2");
            return;
        }

        if(Integer.parseInt(postid) < 0){
            request.setAttribute("status", "o3");
            if(!nextidMap.containsKey(username)){
                nextidMap.put(username, 1);
            }
            int nextid = nextidMap.get(username);
            nextidMap.put(username, nextid+1);
            request.setAttribute("nextid", nextid);
            return;
        }

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex){
            e.printStackTrace("Cannot find JDBC Driver.");
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
                blog.body = rs.getString("title");
                blog.created = rs.getString("created");
                blog.modified = rs.getString("modified");
            }
            request.setAttribute("status", "o0");
            request.setAttribute("blog", blog);
        } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ) {
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
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
        String username = String.valueOf(request.getAttribute("username"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "L1");
            return;
        }
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex){
            e.printStackTrace("Cannot find JDBC Driver.");
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
            if(rs.next()){
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
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ) {
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
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
        String username = String.valueOf(request.getAttribute("username"));
        String postid = String.valueOf(request.getAttribute("postid"));
        String title = String.valueOf(request.getAttribute("title"));
        String body = String.valueOf(request.getAttribute("body"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "p1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "p2");
            return;
        }
        Blog blog = new Blog();
        blog.username = username;
        blog.postid = Integer.parseInt(postid);
        blog.title = title;
        blog.body = body;
        request.setAttribute("blog", blog);
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
        String username = String.valueOf(request.getAttribute("username"));
        String postid = String.valueOf(request.getAttribute("postid"));
        String title = String.valueOf(request.getAttribute("title"));
        String body = String.valueOf(request.getAttribute("body"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "d1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "d2");
            return;
        }
        // Condition when postid < 0
        
        // valid request
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex){
            e.printStackTrace("Cannot find JDBC Driver.");
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            String sql = "DELETE * FROM Posts WHERE username = ? AND postid = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setInt(2, Integer.parseInt(postid))
            rs = pst.executeQuery();
            request.setAttribute("status", "d0");
        } catch (SQLException ex){
            request.setAttribute("status", "d3");
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ) {
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }

    /*
    s0: successfully executed
    s1: username missing
    s2: postid missing
    s3: exception happened when doing db operations
    */
    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        String username = String.valueOf(request.getAttribute("username"));
        String postid = String.valueOf(request.getAttribute("postid"));
        String title = String.valueOf(request.getAttribute("title"));
        String body = String.valueOf(request.getAttribute("body"));
        if(username == null || username.trim().length() == 0){
            request.setAttribute("status", "s1");
            return;
        }
        if(postid == null || postid.trim().length() == 0){
            request.setAttribute("status", "s2");
            return;
        }
        // invalid postid

        // valid request
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex){
            e.printStackTrace("Cannot find JDBC Driver.");
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex){
            e.printStackTrace("Cannot find JDBC Driver.");
        }
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            conn = DriverManager.getConnection(DATABASE_HOST, "cs144", "");
            int postId = Integer.parseInt(postid);
            boolean newPost = false;
            if(!nextidMap.containsKey(username)){
                nextidMap.put(username, 1);
            }

            if(postId > nextidMap.get(username) || postId <= 0){
                postId = nextidMap.get(username);
                nextidMap.put(username, postId+1);
                newPost = true;
            }
            Calendar cld = Calendar.getInstance();
            java.util.Date now = cld.getTime();
            java.sql.Timestamp currStamp = new java.util.Timestamp(now.getTime());
            String sql = null;
            if(newPost){
                sql = "INSERT INTO Posts (username, postid, title, body, modified, created) VALUES (?, ?, ?, ?, ?,)";
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
            rs = pst.executeQuery();
            request.setAttribute("status", "s0");
        } catch (SQLException ex){
            request.setAttribute("status", "s3");
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ) {
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } finally {
            try{conn.close();} catch (Exception e){}
            try{rs.close();} catch (Exception e){}
            try{pst.close();} catch (Exception e){}
        }
        return;
    }
}

class Blog{
    String username;
    int postid;
    String title;
    String body;
    String created;
    String modified;
    public Blog();
}


