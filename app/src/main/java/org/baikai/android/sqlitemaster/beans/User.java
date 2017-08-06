package org.baikai.android.sqlitemaster.beans;


import org.baikai.android.sqlitemaster.annotation.ColumnType;
import org.baikai.android.sqlitemaster.sql.JDBCType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "USER")
public class User implements Serializable {

    @Id
    @Column(name = "ID")
    @ColumnType(jdbcType = JDBCType.BIGINT)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "UNAME", length = 32)
    private String uname;

    /**
     * 密码
     */
    @Column(name = "PASSWORD", length = 64)
    private String passwd;

    /**
     * 手机号
     */
    @Column(name = "MOBILE")
    private String mobile;

    /**
     * 邮箱
     */
    @Column(name = "EMAIL", length = 64)
    private String email;

    /**
     * 用户状态
     */
    @Column(name = "STATE")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer state;

    /**
     * 公钥
     */
    @Column(name = "PUBLIC_KEY", length = 1024)
    private String publicKey;

    /**
     * 用户注册时间
     */
//    @Transient
    private Date regdate;

    @Transient
    private Integer extralimit;

//    @Column("login_time")
//    private Date loginTime;

//    @Column("last_login_time")
//    private Date lastLoginTime;

    @Column(name = "STATUS")
    @ColumnType(jdbcType = JDBCType.INTEGER)
    private Integer status;

    @Column(name = "flag")
    private String flag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Integer getExtralimit() {
        return extralimit;
    }

    public void setExtralimit(Integer extralimit) {
        this.extralimit = extralimit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", passwd='" + passwd + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", state=" + state +
                ", publicKey='" + publicKey + '\'' +
                ", regdate=" + regdate +
                ", extralimit=" + extralimit +
                ", status=" + status +
                ", flag='" + flag + '\'' +
                '}';
    }
}
