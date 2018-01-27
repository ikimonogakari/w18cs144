package blog;

public class Blog implements Comparable{
    public String username;
    public int postid;
    public String title;
    public String body;
    public String created;
    public String modified;
    public Blog(){};
    @Override
    public int compareTo(Object o)
    { 
        Blog b = (Blog) o;
        return this.postid - b.postid;
    }
}

