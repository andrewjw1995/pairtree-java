package pairtree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path that ends in a pair of characters. A Shorty can contain
 * child paths.
 */
public class Shorty extends PairPath
{
	protected PairPath[] children;
	protected PairPath parent;
	
	public Shorty(PairPath parent, File file)
	{
		if (!file.isDirectory())
			throw new RuntimeException(file.getName() + " is not a directory");
		if (file.getName().length() != 2)
			throw new RuntimeException(file.getName() + " is not a shorty (size 2) directory");
		this.file = file;
		this.parent = parent;
	}

	@Override
	public File[] getLeaves()
	{
		if (objects == null)
		{
			File[] files = file.listFiles();
			List<File> list = new ArrayList<>();
			for (File f : files)
				if (f.getName().length() > 2)
					list.add(f);
			
			objects = new File[list.size()];
			objects = list.toArray(objects);
		}
		return objects;
	}

	@Override
	public PairPath[] getChildPaths()
	{
		if (children == null)
		{
			File[] files = file.listFiles();
			
			List<PairPath> list = new ArrayList<>();
			for (File f : files)
				if (f.getName().length() <= 2)
					if (f.isDirectory())
						list.add(fromFile(f));
					else
						System.err.println("Improperly encapsulated pairtree object " + f);
			
			children = new PairPath[list.size()];
			children = list.toArray(children);
		}
		return children;
	}
	
	@Override
	public String getEscapedName()
	{
		return parent.getEscapedName() + file.getName();
	}
}
