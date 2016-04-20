package edu.ou.cs.cacheprototypelibrary.querycache.trimming;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import edu.ou.cs.cacheprototypelibrary.querycache.exception.CycleFoundException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.InvalidPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.NPHardProblemException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.TrivialPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Predicate;
import edu.ou.cs.cacheprototypelibrary.querycache.query.PredicateFactory;
import edu.ou.cs.cacheprototypelibrary.querycache.query.XopCPredicate;
import edu.ou.cs.cacheprototypelibrary.querycache.query.XopYPredicate;

/**
 * @author Mikael Perrin
 * @since 1.0
 *
 * implementation of a predicate analyzer
 * (Singleton)
 */
public class GuoEtAlPredicateAnalyzer implements PredicatesAnalyzer {
	
	private HashMap<String, AttributeNode> mNodeMap = null;
	
	private LabeledDirectedGraph mNodeGraph = null;
	
	private LabeledDirectedGraph mNodeGraphCollapsed = null;
	
	public GuoEtAlPredicateAnalyzer() 
	{
		mNodeMap = new LinkedHashMap<String,AttributeNode>();
		mNodeGraph = new LabeledDirectedGraph();
	}

	
	/**
	 * method used to respect all the rules of the real domain
	 * for the solving of satisfiability and implications
	 * @param predicates the given predicate list
	 * @return the cleaned predicate list
	 * @throws TrivialPredicateException 
	 * @throws NPHardProblemException 
	 * @throws CycleFoundException 
	 */
	private Set<Predicate> transformToIntegerDomain(Set<Predicate> predicates) 
			throws InvalidPredicateException, TrivialPredicateException, NPHardProblemException
	{
		Set<Predicate> cleanPredicateList = new HashSet<Predicate>();
		for(Predicate p: predicates)
		{
			// replace the X=Y or X=C
			// by X<=Y AND X>=Y or X<=C AND X>=C
			// and each X<C or X>C
			// by X<=C-1 and X>=C+1
			// if "<>" then NP-Hard
			if (p.getOperator().equals("="))
			{
				Predicate pLowBound = PredicateFactory.copyPredicate(p);
				pLowBound.setOperator("<=");
				pLowBound.transformToIntegerDomainPredicate();
				Predicate pUpBound = PredicateFactory.copyPredicate(p);
				pUpBound.setOperator(">=");
				pUpBound.transformToIntegerDomainPredicate();
				cleanPredicateList.add(pLowBound);
				cleanPredicateList.add(pUpBound);
			}
			else if (! p.getOperator().equals("<>") )
			{
				Predicate copyP = PredicateFactory.copyPredicate(p);
				copyP.transformToIntegerDomainPredicate();
				cleanPredicateList.add(copyP);
			}
			else
			{
				throw new NPHardProblemException();
			}
		}
		return cleanPredicateList;
	}
	
	/**
	 * method used to respect all the rules of the real domain
	 * for the solving of satisfiability and implications
	 * @param predicates the given predicate list
	 * @return the cleaned predicate list
	 * @throws TrivialPredicateException 
	 * @throws NPHardProblemException 
	 * @throws CycleFoundException 
	 */
	private Set<Predicate> transformToRealDomain(Set<Predicate> predicates) 
			throws InvalidPredicateException, TrivialPredicateException, NPHardProblemException
	{
		Set<Predicate> cleanPredicateList = new HashSet<Predicate>();
		for(Predicate p: predicates)
		{
			// replace the X=Y or X=C
			// by X<=Y AND X>=Y or X<=C AND X>=C
			// if "<>" then NP-Hard
			if (p.getOperator().equals("="))
			{
				Predicate pLowBound = PredicateFactory.copyPredicate(p);
				pLowBound.setOperator("<=");
				pLowBound.transformToRealDomainPredicate();
				Predicate pUpBound = PredicateFactory.copyPredicate(p);
				pUpBound.setOperator(">=");
				pUpBound.transformToRealDomainPredicate();
				cleanPredicateList.add(pLowBound);
				cleanPredicateList.add(pUpBound);
			}
			else if (! p.getOperator().equals("<>") )
			{
				Predicate copyP = PredicateFactory.copyPredicate(p);
				copyP.transformToRealDomainPredicate();
				cleanPredicateList.add(copyP);
			}
			else
			{
				throw new NPHardProblemException();
			}
		}
		return cleanPredicateList;
	}
	
