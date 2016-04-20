package edu.ou.cs.cacheprototypelibrary.querycache.exception;

import edu.ou.cs.cacheprototypelibrary.estimationcache.Estimation;

public class ConstraintsNotRespectedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConstraintsNotRespectedException()
	{
		super();
	}
	
	public ConstraintsNotRespectedException(String detailedMessage) {
		super(detailedMessage);
	}
	
	public ConstraintsNotRespectedException(Estimation estimation)
	{
		super(new StringBuilder().append("Constraints not respected: ").append(estimation).toString());
	}
	
}
