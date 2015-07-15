package pairtree;

import java.util.Iterator;

/**
 * An iterator over all the child paths of a particular PairPath.
 */
public class PairPathIterator implements Iterator<PairPath>
{
	private PairPath source;
	private PairPath[] paths;
	private int position;
	
	public PairPathIterator(PairPath source)
	{
		this.source = source;
		paths = source.getChildPaths();
	}
	
	public PairPath getSource()
	{
		return source;
	}

	@Override
	public boolean hasNext()
	{
		return position < paths.length;
	}

	@Override
	public PairPath next()
	{
		return paths[position++];
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