	/**
	 * init the different collections to check satisfiability for cachePredicates + queryPredicates
	 * @param cachePredicates
	 * @param queryPredicates
	 * @return
	 * @throws CycleFoundException
	 * @throws TrivialPredicateException
	 * @throws NPHardProblemException 
	 */
	private Set<Predicate> initPrerequisitesIntegerDomain(Set<Predicate> predicates) 
			throws InvalidPredicateException, TrivialPredicateException, NPHardProblemException
	{	
		Set<Predicate> newPredicates = new HashSet<Predicate>();
		// aggregation of the two lists 
		newPredicates.addAll(transformToIntegerDomain(predicates));
		
		return newPredicates;
	}
	
	/**
	 * init the different collections to check satisfiability for cachePredicates + queryPredicates
	 * @param cachePredicates
	 * @param queryPredicates
	 * @return
	 * @throws CycleFoundException
	 * @throws TrivialPredicateException
	 * @throws NPHardProblemException 
	 */
	private Set<Predicate> initPrerequisitesRealDomain(Set<Predicate> predicates) 
			throws InvalidPredicateException, TrivialPredicateException, NPHardProblemException
	{
		Set<Predicate> newPredicates = new HashSet<Predicate>();
		// aggregation of the two lists 
		newPredicates.addAll(transformToRealDomain(predicates));
		
		return newPredicates;
	}
	
