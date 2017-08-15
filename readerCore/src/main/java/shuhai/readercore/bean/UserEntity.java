package shuhai.readercore.bean;

/**
 * @author 55345364
 * @date 2017/8/14.
 */

public class UserEntity {


    /**
     * code : 0000
     * message : {"uid":"173434","uname":"wangxu","name":"wangxu","pass":"e10adc3949ba59abbe56e057f20f883e","email":"1471999476@qq.com","avatar":"http://www.shuhai.com/files/system/avatar/173/173434l.jpg","egold":"8104.9","egold_ios":0,"score":"12308","experience":"1315","dayvotes":"19","daypollvotes":0,"monthvotes":0,"monthpollvotes":0,"vip":"3","esilver":"1143.55","freelycoin":"7.9","issign":"0","honor":"10","mobile":""}
     */

    private String code;
    private MessageBean message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * uid : 173434
         * uname : wangxu
         * name : wangxu
         * pass : e10adc3949ba59abbe56e057f20f883e
         * email : 1471999476@qq.com
         * avatar : http://www.shuhai.com/files/system/avatar/173/173434l.jpg
         * egold : 8104.9
         * egold_ios : 0
         * score : 12308
         * experience : 1315
         * dayvotes : 19
         * daypollvotes : 0
         * monthvotes : 0
         * monthpollvotes : 0
         * vip : 3
         * esilver : 1143.55
         * freelycoin : 7.9
         * issign : 0
         * honor : 10
         * mobile :
         */

        private String uid;
        private String uname;
        private String name;
        private String pass;
        private String email;
        private String avatar;
        private String egold;
        private int egold_ios;
        private String score;
        private String experience;
        private String dayvotes;
        private int daypollvotes;
        private int monthvotes;
        private int monthpollvotes;
        private String vip;
        private String esilver;
        private String freelycoin;
        private String issign;
        private String honor;
        private String mobile;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getEgold() {
            return egold;
        }

        public void setEgold(String egold) {
            this.egold = egold;
        }

        public int getEgold_ios() {
            return egold_ios;
        }

        public void setEgold_ios(int egold_ios) {
            this.egold_ios = egold_ios;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getDayvotes() {
            return dayvotes;
        }

        public void setDayvotes(String dayvotes) {
            this.dayvotes = dayvotes;
        }

        public int getDaypollvotes() {
            return daypollvotes;
        }

        public void setDaypollvotes(int daypollvotes) {
            this.daypollvotes = daypollvotes;
        }

        public int getMonthvotes() {
            return monthvotes;
        }

        public void setMonthvotes(int monthvotes) {
            this.monthvotes = monthvotes;
        }

        public int getMonthpollvotes() {
            return monthpollvotes;
        }

        public void setMonthpollvotes(int monthpollvotes) {
            this.monthpollvotes = monthpollvotes;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getEsilver() {
            return esilver;
        }

        public void setEsilver(String esilver) {
            this.esilver = esilver;
        }

        public String getFreelycoin() {
            return freelycoin;
        }

        public void setFreelycoin(String freelycoin) {
            this.freelycoin = freelycoin;
        }

        public String getIssign() {
            return issign;
        }

        public void setIssign(String issign) {
            this.issign = issign;
        }

        public String getHonor() {
            return honor;
        }

        public void setHonor(String honor) {
            this.honor = honor;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
