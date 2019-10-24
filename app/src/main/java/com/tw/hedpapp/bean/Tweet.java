package com.tw.hedpapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Date 2019/10/18
 * @Time 20:57
 */
public class Tweet implements Serializable {
    @Override
    public String toString() {
        return "Tweet{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                ", error='" + error + '\'' +
                ", _$UnknownError137='" + _$UnknownError137 + '\'' +
                ", images=" + images +
                ", comments=" + comments +
                '}';
    }

    /**
     * content : 沙发！
     * images : [{"url":"https://encrypted-tbn1.gstatic
     * .com/images?q=tbn:ANd9GcRDy7HZaHxn15wWj6pXE4uMKAqHTC_uBgBlIzeeQSj2QaGgUzUmHg"},{"url
     * ":"https://encrypted-tbn1.gstatic
     * .com/images?q=tbn:ANd9GcTlJRALAf-76JPOLohBKzBg8Ab4Q5pWeQhF5igSfBflE_UYbqu7"},{"url":"http
     * ://i.ytimg.com/vi/rGWI7mjmnNk/hqdefault.jpg"}]
     * sender : {"username":"jport","nick":"Joe Portman","avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}
     * comments : [{"content":"Good.","sender":{"username":"outman","nick":"Super hero",
     * "avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}},{"content
     * ":"Like it too","sender":{"username":"inman","nick":"Doggy Over",
     * "avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}}]
     * error : losted
     * unknown error : STARCRAFT2
     */

    private String content;
    private SenderBean sender;
    private String error;
    @SerializedName("unknown error")
    private String _$UnknownError137; // FIXME check this code
    private List<ImagesBean> images;
    private List<CommentsBean> comments;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SenderBean getSender() {
        return sender;
    }

    public void setSender(SenderBean sender) {
        this.sender = sender;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String get_$UnknownError137() {
        return _$UnknownError137;
    }

    public void set_$UnknownError137(String _$UnknownError137) {
        this._$UnknownError137 = _$UnknownError137;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class SenderBean {
        @Override
        public String toString() {
            return "SenderBean{" +
                    "username='" + username + '\'' +
                    ", nick='" + nick + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }

        /**
         * username : jport
         * nick : Joe Portman
         * avatar : https://encrypted-tbn3.gstatic
         * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w
         */

        private String username;
        private String nick;
        private String avatar;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class ImagesBean {
        @Override
        public String toString() {
            return "ImagesBean{" +
                    "url='" + url + '\'' +
                    '}';
        }

        /**
         * url : https://encrypted-tbn1.gstatic
         * .com/images?q=tbn:ANd9GcRDy7HZaHxn15wWj6pXE4uMKAqHTC_uBgBlIzeeQSj2QaGgUzUmHg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class CommentsBean {
        /**
         * content : Good.
         * sender : {"username":"outman","nick":"Super hero","avatar":"https://encrypted-tbn3
         * .gstatic.com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}
         */

        private String content;
        private SenderBeanX sender;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public SenderBeanX getSender() {
            return sender;
        }

        public void setSender(SenderBeanX sender) {
            this.sender = sender;
        }

        public static class SenderBeanX {
            /**
             * username : outman
             * nick : Super hero
             * avatar : https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w
             */

            private String username;
            private String nick;
            private String avatar;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
