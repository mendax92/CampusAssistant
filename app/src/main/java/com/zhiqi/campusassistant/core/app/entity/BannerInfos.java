package com.zhiqi.campusassistant.core.app.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

public class BannerInfos implements BaseJsonData {
    public static final int flag_app_index = 1;
    public static final int flag_qrcode_index = 2;

    /**
     * error_code : 0
     * message : success
     * data : [{"id":1,"flag":1,"imgUrl":"http://api.cloupus.com/advertise/004.jpg","type":1,"url":"http://api.cloupus.com/advertise/004-1.jpg","status":1,"createTime":"2019-09-19 08:48:11"},{"id":3,"flag":1,"imgUrl":"http://api.cloupus.com/advertise/002.jpg","type":1,"url":"https://wx.kingpointshow.com/html/kpact/sop/zy/xyxbk/index.html?&subchannel=dstg&dealoper=001&citycode=758&takeDelivery=2","status":1,"createTime":"2019-07-30 16:58:21","remark":""},{"id":5,"flag":1,"imgUrl":"http://api.cloupus.com/advertise/003.jpg","type":1,"url":"https://mp.weixin.qq.com/s/J0UyHM7WqCKA5bolhYrY8g","status":1,"createTime":"2019-09-05 15:12:34"}]
     */

    private List<BannerInfo> data;

    public List<BannerInfo> getData() {
        return data;
    }

    public void setData(List<BannerInfo> data) {
        this.data = data;
    }

    public static class BannerInfo implements BaseJsonData  {
        /**
         * id : 1
         * flag : 1
         * imgUrl : http://api.cloupus.com/advertise/004.jpg
         * type : 1
         * url : http://api.cloupus.com/advertise/004-1.jpg
         * status : 1
         * createTime : 2019-09-19 08:48:11
         * remark :
         */

        private int id;
        private int flag;
        private String imgUrl;
        private int type;
        private String url;
        private int status;
        private String createTime;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
