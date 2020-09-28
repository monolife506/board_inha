package board.like;

public class Like
{
    private String user;
    private int post_idx;
    private boolean toggle;

    public String getUser() {
        return user;
    }

    public int getPost_idx() {
        return post_idx;
    }

    public boolean getToggle() {
        return toggle;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPost_idx(int post_idx) {
        this.post_idx = post_idx;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }
}
