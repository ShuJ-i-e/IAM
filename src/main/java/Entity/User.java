package Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User{
    
	@Id
	@Column("id")
    private Long id;
	
	@Column("username")
	private String username;
	
	@Column("firstName")
    private String firstName;
	
	@Column("lastName")
    private String lastName;
	
	@Column("email")
    private String email;
	
	@Column("about")
    private String about;
	
	@Column("roles")
    private String roles;
	
	@Column("languages")
    private String languages;
	
	@Column("skills")
    private String skills;
	
	@Column("project_experiences")
    private String project_experiences;
	
	@Column("assignments")
    private String assignments;
	
	@Column("profilePic")
    private String profilePic;
	
	@Column("password")
	private String password;
	
	@Column("reset_password_token")
	private String resetPasswordToken;
	 
	@Column("verificationToken")
	private String verificationToken;
	
	@Column("verificationTokenCreationTime")
	private LocalDateTime verificationTokenCreationTime;
	
	@Column("verified")
	private boolean verified;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastname() {
		return lastName;
	}
	public void setLastname(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getLanguages() {
		return languages;
	}
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getProject_experiences() {
		return project_experiences;
	}
	public void setProject_experiences(String project_experiences) {
		this.project_experiences = project_experiences;
	}
	public String getAssignments() {
		return assignments;
	}
	public void setAssignments(String assignments) {
		this.assignments = assignments;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}
	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
	public String getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	public LocalDateTime getVerificationTokenCreationTime() {
		return verificationTokenCreationTime;
	}
	public void setVerificationTokenCreationTime(LocalDateTime verificationTokenCreationTime) {
		this.verificationTokenCreationTime = verificationTokenCreationTime;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

     
}
