package org.baikai.android.sqlitemaster.beans;


import org.baikai.android.sqlitemaster.annotation.ColumnType;
import org.baikai.android.sqlitemaster.sql.JDBCType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by baikai on 2017/4/20.
 */

@Table(name = "USER_INFO")
public class UserInfo implements Serializable {

    @Id
    @Column(name = "ID")
    @ColumnType(jdbcType = JDBCType.BIGINT)
    private Long id;

    @Column(name = "USERID")
    @ColumnType(jdbcType = JDBCType.BIGINT)
    private Long userid;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NICK_NAME")
    private String nickname;

    @Column(name = "SEX")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer sex;

    @Transient
    private String strSex;

    @Column(name = "WEIXIN")
    private String weixin;

    @Column(name = "QQ")
    private String qq;

    @Column(name = "IDCARD")
    private String idcard;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "PATH", length = 256)
    private String path;

    @Column(name = "AUTH_STATE")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer authState;

    @Column(name = "AUTH_LIMITS")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer authLimits;

    @Column(name = "INVITER")
    private String inviter;

    @Column(name = "URL", length = 128)
    private String url;

    @Column(name = "GRADE")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer grade;

    @Column(name = "AUTH_REMARK")
    private String authRemark;

    // 推荐人编号
    @Column(name = "INVITED")
    @ColumnType(jdbcType = JDBCType.BIGINT)
    private Long invited;

    @Column(name = "IDCARD_TYPE")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer idcardType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "*" + name.substring(1);
    }

    public void setNameWithNoHide(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
        switch (this.sex) {
            case 0:
                this.strSex = "保密";
                break;
            case 1:
                this.strSex = "女";
                break;
            case 2:
                this.strSex = "男";
                break;
        }
    }

    public String getStrSex() {
        return strSex;
    }

    public void setStrSex(String strSex) {
        this.strSex = strSex;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = UserInfo.hideChar(idcard);
    }

    public void setIdcardWithNoHide(String idcard) {
        this.idcard = idcard;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        this.authState = authState;
    }

    public Integer getAuthLimits() {
        return authLimits;
    }

    public void setAuthLimits(Integer authLimits) {
        this.authLimits = authLimits;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getAuthRemark() {
        return authRemark;
    }

    public void setAuthRemark(String authRemark) {
        this.authRemark = authRemark;
    }

    public Long getInvited() {
        return invited;
    }

    public void setInvited(Long invited) {
        this.invited = invited;
    }

    public Integer getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(Integer idcardType) {
        this.idcardType = idcardType;
    }

    public static String hideChar(String str) {

        if (str != null && str.length() > 2) {

            str = str.trim();

            char[] chs = str.toCharArray();

            String temp = "";

            for (int i = 0; i < chs.length; i++) {
                char c = chs[i];
                if (i == 0 || i == str.length() - 1) {
                    temp += c + "";
                } else {
                    temp += "*";
                }
            }

            str = temp;
        }

        return str;
    }

    @Override
    public String toString() {
        return "UserInfo2{" +
                "id=" + id +
                ", userid=" + userid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", strSex='" + strSex + '\'' +
                ", weixin='" + weixin + '\'' +
                ", qq='" + qq + '\'' +
                ", idcard='" + idcard + '\'' +
                ", image='" + image + '\'' +
                ", path='" + path + '\'' +
                ", authState=" + authState +
                ", authLimits=" + authLimits +
                ", inviter='" + inviter + '\'' +
                ", url='" + url + '\'' +
                ", grade=" + grade +
                ", authRemark='" + authRemark + '\'' +
                ", invited=" + invited +
                '}';
    }
}
