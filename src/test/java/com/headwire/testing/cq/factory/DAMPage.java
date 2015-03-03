package com.headwire.testing.cq.factory;

/**
 * Provides helper methods to perform actions on DAM assets
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public interface DAMPage {
	
	/**
	 * Opens the timeline for the specified asset
	 * 
	 * @param imagePath Path of the image
	 * @return A newly initialized DAMPage
	 */
	public DAMPage viewTimeline(String imagePath);

	/**
	 * Validates a comment exists in the timeline for an asset
	 * 
	 * @param comment The comment to search for
	 * @return The current DAMPage
	 */
	public DAMPage verifyTimelineComment(String comment);
	
	/**
	 * Opens the references pane for the specified asset
	 * 
	 * @param imagePath Path of the image
	 * @return A newly initialized DAMPage
	 */
	public DAMPage viewReferences(String imagePath);
	
	/**
	 * Opens the site references after an asset is selected
	 * 
	 * @return A newly initialized DAMPage
	 */
	public DAMPage openSiteReferences();
	
	/**
	 * Validates references exist for a selected asset
	 * 
	 * @param references Array of reference text values
	 * @return The current DAMPage
	 */
	public DAMPage verifyReferences(String[] references);

}