	public boolean respectsSatifiabilityIntegerDomain(Set<Predicate> predicates)
	{
		XopCPredicate curXopCPredicate = null;
		AttributeNode curNode = null;
		boolean respectsSatisfiability = true;
		Set<Predicate> cleanPredicates = null;
		Set<XopYPredicate> listXopYPredicates = new HashSet<XopYPredicate>();
		
		try {
			cleanPredicates = initPrerequisitesIntegerDomain(predicates);
		} catch (InvalidPredicateException | TrivialPredicateException e1) {
			System.err.println("Bad Predicate within provided lists of predicate");
			respectsSatisfiability = false;
		} catch (NPHardProblemException e) {
			respectsSatisfiability = false;
		}
				
		Iterator<Predicate> it = cleanPredicates.iterator();
		Predicate p = null;
		while(it.hasNext() && respectsSatisfiability)
		{
			p = it.next();
			if (p instanceof XopCPredicate)
			{
				curXopCPredicate = (XopCPredicate) p;
				
				// if the node already exists
				if (mNodeMap.containsKey(curXopCPredicate.getLeftOperand()))
				{
					curNode = mNodeMap.get(curXopCPredicate.getLeftOperand());
				}
				else // if the node does not exist
				{
					curNode = new AttributeNode(curXopCPredicate.getLeftOperand());
				}
				
				// Algorithm 1: stp 2
				switch ( curXopCPredicate.getOperator() ) {
				case "<=":
					if ( curXopCPredicate.getRightOperand() <= curNode.getUpClosedMinRange() )
					{
						curNode.setUpClosedMinRange(curXopCPredicate.getRightOperand(), false);
					}
					break;
				case ">=":
					if ( curXopCPredicate.getRightOperand() >= curNode.getLowClosedMinRange() )
					{
						curNode.setLowClosedMinRange(curXopCPredicate.getRightOperand(), false);
					}
					break;
				} 
				mNodeMap.put(curNode.getAttribute(), curNode);
			}
			else // p instanceof XopYPredicate
			{
				XopYPredicate pXopY = ((XopYPredicate) p);
				listXopYPredicates.add(pXopY);
				if (!mNodeMap.containsKey(pXopY.getLeftOperand()))
					mNodeMap.put(pXopY.getLeftOperand(), new AttributeNode(pXopY.getLeftOperand()));
				if (!mNodeMap.containsKey(pXopY.getRightOperand()))
					mNodeMap.put(pXopY.getRightOperand(), new AttributeNode(pXopY.getRightOperand()));
				
			}
		}
		

		if (respectsSatisfiability)
		{
			mNodeGraph.addAllNodes(mNodeMap.values());
			
			for(XopYPredicate pXopY: listXopYPredicates)
			{
				//Build the edges
				mNodeGraph.addEdge(mNodeMap.get(pXopY.getLeftOperand()), mNodeMap.get(pXopY.getRightOperand()), pXopY.getOperator());
			}
						
			// get the sccs
			mNodeGraphCollapsed = mNodeGraph.getCollapsedGraph();
			if (mNodeGraphCollapsed != null)
			{
				try {
					//compute Alow, Aup
					mNodeGraphCollapsed.computeRealMinRanges();
					
					// check real minimum ranges
					respectsSatisfiability = mNodeGraphCollapsed.areValidRealMinRangesInIntegerDomain();
				} catch (CycleFoundException e) {
					respectsSatisfiability = false;
				}
			}
			else
			{
				respectsSatisfiability = false;
			}
		}
		
		
		return respectsSatisfiability;
	}
	
	
	public boolean respectsSatifiabilityRealDomain(Set<Predicate> predicates)
	{
		XopCPredicate curXopCPredicate = null;
		AttributeNode curNode = null;
		boolean respectsSatisfiability = true;
		Set<Predicate> cleanPredicates = null;
		Set<XopYPredicate> listXopYPredicates = new HashSet<XopYPredicate>();
		
		
		try {
			cleanPredicates = initPrerequisitesRealDomain(predicates);
		} catch (InvalidPredicateException | TrivialPredicateException e1) {
			System.err.println("Bad Predicate within provided lists of predicate");
			respectsSatisfiability = false;
		} catch (NPHardProblemException e) {
			respectsSatisfiability = false;
		}
				
		Iterator<Predicate> it = cleanPredicates.iterator();
		Predicate p = null;
		while(it.hasNext() && respectsSatisfiability)
		{
			p = it.next();
			if (p instanceof XopCPredicate)
			{
				curXopCPredicate = (XopCPredicate) p;
				
				// if the node already exists
				if (mNodeMap.containsKey(curXopCPredicate.getLeftOperand()))
				{
					curNode = mNodeMap.get(curXopCPredicate.getLeftOperand());
				}
				else // if the node does not exist
				{
					curNode = new AttributeNode(curXopCPredicate.getLeftOperand());
				}
				
				// Algorithm 2: stp 2
				switch ( curXopCPredicate.getOperator() ) {
				case "<=":
					if ( curXopCPredicate.getRightOperand() <= curNode.getUpClosedMinRange() )
					{
						curNode.setUpClosedMinRange(curXopCPredicate.getRightOperand(), false);
					}
					break;
				case "<":
					if ( curXopCPredicate.getRightOperand() < curNode.getUpClosedMinRange() )
					{
						curNode.setUpClosedMinRange(curXopCPredicate.getRightOperand(), true);
					}
					break;
				case ">=":
					if ( curXopCPredicate.getRightOperand() >= curNode.getLowClosedMinRange() )
					{
						curNode.setLowClosedMinRange(curXopCPredicate.getRightOperand(), false);
					}
					break;
				case ">":
					if ( curXopCPredicate.getRightOperand() > curNode.getLowClosedMinRange() )
					{
						curNode.setLowClosedMinRange(curXopCPredicate.getRightOperand(), true);
					}
					break;
				} 
				mNodeMap.put(curNode.getAttribute(), curNode);
			}
			else // p instanceof XopYPredicate
			{
				XopYPredicate pXopY = ((XopYPredicate) p);
				listXopYPredicates.add(pXopY);
				if (!mNodeMap.containsKey(pXopY.getLeftOperand()))
					mNodeMap.put(pXopY.getLeftOperand(), new AttributeNode(pXopY.getLeftOperand()));
				if (!mNodeMap.containsKey(pXopY.getRightOperand()))
					mNodeMap.put(pXopY.getRightOperand(), new AttributeNode(pXopY.getRightOperand()));
				
			}
		}
		

		if (respectsSatisfiability)
		{
			mNodeGraph.addAllNodes(mNodeMap.values());
			
			for(XopYPredicate pXopY: listXopYPredicates)
			{
				//Build the edges
				mNodeGraph.addEdge(mNodeMap.get(pXopY.getLeftOperand()), mNodeMap.get(pXopY.getRightOperand()), pXopY.getOperator());
			}
						
			// get the sccs
			mNodeGraphCollapsed = mNodeGraph.getCollapsedGraph();
			if (mNodeGraphCollapsed != null)
			{
				try {
					//compute Alow, Aup
					mNodeGraphCollapsed.computeRealMinRanges();
					
					// check real minimum ranges
					respectsSatisfiability = mNodeGraphCollapsed.areValidRealMinRangesInRealDomain();
				} catch (CycleFoundException e) {
					respectsSatisfiability = false;
				}
			}
			else
			{
				respectsSatisfiability = false;
			}
		}
		
		
		return respectsSatisfiability;
	}
	
	
	public boolean respectsImplicationIntegerDomain(Set<Predicate> queryPredicates, Set<Predicate> cachePredicates)
	{
		boolean respectsImplication = true;
		Set<Predicate> cleanCachePredicates = null;

		mNodeMap.clear();
		mNodeGraph.clear();
		
		if(respectsSatifiabilityIntegerDomain(queryPredicates))
		{
			try {
				cleanCachePredicates = initPrerequisitesIntegerDomain(cachePredicates);		
			} catch (InvalidPredicateException | TrivialPredicateException e1) {
				System.err.println("Bad Predicate within provided lists of predicate");
				respectsImplication = false;
			} catch (NPHardProblemException e) {
				respectsImplication = false;
			}
			
			Iterator<Predicate> iterator = cleanCachePredicates.iterator();
			Predicate curPredicate;
			while(iterator.hasNext() && respectsImplication)
			{
				curPredicate = iterator.next();
				if (mNodeGraphCollapsed != null)
				{
					if (curPredicate instanceof XopYPredicate)
						respectsImplication = mNodeGraphCollapsed.impliesPredicateIntegerDomain((XopYPredicate) curPredicate);
					else if(curPredicate instanceof XopCPredicate)
					{
						respectsImplication = mNodeGraphCollapsed.impliesPredicateIntegerDomain((XopCPredicate) curPredicate);
					}
					else
						throw new UnsupportedOperationException();
				}
				else
				{
					respectsImplication = false;
				}
			}
		}
		
		return respectsImplication;
	}
	

