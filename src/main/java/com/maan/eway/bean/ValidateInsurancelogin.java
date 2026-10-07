package com.maan.eway.bean;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name = "Validate_Insurance_login_token")
public class ValidateInsurancelogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Lob
	@Column(name = "token", nullable = false)
	private String token;

	@Column(name = "login_user_id", length = 36, nullable = false)
	private String loginUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "issue_at")
	private Date issueAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires")
	private Date expires;

	@Column(name = "code")
	private Integer code;

	@Column(name = "login_history_id")
	private Long loginHistoryId;

	@Column(name = "first_name", length = 100)
	private String firstName;

	@Column(name = "last_name", length = 100)
	private String lastName;

	@Column(name = "loggedin_entity_id")
	private Integer loggedinEntityId;

	@Column(name = "apim_subscription_key", length = 255)
	private String apimSubscriptionKey;

	@Column(name = "industry_type_id")
	private Integer industryTypeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;


}
