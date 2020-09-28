package board.reply;

import java.sql.Timestamp;

public class Reply
{
    private int idx;
    private int post_idx;
    private String author;
    private String content;

    private Timestamp updated_at;
    private boolean is_deleted;

    public int getIdx() {
        return idx;
    }

    public int getPost_idx() {
        return post_idx;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setPost_idx(int post_idx) {
        this.post_idx = post_idx;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
}