	public boolean respectsImplicationRealDomain(Set<Predicate> queryPredicates, Set<Predicate> cachePredicates)
	{
		boolean respectsImplication = true;
		Set<Predicate> cleanCachePredicates = null;
		
		mNodeMap.clear();
		mNodeGraph.clear();
		
		if(respectsSatifiabilityRealDomain(queryPredicates))
		{
			try {
				cleanCachePredicates = initPrerequisitesRealDomain(cachePredicates);
			} catch (InvalidPredicateException | TrivialPredicateException e1) {
				System.err.println("Bad Predicate within provided lists of predicate");
				respectsImplication = false;
			} catch (NPHardProblemException e) {
				respectsImplication = false;
			}
			
			Iterator<Predicate> iterator = cleanCachePredicates.iterator();
			Predicate curPredicate;
			while(iterator.hasNext() && respectsImplication)
			{
				curPredicate = iterator.next();
				if (mNodeGraphCollapsed != null)
				{
					if (curPredicate instanceof XopYPredicate)
						respectsImplication = mNodeGraphCollapsed.impliesPredicateRealDomain((XopYPredicate) curPredicate);
					else if(curPredicate instanceof XopCPredicate)
						respectsImplication = mNodeGraphCollapsed.impliesPredicateRealDomain((XopCPredicate) curPredicate);
					else
						throw new UnsupportedOperationException();
				}
				else
				{
					respectsImplication = false;
				}
			}
		}
		
		return respectsImplication;
	}
	
	
}


