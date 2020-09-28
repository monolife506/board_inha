package board.post;

import java.sql.Timestamp;

public class Post
{
    private int idx;

    private String title;
    private String author;
    private int board;
    private String img;
    private String sub_img;
    private String content;
    private Timestamp updated_at;

    private int reply_cnt = 0;
    private int upvote_cnt = 0;
    private int read_cnt = 0;

    private boolean is_deleted = false;

    public int getIdx() {
        return idx;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() { return author; }

    public int getBoard() {
        return board;
    }

    public String getImg() { return img; }

    public String getSub_img() { return sub_img; }

    public String getContent() {
        return content;
    }

    public Timestamp getUpdated_at() { return updated_at; }

    public int getReply_cnt() {
        return reply_cnt;
    }

    public int getUpvote_cnt() {
        return upvote_cnt;
    }

    public int getRead_cnt() {
        return read_cnt;
    }

    public boolean getIs_deleted() {return is_deleted; }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) { this.author = author; }

    public void setBoard(int board) {
        this.board = board;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSub_img(String sub_img) { this.sub_img = sub_img; }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdated_at(Timestamp updated_at) { this.updated_at = updated_at; }

    public void setReply_cnt(int reply_cnt) {
        this.reply_cnt = reply_cnt;
    }

    public void setUpvote_cnt(int upvote_cnt) {
        this.upvote_cnt = upvote_cnt;
    }

    public void setRead_cnt(int read_cnt) {
        this.read_cnt = read_cnt;
    }

    public void setIs_deleted(boolean is_deleted) { this.is_deleted = is_deleted; }
}
