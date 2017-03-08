package com.cqblueprints.testing.cq.pageobjects;

/**
 * Provides helper methods to perform actions on the inbox page
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public interface InboxPage {
	
	/**
	 * Advanced a workflow through the inbox using the title of the page
	 * 
	 * @param pageTitle The title of the page shown in the inbox
	 * @param comment   The comment to input when advancing
	 * @return          A new instance of InboxPage after JS has run updates
	 */
	public InboxPage advanceWorkflowThroughInbox(String pageTitle, String comment);
	
	/**
	 * Validates the current status of a workflow
	 * 
	 * @param status The expected status of the page
	 */
	public void validateStatus(String status);
	
	/**
	 * Validates the specified comment exists
	 * 
	 * @param comment The expected comment
	 */
	public void validateComment(String comment);
	
}
