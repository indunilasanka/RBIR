package aztec.rbir_database.Entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "public_user")
public class PublicUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4706834137488474753L;

	@Id
    @Column(name= "email")
    private String email;
	
    @Column(name = "username")
    private String username;
    
    @Column
	@CreationTimestamp
	private Timestamp createDateTime;

    @Column(name= "reputation")
    private int reputation;
    
    @Column(name= "image_url")
    private String image;

    
    
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}   
}
