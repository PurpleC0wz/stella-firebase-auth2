package com.stellanow;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SubscriptionCustomer {
	
	private String firstName;
	private String email;
	private String lastName;
	private String companyName;
	private String accountOwner;
	private String businessCoach;
	private String customerId;
	private Long createdAt;
	private Object access;
	
	public SubscriptionCustomer() {
		// TODO Auto-generated constructor stub
	}
	
	public SubscriptionCustomer(String firstName,String email, String lastName, String companyName, Long createdAt) {		
		this.firstName = firstName;
		this.email = email;
		this.lastName = lastName;
		this.companyName = companyName;
		this.createdAt = createdAt;
	}
	
	/**
	 * 
	 * @return The firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName
	 *            The first_name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 *            The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return The lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName
	 *            The last_name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Object getAccess() {
		return access;
	}

	public void setAccess(Object access) {
		this.access = access;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getBusinessCoach() {
		return businessCoach;
	}

	public void setBusinessCoach(String businessCoach) {
		this.businessCoach = businessCoach;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}