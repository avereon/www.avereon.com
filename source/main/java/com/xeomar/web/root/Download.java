package com.xeomar.web.root;

public interface Download {

	String getArtifact();

	String getVersion();

	// FIXME Rename to getCategory
	String getClassifier();

	String getType();

}
