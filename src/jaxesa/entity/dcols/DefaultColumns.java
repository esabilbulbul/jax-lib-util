/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.entity.dcols;

import java.math.BigDecimal;
import jaxesa.persistence.annotations.Basic;
import jaxesa.persistence.annotations.ByUser;
import jaxesa.persistence.annotations.Column;
import jaxesa.persistence.annotations.GeneratedValue;
import jaxesa.persistence.annotations.GenerationType;
import jaxesa.persistence.annotations.Id;
import jaxesa.persistence.annotations.InsertDate;
import jaxesa.persistence.annotations.LastUpdate;
import jaxesa.persistence.annotations.Status;
import jaxesa.persistence.annotations.SysGMT;
import jaxesa.persistence.annotations.Version;
import jaxesa.persistence.annotations.RefId;

/**
 *
 * @author Administrator
 */
public class DefaultColumns
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_UID")
    @Basic(optional = false)
    @Column(name = "UID", nullable = false)
    public Long uid;
    
    @Status
    @Basic(optional = false)
    @Column(name = "STAT", nullable = false)
    public int stat = 1;//default
    
    @InsertDate
    @Basic(optional = false)
    @Column(name = "INSERTDATE", nullable = false)
    public long insertdate;
    
    @LastUpdate
    @Basic(optional = false)
    @Column(name = "LASTUPDATE", nullable = false)
    public long lastupdate;
    
    //@RefId
    //@Column(name = "REFID", length = 40)
    //public String refid;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    public long version;
    
    @ByUser
    @Column(name = "BYUSER", length = 20)
    public String byuser;
    
    @Column(name = "CLIENT_IP", length = 15)
    public String clientIp;
    
    @Column(name = "CLIENT_DTIME")
    public long clientDtime;
    
    @SysGMT
    @Basic(optional = false)
    @Column(name = "SYS_GMT", nullable = false, precision = 4, scale = 2)    
    public BigDecimal sysGmt;
    
}

