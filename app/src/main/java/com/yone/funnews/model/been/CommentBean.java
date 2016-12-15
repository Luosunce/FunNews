package com.yone.funnews.model.been;

import java.util.List;

/**
 * Created by Yoe on 2016/10/15.
 */

public class CommentBean {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments(){return comments;}

    public void setComments(List<CommentsBean> comments){this.comments = comments;}

    public static class CommentsBean{

        private String author;
        private String content;
        private String avatar;
        private int time;
        private ReplyToBean reply_to;
        private int id;
        private int likes;

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }


        public static class ReplyToBean{

            private String content;
            private int status;
            private int id;
            private String author;
            private int expandState = 0;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getExpandState() {
                return expandState;
            }

            public void setExpandState(int expandState) {
                this.expandState = expandState;
            }

        }
    }
}
