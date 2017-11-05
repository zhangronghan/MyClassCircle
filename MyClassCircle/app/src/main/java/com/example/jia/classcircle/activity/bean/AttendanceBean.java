package com.example.jia.classcircle.activity.bean;

import java.util.List;

/**
 * Created by jia on 2017/10/17.
 */

public class AttendanceBean {

    /**
     * data : {"updatedAt":"2017-10-17 16:11:38","identity":"管理员","objectId":"424f7c3d02","createdAt":"2017-10-17 12:19:30","contentList":[{"content":"同学们，请赶快过来上英语课，我要考勤","time":"2017/10/17    13:4"},{"content":"自习考勤，同学们赶快。","time":"2017/10/17    14:33"},{"content":"软件测试开始了，快来点名","time":"2017/10/17    15:6"},{"content":"自习考勤/上课考勤","time":"2017/10/17    15:14"},{"content":"自习考勤123456","time":"2017/10/17    15:31"},{"content":"上课考勤666","time":"2017/10/17    15:40"},{"content":"赶快上课","time":"2017/10/17    15:48"},{"content":"大家好","time":"2017/10/17    15:57"},{"content":"额","time":"2017/10/17    15:58"},{"content":"兄弟们好","time":"2017/10/17    16:11"}],"userName":"0","className":"2015软件1","stuAboutCheckList":[]}
     * action : updateRow
     * objectId : 424f7c3d02
     * tableName : Attendance
     * appKey : 9db39777bc7ec0846df5c87480543a31
     */

    private DataBean data;
    private String action;
    private String objectId;
    private String tableName;
    private String appKey;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public static class DataBean {
        /**
         * updatedAt : 2017-10-17 16:11:38
         * identity : 管理员
         * objectId : 424f7c3d02
         * createdAt : 2017-10-17 12:19:30
         * contentList : [{"content":"同学们，请赶快过来上英语课，我要考勤","time":"2017/10/17    13:4"},{"content":"自习考勤，同学们赶快。","time":"2017/10/17    14:33"},{"content":"软件测试开始了，快来点名","time":"2017/10/17    15:6"},{"content":"自习考勤/上课考勤","time":"2017/10/17    15:14"},{"content":"自习考勤123456","time":"2017/10/17    15:31"},{"content":"上课考勤666","time":"2017/10/17    15:40"},{"content":"赶快上课","time":"2017/10/17    15:48"},{"content":"大家好","time":"2017/10/17    15:57"},{"content":"额","time":"2017/10/17    15:58"},{"content":"兄弟们好","time":"2017/10/17    16:11"}]
         * userName : 0
         * className : 2015软件1
         * stuAboutCheckList : []
         */

        private String updatedAt;
        private String identity;
        private String objectId;
        private String createdAt;
        private String userName;
        private String className;
        private List<ContentListBean> contentList;
        private List<?> stuAboutCheckList;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<ContentListBean> getContentList() {
            return contentList;
        }

        public void setContentList(List<ContentListBean> contentList) {
            this.contentList = contentList;
        }

        public List<?> getStuAboutCheckList() {
            return stuAboutCheckList;
        }

        public void setStuAboutCheckList(List<?> stuAboutCheckList) {
            this.stuAboutCheckList = stuAboutCheckList;
        }

        public static class ContentListBean {
            /**
             * content : 同学们，请赶快过来上英语课，我要考勤
             * time : 2017/10/17    13:4
             */

            private String content;
            private String time;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
