package pairtree;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;

/**
 * An iterator to traverse all the paths that exist within a PairTree, whether
 * they contain leaf nodes or not.
 */
public class PairTreeIterator implements Iterator<PairPath>
{
	Stack<PairPathIterator> paths = new Stack<>();
	PairPath last;
	
	public PairTreeIterator(PairPath root)
	{
		PairPathIterator iterator = new PairPathIterator(root);
		paths.push(iterator);
		while(iterator.hasNext())
		{
			iterator = new PairPathIterator(iterator.next());
			paths.push(iterator);
		}
	}

	@Override
	public boolean hasNext()
	{
		return paths.size() > 0;
	}

	@Override
	public PairPath next()
	{
		PairPathIterator leaf = paths.pop();
		if (paths.size() > 0)
		{
			PairPathIterator parent = paths.peek();
			while (parent.hasNext())
			{
				parent = new PairPathIterator(parent.next());
				paths.push(parent);
			}
		}
		last = leaf.getSource();
		return last;
	}

	@Override
	public void remove()
	{
		delete(last.getFile());
	}
	
	private void delete(File file)
	{
		if (file.isDirectory())
			for (File child : file.listFiles())
				delete(child);
		file.delete();
	}
}
