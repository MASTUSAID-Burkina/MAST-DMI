package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: PersonType
 *
 * @author Shruti.Thakur
 */
@Entity
@Table(name = "la_partygroup_persontype")
public class PersonType implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//    private static final long serialVersionUID = 1L;
    public static int TYPE_NATURAL = 1;
    public static int TYPE_NON_NATURAL = 2;
    public static int TYPE_OWNER = 3;
    public static int TYPE_ADMINISTRATOR = 4;
    public static int TYPE_GUARDIAN = 5;
    
    @Id
    @SequenceGenerator(name="pk_la_persontype",sequenceName="la_partygroup_persontype_persontypeid_seq", allocationSize=1) 
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_la_persontype") 
	@Column(name="persontypeid")
	private Integer persontypeid;

	@Column(name="isactive")
	private Boolean isactive;

	@Column(name="persontype")
	private String persontype;

	@Column(name="persontype_en")
	private String persontypeEn;

	public Integer getPersontypeid() {
		return persontypeid;
	}

	public void setPersontypeid(Integer persontypeid) {
            this.persontypeid = persontypeid;
	}

	public Boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}

	public String getPersontypeEn() {
		return persontypeEn;
	}

	public void setPersontypeEn(String persontypeEn) {
		this.persontypeEn = persontypeEn;
	}
}
