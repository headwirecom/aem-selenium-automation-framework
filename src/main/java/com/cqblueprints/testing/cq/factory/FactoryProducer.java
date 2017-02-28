package com.cqblueprints.testing.cq.factory;

public class FactoryProducer {

	public static AbstractPageFactory getPageFactory() {
		return new PageFactory();
	}
}
