package edu.ou.cs.cacheprototypelibrary.querycache.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ou.cs.cacheprototypelibrary.metadata.Metadata;
import edu.ou.cs.cacheprototypelibrary.metadata.ObjectSizer;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.InvalidPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.TrivialPredicateException;

/**
 * @author Mikael Perrin
 * @since 1.0
 * Definition of a predicate with Attribute-Operator-Attribute
 */
public class XopYPredicate extends Predicate{

	private String mLeftOperand;
	private String mRightOperand;
	
	/**
	 * The XopYPredicate constructor
	 * @param leftOperand the left operand of the predicate
	 * @param operator the predicate operator
	 * @param rightOperand the right operand of the predicate
	 * @throws InvalidPredicateException thrown when predicate is invalid
	 * @throws TrivialPredicateException thrown when predicate is useless
	 */
	public XopYPredicate(String leftOperand, String operator, String rightOperand) 
			throws InvalidPredicateException, TrivialPredicateException {
		super(operator);
		mLeftOperand = leftOperand;
		mRightOperand = rightOperand;
		
		if (!isValidPredicate())
		{
			throw new InvalidPredicateException();
		}
		else if (isTrivialPredicate())
		{
			throw new TrivialPredicateException();
		}
	}

	@Override
	public boolean isValidPredicate() 
	{
		boolean isValidPredicate = hasValidOperator();
		
		if ((mLeftOperand.equals(mRightOperand)) && 
				(mOperator == "<" || mOperator == ">" || mOperator == "<>"))
		{
			isValidPredicate = false;
		}
		return isValidPredicate;
	}

	
	@Override
	public boolean isTrivialPredicate()
	{
		boolean isTrivialPredicate = false;
		if (mLeftOperand == mRightOperand)
		{
			isTrivialPredicate = true;
		}
		
		return isTrivialPredicate;
	}
	
	@Override
	public boolean transformToRealDomainPredicate() {
		
		boolean transformed = false;
		String swap;
		
		switch (mOperator) {
		case ">":
			swap = mLeftOperand;
			mLeftOperand = mRightOperand;
			mRightOperand = swap;
			mOperator = "<";
			transformed = true;
			break;
		case ">=":
			swap = mLeftOperand;
			mLeftOperand = mRightOperand;
			mRightOperand = swap;
			mOperator = "<=";
			transformed = true;
			break;
		}
		
		return transformed;
	}
	
	@Override
	public boolean transformToIntegerDomainPredicate() {
		return transformToRealDomainPredicate();
	}
	
	@Override
	public boolean apply(String relation, List<String> tuple) throws NumberFormatException {
		int indexLeft = Metadata.getInstance().getRelationMetadata(relation).getAttributeIndex(mLeftOperand);
		int indexRight = Metadata.getInstance().getRelationMetadata(relation).getAttributeIndex(mRightOperand);
		boolean isValidTuple = false;
		
		Double valueLeft = Double.parseDouble(tuple.get(indexLeft));
		Double valueRight = Double.parseDouble(tuple.get(indexRight));
		
		switch(mOperator)
		{
			case "<":
				isValidTuple = valueLeft < valueRight;
				break;
			case ">":
				isValidTuple = valueLeft > valueRight;
				break;
			case "<=":
				isValidTuple = valueLeft <= valueRight;
				break;
			case ">=":
				isValidTuple = valueLeft >= valueRight;
				break;
			case "=":
				isValidTuple = valueLeft == valueRight;
				break;
			case "<>":
				isValidTuple = valueLeft != valueRight;
				break;
		}
			
		return isValidTuple;
	}

	@Override
	public Set<String> getAttributes() {
		Set<String> attributeSet = new HashSet<String>();
		attributeSet.add(mLeftOperand);
		attributeSet.add(mRightOperand);
		return attributeSet;
	}
	
	/**
	 * @return the leftOperand
	 */
	public String getLeftOperand() {
		return this.mLeftOperand;
	}

	/**
	 * @param leftOperand the leftOperand to set
	 */
	public void setLeftOperand(String leftOperand) {
		if (leftOperand != null) {
			this.mLeftOperand = leftOperand;
		}
	}

	/**
	 * @return the rightOperand
	 */
	public String getRightOperand() {
		return this.mRightOperand;
	}

	/**
	 * @param rightOperand the rightOperand to set
	 */
	public void setRightOperand(String rightOperand) {
		if (rightOperand != null) {
			this.mRightOperand = rightOperand;
		}
	}
	
	@Override
	public String getSerializedLeftOperand() {
		return this.mLeftOperand;
	}
	
	@Override
	public String getSerializedRightOperand() {
		return this.mRightOperand;
	}
	
	@Override
	public long size() {
		return ObjectSizer.getStringSize32bits(mLeftOperand.length())
				+ ObjectSizer.getStringSize32bits(mOperator.length())
				+ ObjectSizer.getStringSize32bits(mRightOperand.length());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((mLeftOperand == null) ? 0 : mLeftOperand.hashCode());
		result = prime * result
				+ ((mRightOperand == null) ? 0 : mRightOperand.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		XopYPredicate other = (XopYPredicate) obj;
		if (mLeftOperand == null) {
			if (other.mLeftOperand != null) {
				return false;
			}
		} else if (!mLeftOperand.equals(other.mLeftOperand)) {
			return false;
		}
		if (mRightOperand == null) {
			if (other.mRightOperand != null) {
				return false;
			}
		} else if (!mRightOperand.equals(other.mRightOperand)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mLeftOperand);
		sb.append(" ");
		sb.append(mOperator);
		sb.append(" ");
		sb.append(mRightOperand);
		return sb.toString();
	}
	
}
